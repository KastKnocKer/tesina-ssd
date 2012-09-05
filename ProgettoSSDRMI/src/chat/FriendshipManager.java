package chat;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import RMIMessages.RMISIPBasicResponseMessage;
import RMIMessages.RequestFriendshipMessage;
import client.ClientEngine;

/**
 * Classe static per la gestione delle richieste d'amicizia. 
 * Il protocollo sar� descritto maggiormente nel dettaglio su un documento a parte. 
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
				return new RMISIPBasicResponseMessage(false, "Siamo spiacenti; non � stato possibile reperire il contatto avente email:\n" + email); 
			}
				
			/* **********************************************
			 * 2. send friendship request (try client, then eventually SIP)
			 * **********************************************/
//			ClientEngine.getClient(futureFriend.getID()).
			
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
	 * 1) il client con cui voglio stringere direttamente amicizia � offline (quindi il metodo sendFriendshipRequestToContact � fallito)
	 * 2) come "acknoledgment" di amicizia stretta: i due client, se riescono a stringere amicizia in maniera esclusivamente p2p, 
	 * poi mandano entrambi una friendship-request al SIP, per notificargli l'avvenuta amicizia. Vedendo due friendship requests dai due
	 * client, il SIP capir� che hanno stretto amicizia. 
	 * 
	 * @author Fabio Pierazzi 
	 */
	public static void sendFriendshipRequestToSIP() {
		
	}
}