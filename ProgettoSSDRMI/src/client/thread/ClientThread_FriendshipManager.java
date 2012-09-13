package client.thread;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import layout.friendslist.FriendsList_Table;
import layout.managers.LayoutReferences;
import managers.ContactListManager;
import managers.Status;
import RMI.ClientInterface;
import RMIMessages.FriendshipRequest;
import RMIMessages.FriendshipRequest_Types;
import RMIMessages.RMISIPBasicResponseMessage;
import chat.Contact;
import client.ClientEngine;

/**
 * Thread usato per l'invio e la gestione delle
 * richieste di amicizia. 
 * 
 * @author Fabio Pierazzi
 *
 */
public class ClientThread_FriendshipManager extends Thread {

	/** Parametro in base al quale viene specificato il tipo di richiesta che dev'essere
	 * eseguita dal Thread */
	ClientThread_FriendshipManager_RequestTypes requestType = null;  
	/** Informazioni sul contatto che ha inviato in primo luogo la richiesta d'amicizia. 
	 * ATTENZIONE: potrebbe non contenere informazioni complete sul contatto. */
	Contact contattoMittente = null; 
	/** Informazioni sul contatto destinatario della richiesta di amicizia 
	 * ATTENZIONE: potrebbe non contenere informazioni complete sul contatto. */
	Contact contattoDestinatario = null; 
	
	/**
	 * Costruttore 
	 * @param requestType tipo di richiesta che consente di specializzare le operazioni da eseguire.
	 * @param contatto con cui interagire
	 */
	public ClientThread_FriendshipManager(
			ClientThread_FriendshipManager_RequestTypes requestType,
			Contact contattoMittente, 
			Contact contattoDestinatario) {
		this.requestType = requestType; 
		this.contattoMittente = contattoMittente;
		this.contattoDestinatario = contattoDestinatario; 
	}
	
	/**
	 * Metodo run del thread
	 */
	public void run() {
		
		/* A seconda del parametro, specializzo */
		System.err.println("Thread ClientThread_FriendshipManager: entrato");
		
		/*****************************
		 * SEND FRIENDSHIP REQUEST
		 *****************************/
		if(requestType == ClientThread_FriendshipManager_RequestTypes.SEND_FRIENDSHIP_REQUEST_TO_CONTACT) {
			
			System.err.println("Thread ClientThread_FriendshipManager: SEND_FRIENDSHIP_REQUEST_TO_CONTACT");
			
				String email = contattoMittente.getEmail();
				RMISIPBasicResponseMessage answer = sendFriendshipRequestToContact(email);
				
				if(answer.isSUCCESS()) {
					JOptionPane.showMessageDialog(null, answer.getMESSAGE(), "Richiesta amicizia", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, answer.getMESSAGE(), "Richiesta amicizia", JOptionPane.ERROR_MESSAGE);
				}
		
		/*****************************
		 * ACCEPT FRIENDSHIP REQUEST
		 *****************************/
		} else if(requestType == ClientThread_FriendshipManager_RequestTypes.ACCEPT_FRIENDSHIP_REQUEST) {
			
			System.err.println("Thread ClientThread_FriendshipManager: ACCEPT_FRIENDSHIP_REQUEST");
				acceptFriendshipRequest(contattoMittente); 
		
		/*****************************
		 * SHOW_FRIENDSHIP_REQUEST_FROM_CONTACT
		 *****************************/
		} else if(requestType == (ClientThread_FriendshipManager_RequestTypes.SHOW_FRIENDSHIP_REQUEST_FROM_CONTACT) ) {
			System.err.println("Thread ClientThread_FriendshipManager: SHOW_FRIENDSHIP_REQUEST_FROM_CONTACT");
			showFriendshipRequestFromContact(contattoMittente); 
			
		/*****************************
		 * REMOVE_FRIEND
		 *****************************/
		} else if(requestType == (ClientThread_FriendshipManager_RequestTypes.REMOVE_FRIEND) ) {
			removeFriend(contattoDestinatario);
		}
		
		System.err.println("Thread ClientThread_FriendshipManager: ended");
				
	}
	
