package RMI;




import java.rmi.RemoteException;
import java.util.ArrayList;

import chat.Contact;

import RMIMessages.RMIBasicMessage;
import SIP.DBConnection;

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

	public boolean login(String username, String password) throws RemoteException {
		DBConnection dbConn = new DBConnection();
		return dbConn.login(username, password);
	}

	public ArrayList<Contact> getMyContacts(RMIBasicMessage msg) {
		DBConnection dbConn = new DBConnection();
		return dbConn.getMyContacts(msg);
	}


}
