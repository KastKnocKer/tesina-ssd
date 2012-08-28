package test;
import java.util.ArrayList;

import chat.Contact;
import chat.StatusList;
import RMIMessages.RMIBasicMessage;
import RMIMessages.RequestFriendshipMessage;
import SIP.DBConnection;


public class MAINPERPROVA {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DBConnection dbconn = new DBConnection();
		dbconn.connetti();
		
//		dbconn.eseguiQuery("SELECT * FROM user");
//		
//		//dbconn.insertContact(new Contact("Nickname", "Nome", "Cognome", "eMail","password", "GlobalIP", null));
//		
//		//dbconn.requestFriendship(new RequestFriendshipMessage("mailz", "eMail"));
//		
//		ArrayList<Contact> listaContatti = dbconn.getMyContacts(new RMIBasicMessage());
//		
//		dbconn.login("email", "password");
		
		System.out.println(StatusList.AWAY==StatusList.AWAY);
		
	}

}
