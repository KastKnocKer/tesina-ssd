package RMI;


import java.rmi.Remote;
import java.rmi.RemoteException;

import RMIMessages.RMIBasicResponseMessage;
import RMIMessages.RequestHowAreYou;
import RMIMessages.ResponseHowAreYou;

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
	 * Chiede all'utente contattato il suo stato, comunicando anche il proprio
	 */
	ResponseHowAreYou howAreYou(RequestHowAreYou rhay) throws RemoteException;
	
	/**
	 * Permette l'invio dei messaggi di chat
	 */
	RMIBasicResponseMessage sendMessageToContact(Message[] chatMsgs, String senderGlobalIP, String senderLocalIP) throws RemoteException;
	
	/**
	 * Invio richiesta di amicizia direttamente ad un altro client.
	 */
	RMIBasicResponseMessage sendFriendshipRequest(Contact contattoRichiedente) throws RemoteException;
	
	/**
	 * Permette di effettuare il retrieval di un contatto in modo P2P
	 */
	public Contact whois(String email, int TTL) throws RemoteException;
}