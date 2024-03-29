package managers;

import RMIMessages.FriendshipRequest;
import RMIMessages.RMISIPBasicResponseMessage;
import chat.Contact;
import client.thread.ClientThread_FriendshipManager;
import client.thread.ClientThread_FriendshipManager_RequestTypes;

/**
 * Classe static per la gestione delle richieste d'amicizia. 
 * Il protocollo sar� descritto maggiormente nel dettaglio 
 * su un documento a parte. 
 * 
 * Questa classe ha in realt� lo scopo di fungere da interfaccia con 
 * il ClientThread_FriendshipManager, utile a non bloccare l'interfaccia
 * grafica durante lo svolgimento delle operazioni necessarie alla
 * richiesta di amicizia. 
 * 
 * @author Fabio Pierazzi
 *
 */
public class FriendshipManager {

	/**
	 * Richiede l'amicizia ad un altro contatto. In realt�, 
	 * funge da interfaccia per l'invocazione di un thread
	 * per l'invio di tale richiesta. 
	 * 
	 * @param email del destinatario dell'amicizia. 
	 * @author Fabio Pierazzi
	 */
	public static RMISIPBasicResponseMessage sendFriendshipRequestToContact(String email_destinatario){
		
		Contact contatto = new Contact(); 
		contatto.setEmail(email_destinatario); 
		
//		Contact contattoMittente = Status.getMyInfoIntoContact(); 
//		
		FriendshipRequest friendshipRequest = new FriendshipRequest(
				null, 
				contatto, 
				null);
		
		ClientThread_FriendshipManager thread = new ClientThread_FriendshipManager(
				ClientThread_FriendshipManager_RequestTypes.SEND_FRIENDSHIP_REQUEST_TO_CONTACT, 
				friendshipRequest); 
		
		thread.start(); 
		
		/* In realt� � stato inviato il solo tentativo di richiesta, a questo punto non � certo */
		return new RMISIPBasicResponseMessage(true, "Richiesta di amicizia verso " + email_destinatario + " inviata con successo."); 
	}
	
	/** 
	 * La richiesta d'amicizia effettiva viene inviata al SIP solo in due casi: 
	 * 1) il client con cui voglio stringere direttamente amicizia � offline (quindi il metodo sendFriendshipRequestToContact � fallito)
	 * 2) come "acknoledgment" di amicizia stretta: i due client, se riescono a stringere amicizia in maniera esclusivamente p2p, 
	 * poi mandano entrambi una friendship-request al SIP, per notificargli l'avvenuta amicizia. Vedendo due friendship requests dai due
	 * client, il SIP capir� che hanno stretto amicizia. 
	 * 
	 * @author Fabio Pierazzi 
	 */
	public static void sendFriendshipRequestToSIP(FriendshipRequest request) {
		
		ClientThread_FriendshipManager thread = new ClientThread_FriendshipManager(
				ClientThread_FriendshipManager_RequestTypes.SEND_FRIENDSHIP_REQUEST_TO_SIP, 
				request); 
		
		thread.start(); 
	}
	
	/**
	 * Metodo per mostrare la richiesta di amicizia inviata dal contatto
	 * passato come parametro. Questo metodo viene invocato in seguito 
	 * all'invocazione remota sul client del metodo per fare richiesta. 
	 * 
	 * @param contattoMittente, il contatto di chi ha inviato la richiesta
	 */
	public static void showFriendshipRequestFromContact(Contact contattoMittente) {

		FriendshipRequest friendshipRequest = new FriendshipRequest(
				null, 
				contattoMittente, 
				Status.getMyInfoIntoContact());

		ClientThread_FriendshipManager thread = new ClientThread_FriendshipManager(
				ClientThread_FriendshipManager_RequestTypes.SHOW_FRIENDSHIP_REQUEST_FROM_CONTACT, 
				friendshipRequest); 
		
		thread.start(); 
	}
	
//	/**
//	 * Accetto la richiesta di amicizia. 
//	 * @param contattoMittente di colui che ha inviato la richiesta inizialmente
//	 */
//	public static void acceptFriendshipRequest(Contact contattoMittente) {
//		
//		FriendshipRequest friendshipRequest = new FriendshipRequest(
//				null, 
//				contattoMittente, 
//				Status.getMyInfoIntoContact());
//		
//		ClientThread_FriendshipManager thread = new ClientThread_FriendshipManager(
//				ClientThread_FriendshipManager_RequestTypes.ACCEPT_FRIENDSHIP_REQUEST, 
//				friendshipRequest); 
//		
//		thread.start(); 
//	}
//	
	/**
	 * Funzione per aggiungere un nuovo amico all'interno della propria 
     * lista contatti. Il contatto viene aggiunto, e viene aggiornata la
     * tabella contenente la lista amici. 
     *  
	 * @param newContact, nuovo contatto da aggiungere alla propria friendsList. 
	 */
	public static void addNewFriend(Contact newContact) {
		
	}
	
