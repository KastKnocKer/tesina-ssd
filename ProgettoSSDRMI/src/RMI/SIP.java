package RMI;




import java.rmi.RemoteException;
import java.util.ArrayList;

import RMIMessages.RMIBasicMessage;
import RMIMessages.RMISIPBasicResponseMessage;
import RMIMessages.RequestFriendshipMessage;
import RMIMessages.RequestLoginMessage;
import RMIMessages.RequestModifyContactInfos;
import RMIMessages.ResponseLoginMessage;
import RMIMessages.ResponseModifyContactInfos;
import SIP.DBConnection;
import chat.Contact;

public class SIP implements SIPInterface{

	public SIP() {super();}
	
	public String sayHello() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean imAlive(RMIBasicMessage msg) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean imLeaving(RMIBasicMessage msg) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean register(String nome, String cognome, String email, String nickname, String password) throws RemoteException {
		Contact contact = new Contact(nickname, nome, cognome, email, password, null, null);
		DBConnection dbConn = new DBConnection();
		return dbConn.insertContact(contact);
	}

	public ResponseLoginMessage login(RequestLoginMessage rlm) throws RemoteException {
		DBConnection dbConn = new DBConnection();
		ResponseLoginMessage resplm = dbConn.login(rlm);
		//aggiorno l'userid in rlm per permettere il recupero della contact list
		rlm.setUserID(resplm.getLoggedContact().getID());
		resplm.setContactList(this.getMyContacts(rlm));
		return resplm;
	}

	public ArrayList<Contact> getMyContacts(RMIBasicMessage msg) {
		DBConnection dbConn = new DBConnection();
		return dbConn.getMyContacts(msg);
	}

	public RMISIPBasicResponseMessage askFriendship(RequestFriendshipMessage requestFriendshipMessage) throws RemoteException {
		DBConnection dbConn = new DBConnection();
		return dbConn.requestFriendship(requestFriendshipMessage);
	}
	
	
	public ResponseModifyContactInfos modifyContactInfos(RequestModifyContactInfos rmci) throws RemoteException{
		DBConnection dbConn = new DBConnection();
		//TODO Sviluppi futuri : controllare l'autenticazione dell'utente
		boolean success = dbConn.modifyContact(rmci.getContact());
		return new ResponseModifyContactInfos(true,"OK",rmci.getContact());
	}

	/**
	 * Restituisce il contatto relativo all'email passata come parametro. 
	 * @author Fabio Pierazzi
	 */
	public Contact whois(String email) {
		DBConnection dbConn = new DBConnection();
		return dbConn.getContactByEmail(email);
	}


}
