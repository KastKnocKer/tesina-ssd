package Friendship;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import RMIMessages.RMISIPBasicResponseMessage;
import RMIMessages.RequestFriendshipMessage;
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
			myContact.setNickname(Status.getNickname()); 
			myContact.seteMail(Status.getEmail());
			ClientEngine.getClient(futureFriend.getID()).sendFriendshipRequest(myContact);
			
			if(Status.DEBUG) 
				System.out.println("Client - Richiesta di amicizia a: " + email);
			
//			return ClientEngine.getSIP().askFriendship(new RequestFriendshipMessage(email));
			return new RMISIPBasicResponseMessage(true, "Richiesta inviata correttamente"); 

		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.RequestFriendship() exception", JOptionPane.ERROR_MESSAGE);
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
		
		System.out.println("Ricevuto richiesta amicizia.");
		
		final JOptionPane optionPane = new JOptionPane(
			    "Hai ricevuto una richiesta di amicizia da " + contattoRichiedente.getNickname() + " ( " + contattoRichiedente.geteMail() + ". \n " +
			    		"Desideri accettare?",
			    JOptionPane.QUESTION_MESSAGE,
			    JOptionPane.YES_NO_OPTION);
		// TODO: gestire il fatto che l'altro client cada o cambi ip nel mentre
	}
}
