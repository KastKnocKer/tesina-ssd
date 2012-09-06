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
	 * Invio richiesta di amicizia per un altro contatto al SIP.
	 *
	 * @param contattoRichiedente contatto di chi ha richiesto l'amicizia
	 * @param contattoRicevente contatto di chi ha ricevuto la richiesta di amicizia
	 * @return messaggio di risposta al client per comunicare eventuali errori 
	 * @throws RemoteException
	 * 
	 * @author Fabio Pierazzi
	 */
	RMIBasicResponseMessage sendFriendshipRequestToSIP(Contact contattoRichiedente, Contact contattoRicevente) throws RemoteException; 
	
	/**
	 * Invio conferma per la richiesta di amicizia direttamente ad un altro
	 * contatto. Questo per far sì che possano aggiungersi come amici anche 
	 * se il SIP è offline. 
	 * 
	 * @param contattoRicevente il contatto di chi ha accettato la richiesta di amicizia inviata dal 
	 * contattoRichiedente. 
	 * 
	 *  @author Fabio Pierazzi
	 */
	RMIBasicResponseMessage sendFriendshipAckToContact(Contact contattoRicevente) throws RemoteException; 
	
	/**
	 * Permette di effettuare il retrieval di un contatto in modo P2P
	 */
	public void whois(int requestorUserID, String requestorGlobalIP,int requestorNum, int TTL, String emailToSearch) throws RemoteException;
}