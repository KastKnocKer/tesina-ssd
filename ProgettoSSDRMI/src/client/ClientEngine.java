package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import layout.friendslist.FriendsList_Table;
import layout.managers.ConversationWindowsManager;
import layout.managers.LayoutReferences;
import managers.ContactListManager;
import managers.FileConfManager;
import managers.FileContactsManager;
import managers.FriendshipManager;
import managers.Status;
import RMI.ClientInterface;
import RMI.SIPInterface;
import RMIMessages.FriendshipRequest;
import RMIMessages.RMIBasicMessage;
import RMIMessages.RequestLoginMessage;
import RMIMessages.RequestModifyContactInfos;
import RMIMessages.ResponseLoginMessage;
import chat.ChatStatusList;
import chat.Contact;
import chat.Message;
import client.requesttosip.RequestToSIP;
import client.requesttosip.RequestToSIPListManager;
import client.requesttosip.RequestToSIPTypeList;
import client.thread.ClientThread_LogoutInformer;
import client.thread.ClientThread_WhoisRequestor;

/**
 * Rappresenta l'insieme dei metodi richiamabili dall'applicazione per interagire con gli altri nodi della rete
 */

public class ClientEngine {
	
	public static ResponseLoginMessage Login(String username, String password, boolean calledFromSendRequestToSIP) {
		Status.setLastLoginUsername(username);
		Status.setLastLoginPassword(password);
		ResponseLoginMessage response = null;
		//Login mediante server SIP
		if(Status.DEBUG) System.out.println("[CLIENT] Tentativo di login username: "+username+" password: "+password);
		
		
		SIPInterface SIP = getSIP();
		if(SIP == null){
			System.err.println("IL SIP e' OFFLINE!!! Procedo con la procedura di login P2P (SIP == null)");
			Status.setSIPStatusOnline(false);
			return LoginP2P(username, password);
		}
		
		System.out.println(SIP.toString());
		
		try {
			response = SIP.login(new RequestLoginMessage(username, password, ChatStatusList.ONLINE));
		} catch (RemoteException e) {
			Status.setSIPStatusOnline(false);
			System.err.println("IL SIP e' OFFLINE!!! Procedo con la procedura di login P2P (RemoteException)");
			return LoginP2P(username, password);
		}
		
		if(response.isSUCCESS()){
			Status.bindClient();
			Status.setSIPStatusOnline(true);
			//Aggiorno i dati personali
			Status.setUserID(response.getLoggedContact().getID());
			Status.setEmail(response.getLoggedContact().getEmail());
			Status.setNome(response.getLoggedContact().getNome());
			Status.setCognome(response.getLoggedContact().getCognome());
			Status.setNickname(response.getLoggedContact().getNickname());
			Status.setLOGGED(true);
			
			//Carico eventuali richieste al primo tentativo di login verso il SIP
			if(!Status.isLOGGEDP2P())
				FileContactsManager.readContactsXML();
			
			//Invio le richieste al SIP
			if(!calledFromSendRequestToSIP)
				RequestToSIPListManager.sendRequests();
			
//			//Controllo se è stata inviata anche la lista dei contatti
//			ArrayList<Contact> contactList = response.getContactList();
//			if(contactList != null){
//				System.out.println("Contact list caricata dal Login: " +contactList.size());
//				ContactListManager.addContactsFromSIPContactList(contactList);
//			}
			
			//Aggiorno i dati del LastLogin su Status
			Status.setLastLoginUsername(username);
			Status.setLastLoginPassword(password);
			FileConfManager.writeConfXML();
			
			/* Mostro richieste di amicizia PENDING, se ce ne sono */
			ArrayList<FriendshipRequest> friendshipRequestsList = response.getFriendshipRequestList();
			if(friendshipRequestsList != null) {
				System.out.println("Login Response - FriendshipRequestList caricata! Ci sono richieste di amicizia pending.");
				for(FriendshipRequest fr : friendshipRequestsList) {
					System.err.println("Mostro Richiesta di Amicizia PENDING da parte di: " + fr.getContattoMittente().getEmail());
					fr.getContattoMittente().printInfo(); 
					FriendshipManager.showFriendshipRequestFromContact(fr.getContattoMittente());
				}
				
			} else {
				System.out.println("Login Response - Non ci sono richieste di amicizia pending. ");
			}
			
			LoadContactsFromSIP();
			
		}
		Status.setLOGGED(true);
		Status.setLOGGEDP2P(false);
		return response;
	}
	
