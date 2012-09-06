package managers;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import RMIMessages.RMISIPBasicResponseMessage;
import chat.Contact;
import chat.Status;
import client.ClientEngine;

/**
 * Classe static per la gestione delle richieste d'amicizia. 
 * Il protocollo sarà descritto maggiormente nel dettaglio su un documento a parte. 
 * 
 * @author Fabio Pierazzi
 *
 */
public class FriendshipManager {

	/**
	 * Richiede l'amicizia ad un altro contatto
	 * @author Fabio Pierazzi
	 */
	public static RMISIPBasicResponseMessage sendFriendshipRequestToContact(String email){
		try {
			
			/* **********************************************
			 * 1. whois email? ask SIP and p2p network 
			 * **********************************************/
			if(Status.DEBUG) 
				System.out.println("Client - [whois] - Richiesta a SIP del contatto avente mail: " + email);
			
			/* Voglio ottenere dal Server SIP l'IP di un contatto data la sua Email. */
			Contact futureFriend = ClientEngine.getSIP().whois(email); 
			
			if(Status.DEBUG) {
				System.out.println("Client - [whois] - risultato: ");
				futureFriend.printInfo(); 
			}
			
			if(futureFriend.getGlobalIP() == null || futureFriend.getGlobalIP().equals("")) {
				return new RMISIPBasicResponseMessage(false, "Siamo spiacenti; non è stato possibile reperire il contatto avente email:\n" + email); 
			}
			
			
			/* **********************************************
			 * 2. send friendship request (try client, then eventually SIP)
			 * **********************************************/
			Contact myContact = new Contact(); 
			myContact.setID(Status.getUserID()); 
			myContact.setNickname(Status.getNickname()); 
			myContact.seteMail(Status.getEmail());
			myContact.setGlobalIP(Status.getGlobalIP()); 
			myContact.setLocalIP(Status.getLocalIP()); 
			
			if( futureFriend.getID() < 0 ) 
				return new RMISIPBasicResponseMessage(false, "Errore di sistema: id contatto " + email + " non valido"); 
			
			
			// TODO scommenta
//			ClientInterface clientInterface = ClientEngine.getClient(futureFriend.getID());
//			
//			if(clientInterface == null) {
//				return new RMISIPBasicResponseMessage(false, "Si è verificato un errore nel corso del reperimento dello stub del contatto."); 
//			} else {
//				clientInterface.sendFriendshipRequest(myContact);
//			}
			// TODO da qui in poi lascia commento
			
//			System.err.println("Mostro finestra di richiesta amicizia");
//			FriendshipManager.showFriendshipRequestFrom(myContact);
			
			if(Status.DEBUG) 
				System.out.println("Client - Richiesta di amicizia a: " + email);
			
//			return ClientEngine.getSIP().askFriendship(new RequestFriendshipMessage(email));
			return new RMISIPBasicResponseMessage(true, "Richiesta inviata correttamente"); 

		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.RequestFriendship() exception", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace(); 
			return new RMISIPBasicResponseMessage(false, "ClientEngine.RequestFriendship() exception");
		} catch (Exception e) {
//			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.RequestFriendship() exception", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace(); 
			return new RMISIPBasicResponseMessage(false, "ClientEngine.RequestFriendship() exception");
		}
	}
	
	/** 
	 * La richiesta d'amicizia effettiva viene inviata al SIP solo in due casi: 
	 * 1) il client con cui voglio stringere direttamente amicizia è offline (quindi il metodo sendFriendshipRequestToContact è fallito)
	 * 2) come "acknoledgment" di amicizia stretta: i due client, se riescono a stringere amicizia in maniera esclusivamente p2p, 
	 * poi mandano entrambi una friendship-request al SIP, per notificargli l'avvenuta amicizia. Vedendo due friendship requests dai due
	 * client, il SIP capirà che hanno stretto amicizia. 
	 * 
	 * @author Fabio Pierazzi 
	 */
	public static void sendFriendshipRequestToSIP() {
		
	}
	
	/**
	 * Metodo per mostrare la richiesta di amicizia inviata dal contatto
	 * passato come parametro. Questo metodo viene invocato in seguito 
	 * all'invocazione remota sul client del metodo per fare richiesta. 
	 * 
	 * @param contattoRichiedente che ha inviato la richiesta
	 */
	public static void showFriendshipRequestFrom(Contact contattoRichiedente) {
		
		System.out.println("Ricevuto richiesta amicizia da: " + contattoRichiedente.geteMail() + " " +
				"(IP: " + contattoRichiedente.getGlobalIP() + ")");
		
		int result = JOptionPane.showConfirmDialog(null, "Hai ricevuto una richiesta di amicizia da: \n" +
				"" + contattoRichiedente.geteMail() + "\n\n" +
						"Desideri accettare?", 
						"Aggiungi Contatto",
                JOptionPane.YES_NO_OPTION);
		
		if(result == JOptionPane.YES_OPTION) {
			System.out.println("ACCETTATA - Richiesta di amicizia proveniente da " + contattoRichiedente.geteMail() + "");
			FriendshipManager.acceptFriendshipRequest(); 
		}
		else if(result == JOptionPane.NO_OPTION)  {
			if(Status.DEBUG)
				System.out.println("RIFIUTATA - Richiesta di amicizia proveniente da " + contattoRichiedente.geteMail() + "");
			// TODO: Avvisa il SIP
			// ClientEngine.getSIP().remove/refuseFriendship() (Do not notify the other client)
		}
			
		//		JFrame frame = new JFrame();
//	    String message = "message";
//	    int answer = JOptionPane.showConfirmDialog(frame, message);
//	    if (answer == JOptionPane.YES_OPTION) {
//	      // User clicked YES.
//	    } else if (answer == JOptionPane.NO_OPTION) {
//	      // User clicked NO.
//	    }
	    
//		final JOptionPane optionPane = new JOptionPane(
//			    "Hai ricevuto una richiesta di amicizia da " + contattoRichiedente.getNickname() + " ( " + contattoRichiedente.geteMail() + ". \n " +
//			    		"Desideri accettare?",
//			    JOptionPane.QUESTION_MESSAGE,
//			    JOptionPane.YES_NO_OPTION);
//		
//		optionPane.setVisible(true); 
		// TODO: gestire il fatto che l'altro client cada o cambi ip nel mentre
	}
	
	/**
	 * 
	 * @param myContact
	 */
	public static void acceptFriendshipRequest() {
		Contact myContact = Status.getMyInfoIntoContact(); 
//		ClientEngine.getClient(contattoRichiedente.getID()).sendFriendshipAck(myContact); 
		// TODO: Aggiungi l'amico nella FriendsListGlobale
	}
	
	
	/**
	 * Funzione per aggiungere un nuovo amico all'interno della propria 
     * lista contatti. Il contatto viene aggiunto, e viene aggiornata la
     * tabella contenente la lista amici. 
     *  
	 * @param newContact, nuovo contatto da aggiungere alla propria friendsList. 
	 */
	public static void addNewFriend(Contact newContact) {
		
		
		
	}
}
