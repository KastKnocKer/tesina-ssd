package RMI;
import java.rmi.RemoteException;
import java.util.ArrayList;

import managers.ContactListManager;
import managers.FriendshipManager;
import managers.Status;

import RMIMessages.RMIBasicResponseMessage;
import RMIMessages.RequestHowAreYou;
import RMIMessages.ResponseHowAreYou;
import chat.Contact;
import chat.Message;
import chat.ChatStatusList;
import client.ClientEngine;
import client.ClientThreadWhoIsRequestor;


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
				contact.updateInfoFromContact(senderContact);
//				Status.getContactList().remove(contact);
//				Status.getContactList().add(senderContact);
				break;
			}
		}
		return new ResponseHowAreYou(true, Status.getMyInfoIntoContact());
	}
	
	public RMIBasicResponseMessage sendMessageToContact(Message[] chatMsgs, String senderGlobalIP, String senderLocalIP) throws RemoteException {
		for(Message msg : chatMsgs)ClientEngine.receiveMessageFromContact(msg);
		if(chatMsgs != null || chatMsgs.length>0){
			int senderID = chatMsgs[0].getFrom();
			for(Contact contact :  ContactListManager.getContactList() ){
				if(contact.getID() == senderID){
					contact.setGlobalIP(senderGlobalIP);
					contact.setLocalIP(senderLocalIP);
					break;
				}
			}
		}
		return new RMIBasicResponseMessage(true, "OK");
	}

	@Override
	public RMIBasicResponseMessage sendFriendshipRequest(
			Contact contattoRichiedente) throws RemoteException {
		try {
			System.out.println("sendFriendshipRequest: sto per mostrare le finestra al contatto ricevente.");
			contattoRichiedente.printInfo(); 
			FriendshipManager.showFriendshipRequestFrom(contattoRichiedente);
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
	public RMIBasicResponseMessage sendFriendshipAckToContact(
			Contact contattoRicevente) throws RemoteException {

		// TODO : aggiungo il contatto alla lista amici
		
		// TODO: ricarico la tabella della lista amici
		
		// TODO: invio friendshipRequest al SIP (se è offline, devo salvarlo in una coda e ritentare periodicamente) 
		
		return new RMIBasicResponseMessage(true, "L'amicizia è stata confermata correttamente all'utente " + Status.getEmail() + ".");
	}


	public void whois(int requestorUserID, String requestorGlobalIP,int requestorNum, int TTL, String emailToSearch) throws RemoteException {
		System.out.println("Ho ricevuto WHOIS() da: "+requestorUserID+ "  -  "+requestorGlobalIP);
		new ClientThreadWhoIsRequestor(requestorUserID, requestorGlobalIP, requestorNum, TTL, emailToSearch);
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

	@Override
	public RMIBasicResponseMessage sendFriendshipRequestToContact(
			Contact contattoRichiedente) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
}
