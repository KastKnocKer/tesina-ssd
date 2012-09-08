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
import chat.ChatStatusList;

/**
 * 
 * @author Andrea Castelli
 */
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
	 * Richiede informazioni su un contatto avente una 
	 * certa email. Sfruttata, ad esempio, in fase di
	 * richiesta d'amicizia. 
	 * 
	 * @param email del contatto da recuperare
	 * @return un'istanza della classe Contact, con tutte le info a disposizione sul contatto, incluso il suo ultimo IP. 
	 * @throws RemoteException
	 * 
	 * @author Fabio Pierazzi
	 */
	public Contact whois(String email) throws RemoteException; 
	
	/**
	 * Richiede l'amicizia di un nuovo contatto
	 */
	RMISIPBasicResponseMessage askFriendship(RequestFriendshipMessage requestFriendshipMessage) throws RemoteException;
	
	/**
	 * Aggiorna sul DB i dati dell'utente
	 */
	ResponseModifyContactInfos modifyContactInfos(RequestModifyContactInfos rmci) throws RemoteException;
	
	/**
	 * Funzione usata per rimuovere l'amicizia fra due contatti. L'amicizia
	 * può essere rimossa arbitrariamente anche solo da uno dei due. 
	 * 
	 * @param idUser1 id del primo contatto che fa parte dell'amicizia da rimuovere
	 * @param idUser2 id del secondo contatto che fa parte dell'amicizia da rimuovere
	 * @return un messaggio di risposta, che comunica successo/fallimento della funzione
	 * @throws RemoteException
	 * @author Fabio Pierazzi 
	 */
	public RMISIPBasicResponseMessage removeFriendship(int idUser1, int idUser2) throws RemoteException;
	
}