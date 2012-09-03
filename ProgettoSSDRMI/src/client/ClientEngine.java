package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import RMI.ClientInterface;
import RMI.SIPInterface;
import RMIMessages.RMIBasicMessage;
import RMIMessages.RMISIPBasicResponseMessage;
import RMIMessages.RequestFriendshipMessage;
import RMIMessages.RequestLoginMessage;
import RMIMessages.RequestModifyContactInfos;
import RMIMessages.ResponseLoginMessage;
import chat.Contact;
import chat.Message;
import chat.Status;
import chat.StatusList;

import layout.managers.*;

public class ClientEngine {
	
	private static ArrayList<Message> OUTList = new ArrayList<Message>();
	private static ArrayList<Message> INList = new ArrayList<Message>();
	
	public static ResponseLoginMessage Login(String username, String password){
		ResponseLoginMessage response = null;
		//Login mediante server SIP
		try {
			if(Status.DEBUG) System.out.println("Client - Tentativo di login username: "+username+" password: "+password);
			response = getSIP().login(new RequestLoginMessage(username, password, StatusList.ONLINE));
			if(response.isSUCCESS()){
				Status.setUserID(response.getLoggedContact().getID());
				Status.setEmail(response.getLoggedContact().geteMail());
				Status.setNome(response.getLoggedContact().getNome());
				Status.setCognome(response.getLoggedContact().getCognome());
				Status.setNickname(response.getLoggedContact().getNickname());
				Status.setLOGGED(true);
				
				//Aggiorno i dati del LastLogin su Status
				Status.setLastLoginUsername(username);
				Status.setLastLoginPassword(password);
				Status.writeConfXML();
			}
			return response;
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.Login() exception", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.Login() exception: " + e.toString());
			//e.printStackTrace();
			return new ResponseLoginMessage(false, "ClientEngine.Login() exception", null);
		}
	}
	
	public static boolean RegisterNewAccount(String nome, String cognome,String email, String nickname, String password) {
		boolean response = false;
		try {
			if(Status.DEBUG) System.out.println("Client - Tentativo registrazione nuovo utente: "+nome+", "+cognome+", "+email+", "+nickname+", ["+password+"]");
			response = getSIP().register(nome, cognome, email, nickname, password);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.RegisterNewAccount() exception", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.Login() exception: " + e.toString());
			//e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * Richiede al sip l'intera lista dei propri conatti, aggiornando la lista dei contatti in Status
	 */
	public static boolean LoadContactsFromSIP(){
		boolean response = false;
		try {
			if(Status.DEBUG) System.out.println("Client - Richiesta lista contatti al SIP");
			ArrayList<Contact> contacts = getSIP().getMyContacts(new RMIBasicMessage());
			if(contacts != null){
				Status.setContactList(contacts);
				response = true;
			}
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.LoadContactsFromSIP() exception", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.Login() exception: " + e.toString());
			//e.printStackTrace();
			return false;
		}
		return response;
	}
	
	/**
	 * Richiede l'amicizia ad un altro contatto
	 */
	public static RMISIPBasicResponseMessage RequestFriendship(String email){
		try {//TODO
			if(Status.DEBUG) System.out.println("Client - Richiesta di amicizia a: "+email);
			return getSIP().askFriendship(new RequestFriendshipMessage(email));
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.RequestFriendship() exception", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.Login() exception: " + e.toString());
			//e.printStackTrace();
			return new RMISIPBasicResponseMessage(false, "ClientEngine.RequestFriendship() exception");
		}
	}
	
	/**
	 * Invia un messaggio ad un contatto
	 */
	public static synchronized boolean sendMessageToContact(Message chatMsg){
		System.out.println("Client - Inserimento in OUTList del messaggio da:" +chatMsg.getFrom()+ " a:"+ chatMsg.getTo() +" Messaggio: "+chatMsg.getMessage());
		OUTList.add(chatMsg);
		return true;
	}
	/**
	 * Riceve un messaggio da un contatto
	 */
	public static synchronized boolean receiveMessageFromContact(Message chatMsg){
		INList.add(chatMsg);
		System.out.println("---> CLIENT - Ho ricevuto il messaggio da: " +chatMsg.getFrom()+ " a:"+ chatMsg.getTo() +" Messaggio: "+chatMsg.getMessage()+" Messaggi INLIST: "+INList.size());
		
		/* Mostro la finestra se arriva il messaggio */
		Contact contact = Status.searchContactById(chatMsg.getFrom()); 
		ConversationWindowsManager.showConversationFrame(contact); 
		
		/* Scrivo il messaggio nella finestra */
		ConversationWindowsManager.writeToConversationFrame(chatMsg.getFrom(), chatMsg.getMessage());
		
		return true;
	}
	
	/**
	 * Consegna tutti i messaggi in coda al client di destinazione
	 */
	public static synchronized boolean deliverAllMessages(){
		//Se la coda è vuota ritorno subito
		if(OUTList.size() == 0) return true;
		//Altrimenti
		int temporaryClientTarget = -1;	//Inizializzo così entra nel ciclo while almeno una volta
		System.out.println("*************************************************************");
		ArrayList<Message> MessagesToDeliver = null;
		while( (temporaryClientTarget < 0) || (OUTList.size()>5) ){
			temporaryClientTarget = -1;
			MessagesToDeliver = new ArrayList<Message>();
			for(int i=0; i<OUTList.size(); i++){
				Message msg = OUTList.get(i);
				if(temporaryClientTarget<0){
					temporaryClientTarget = msg.getTo();
					System.out.println("Servo i messaggi verso l'utente: "+temporaryClientTarget);
				}
				if(msg.getTo() == temporaryClientTarget){
					System.out.println("In consegna: " +msg.getMessage());
					MessagesToDeliver.add(msg);
					OUTList.remove(msg);
					i--;
				}
					
				System.out.println("Deliver message - " + msg.getMessage());
			}
			
			
			
			ClientThreadSender cts = new ClientThreadSender(MessagesToDeliver);
			cts.start();
			
			System.err.println("Consegna di "+MessagesToDeliver.size()+" messaggi. Messaggi residui: "+OUTList.size());
		}
		
		
		return true;
	}
	
	
	/**
	 * Richiede la modifica delle proprie informazioni
	 */
	public static Contact ModifyMyInfos(Contact contact){
		try {
			if(Status.DEBUG) System.out.println("Client -  ModifyMyInfos()");
			return getSIP().modifyContactInfos(new RequestModifyContactInfos(contact)).getContact();
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.ModifyMyInfos() exception", JOptionPane.ERROR_MESSAGE);
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
			if(Status.DEBUG) System.out.println("Client - Tentativo getSIP() SIP: "+Status.getSIPAddress()+":"+Status.getSIP_Port());
			registry = LocateRegistry.getRegistry(Status.getSIPAddress());
			sip = (SIPInterface) registry.lookup("SIP");
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.getSIP()", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.getSIP() exception: " + e.toString());
			//e.printStackTrace();
		} catch (NotBoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.getSIP()", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.getSIP() exception: " + e.toString());
			//e.printStackTrace();
		}
		return sip;
	}
	
	/**
	 * @return Riferimento al Client
	 */
	public static ClientInterface getClient(int ContactUserID){
		Contact contact = null;
		for(Contact cont : Status.getContactList()){
			if(cont.getID() == ContactUserID){
				contact = cont;
				if(cont.getStatus() == StatusList.OFFLINE){
					return null;
				}
				break;
			}
		}
		if(contact == null) return null;
		Registry registry;
		ClientInterface client = null;
		try {
			
			
			if(contact.getGlobalIP().equals(Status.getGlobalIP())){
				if(Status.DEBUG) System.out.println("Client - Tentativo getClient() Client: "+contact.getLocalIP()+":"+contact.getClient_Port());
				registry = LocateRegistry.getRegistry(contact.getLocalIP());
			}else{
				if(Status.DEBUG) System.out.println("Client - Tentativo getClient() Client: "+contact.getGlobalIP()+":"+contact.getClient_Port());
				registry = LocateRegistry.getRegistry(contact.getGlobalIP());
			}
				
			
			client = (ClientInterface) registry.lookup("Client");
		} catch (java.rmi.ConnectException e) {
			//Quando sull'host non risponde l'rmiregistry
			//Ritengo quindi che l'utente sia andato offline
			contact.setStatus(StatusList.OFFLINE);
			System.err.println("Client: Utente["+contact.getID()+" "+contact.getNickname()+"] e' OFFLINE!");
			
			
			//JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.getClient() java.rmi.ConnectException", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.getSIP() exception: " + e.toString());
			//e.printStackTrace();
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.getClient()", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.getSIP() exception: " + e.toString());
			//e.printStackTrace();
		} catch (NotBoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.getClient()", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.getSIP() exception: " + e.toString());
			//e.printStackTrace();
		}
		return client;
	}
	
	
	

	public static ArrayList<Message> getOUTList() {		return OUTList;	}
	public static ArrayList<Message> getINList() {		return INList;	}

}
