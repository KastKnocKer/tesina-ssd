package RMI;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import layout.friendslist.FriendsList_Table;
import layout.managers.LayoutReferences;
import managers.ContactListManager;
import managers.FriendshipManager;
import managers.Status;

import RMIMessages.RMIBasicMessage;
import RMIMessages.RMIBasicResponseMessage;
import RMIMessages.RequestHowAreYou;
import RMIMessages.ResponseHowAreYou;
import chat.Contact;
import chat.Message;
import chat.ChatStatusList;
import chat.StatusP2P;
import client.ClientEngine;
import client.thread.ClientThread_WhoisRequestor;


public class Client implements ClientInterface{

	public Client() {}
	
	@Override
	public String sayHello() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMSG(Message msg) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Contact whoAreYou() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}



	public ResponseHowAreYou howAreYou(RequestHowAreYou rhay) throws RemoteException {
		Contact senderContact = rhay.getSenderContact();
		int senderID = senderContact.getID();
		
		for(Contact contact :  ContactListManager.getContactList() ){
			if(contact.getID() == senderID){
				//Aggiorno le informazioni ricevute
				boolean updateTable = !contact.isConnected();
				contact.updateInfoFromContact(senderContact);
				
				//Aggiorno la tabella se il contatto era per me offline in precedenza
				if(updateTable){
					FriendsList_Table table = LayoutReferences.getFriendsListTable();
					if(table!=null) 
						table.updateTable(); 
				}
				break;
			}
		}
		return new ResponseHowAreYou(true, Status.getMyInfoIntoContact());
	}
	
	public RMIBasicResponseMessage sendMessageToContact(Message[] chatMsgs, String senderGlobalIP, String senderLocalIP) throws RemoteException {
		
		for(Message msg : chatMsgs)
			ClientEngine.receiveMessageFromContact(msg);
		
		if(chatMsgs != null || chatMsgs.length>0){
			
			int senderID = chatMsgs[0].getFrom();
			Contact contact = ContactListManager.searchContactById(senderID);
				contact.setGlobalIP(senderGlobalIP);
				contact.setLocalIP(senderLocalIP);
				//Il contatto deve esser per forza raggiungibile se mi ha contattato quindi lo metto AWAY
				if(!contact.isConnected()){
					contact.setStatus(ChatStatusList.AWAY);
					//Aggiorno la tabella
					FriendsList_Table table = LayoutReferences.getFriendsListTable();
					if(table!=null) 
						table.updateTable(); 
				}
		}
		return new RMIBasicResponseMessage(true, "OK");
	}

	@Override
	public RMIBasicResponseMessage receiveFriendshipRequestFromContact(
			Contact contattoRichiedente) throws RemoteException {
		try {
			System.out.println("sendFriendshipRequest: sto per mostrare le finestra al contatto ricevente.");
			contattoRichiedente.printInfo(); 
			FriendshipManager.showFriendshipRequestFromContact(contattoRichiedente);
		} catch (Exception e) {
			System.err.println("Si è verificata un'eccezione eseguendo: FriendshipManager.showFriendshipRequestFrom(contattoRichiedente); ");
			e.printStackTrace(); 
		}
		
		System.out.println("Richiesta di amicizia inviata correttamente a " + contattoRichiedente.getEmail() + ".");
		return new RMIBasicResponseMessage(true, "Richiesta inviata correttamente");
	}

	@Override
	public RMIBasicResponseMessage sendFriendshipRequestToSIP(
			Contact contattoRichiedente, Contact contattoRicevente)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RMIBasicResponseMessage receiveFriendshipAckFromContact(
			Contact contattoRicevente) throws RemoteException {

		/* Aggiungo l'amico alla mia lista amici, ed eseguo il refresh 
		 * della tabella con la lista amici. */
		ContactListManager.addToContactList(contattoRicevente); 
		LayoutReferences.getFriendsListTable().updateTable(); 
		
		JOptionPane.showMessageDialog(null, "Il contatto " + contattoRicevente.getNickname() + " ( " + 
				contattoRicevente.getEmail() + " )  \nha accettato la tua richiesta di amicizia.", 
				"Aggiungi contatto", JOptionPane.INFORMATION_MESSAGE);
		
		// TODO: invio friendshipRequest al SIP (se è offline, devo salvarlo in una coda e ritentare periodicamente) 
		
		return new RMIBasicResponseMessage(true, "L'amicizia è stata confermata correttamente all'utente " + Status.getEmail() + ".");
	}


	public void whois(int requestorUserID, String requestorGlobalIP,int requestorNum, int TTL, String emailToSearch) throws RemoteException {
		System.out.println("Ho ricevuto WHOIS() da: "+requestorUserID+ "  -  "+requestorGlobalIP+" - TTL: "+TTL);
		new ClientThread_WhoisRequestor(requestorUserID, requestorGlobalIP, requestorNum, TTL, emailToSearch);
//		Contact whoisContact;
//		//Controllo di conoscere il contatto
//		ArrayList<Contact> contactList = ContactListManager.getContactList();
//		for(Contact contact : contactList){
//			if(contact.geteMail().equals(emailToSearch)){
//				//Contatto trovato
//				ChatStatusList status = contact.getStatus();
//				if( (status == ChatStatusList.ONLINE)||(status == ChatStatusList.BUSY)||(status == ChatStatusList.AWAY)){
//					//Se il contatto è per me connesso lo ritorno
//					return contact;
//				}else{
//					//Altrimenti mando agli altri miei contatti la richiesta
//					break;
//				}
//			}
//		}
//		
//		//Se il Time To Live è 0 non interrogo i miei contatti
//		if(TTL == 0) return null;
//		
//		//Chiedo ai miei contatti se hanno informazioni sul contatto
//		for(Contact contact : contactList){
//			ChatStatusList status = contact.getStatus();
//			if( (status == ChatStatusList.ONLINE)||(status == ChatStatusList.BUSY)||(status == ChatStatusList.AWAY)){
//				//Se il contatto è per me connesso lo interrogo
//				ClientInterface client = ClientEngine.getClient(contact.getID());
//				whoisContact = client.whois(email, TTL-1);
//				//Se il contatto ritornato è diverso da null è stato trovato e quindi lo ritorno!
//				if(whoisContact != null) return contact;
//				
//			}else{
//				//Se il contatto non è connesso chiedo al prossimo
//				continue;
//			}
//		}
//		
//		
//		//Se non trovo nessun contatto ritorno null
//		return null;
	}
	
	public void whoisResponse(int requestorUserID, int responseFromID, int requestorNum, Contact contact) throws RemoteException {
		StatusP2P.addWhoisResponse(requestorUserID, requestorNum, responseFromID, contact);
	}
	
	@Override
	public boolean receiveFriendshipRemovalNotificationFromContact(Contact contattoMittente) {
		
		boolean result = ContactListManager.removeFromContactList(contattoMittente); 
		
		return result; 
	}

	
	public boolean iGoOffline(RMIBasicMessage rmibm) throws RemoteException {
		Contact contact = ContactListManager.searchContactById(rmibm.getRequestorUserID());
		if(contact != null){
			contact.setStatus(ChatStatusList.OFFLINE);
			FriendsList_Table table = LayoutReferences.getFriendsListTable();
			if(table!=null) 
				table.updateTable(); 
			return true;
		}
		return false;
	}

	
}
