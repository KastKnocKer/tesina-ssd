package RMI;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import RMIMessages.RMIBasicMessage;
import RMIMessages.RMIBasicResponseMessage;
import RMIMessages.RMISIPBasicResponseMessage;
import RMIMessages.RequestFriendshipMessage;
import RMIMessages.RequestLoginMessage;
import RMIMessages.RequestModifyContactInfos;
import RMIMessages.ResponseLoginMessage;
import RMIMessages.ResponseModifyContactInfos;

import chat.Contact;
import chat.Message;
import chat.StatusList;

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
	ResponseLoginMessage login(RequestLoginMessage rlm) throws RemoteException;
	
	/**
	 * Richiede la lista dei contatti al SIP
	 */
	ArrayList<Contact> getMyContacts(RMIBasicMessage msg) throws RemoteException;
	
	/**
	 * Richiede l'amicizia di un nuovo contatto
	 */
	RMISIPBasicResponseMessage askFriendship(RequestFriendshipMessage requestFriendshipMessage) throws RemoteException;
	
	/**
	 * Aggiorna sul DB i dati dell'utente
	 */
	ResponseModifyContactInfos modifyContactInfos(RequestModifyContactInfos rmci) throws RemoteException;
	
}