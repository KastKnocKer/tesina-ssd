package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.ProtectionDomain;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import utility.RMIResponse;

import RMI.ClientInterface;
import RMI.SIPInterface;
import RMIMessages.RMIBasicMessage;
import RMIMessages.RMISIPBasicResponseMessage;
import RMIMessages.RequestFriendshipMessage;
import RMIMessages.RequestLoginMessage;
import RMIMessages.ResponseLoginMessage;
import chat.Contact;
import chat.Message;
import chat.Status;
import chat.StatusList;

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
	public static /*synchronized*/ boolean receiveMessageFromContact(Message chatMsg){
		System.out.println("OthClient - Ho ricevuto il messaggio da: " +chatMsg.getFrom()+ " a:"+ chatMsg.getTo() +" Messaggio: "+chatMsg.getMessage());
		INList.add(chatMsg);
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
			
			
			
			Message[] messagesToDeliver = new Message[MessagesToDeliver.size()];
			for(int i=0; i<MessagesToDeliver.size(); i++){
				messagesToDeliver[i] = MessagesToDeliver.get(i);
			}
			try {
				//TODO modificare con GetClient
				SIPInterface client = getSIP();
				client.sendMessageToContact(messagesToDeliver);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			System.err.println("Consegna di "+MessagesToDeliver.size()+" messaggi. Messaggi residui: "+OUTList.size());
		}
		
		
		
		
		
		
		
		return true;
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
	public static ClientInterface getClient(Contact contact){
		Registry registry;
		ClientInterface client = null;
		try {
			if(Status.DEBUG) System.out.println("Client - Tentativo getClient() Client: "+contact.getGlobalIP()+":"+contact.getClient_Port());
			registry = LocateRegistry.getRegistry(contact.getGlobalIP());
			client = (ClientInterface) registry.lookup("Client");
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

	public static ArrayList<Message> getOUTList() {
		return OUTList;
	}

	public static ArrayList<Message> getINList() {
		return INList;
	}



}
