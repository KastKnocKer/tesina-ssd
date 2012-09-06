package managers;

import chat.FriendsList;

/**
 * Manager static della FriendList
 * 
 * @author Fabio Pierazzi
 */
public class FriendsListManager {

	static FriendsList friendsList = null;

	/** Metodo per reperire la lista amici globale 
	 * @return lista amici
	 */
	public static FriendsList getFriendsList() {
		return friendsList;
	}

	/**
	 * Metodo per impostare il riferimento globale alla lista amici 
	 * dell'utente.
	 * @param lista amici
	 */
	public static void setFriendsList(FriendsList fl) {
		friendsList = fl; 
	}

}
