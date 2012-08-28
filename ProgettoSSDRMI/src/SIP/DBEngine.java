package SIP;

import java.util.ArrayList;

import RMIMessages.RMIBasicMessage;
import RMIMessages.RMISIPBasicResponseMessage;
import RMIMessages.RequestFriendshipMessage;
import RMIMessages.ResponseLoginMessage;
import chat.Contact;

public interface DBEngine {
	
	public boolean insertContact(Contact contact);												//Registrazione
	public boolean modifyContact(Contact contact);												//Modifica dati utente
	public RMISIPBasicResponseMessage requestFriendship(RequestFriendshipMessage msg);			//Richiesta d'amicizia
	public ResponseLoginMessage login(String username, String password);						//Login
	public ArrayList<Contact> getMyContacts(RMIBasicMessage msg);								//Richiesta dei propri contatti
	

}
