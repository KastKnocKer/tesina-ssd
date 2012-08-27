package test;
import chat.Contact;
import RMIMessages.RequestFriendshipMessage;
import SIP.DBConnection;


public class MAINPERPROVA {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DBConnection dbconn = new DBConnection();
		dbconn.connetti();
		
		dbconn.eseguiQuery("SELECT * FROM user");
		
		dbconn.insertContact(new Contact("Nickname", "Nome", "Cognome", "eMail","password", "GlobalIP", null));
		
		dbconn.requestFriendship(new RequestFriendshipMessage("mailz", "eMail"));
		
		dbconn.login("email", "password");
		
		
	}

}
