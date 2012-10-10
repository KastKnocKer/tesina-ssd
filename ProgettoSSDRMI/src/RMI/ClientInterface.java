package RMI;


import java.rmi.Remote;
import java.rmi.RemoteException;

import RMIMessages.RMIBasicMessage;
import RMIMessages.RMIBasicResponseMessage;
import RMIMessages.RequestHowAreYou;
import RMIMessages.ResponseHowAreYou;

import chat.Contact;
import chat.Message;


public interface ClientInterface extends Remote{
	
	
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
	 * @param contattoRichiedente contatto di chi ha chiesto l'amicizia
	 */
	RMIBasicResponseMessage receiveFriendshipRequestFromContact(Contact contattoRichiedente) throws RemoteException;
	

//	/**
//	 * Invio richiesta di amicizia per un altro contatto al SIP.
//	 *
//	 * @param contattoRichiedente contatto di chi ha richiesto l'amicizia
//	 * @param contattoRicevente contatto di chi ha ricevuto la richiesta di amicizia
//	 * @return messaggio di risposta al client per comunicare eventuali errori 
//	 * @throws RemoteException
//	 * 
//	 * @author Fabio Pierazzi
//	 */
//	RMIBasicResponseMessage sendFriendshipRequestToSIP(Contact contattoRichiedente, Contact contattoRicevente) throws RemoteException; 
	
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
	RMIBasicResponseMessage receiveFriendshipAckFromContact(Contact contattoRicevente) throws RemoteException; 
	
	/**
	 * Permette di effettuare il retrieval di un contatto in modo P2P
	 */
	public void whois(int requestorUserID, String requestorGlobalIP,int requestorNum, int TTL, String emailToSearch) throws RemoteException;

	/**
	 * Risponde alla richiesta whoisP2P
	 */
	public void whoisResponse(int requestorUserID, int responseFromID, int requestorNum, Contact contact) throws RemoteException;

	
	/**
	 * Metodo usato per notificare che un altro amico mi ha rimosso
	 * dalla lista contatti. Utile solo nel momento in cui entrambi
	 * i contatti coinvolti sono online
	 * 
	 * @param contattoMittente di chi mi ha rimosso dalla lista amici. 
	 * @return true, se va a buon fine
	 */
	public boolean receiveFriendshipRemovalNotificationFromContact(Contact contattoMittente) throws RemoteException; 
	
	/**
	 * Comunica la volontà di uscire dal sistema
	 */
	public boolean iGoOffline(RMIBasicMessage rmibm) throws RemoteException;
	
	/**
	 * Comunica il proprio ID
	 */
	public int whatIsYourID(RMIBasicMessage rmibm) throws RemoteException;
}