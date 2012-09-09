package RMIMessages;

import chat.Contact;

/**
 * Messaggio per comunicare al SIP:
 * - nuove richieste di amicizia
 * - eliminazione di un amico
 * 
 * @author Fabio Pierazzi
 */
public class FriendshipRequest extends RMIBasicMessage {

	/** Il tipo della richiesta che voglio inviare al SIP */
	private FriendshipRequest_Types requestType = null;
	/** Mittente della richiesta al SIP */
	private Contact contattoMittente;  
	/** Contatto a cui è riferito il messaggio (chi voglio aggiungere/rimuovere) */
	private Contact contattoDestinatario; 
	
	/**
	 * Costruttore di un messaggio del tipo FriendshipRequest
	 * 
	 * @param requestType tipo della richiesta di amicizia (può essere anche una richiesta di rimozione)
	 * @param contattoMittente di colui che ha richiesto l'amicizia
	 * @param contattoDestinatario di colui che ha ricevuto ola richiesta di amicizia
	 */
	public FriendshipRequest(
			FriendshipRequest_Types requestType,
			Contact contattoMittente, 
			Contact contattoDestinatario) {
		
		super(); 
		
		this.setRequestType(requestType); 
		this.setContattoMittente(contattoMittente); 
		this.setContattoDestinatario(contattoDestinatario); 
		
	}

	public FriendshipRequest_Types getRequestType() {
		return requestType;
	}

	public void setRequestType(FriendshipRequest_Types requestType) {
		this.requestType = requestType;
	}

	public Contact getContattoMittente() {
		return contattoMittente;
	}

	public void setContattoMittente(Contact contattoMittente) {
		this.contattoMittente = contattoMittente;
	}

	public Contact getContattoDestinatario() {
		return contattoDestinatario;
	}

	public void setContattoDestinatario(Contact contattoDestinatario) {
		this.contattoDestinatario = contattoDestinatario;
	}
	
}
