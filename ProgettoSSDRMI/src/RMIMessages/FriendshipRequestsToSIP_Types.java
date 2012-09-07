package RMIMessages;

/**
 * Elenco dei possibili valori delle richieste che un 
 * Client potrebbe fare al SIP in merito alla gestione
 * delle amicizie. 
 * 
 * @author Fabio Pierazzi
 */
public enum FriendshipRequestsToSIP_Types {
	/* Richiede l'aggiunta di un amico */
	ADD_FRIEND,
	/* Richiede la rimozione di un amico */
	REMOVE_FRIEND,
	/* forza l'aggiunta di un amicizia; usato se il SIP è offline */
	FORCE_ADD_FRIEND 
}
