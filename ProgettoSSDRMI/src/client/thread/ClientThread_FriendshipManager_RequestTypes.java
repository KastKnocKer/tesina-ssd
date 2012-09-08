package client.thread;

/**
 * Parametro usato per differenziare le operazioni eseguite da
 * ClientThread_FriendshipManager. 
 * 
 * @author Fabio Pierazzi
 *
 */
public enum ClientThread_FriendshipManager_RequestTypes {
	SEND_FRIENDSHIP_REQUEST_TO_CONTACT,
	SEND_FRIENDSHIP_REQUEST_TO_SIP,
	ACCEPT_FRIENDSHIP_REQUEST,
	SHOW_FRIENDSHIP_REQUEST_FROM_CONTACT,
	ADD_NEW_FRIEND
}
