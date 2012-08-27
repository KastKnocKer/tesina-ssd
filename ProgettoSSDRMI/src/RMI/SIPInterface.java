package RMI;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import RMIMessages.RMIBasicMessage;

import chat.Contact;

public interface SIPInterface extends Remote{
	String sayHello() throws RemoteException;
	
	/**
	 * Il client indica al SIP il mio stato
	 */
	boolean imAlive(RMIBasicMessage msg) throws RemoteException;
	
	/**
	 * Il client indica al SIP di volersi disconnettere
	 */
	boolean imLeaving(RMIBasicMessage msg) throws RemoteException;
	
	
	//TODO
	/**
	 * Registrazione del client presso il SIP
	 */
	boolean register(String nome, String cognome,String email, String nickname, String password) throws RemoteException;
	
	/**
	 * Login del client presso il SIP
	 */
	boolean login(String username, String password) throws RemoteException;
	
	/**
	 * Richiede la lista dei contatti al SIP
	 */
	ArrayList<Contact> getMyContacts(RMIBasicMessage msg) throws RemoteException;
	
}