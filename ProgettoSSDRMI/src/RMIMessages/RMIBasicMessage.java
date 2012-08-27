package RMIMessages;

import java.io.Serializable;

import chat.Status;
/**
 * Classe messaggio utilizzata per il maggior numero di richieste tra client-sip e client-client
 * Creata per offrire a tutti i messaggi il servizio di autenticazione del mittente\richiedente
 * Utilizzata in particolare nella sua forma non estesa per l'invio di richieste RMI senza il passaggio di parametri
 */
public class RMIBasicMessage implements Serializable {

	private int UserID;
	
	public RMIBasicMessage(){
		
	}

}
