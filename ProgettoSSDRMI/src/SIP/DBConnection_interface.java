package SIP;

import java.util.ArrayList;

import RMIMessages.FriendshipRequest;
import RMIMessages.RMIBasicMessage;
import RMIMessages.RMISIPBasicResponseMessage;
import RMIMessages.RequestFriendshipMessage;
import RMIMessages.RequestLoginMessage;
import RMIMessages.ResponseLoginMessage;
import chat.Contact;
import chat.ChatStatusList;

/**
 * Interfaccia per DB Connection
 * @autor Andrea Castelli
 *
 */
public interface DBConnection_interface {
	
	/**
	 * Usata in fase di registrazione di un nuovo utente. 
	 * 
	 * @param contact
	 * @return
	 */
	public boolean insertContact(Contact contact);												

	/**
	 * Modifica i dati di un contatto. Usato quando l'utente modifica i 
	 * propri dati personali tramite l'applicazione (nickname, status, avatar..)
	 * 
	 * @param contact
	 * @return
	 */
	public boolean modifyContact(Contact contact);												

	/**
	 * Aggiorna lo stato di connessione di un contatto.
	 * 
	 * @param UserID
	 * @param PublicIP
	 * @param LocalIP
	 * @param rmiregistryPort
	 * @param clientport
	 * @param status
	 * @return
	 */
	public boolean updateContactConnectionStatus(int UserID, String PublicIP, String LocalIP, int rmiregistryPort, int clientport, ChatStatusList status);		

//	/**
//	 * TODO: REMOVE.
//	 * 
//	 * Richiesta d'amicizia senza conferma. 
//	 * 
//	 * @param msg
//	 * @return
//	 */
//	public RMISIPBasicResponseMessage requestFriendship(RequestFriendshipMessage msg);
	
	/**
	 * Gestisce il login. 
	 * 
	 * @param rlm
	 * @return
	 */
	public ResponseLoginMessage login(RequestLoginMessage rlm);						
	
	/**
	 * Richiesta dei propri contatti dal server SIP, che restituisce quelli
	 * considerati legittimi andando a vedere la tabella con le amicizie. 
	 * 
	 * @param msg 
	 * @return lista dei propri contatti 
	 */
	public ArrayList<Contact> getMyContacts(RMIBasicMessage msg);								
	

	/**
	 * Aggiunge un'amicizia nel database. 
	 * 
	 * @param request : se il requestType è ADD_FRIEND, aggiunge una richiesta di amicizia in sospeso; 
	 * se il requestType è FORCE_ADD_FRIEND, aggiunge una richiesta di amicizia senza aspettare feedback dell'altro contatto. 
	 * @return true, se va a buon fine
	 */
	public boolean addFriendship(FriendshipRequest request); 
	
	/**
	 * Rimuove un'amicizia dal database. 
	 * 
	 * @param request, la richiesta che contiene informazioni su mittente e destinatario della richiesta
	 * di rimozione dell'amicizia (requestType = REMOVE_FRIEND) 
	 * @return true, se va a buon fine
	 */
	public boolean removeFriendship(FriendshipRequest request); 
	
	/**
	 * Setta l'utente come offline
	 */
	public boolean setContactOffline(int userID);
	
	/**
	 * Ritorna le amicizie in attesa di accettazione di un utente dato l'id
	 */
	public ArrayList<FriendshipRequest> getPendingFriendships(int userID);

}
