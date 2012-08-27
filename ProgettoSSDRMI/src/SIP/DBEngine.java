package SIP;

import RMIMessages.RMIBasicMessage;
import RMIMessages.RequestFriendshipMessage;
import chat.Contact;

public interface DBEngine {
	
	public boolean insertContact(Contact contact);							//Registrazione
	public boolean modifyContact(Contact contact);							//Modifica dati utente
	public String requestFriendship(RequestFriendshipMessage msg);			//Richiesta d'amicizia
	public boolean login(String username, String password);					//Login
	public Contact[] getMyContacts(RMIBasicMessage msg);					//Richiesta dei propri contatti
	

}