	/**
	 * Richiede l'amicizia ad un altro contatto
	 * @author Fabio Pierazzi
	 */
	private RMISIPBasicResponseMessage sendFriendshipRequestToContact(String email){
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
			Contact myContact = Status.getMyInfoIntoContact(); 
			
			if( futureFriend.getID() < 0 ) 
				return new RMISIPBasicResponseMessage(false, "Errore di sistema: id contatto " + email + " non valido"); 
			
			
			// TODO scommenta
			ClientInterface clientInterface = null;
			
			try {
				if(Status.getGlobalIP().equals(futureFriend.getGlobalIP())) {

					if(Status.DEBUG)
						System.err.println("Chiedo amicizia a " + futureFriend.getLocalIP() + " nella mia Lan");
					
					clientInterface = ClientEngine.getClient(futureFriend.getLocalIP());
				}
				else {

					if(Status.DEBUG)
						System.err.println("Chiedo amicizia a " + futureFriend.getGlobalIP() + " fuori dalla mia Lan");
					
					clientInterface = ClientEngine.getClient(futureFriend.getGlobalIP()); 
				}
					
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			if(clientInterface == null) {
				return new RMISIPBasicResponseMessage(false, "Si è verificato un errore nel corso del reperimento dello stub del contatto.");
			} else {
				clientInterface.receiveFriendshipRequestFromContact(myContact);
			}
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
	private void sendFriendshipRequestToSIP(FriendshipRequest request) {
		
		/* - controllo se su DB esiste già qualcosa
		 * - 
		 */
		try {
			if(request.getRequestType() == FriendshipRequest_Types.ADD_FRIEND ||
					request.getRequestType() == FriendshipRequest_Types.FORCE_ADD_FRIEND ) {
				ClientEngine.getSIP().addFriendship(request);
			} else if(request.getRequestType() == FriendshipRequest_Types.REMOVE_FRIEND) {
				ClientEngine.getSIP().removeFriendship(request);
			}
			
		} catch (RemoteException e) {
			/* TODO: se il SIP è offline, aggiungo messaggi 
			di amicizia in una coda con FORCE_ADD_FRIEND */
		}
		
	}
	
	/**
	 * Metodo per mostrare la richiesta di amicizia inviata dal contatto
	 * passato come parametro. Questo metodo viene invocato in seguito 
	 * all'invocazione remota sul client del metodo per fare richiesta. 
	 * 
	 * @param contattoRichiedente che ha inviato la richiesta
	 */
	private void showFriendshipRequestFromContact(Contact contattoRichiedente) {
		
		System.out.println("Ricevuto richiesta amicizia da: " + contattoRichiedente.getEmail() + " " +
				"(IP: " + contattoRichiedente.getGlobalIP() + ")");
		
		int result = JOptionPane.showConfirmDialog(null, "Hai ricevuto una richiesta di amicizia da: \n" +
				"" + contattoRichiedente.getEmail() + "\n\n" +
						"Desideri accettare?", 
						"Aggiungi Contatto",
                JOptionPane.YES_NO_OPTION);
		
		if(result == JOptionPane.YES_OPTION) {
			System.out.println("ACCETTATA - Richiesta di amicizia proveniente da " + contattoRichiedente.getEmail() + "");
			acceptFriendshipRequest(contattoRichiedente); 
		}
		else if(result == JOptionPane.NO_OPTION)  {
			if(Status.DEBUG)
				System.out.println("RIFIUTATA - Richiesta di amicizia proveniente da " + contattoRichiedente.getEmail() + "");
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
	private void acceptFriendshipRequest(Contact contattoRichiedente) {
		
		/* Aggiungo l'amico alla mia lista amici, ed eseguo il refresh 
		 * della tabella con la lista amici. */
		ContactListManager.addToContactList(contattoRichiedente); 
		FriendsList_Table table = LayoutReferences.getFriendsListTable();
		if(table != null) {
			table.updateTable(); 
		}
		
		/* Invio ack dell'amicizia al contatto, 
		 * per fargli sapere che può aggiungermi. */
		Contact myContact = Status.getMyInfoIntoContact(); 
		
		// TODO: cosa succede se mentre rispondo al contatto, questi finisce offline? FORCE_ADD_FRIEND al SIP?
		try {
			
			/* se sono in lan */
			if(contattoRichiedente.getGlobalIP().equals(Status.getGlobalIP()))
				ClientEngine.getClient(contattoRichiedente.getLocalIP()).receiveFriendshipAckFromContact(myContact);
			/* se non sono in lan */
			else
				ClientEngine.getClient(contattoRichiedente.getGlobalIP()).receiveFriendshipAckFromContact(myContact);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} 
		


		
		
		/** Richiedo amicizia al SIP */
		System.err.println("Adesso richiedo amicizia al SIP");
		FriendshipRequest request = new FriendshipRequest(FriendshipRequest_Types.ADD_FRIEND, myContact, contattoRichiedente);
		sendFriendshipRequestToSIP(request); 
		
		// TODO: Problema: se il SIP è down, e torna up in un secondo momento, 
		// io invio mia friend request, ma il contatto poi non risulta più fra i miei 
		// amici fintanto che non accetta anche lui.
		
		// TODO: Se il SIP è offline, invio un FORCE_FRIEND_REQUEST
	}
	
	
	/**
	 * Funzione per aggiungere un nuovo amico all'interno della propria 
     * lista contatti. Il contatto viene aggiunto, e viene aggiornata la
     * tabella contenente la lista amici. 
     *  
	 * @param newContact, nuovo contatto da aggiungere alla propria friendsList. 
	 */
	private void addNewFriend(Contact newContact) {
		
	}
	
	private void removeFriend(Contact contactToRemove) {
		/* rimuovo il contatto */
		try {
			ContactListManager.removeFromContactList(contactToRemove);
		} catch(Exception e) {
			System.err.println("Errore durante la rimozione del contatto.");
			e.printStackTrace(); 
		}
		
	}
	
}
