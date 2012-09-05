package RMI;
import java.rmi.RemoteException;

import Friendship.FriendshipManager;
import RMIMessages.RMIBasicResponseMessage;
import RMIMessages.RequestHowAreYou;
import RMIMessages.ResponseHowAreYou;
import chat.Contact;
import chat.Message;
import chat.Status;
import client.ClientEngine;


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
		for(Contact contact :  Status.getContactList() ){
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
			for(Contact contact :  Status.getContactList() ){
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
		
		System.out.println("Richiesta di amicizia inviata correttamente a " + contattoRichiedente + ".");
		return new RMIBasicResponseMessage(true, "Richiesta inviata correttamente");
	}

}
