package SIP;

import java.util.ArrayList;

import RMIMessages.RMIBasicMessage;
import RMIMessages.RMISIPBasicResponseMessage;
import RMIMessages.RequestFriendshipMessage;
import RMIMessages.RequestLoginMessage;
import RMIMessages.ResponseLoginMessage;
import chat.Contact;
import chat.ChatStatusList;

public interface DBEngine {
	
	public boolean insertContact(Contact contact);												//Registrazione
	public boolean modifyContact(Contact contact);												//Modifica dati utente
	public boolean updateContactConnectionStatus(int UserID, String PublicIP, String LocalIP, int rmiregistryPort, int clientport, ChatStatusList status);		//Aggiorna lo stato di connessione del contatto
	public RMISIPBasicResponseMessage requestFriendship(RequestFriendshipMessage msg);			//Richiesta d'amicizia
	public ResponseLoginMessage login(RequestLoginMessage rlm);						//Login
	public ArrayList<Contact> getMyContacts(RMIBasicMessage msg);								//Richiesta dei propri contatti


}