	/**
	 * Permette, se possibile, di fare il login al sistema senza passare per il SIP
	 */
	public static ResponseLoginMessage LoginP2P(String username, String password) {
		//Se sono già loggato in modalità P2P: per non causare un ciclo di invocazioni ritorno null
		if(Status.isLOGGEDP2P()) return null;
		
		Status.setLastLoginUsername(username);
		Status.setLastLoginPassword(password);
		ResponseLoginMessage response = null;
		
		if(FileContactsManager.readContactsXML()){
			Status.setLOGGED(true);
			Status.setLOGGEDP2P(true);
			Status.bindClient();
			//Aggiungo la richiesta asincrona al SIP per conttattarlo appena torna online
			RequestToSIPListManager.addRequest(new RequestToSIP(RequestToSIPTypeList.LOGIN, new RequestLoginMessage(username, password, ChatStatusList.ONLINE)));
			return new ResponseLoginMessage(true, "ClientEngine.Login() exception", null);
		}else{
			JOptionPane.showMessageDialog(null, "Non e' stato possibile leggere il file temporaneo.", "Login P2P failed", JOptionPane.WARNING_MESSAGE);
		}
		return new ResponseLoginMessage(false, "ClientEngine.Login() exception", null);
	}
	
	public static boolean Logout(){
		new ClientThread_LogoutInformer(true, null);
		ArrayList<Contact> contactList =  ContactListManager.getContactList();
		for(Contact contact : contactList){
			new ClientThread_LogoutInformer(false, contact);
		}
		
		//Unbind client
		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 1099);
			registry.unbind("Client");
		} catch (RemoteException | NotBoundException e1) {
			System.err.println("ON LOGOUT - Exception catched: GetRegistry & Unbind Client");
		}
		//Clean SipRequestor
		RequestToSIPListManager.getRequestsToSIP().clear();
		return true;
	}
	
	public static boolean RegisterNewAccount(String nome, String cognome,String email, String nickname, String password) {
		boolean response = false;
		try {
			if(Status.DEBUG) System.out.println("[CLIENT] Tentativo registrazione nuovo utente: "+nome+", "+cognome+", "+email+", "+nickname+", ["+password+"]");
			response = getSIP().register(nome, cognome, email, nickname, password);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.RegisterNewAccount() exception", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.Login() exception: " + e.toString());
			//e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * Richiede al sip l'intera lista dei propri contatti, 
	 * aggiornando la lista dei contatti in Status
	 */
	public static boolean LoadContactsFromSIP(){
		boolean response = false;
		try {
			if(Status.DEBUG) System.out.println("[CLIENT] ClientEngine.LoadContactsFromSIP() Richiesta lista contatti al SIP");
			ArrayList<Contact> contacts = getSIP().getMyContacts(new RMIBasicMessage());
			
			if(contacts != null){
				ContactListManager.addContactsFromSIPContactList(contacts);
				response = true;
				if(Status.DEBUG) System.out.println("[CLIENT] ClientEngine.LoadContactsFromSIP() Richiesta lista contatti al SIP e caricamento effettuati.");
			}
			
		} catch (RemoteException e) {
			System.err.println("IL SIP e' OFFLINE!!!");
			Status.setSIPStatusOnline(false);
			return false;
		}
		FileContactsManager.writeContactsXML();
		return response;
	}
	
//	/**
//	 * Richiede l'amicizia ad un altro contatto
//	 */
//	public static RMISIPBasicResponseMessage RequestFriendship(String email){
//		try {
//			
//			/* 1. whois email? ask SIP and p2p network */
//			
//			/* 2. send friendship request (try client, then eventually SIP) */
//			
//			if(Status.DEBUG) System.out.println("[CLIENT] Richiesta di amicizia a: " + email);
//			return getSIP().askFriendship(new RequestFriendshipMessage(email));
//		} catch (RemoteException e) {
//			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.RequestFriendship() exception", JOptionPane.ERROR_MESSAGE);
//			//System.err.println("ClientEngine.Login() exception: " + e.toString());
//			//e.printStackTrace();
//			return new RMISIPBasicResponseMessage(false, "ClientEngine.RequestFriendship() exception");
//		}
//	}
	
	/**
	 * Invia un messaggio ad un contatto
	 */
	public static boolean sendMessageToContact(Message chatMsg){
		if(Status.SUPER_DEBUG) System.out.println("[CLIENT] Inserimento in OUTList del messaggio da:" +chatMsg.getFrom()+ " a:"+ chatMsg.getTo() +" Messaggio: "+chatMsg.getMessage());
		OUTChatMessageListManager.addMsgToSend(chatMsg);
		return true;
	}
	/**
	 * Riceve un messaggio da un contatto
	 */
	public static boolean receiveMessageFromContact(Message chatMsg){
		if(Status.SUPER_DEBUG) System.out.println("---> [CLIENT] Ho ricevuto il messaggio da: " +chatMsg.getFrom()+ " a:"+ chatMsg.getTo() +" Messaggio: "+chatMsg.getMessage());
		INChatMessageListManager.addReceivedMsg(chatMsg);
		
		/* Mostro la finestra se arriva il messaggio */
		Contact contact = ContactListManager.searchContactById(chatMsg.getFrom()); 
		ConversationWindowsManager.showConversationFrame(contact); 
		
		/* Scrivo il messaggio nella finestra */
		ConversationWindowsManager.writeToConversationFrame(chatMsg.getFrom(), chatMsg.getMessage());
		
		return true;
	}
	
	/**
	 * Consegna tutti i messaggi in coda al client di destinazione
	 */
	public static boolean deliverAllMessages(){
		return OUTChatMessageListManager.deliverAllMessages();
//		//Se la coda è vuota ritorno subito
//		if(OUT_ChatMessageList.size() == 0) return true;
//		//Altrimenti
//		int temporaryClientTarget = -1;	//Inizializzo così entra nel ciclo while almeno una volta
//		System.out.println("*************************************************************");
//		ArrayList<Message> MessagesToDeliver = null;
//		Collections.sort(OUT_ChatMessageList, new Comparator<? super Message>());
//		while( (temporaryClientTarget < 0) || (OUT_ChatMessageList.size()>5) ){
//			temporaryClientTarget = -1;
//			MessagesToDeliver = new ArrayList<Message>();
//			for(int i=0; i<OUT_ChatMessageList.size(); i++){
//				Message msg = OUT_ChatMessageList.get(i);
//				if(temporaryClientTarget<0){
//					temporaryClientTarget = msg.getTo();
//					System.out.println("Servo i messaggi verso l'utente: "+temporaryClientTarget);
//				}
//				if(msg.getTo() == temporaryClientTarget){
//					System.out.println("In consegna: " +msg.getMessage());
//					MessagesToDeliver.add(msg);
//					OUT_ChatMessageList.remove(msg);
//					i--;
//				}
//					
//				System.out.println("Deliver message - " + msg.getMessage());
//			}
//			
//			
//			
//			ClientThread_MessageSender cts = new ClientThread_MessageSender(MessagesToDeliver);
//			cts.start();
//			
//			System.err.println("Consegna di "+MessagesToDeliver.size()+" messaggi. Messaggi residui: "+OUT_ChatMessageList.size());
//		}
//		
//		
//		return true;
	}
	
	
	/**
	 * Richiede la modifica delle proprie informazioni
	 */
	public static Contact ModifyMyInfos(Contact contact){
		try {
			if(Status.DEBUG) System.out.println("[CLIENT]  ModifyMyInfos()");
			return getSIP().modifyContactInfos(new RequestModifyContactInfos(contact)).getContact();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, 
					"Impossibile modificare le impostazioni personali. \n" +
					"Il SIP è probabilmente offline. Occorre attendere che torni online.", 
					"Modifica informazioni personali", 
					JOptionPane.WARNING_MESSAGE);
			//System.err.println("ClientEngine.Login() exception: " + e.toString());
			//e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * @return Riferimento al SIP
	 */
	public static SIPInterface getSIP(){
		Registry registry;
		SIPInterface sip = null;
		try {
			if(Status.DEBUG) System.out.println("[CLIENT] Tentativo getSIP() SIP: "+Status.getSIPAddress()+":"+Status.getSIP_Port());
			registry = LocateRegistry.getRegistry(Status.getSIPAddress());
			sip = (SIPInterface) registry.lookup("SIP");
			if(sip == null)
				Status.setSIPStatusOnline(false);
		} catch (RemoteException e) {
			System.out.println("ClientEngine.getSIP() : RemoteException");
			Status.setSIPStatusOnline(false);
			return null;
			//JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.getSIP()", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.getSIP() exception: " + e.toString());
			//e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println("ClientEngine.getSIP() : NotBoundException");
			Status.setSIPStatusOnline(false);
			return null;
			//JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.getSIP()", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.getSIP() exception: " + e.toString());
			//e.printStackTrace();
		}
		
		try {
			sip.areYouAliveSIP();
		} catch (RemoteException e) {
			System.out.println("ClientEngine.getSIP() : RemoteException (in chiamata)");
			Status.setSIPStatusOnline(false);
			return null;
		}
		
		return sip;
	}
	
	/**
	 * Permette di ottenere lo stub verso un oggetto client remoto di un contatto che si ha nella propria lista contatti (quindi amico)
	 * senza controllarne la variabile di stato della connessione
	 * @return Riferimento al Client
	 */
	public static ClientInterface getClientWithoutOfflineControl(int ContactUserID){
		String IP = null;
		//Cerco il contatto nella propria lista contatti
		Contact contact = ContactListManager.searchContactById(ContactUserID);
		//Se è null il contatto non è stato trovato
		if(contact == null){
			System.out.println("[CLIENT] ClientInterface.getClientWithoutOfflineControl() - Utente "+ContactUserID+" non presente nella lista contatti!");
			return null;
		}
		Registry registry;
		ClientInterface client = null;
		if(contact.getGlobalIP() == null)
			return null;
		try {
			
			if(contact.getGlobalIP().equals(Status.getGlobalIP())){
				if(Status.DEBUG) System.out.println("[CLIENT] Tentativo ClientInterface.getClientWithoutOfflineControl() Client: "+contact.getLocalIP()+":"+contact.getClient_Port());
				IP = contact.getLocalIP();
			}else{
				if(Status.DEBUG) System.out.println("[CLIENT] Tentativo ClientInterface.getClientWithoutOfflineControl() Client: "+contact.getGlobalIP()+":"+contact.getClient_Port());
				IP = contact.getGlobalIP();
			}

			if(IP == null)
				return null;
			registry = LocateRegistry.getRegistry(IP);
		
			client = (ClientInterface) registry.lookup("Client");
			if(client == null){
				System.out.println("[CLIENT] ClientEngine.getClientWithoutOfflineControl() registry.lookup(\"Client\") == NULL");
				return null;
			}
			
		} catch (java.rmi.ConnectException e) {
			//Quando sull'host non risponde l'rmiregistry
			//Ritengo quindi che l'utente sia andato offline
			contact.setStatus(ChatStatusList.OFFLINE);
			System.err.println("[CLIENT] Client: Utente["+contact.getID()+" "+contact.getNickname()+"] e' OFFLINE! (ConnectException)");
			//Aggiorno la tabella
			FriendsList_Table table = LayoutReferences.getFriendsListTable();
			
			if(table!=null) 
				table.updateTable(); 
			
//			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.getClient() java.rmi.ConnectException", JOptionPane.ERROR_MESSAGE);
			System.err.println("ClientEngine.getClientWithoutOfflineControl("+ContactUserID+") java.rmi.ConnectException: "/* + e.toString()*/);
			//e.printStackTrace();
			return null;
		} catch (RemoteException e) {
//			JOptionPane.showMessageDialog(null, e.getMessage(), "[CLIENT] ClientEngine.getClient() RemoteException", JOptionPane.ERROR_MESSAGE);
			System.err.println("[CLIENT] ClientEngine.getClientWithoutOfflineControl("+ContactUserID+") RemoteException: "/* + e.toString()*/);
			//e.printStackTrace();
			return null;
		} catch (NotBoundException e) {
//			JOptionPane.showMessageDialog(null, e.getMessage(), "[CLIENT] ClientEngine.getClient() NotBoundException", JOptionPane.ERROR_MESSAGE);
			System.err.println("[CLIENT] ClientEngine.getClientWithoutOfflineControl("+ContactUserID+") NotBoundException: "/* + e.toString()*/);
			//e.printStackTrace();
			return null;
		}
		
		int userID = -1;
		
		try {
			userID = client.whatIsYourID(new RMIBasicMessage());
		} catch (RemoteException e) {
			System.err.println("[CLIENT] ClientEngine.getClientWithoutOfflineControl("+ContactUserID+") RemoteException: " + e.toString());
			contact.setStatus(ChatStatusList.OFFLINE);
			return null;
		}
		
		if(userID != ContactUserID ){
			System.err.println("[CLIENT] ClientEngine.getClientWithoutOfflineControl("+ContactUserID+"): l'utente contattato ha id "+userID);
			contact.setStatus(ChatStatusList.OFFLINE);
			return null;
		}
		
		return client;
	}
	
	/**
	 * Permette di ottenere lo stub verso un oggetto client remoto di un contatto che si ha nella propria lista contatti (quindi amico)
	 * @return Riferimento al Client
	 */
	public static ClientInterface getClient(int ContactUserID){
		String IP = null;
		//Cerco il contatto nella propria lista contatti
		Contact contact = ContactListManager.searchContactById(ContactUserID);
		//Se è null il contatto non è stato trovato
		if(contact == null){
			System.out.println("[CLIENT] ClientInterface.getClient() - Utente "+ContactUserID+" non presente nella lista contatti!");
			return null;
		}
		if(!contact.isConnected()){
			System.out.println("Il contatto "+contact.getNickname()+" è OFFLINE non lo contatterò");
			return null;
		}
		Registry registry;
		ClientInterface client = null;
		try {
			
			if(contact.getGlobalIP().equals(Status.getGlobalIP())){
				if(Status.DEBUG) System.out.println("[CLIENT] Tentativo ClientInterface.getClient() Client: "+contact.getLocalIP()+":"+contact.getClient_Port());
				IP = contact.getLocalIP();
			}else{
				if(Status.DEBUG) System.out.println("[CLIENT] Tentativo ClientInterface.getClient() Client: "+contact.getGlobalIP()+":"+contact.getClient_Port());
				IP = contact.getGlobalIP();
			}
			if(IP == null)
				return null;
			registry = LocateRegistry.getRegistry(IP);
			client = (ClientInterface) registry.lookup("Client");
			if(client == null){
				System.out.println("[CLIENT] ClientEngine.getClient() registry.lookup(\"Client\") == NULL");
				return null;
			}
			
		} catch (java.rmi.ConnectException e) {
			//Quando sull'host non risponde l'rmiregistry
			//Ritengo quindi che l'utente sia andato offline
			contact.setStatus(ChatStatusList.OFFLINE);
			System.err.println("[CLIENT] Client: Utente["+contact.getID()+" "+contact.getNickname()+"] e' OFFLINE! (ConnectException)");
			//Aggiorno la tabella
			FriendsList_Table table = LayoutReferences.getFriendsListTable();
			if(table!=null) 
				table.updateTable(); 
			
			//JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.getClient() java.rmi.ConnectException", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.getSIP() exception: " + e.toString());
			//e.printStackTrace();
		} catch (RemoteException e) {
//			JOptionPane.showMessageDialog(null, e.getMessage(), "[CLIENT] ClientEngine.getClient() RemoteException", JOptionPane.ERROR_MESSAGE);
			System.err.println("[CLIENT] ClientEngine.getClient() RemoteException: " + e.toString());
			//e.printStackTrace();
		} catch (NotBoundException e) {
//			JOptionPane.showMessageDialog(null, e.getMessage(), "[CLIENT] ClientEngine.getClient() NotBoundException", JOptionPane.ERROR_MESSAGE);
			System.err.println("[CLIENT] ClientEngine.getClient() NotBoundException: " + e.toString());
			//e.printStackTrace();
		} catch (Exception e) {
//			JOptionPane.showMessageDialog(null, e.getMessage(), "[CLIENT] ClientEngine.getClient() Exception", JOptionPane.ERROR_MESSAGE);
			System.err.println("[CLIENT] ClientEngine.getClient() Exception: " + e.toString());
			//e.printStackTrace();
		}
		
		int userID = -1;
		
		try {
			userID = client.whatIsYourID(new RMIBasicMessage());
		} catch (RemoteException e) {
			System.err.println("[CLIENT] ClientEngine.getClient() RemoteException: " + e.toString());
			contact.setStatus(ChatStatusList.OFFLINE);
			return null;
		}
		
		if(userID != ContactUserID ){
			System.err.println("[CLIENT] ClientEngine.getClient("+ContactUserID+"): l'utente contattato ha id "+userID);
			contact.setStatus(ChatStatusList.OFFLINE);
			return null;
		}
		
		return client;
	}
	
	/**
	 * Permette di ottenere lo stub verso un qualsiasi oggetto client remoto di un contatto dato l'ip
	 * @return Riferimento al Client
	 */
	public static ClientInterface getClient(String IP){
		if(IP == null || IP.length()==0)
			return null;
		Registry registry;
		ClientInterface client = null;
		try {
			if(Status.DEBUG) System.out.println("[CLIENT] Tentativo ClientInterface.getClient(IP) Client: "+IP);
			registry = LocateRegistry.getRegistry(IP);
			client = (ClientInterface) registry.lookup("Client");
			if(client == null){
				System.out.println("[CLIENT] ClientEngine.getClient(IP) registry.lookup(\"Client\") == NULL");
				return null;
			}
			
		} catch (java.rmi.ConnectException e) {
			System.err.println("[CLIENT] ClientEngine.getClient(IP) Contatto OFFLINE! (ConnectException)");
			//JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.getClient() java.rmi.ConnectException", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.getSIP() exception: " + e.toString());
			//e.printStackTrace();
		} catch (RemoteException e) {
//			JOptionPane.showMessageDialog(null, e.getMessage(), "[CLIENT] ClientEngine.getClient(IP) RemoteException", JOptionPane.ERROR_MESSAGE);
			System.err.println("[CLIENT] ClientEngine.getClient(IP) RemoteException: " + e.toString());
			//e.printStackTrace();
		} catch (NotBoundException e) {
//			JOptionPane.showMessageDialog(null, e.getMessage(), "[CLIENT] ClientEngine.getClient(IP) NotBoundException", JOptionPane.ERROR_MESSAGE);
			System.err.println("[CLIENT] ClientEngine.getClient(IP) NotBoundException: " + e.toString());
			//e.printStackTrace();
		}
		return client;
	}
	
	/**
	 * Permette di ottenere l'IP di un contatto data l'email richiedendolo alla rete P2P
	 * @return randomNum Numero random della richiesta, assieme all'id del richiedente fornisce l'identificatore della richiesta
	 */
	public static int whois(String email){
		Contact contactToSearch = ContactListManager.searchContactByEmail(email);
		int randomNum = -1;
		if(contactToSearch == null || contactToSearch.getStatus()==ChatStatusList.OFFLINE){
			randomNum = 1+(int)(Math.random()*9999);
			if(Status.DEBUGINLAN)
				new ClientThread_WhoisRequestor(Status.getUserID(),Status.getLocalIP(),randomNum,Status.P2P_TTL,email);
			else
				new ClientThread_WhoisRequestor(Status.getUserID(),Status.getGlobalIP(),randomNum,Status.P2P_TTL,email);
		}else{
			//Contatto trovato e online
			System.out.println("[CLIENT] ClientEngine.whoIs() : Contatto trovato e ONLINE.");
		}
		return randomNum;
	}
	
//	/**
//	 * Restituisce la lista dei messaggi di 
//	 * Chat in uscita. 
//	 * @return elenco messaggi chat in uscita
//	 */
//	public static ArrayList<Message> getOUTList() {	
//		return OUT_ChatMessageList;	
//	}
//	
//	/**
//	 * Restituisce la lista dei messaggi di 
//	 * Chat in ingresso. 
//	 * @return elenco messaggi chat in ingresso
//	 */
//	public static ArrayList<Message> getINList() {		
//		return IN_ChatMessageList;	
//	}

}


