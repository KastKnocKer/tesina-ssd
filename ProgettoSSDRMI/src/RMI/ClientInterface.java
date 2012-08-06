package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Chat.Contact;
import Chat.Message;

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
	
}