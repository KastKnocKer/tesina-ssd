package managers;

import java.util.ArrayList;

import chat.Contact;
import chat.FriendsList;
import client.ClientEngine;

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

	/** 
		 * Mediante questo metodo, il sistema carica da file di configurazione
		 * la lista amici (senza filtri), ed essa viene mostrata all'interno
		 * della tabella lista amici dell'interfaccia grafica.
		 * 
		 * @author Fabio Pierazzi
		 */
		// TODO da perfezionare
		public static synchronized void loadFriendsList() {
			
			// TODO Controllare che esista il file CONTACTS.xml
			// TODO Se esiste, caricare i contatti dal file altrimenti richiedere al sip
			ArrayList<Contact> contactList = ContactListManager.getContactList(); 
			
			FriendsList fl = new FriendsList(); 
			
			for(Contact contact : contactList) {
				fl.addFriend(contact.getFriend()); 
			}
			
			setFriendsList(fl); 
		}
		
		/**
		 * Carica la lista amici andando a ricaricare i contatti
		 * chiedendoli al SIP.
		 */
		public static void loadFriendsListFromSIP() {
			ClientEngine.LoadContactsFromSIP();
		}

}
