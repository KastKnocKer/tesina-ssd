package RMI;




import java.rmi.RemoteException;
import java.util.ArrayList;

import RMIMessages.FriendshipRequest;
import RMIMessages.FriendshipRequestType;
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
	
	
	public boolean areYouAliveSIP() throws RemoteException {
		return true;
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
		//aggiungo le possibili richieste di amicizia pendenti
		resplm.setFriendshipRequestList(dbConn.getPendingFriendships(resplm.getLoggedContact().getID()));
		return resplm;
	}

	public ArrayList<Contact> getMyContacts(RMIBasicMessage msg) {
		DBConnection dbConn = new DBConnection();
		return dbConn.getMyContacts(msg);
	}

//	public RMISIPBasicResponseMessage askFriendship(RequestFriendshipMessage requestFriendshipMessage) throws RemoteException {
//		DBConnection dbConn = new DBConnection();
//		return dbConn.requestFriendship(requestFriendshipMessage);
//	}
	
	
	public ResponseModifyContactInfos modifyContactInfos(RequestModifyContactInfos rmci) throws RemoteException{
		DBConnection dbConn = new DBConnection();
		//TODO Sviluppi futuri : controllare l'autenticazione dell'utente
		boolean success = dbConn.modifyContact(rmci.getContact());
		return new ResponseModifyContactInfos(true,"OK",rmci.getContact());
	}
	
	/**
	 * Metodo per invocare l'aggiunta di un'amicizia sul DB. 
	 * @author Fabio Pierazzi
	 */
	@Override
	public synchronized RMISIPBasicResponseMessage addFriendship(FriendshipRequest request)
			throws RemoteException {

		DBConnection dbConn = new DBConnection();
		boolean response = dbConn.addFriendship(request);
		return new RMISIPBasicResponseMessage(response, ""); 
	}
	
	
	public synchronized RMISIPBasicResponseMessage removeFriendship(FriendshipRequest request) {
		
		DBConnection dbConn = new DBConnection();
		boolean response = dbConn.removeFriendship(request); 
		return new RMISIPBasicResponseMessage(response, ""); 
	}

	/**
	 * Restituisce il contatto relativo all'email passata come parametro. 
	 * @author Fabio Pierazzi
	 */
	public Contact whois(String email) {
		DBConnection dbConn = new DBConnection();
		return dbConn.getContactByEmail(email);
	}
	
	
//	/**
//	 * Metodo per aggiungere/rimuovere amicizia fra contatto mittente e contatto destinatario. 
//	 * @param contattoMittente di colui che ha richiesto l'amicizia (o la sua rimozione)
//	 * @param contattoDestinatario di colui che ha ricevuto la richiesta d'amicizia (o la sua rimozione)
//	 * @return valore booleano true\false accompagnato da un messaggio testuale che è possibile mostrare in dialog box
//	 */
//	public RMISIPBasicResponseMessage manageFriendshipRequest(FriendshipRequest request) {
//		
//		/* Controllo se è valido il tipo di richiesta */
//		if(request.getRequestType() == null) {
//			System.err.println("La richiesta di aggiunta/rimozione amicizia non è di un tipo valido.");
//			return new RMISIPBasicResponseMessage(false, "La richiesta di aggiunta/rimozione amicizia non è di un tipo valido."); 
//		}
//		
//		DBConnection dbConn = new DBConnection();
//		
//		if(request.getRequestType() == FriendshipRequest_Types.ADD_FRIEND) {
//			
//		} else if(request.getRequestType() == FriendshipRequest_Types.FORCE_ADD_FRIEND) {
//			
//		} else if(request.getRequestType() == FriendshipRequest_Types.REMOVE_FRIEND) {
//			
//		} else {
//			return null; 
//		}
//		return null; 
//	}

	public boolean iGoOffline(RMIBasicMessage rmibm) throws RemoteException {
		DBConnection dbConn = new DBConnection();
		return dbConn.setContactOffline(rmibm.getRequestorUserID());
	}


}
