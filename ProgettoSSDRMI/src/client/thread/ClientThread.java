package client.thread;

import java.util.ArrayList;

import client.ClientEngine;

import layout.managers.LayoutReferences;
import managers.ContactListManager;
import managers.Status;

import chat.Contact;
import chat.Message;

public class ClientThread extends Thread{

	private int index;
	
	/* Costanti che indicano ogni quanto eseguire certe funzioni */

	/** Ogni quanto tempo aggiornare la lista contatti. 
	 * NOTA: Questo valore dev'essere superiore a quello di timeout (30 secondi). */
	private final int CheckContactListTime = 120;
	
	/** Ogni quanto tempo controllare se ci sono messaggi da consegnare tramite la chat */
	private final int DeliveryAllMessagesTime = 5;
	
	/** Ogni quanto tempo controllare se ci sono richieste di amicizia in ingresso*/
	private final int CheckNewFriendshipTime = 5; 
	
	
	public void run() {
		index = 0;
		while(true){
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {	
				e.printStackTrace(); System.out.println("ClientThread Exception");
			}
			
			//Se non sono loggato skippo
			if(!Status.isLOGGED()) 
				continue;	

			//Controllo dello stato di connessione dei propri contatti amici
			if(index%CheckContactListTime == 0)		
				checkContactList();
			
			//Consegna dei messaggi
			if(index%DeliveryAllMessagesTime == 0)	
				ClientEngine.deliverAllMessages();
			
			index++;
			
		}
		
	}
	
	private void checkContactList(){
		ArrayList<Contact> contactList = ContactListManager.getContactList();
		for(Contact contact : contactList){
			new ClientThread_FriendsStatusTester(contact).start();
		}
	}


}
