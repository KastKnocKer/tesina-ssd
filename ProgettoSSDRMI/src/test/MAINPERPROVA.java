package test;
import chat.Contact;
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
		
		dbconn.requestFriendship("mailz", "eMail");
		
		dbconn.login("email", "password");
		
		
	}

}
