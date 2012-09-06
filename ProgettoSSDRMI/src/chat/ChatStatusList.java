package chat;

/**
 * Elenco di possibili stati dell'utente in chat.
 * Nonostante il nome, non c'entra nulla con la classe Status, 
 * che contiene invece delle funzioni di utilit� per la gestione
 * dell'applicazione. 
 * 
 * @author Fabio Pierazzi
 *
 */
public enum ChatStatusList {
	ONLINE, 
	BUSY,
	AWAY,
	OFFLINE,
	WAITING
}
