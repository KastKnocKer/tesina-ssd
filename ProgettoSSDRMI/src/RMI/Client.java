package RMI;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import layout.friendslist.FriendsList_Table;
import layout.managers.ConversationWindowsManager;
import layout.managers.LayoutReferences;
import managers.ContactListManager;
import managers.FriendshipManager;
import managers.Status;
import managers.StatusP2P;
import RMIMessages.FriendshipRequest;
import RMIMessages.FriendshipRequestType;
import RMIMessages.RMIBasicMessage;
import RMIMessages.RMIBasicResponseMessage;
import RMIMessages.RequestHowAreYou;
import RMIMessages.ResponseHowAreYou;
import chat.ChatStatusList;
import chat.Contact;
import chat.Message;
import client.ClientEngine;
import client.thread.ClientThread;
import client.thread.ClientThread_WhoisRequestor;


public class Client implements ClientInterface{

	public Client() {}
	
	public ResponseHowAreYou howAreYou(RequestHowAreYou rhay) throws RemoteException {
		Contact senderContact = rhay.getSenderContact();
		int senderID = senderContact.getID();
		
		//Cerco il mittente nella mia lista contatti
		Contact localSenderContact = ContactListManager.searchContactById(senderID);
		//Se lo trovo aggiorno le informazioni che mi ha comunicato
		if(localSenderContact != null){
			//Aggiorno le informazioni ricevute
			boolean updateTable = !localSenderContact.isConnected();
			localSenderContact.updateInfoFromContact(senderContact);
			ConversationWindowsManager.updateOneContactInfos(localSenderContact);
			
			//Aggiorno la tabella se il contatto era per me offline in precedenza
			if(updateTable){
				FriendsList_Table table = LayoutReferences.getFriendsListTable();
				if(table!=null) 
					table.updateTable(); 
			}
			System.err.println("ResponseHowAreYou - we ARE friends");		
			return new ResponseHowAreYou(true, Status.getMyInfoIntoContact(),true);
		}else{
			System.err.println("ResponseHowAreYou - we are NOT friends");
			return new ResponseHowAreYou(true, Status.getMyInfoIntoContact(),false);
		}
		
//		for(Contact contact :  ContactListManager.getContactList() ){
//			if(contact.getID() == senderID){
//				//Aggiorno le informazioni ricevute
////				boolean updateTable = !contact.isConnected();
////				contact.updateInfoFromContact(senderContact);
////				ConversationWindowsManager.updateOneContactInfos(contact);
//				
//				//Aggiorno la tabella se il contatto era per me offline in precedenza
////				if(updateTable){
////					FriendsList_Table table = LayoutReferences.getFriendsListTable();
////					if(table!=null) 
////						table.updateTable(); 
////				}
////				break;
//			}
//		}
//		return new ResponseHowAreYou(true, Status.getMyInfoIntoContact());
	}
	
	public RMIBasicResponseMessage sendMessageToContact(Message[] chatMsgs, String senderGlobalIP, String senderLocalIP) throws RemoteException {

		/* Se ricevo il messaggio da un contatto che non è online, 
		 * lo rimetto ONLINE */
		if(chatMsgs != null || chatMsgs.length>0){
			
			int senderID = chatMsgs[0].getFrom();
			Contact contact = ContactListManager.searchContactById(senderID);
				contact.setGlobalIP(senderGlobalIP);
				contact.setLocalIP(senderLocalIP);
				//Il contatto deve esser per forza raggiungibile se mi ha contattato quindi lo metto ONLINE
				if(!contact.isConnected()){
					contact.setStatus(ChatStatusList.ONLINE);
					//Aggiorno la tabella
					FriendsList_Table table = LayoutReferences.getFriendsListTable();
					if(table!=null) 
						table.updateTable(); 
				}
		}

		/* mostro il messaggio ricevuto */
		for(Message msg : chatMsgs)
			ClientEngine.receiveMessageFromContact(msg);
		
		return new RMIBasicResponseMessage(true, "OK");
	}

	@Override
	public RMIBasicResponseMessage receiveFriendshipRequestFromContact(
			Contact contattoRichiedente, 
			String emailDestinatario) throws RemoteException {
		try {
			
			System.out.println("sendFriendshipRequest: sto per mostrare le finestra al contatto ricevente.");

			if(emailDestinatario != Status.getEmail()) {
				System.err.println("[CLIENT] receiveFriendshipRequestFromContact: ho ricevuto una richiesta di amicizia rivolta a '" + emailDestinatario + "', ma io sono '" + Status.getEmail() + "'.");
				return new RMIBasicResponseMessage(false, "Siamo spiacenti. Si è verificato un errore nel recapito \n" +
						"della richiesta di amicizia: è stato raggiunto il contatto errato."); 
			}
			
			contattoRichiedente.printInfo(); 
			FriendshipManager.showFriendshipRequestFromContact(contattoRichiedente);
		} catch (Exception e) {
			System.err.println("Si è verificata un'eccezione eseguendo: FriendshipManager.showFriendshipRequestFrom(contattoRichiedente); ");
			e.printStackTrace(); 
		}
		
		System.out.println("Richiesta di amicizia inviata correttamente a " + contattoRichiedente.getEmail() + ".");
		return new RMIBasicResponseMessage(true, "Richiesta inviata correttamente");
	}

//	@Override
//	public RMIBasicResponseMessage sendFriendshipRequestToSIP(
//			Contact contattoRichiedente, Contact contattoRicevente)
//			throws RemoteException {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public RMIBasicResponseMessage receiveFriendshipAckFromContact(
			Contact contattoDestinatario) throws RemoteException {

		/* Aggiungo l'amico alla mia lista amici, ed eseguo il refresh 
		 * della tabella con la lista amici. */
		ContactListManager.addToContactList(contattoDestinatario); 
		LayoutReferences.getFriendsListTable().updateTable(); 
		
		/* Lancio un howAreYou sfruttando il ClienThread */
		ClientThread.setModifiedInfos(true); 
		
		/* Invio friendshipRequest al SIP (se è offline, 
		 * devo salvarlo in una coda e ritentare periodicamente) */
		try {
			FriendshipRequest request = new FriendshipRequest(
					FriendshipRequestType.ADD_FRIEND, 
					Status.getMyInfoIntoContact(),
					contattoDestinatario
					);
			FriendshipManager.sendFriendshipRequestToSIP(request); 
		} catch (Exception e) {
			System.err.println("Client.receiveFriendshipAckFromContact(): error while " +
					"executing 'FriendshipManager.sendFriendshipRequestToSIP(request)' ");
			e.printStackTrace(); 
		}
		
		JOptionPane.showMessageDialog(null, "Il contatto " + contattoDestinatario.getNickname() + " ( " + 
				contattoDestinatario.getEmail() + " )  \nha accettato la tua richiesta di amicizia.", 
				"Aggiungi contatto", JOptionPane.INFORMATION_MESSAGE);
		
		return new RMIBasicResponseMessage(true, "L'amicizia è stata confermata correttamente all'utente " + Status.getEmail() + ".");
	}

	public void whois(int requestorUserID, String requestorGlobalIP,int requestorNum, int TTL, String emailToSearch) throws RemoteException {
		System.out.println("Ho ricevuto una richiesta WHOIS() da: "+requestorUserID+ "  -  "+requestorGlobalIP+" - TTL: "+TTL);
		new ClientThread_WhoisRequestor(requestorUserID, requestorGlobalIP, requestorNum, TTL, emailToSearch);
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

	public int whatIsYourID(RMIBasicMessage rmibm) throws RemoteException {
		return Status.getUserID();
	}

	
}
