package SIP;

import Chat.Contact;

public interface DBEngine {
	
	public boolean insertContact(Contact contact);	//Registrazione
	public boolean modifyContact(Contact contact);	//Modifica dati utente
	public String requestFriendship(String fromEmail, String toEmail);
	

}
