package RMIMessages;

import chat.Contact;

/**
 * Messaggio per comunicare al SIP:
 * - nuove richieste di amicizia
 * - eliminazione di un amico
 * 
 * @author Fabio Pierazzi
 */
public class FriendshipRequestsToSIP extends RMIBasicMessage {

	/** Il tipo della richiesta che voglio inviare al SIP */
	private FriendshipRequestsToSIP_Types requestType = null;
	/** Mittente della richiesta al SIP */
	private Contact fromWhom;  
	/** Contatto a cui è riferito il messaggio (chi voglio aggiungere/rimuovere) */
	private Contact toWhom; 
	
	public FriendshipRequestsToSIP() {
		super(); 
	}
	
}