	/**
	 * Lancio la richiesta per la rimozione 
	 * di un amico dalla lista. 
	 * 
	 * @param contactToRemove
	 */
	public static void removeFriend(Contact contactToRemove) {
		
		Contact contattoMittente = Status.getMyInfoIntoContact(); 
		
		FriendshipRequest friendshipRequest = new FriendshipRequest(
				null, 
				contattoMittente, 
				contactToRemove);
		
		ClientThread_FriendshipManager thread = new ClientThread_FriendshipManager(
				ClientThread_FriendshipManager_RequestTypes.REMOVE_FRIEND, 
				friendshipRequest); 
		
		thread.start(); 
	}
}

/************** VERSIONE PRECEDENTE *****************/

//package managers;
//
//import java.rmi.RemoteException;
//
//import javax.swing.JOptionPane;
//
//import layout.managers.LayoutReferences;
//import RMI.ClientInterface;
//import RMIMessages.RMISIPBasicResponseMessage;
//import chat.Contact;
//import client.ClientEngine;
//
///**
// * Classe static per la gestione delle richieste d'amicizia. 
// * Il protocollo sar� descritto maggiormente nel dettaglio 
// * su un documento a parte. 
// * 
// * @author Fabio Pierazzi
// *
// */
//public class FriendshipManager {
//
//	/**
//	 * Richiede l'amicizia ad un altro contatto
//	 * @author Fabio Pierazzi
//	 */
//	public static RMISIPBasicResponseMessage sendFriendshipRequestToContact(String email){
//		try {
//			
//			/* **********************************************
//			 * 1. whois email? ask SIP and p2p network 
//			 * **********************************************/
//			if(Status.DEBUG) 
//				System.out.println("[CLIENT] [whois] - Richiesta a SIP del contatto avente mail: " + email);
//			
//			/* Voglio ottenere dal Server SIP l'IP di un contatto data la sua Email. */
//			Contact futureFriend = ClientEngine.getSIP().whois(email); 
//			
//			if(Status.DEBUG) {
//				System.out.println("[CLIENT] [whois] - risultato: ");
//				futureFriend.printInfo(); 
//			}
//			
//			if(futureFriend.getGlobalIP() == null || futureFriend.getGlobalIP().equals("")) {
//				return new RMISIPBasicResponseMessage(false, "Siamo spiacenti; non � stato possibile reperire il contatto avente email:\n" + email); 
//			}
//			
//			
//			/* **********************************************
//			 * 2. send friendship request (try client, then eventually SIP)
//			 * **********************************************/
//			Contact myContact = Status.getMyInfoIntoContact(); 
//			
//			if( futureFriend.getID() < 0 ) 
//				return new RMISIPBasicResponseMessage(false, "Errore di sistema: id contatto " + email + " non valido"); 
//			
//			
//			// TODO scommenta
//			ClientInterface clientInterface = null;
//			
//			try {
//				if(Status.getGlobalIP().equals(futureFriend.getGlobalIP())) {
//
//					if(Status.DEBUG)
//						System.err.println("Chiedo amicizia a " + futureFriend.getLocalIP() + " nella mia Lan");
//					
//					clientInterface = ClientEngine.getClient(futureFriend.getLocalIP());
//				}
//				else {
//
//					if(Status.DEBUG)
//						System.err.println("Chiedo amicizia a " + futureFriend.getGlobalIP() + " fuori dalla mia Lan");
//					
//					clientInterface = ClientEngine.getClient(futureFriend.getGlobalIP()); 
//				}
//					
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//			
//			if(clientInterface == null) {
//				return new RMISIPBasicResponseMessage(false, "Si � verificato un errore nel corso del reperimento dello stub del contatto.");
//			} else {
//				clientInterface.sendFriendshipRequestToContact(myContact);
//			}
//			// TODO da qui in poi lascia commento
//			
////			System.err.println("Mostro finestra di richiesta amicizia");
////			FriendshipManager.showFriendshipRequestFrom(myContact);
//			
//			if(Status.DEBUG) 
//				System.out.println("[CLIENT] Richiesta di amicizia a: " + email);
//			
////			return ClientEngine.getSIP().askFriendship(new RequestFriendshipMessage(email));
//			return new RMISIPBasicResponseMessage(true, "Richiesta inviata correttamente"); 
//
//		} catch (RemoteException e) {
//			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.RequestFriendship() exception", JOptionPane.ERROR_MESSAGE);
//			e.printStackTrace(); 
//			return new RMISIPBasicResponseMessage(false, "ClientEngine.RequestFriendship() exception");
//		} catch (Exception e) {
////			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.RequestFriendship() exception", JOptionPane.ERROR_MESSAGE);
//			e.printStackTrace(); 
//			return new RMISIPBasicResponseMessage(false, "ClientEngine.RequestFriendship() exception");
//		}
//	}
//	
//	/** 
//	 * La richiesta d'amicizia effettiva viene inviata al SIP solo in due casi: 
//	 * 1) il client con cui voglio stringere direttamente amicizia � offline (quindi il metodo sendFriendshipRequestToContact � fallito)
//	 * 2) come "acknoledgment" di amicizia stretta: i due client, se riescono a stringere amicizia in maniera esclusivamente p2p, 
//	 * poi mandano entrambi una friendship-request al SIP, per notificargli l'avvenuta amicizia. Vedendo due friendship requests dai due
//	 * client, il SIP capir� che hanno stretto amicizia. 
//	 * 
//	 * @author Fabio Pierazzi 
//	 */
//	public static void sendFriendshipRequestToSIP() {
//		
//	}
//	
//	/**
//	 * Metodo per mostrare la richiesta di amicizia inviata dal contatto
//	 * passato come parametro. Questo metodo viene invocato in seguito 
//	 * all'invocazione remota sul client del metodo per fare richiesta. 
//	 * 
//	 * @param contattoRichiedente che ha inviato la richiesta
//	 */
//	public static void showFriendshipRequestFromContact(Contact contattoRichiedente) {
//		
//		System.out.println("Ricevuto richiesta amicizia da: " + contattoRichiedente.getEmail() + " " +
//				"(IP: " + contattoRichiedente.getGlobalIP() + ")");
//		
//		int result = JOptionPane.showConfirmDialog(null, "Hai ricevuto una richiesta di amicizia da: \n" +
//				"" + contattoRichiedente.getEmail() + "\n\n" +
//						"Desideri accettare?", 
//						"Aggiungi Contatto",
//                JOptionPane.YES_NO_OPTION);
//		
//		if(result == JOptionPane.YES_OPTION) {
//			System.out.println("ACCETTATA - Richiesta di amicizia proveniente da " + contattoRichiedente.getEmail() + "");
//			FriendshipManager.acceptFriendshipRequest(contattoRichiedente); 
//		}
//		else if(result == JOptionPane.NO_OPTION)  {
//			if(Status.DEBUG)
//				System.out.println("RIFIUTATA - Richiesta di amicizia proveniente da " + contattoRichiedente.getEmail() + "");
//			// TODO: Avvisa il SIP
//			// ClientEngine.getSIP().remove/refuseFriendship() (Do not notify the other client)
//		}
//			
//		//		JFrame frame = new JFrame();
////	    String message = "message";
////	    int answer = JOptionPane.showConfirmDialog(frame, message);
////	    if (answer == JOptionPane.YES_OPTION) {
////	      // User clicked YES.
////	    } else if (answer == JOptionPane.NO_OPTION) {
////	      // User clicked NO.
////	    }
//	    
////		final JOptionPane optionPane = new JOptionPane(
////			    "Hai ricevuto una richiesta di amicizia da " + contattoRichiedente.getNickname() + " ( " + contattoRichiedente.geteMail() + ". \n " +
////			    		"Desideri accettare?",
////			    JOptionPane.QUESTION_MESSAGE,
////			    JOptionPane.YES_NO_OPTION);
////		
////		optionPane.setVisible(true); 
//		// TODO: gestire il fatto che l'altro client cada o cambi ip nel mentre
//	}
//	
//	/**
//	 * 
//	 * @param myContact
//	 */
//	public static void acceptFriendshipRequest(Contact contattoRichiedente) {
//		
//		/* Invio ack dell'amicizia al contatto, 
//		 * per fargli sapere che pu� aggiungermi. */
//		Contact myContact = Status.getMyInfoIntoContact(); 
//		
//		// TODO: cosa succede se mentre rispondo al contatto, questi finisce offline? FORCE_ADD_FRIEND al SIP?
//		try {
//			
//			/* se sono in lan */
//			if(contattoRichiedente.getGlobalIP().equals(Status.getGlobalIP()))
//				ClientEngine.getClient(contattoRichiedente.getLocalIP()).sendFriendshipAckToContact(myContact);
//			/* se non sono in lan */
//			else
//				ClientEngine.getClient(contattoRichiedente.getGlobalIP()).sendFriendshipAckToContact(myContact);
//			
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		} 
//		
//		// TODO: invio ack dell'amicizia al SIP 
//		
//		/* Aggiungo l'amico alla mia lista amici, ed eseguo il refresh 
//		 * della tabella con la lista amici. */
//		ContactListManager.addToContactList(contattoRichiedente); 
//		LayoutReferences.getFriendsListTable().updateTable(); 
//		
//		// TODO: Problema: se il SIP � down, e torna up in un secondo momento, 
//		// io invio mia friend request, ma il contatto poi non risulta pi� fra i miei 
//		// amici fintanto che non accetta anche lui.
//		
//		// TODO: Se il SIP � offline, invio un FORCE_FRIEND_REQUEST
//	}
//	
//	
//	/**
//	 * Funzione per aggiungere un nuovo amico all'interno della propria 
//     * lista contatti. Il contatto viene aggiunto, e viene aggiornata la
//     * tabella contenente la lista amici. 
//     *  
//	 * @param newContact, nuovo contatto da aggiungere alla propria friendsList. 
//	 */
//	public static void addNewFriend(Contact newContact) {
//		
//		
//		
//	}
//}



