package client.thread;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import layout.friendslist.FriendsList_Table;
import layout.managers.LayoutReferences;
import managers.ContactListManager;
import managers.Status;
import RMI.ClientInterface;
import RMIMessages.FriendshipRequest;
import RMIMessages.FriendshipRequestType;
import RMIMessages.RMISIPBasicResponseMessage;
import chat.Contact;
import chat.StatusP2P;
import client.ClientEngine;
import client.requesttosip.*;

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
	/** Richiesta di amicizia */
	FriendshipRequest friendshipRequest  = null; 
	
	/**
	 * Costruttore 
	 * @param requestType tipo di richiesta che consente di specializzare le operazioni da eseguire.
	 * @param contatto con cui interagire
	 */
	public ClientThread_FriendshipManager(
			ClientThread_FriendshipManager_RequestTypes requestType,
			FriendshipRequest friendshipRequest) {
		this.requestType = requestType; 
		this.friendshipRequest = friendshipRequest; 
		this.contattoMittente = friendshipRequest.getContattoMittente();
		this.contattoDestinatario = friendshipRequest.getContattoDestinatario(); 
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
		
		/*****************************
		 * SEND FRIENDSHIP REQUEST TO SIP
		 *****************************/
		} else if(requestType == ClientThread_FriendshipManager_RequestTypes.SEND_FRIENDSHIP_REQUEST_TO_SIP) {
			
			System.err.println("Thread ClientThread_FriendshipManager: SEND_FRIENDSHIP_REQUEST_TO_SIP");
			
			sendFriendshipRequestToSIP(friendshipRequest); 
			
//				String email = contattoMittente.getEmail();
//				RMISIPBasicResponseMessage answer = sendFriendshipRequestToContact(email);
				
//				if(answer.isSUCCESS()) {
//					JOptionPane.showMessageDialog(null, answer.getMESSAGE(), "Richiesta amicizia", JOptionPane.INFORMATION_MESSAGE);
//				} else {
//					JOptionPane.showMessageDialog(null, answer.getMESSAGE(), "Richiesta amicizia", JOptionPane.ERROR_MESSAGE);
//				}
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
			
			Contact futureFriend = null; 
			
			// TODO: whois p2p
			
			/* WHOIS p2p */
			try {
				
				if(Status.DEBUG)
					System.err.println("Inizio whois p2p per: " + email);
				
				/* Voglio ottenere dalla rete p2p l'IP di un contatto data la sua Email. 
				 * Lancio pertanto il thread che si occupa di gestire la richiesta
				 * whois p2p. */
				int requestNumber = ClientEngine.whois(email);
				
				/* ogni quanti secondi controlla se è arrivato un risultato */
				int numSecRefresh = 1; 
				/* dopo quanti "refresh" (= controlli della richiesta) 
				 * lanciare il timeout */
				int numMaxRefresh = 10;
				
				/* contatto dell'amico */
				Contact friendContact = null; 
				
				/* Inizio un ciclo in cui controllo se arriva risposta al whois p2p */
				for(int count=0; count < numMaxRefresh; count++) {
					
					if(Status.DEBUG) 
						System.err.println("[count=" + count + "] Attendo risposta p2p per il contatto '" + email + "'.");
					
					/* Attento il numero di secondi per il refresh 
					 * (per controllare se è arrivata la risposta) */
					sleep(numSecRefresh * 1000); 
					
					/* Verifico se è arrivata risposta per quel contatto */
					friendContact = StatusP2P.getWhoisResponse(Status.getUserID(), requestNumber);
					
					if(friendContact == null) {
						continue; 
					} else {
						/* Ho ricevuto risposta! Rimuovo la 
						 * risposta dall'elenco delle richieste */
						StatusP2P.removeWhoisResponse(Status.getUserID(), requestNumber);
						break; 
					}
				}

				/* Se sono uscito e friendContact è null, significa che non 
				 * ho ottenuto quel che volevo */
				if(friendContact == null) {
					System.err.println("Lancio eccezione gestita per passare al 'whois SIP'");
					throw new Exception();
				}
				
			} catch (Exception e) {
				
				if(Status.DEBUG)
					System.err.println("Timeout whois p2p per: " + email);
				
				e.printStackTrace(); 
				
				/* WHOIS SIP */
				try {
					if(Status.DEBUG)
						System.err.println("Inizio whois SIP per: " + email);
					/* Se si verifica un timeout nella richiesta alla rete p2p, voglio ottenere 
					 * dal Server SIP l'IP di un contatto data la sua Email. */
					futureFriend = ClientEngine.getSIP().whois(email); 
				} catch (Exception e1) {
					if(Status.DEBUG)
						System.err.println("Timeout whois SIP per: " + email);
					
					e.printStackTrace(); 
				}
				
			}
			// TODO: fine whois p2p
			
			// se non riesco ad ottenere le info con i whois, devo tornare errore
			if(futureFriend == null) {
				System.err.println("futureFriend == null");
				return new RMISIPBasicResponseMessage(false, "Siamo spiacenti; non è stato possibile reperire il contatto avente email:\n" + email); 
			}
			// TODO: remove da qui... (inizio remove)
//			try {
//				futureFriend = ClientEngine.getSIP().whois(email);
//			} catch (Exception e) {
//				System.err.println("whois '" + email + "': SIP offline!");
//				
//				// DEBUG!! RIMUOVERE!!
//				futureFriend.setGlobalIP("192.168.1.103");
//				futureFriend.setID(1); 
//			}
			// TODO: ... a qui (fine remove)
			
			
			if(Status.DEBUG) {
				System.out.println("Client - [whois] - risultato: ");
				futureFriend.printInfo(); 
			}
			
			/* Se non sono riuscito ad ottenere l'IP del contatto, 
			 * restituisco errore perché non posso inviargli la richiesta di amicizia */
			// TODO: provo ad inviare la richiesta al SIP, anziché uscire con fallimento
			if(futureFriend.getGlobalIP() == null || futureFriend.getGlobalIP().equals("")) {
				return new RMISIPBasicResponseMessage(false, "Siamo spiacenti; non è stato possibile reperire il contatto avente email:\n" + email); 
			}
			
			
			/* **********************************************
			 * 2. send friendship request (try client, then eventually SIP)
			 * **********************************************/
			Contact myContact = Status.getMyInfoIntoContact(); 
			
			if( futureFriend.getID() < 0 ) 
				return new RMISIPBasicResponseMessage(false, "Errore di sistema: id contatto '" + email + "' non valido"); 
			
			
			ClientInterface clientInterface = null;
			
			try {
				
				/* Recupero lo stub del client */
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
				
				/* Invio la richiesta di amicizia */
				if(clientInterface == null) {
					return new RMISIPBasicResponseMessage(false, "Si è verificato un errore nel corso del reperimento dello stub del contatto.");
				} else {
					clientInterface.receiveFriendshipRequestFromContact(myContact);
				}
					
			/* se il client non ha ricevuto la richiesta di amicizia (timeout), 
			 provo a mandarla al SIP */
			} catch(Exception e) {
				
				if(Status.DEBUG) 
					System.err.println("ATTENZIONE [FriendshipRequest]: il contatto avente email '" + email + "' non è online. La richiesta richiesta di amicizia verrà inviata al SIP.");

				e.printStackTrace();
				
				/* Dato che non sono riuscito a raggiungere il client, 
				 * invio la richiesta di amicizia al SIP */
				try {
					
					Contact contattoMittente = Status.getMyInfoIntoContact();
					Contact contattoDestinatario = futureFriend; 
					
					FriendshipRequest request = new FriendshipRequest(
							FriendshipRequestType.ADD_FRIEND, 
							contattoMittente, 
							contattoDestinatario);
					
					ClientEngine.getSIP().addFriendship(request); 
					
				} catch (Exception e1) {
					/* Se anche il SIP è offline, mi salvo la richiesta in coda, e gliela invio periodicamente. */
					if(Status.DEBUG) 
						System.err.println("ATTENZIONE [FriendshipRequest]: il SIP è offline; il SIP era stato " +
								"contattato dopo che il contatto '" + email + "' era stato trovato offline.");
					
					/* Riformulo richiesta di amicizia */
					Contact contattoMittente = Status.getMyInfoIntoContact();
					Contact contattoDestinatario = futureFriend; 
					
					FriendshipRequest request = new FriendshipRequest(
							FriendshipRequestType.ADD_FRIEND, 
							contattoMittente, 
							contattoDestinatario);
					
					/* Aggiungo una richiesta alla coda di invio per il SIP */
					RequestToSIP requestToSip  = new RequestToSIP(
							RequestToSIPTypeList.FRIENDSHIP_REQUEST, 
							request);
					
					 RequestToSIPListManager.addRequest(requestToSip);
				}
				
				return new RMISIPBasicResponseMessage(true, "Richiesta di amicizia inviata al SIP (il contatto era offline)."); 
				
			}
			
			/* Se sono arrivato qua, significa che è andato "tutto" bene, e la richiesta è arrivata
			 * direttamente all'altro contatto */
			
//			System.err.println("Mostro finestra di richiesta amicizia");
//			FriendshipManager.showFriendshipRequestFrom(myContact);
			
			if(Status.DEBUG) 
				System.out.println("Client - Richiesta di amicizia a: " + email);
			
//			return ClientEngine.getSIP().askFriendship(new RequestFriendshipMessage(email));
			return new RMISIPBasicResponseMessage(true, "Richiesta inviata correttamente"); 

//		} catch (RemoteException e) {
//			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.RequestFriendship() exception", JOptionPane.ERROR_MESSAGE);
//			e.printStackTrace(); 
//			return new RMISIPBasicResponseMessage(false, "ClientEngine.RequestFriendship() exception");
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
			if(request.getRequestType() == FriendshipRequestType.ADD_FRIEND ||
					request.getRequestType() == FriendshipRequestType.FORCE_ADD_FRIEND ) {
				ClientEngine.getSIP().addFriendship(request);
			} else if(request.getRequestType() == FriendshipRequestType.REMOVE_FRIEND) {
				ClientEngine.getSIP().removeFriendship(request);
			}
			
		} catch (RemoteException e) {
			/* TODO: se il SIP è offline, aggiungo messaggi 
			di amicizia in una coda con FORCE_ADD_FRIEND */
			System.err.println("ClientThread_FriendshipManager.sendFriendshipRequestToSIP(): errore durante l'esecuzione del metodo.");
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
	private void acceptFriendshipRequest(Contact contattoMittente) {
		
		/* Aggiungo l'amico alla mia lista amici, ed eseguo il refresh 
		 * della tabella con la lista amici. */
		ContactListManager.addToContactList(contattoMittente); 
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
			if(contattoMittente.getGlobalIP().equals(Status.getGlobalIP()))
				ClientEngine.getClient(contattoMittente.getLocalIP()).receiveFriendshipAckFromContact(myContact);
			/* se non sono in lan */
			else
				ClientEngine.getClient(contattoMittente.getGlobalIP()).receiveFriendshipAckFromContact(myContact);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} 

		
		
		/** Richiedo amicizia al SIP */
		/** NOTA: CHIEDO ESPLICITAMENTE L'AMICIZIA NEL VERSO OPPOSTO: 
		 * da me destinatario verso di lui mittente!!! 
		 * (Motivo: il mittente la richiederà al SIP nel verso opposto, 
		 * e saprà che l'amicizia è autentica e confermata )*/
		System.err.println("Adesso richiedo amicizia al SIP");
		FriendshipRequest request = new FriendshipRequest(
				FriendshipRequestType.ADD_FRIEND, 
				myContact, 
				contattoMittente);
		
		sendFriendshipRequestToSIP(request); 
		
		// TODO: Problema: se il SIP è down, e torna up in un secondo momento, 
		// io invio mia friend request, ma il contatto poi non risulta più fra i miei 
		// amici fintanto che non accetta anche lui.
		
		// TODO: Se il SIP è offline, invio un FORCE_FRIEND_REQUEST
	}
	
	
//	/**
//	 * Funzione per aggiungere un nuovo amico all'interno della propria 
//     * lista contatti. Il contatto viene aggiunto, e viene aggiornata la
//     * tabella contenente la lista amici. 
//     *  
//	 * @param newContact, nuovo contatto da aggiungere alla propria friendsList. 
//	 */
//	private void addNewFriend(Contact newContact) {
//		
//	}
	
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
