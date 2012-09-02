package RMI;


import java.rmi.Remote;
import java.rmi.RemoteException;

import RMIMessages.RMIBasicResponseMessage;

import chat.Contact;
import chat.Message;


public interface ClientInterface extends Remote{
	
	String sayHello() throws RemoteException;
	
	/**
	 * Invia un messaggio ad un altro client
	 */
	boolean sendMSG(Message msg) throws RemoteException;
	
	/**
	 * Chiede all'utente contattato di identificarsi
	 */
	Contact whoAreYou() throws RemoteException;
	
	/**
	 * Chiede all'utente contattato il suo stato
	 */
	Contact howAreYou() throws RemoteException;
	
	/**
	 * Permette l'invio dei messaggi di chat
	 */
	RMIBasicResponseMessage sendMessageToContact(Message[] chatMsgs) throws RemoteException;
	
}