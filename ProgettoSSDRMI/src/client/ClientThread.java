package client;

import java.util.ArrayList;

import layout.managers.LayoutReferences;
import managers.ContactListManager;
import managers.Status;

import chat.Contact;
import chat.Message;

public class ClientThread extends Thread{

	private int index;
	
	//Intervalli di controllo
	
	private final int CheckContactListTime = 120;				//Deve esser almeno maggiore di 30 secondi a causa del timeout!
	private final int DeliveryAllMessagesTime = 5;
	
	
	public void run() {
		index = 0;
		while(true){
			try {Thread.sleep(1000);} catch (InterruptedException e) {	e.printStackTrace(); System.out.println("ClientThread Exception");}
			if(!Status.isLOGGED()) continue;	//Se non sono loggato skippo

			//Controllo dello stato di connessione dei propri contatti amici
			if(index%CheckContactListTime == 0)		checkContactList();
			
			//Consegna dei messaggi
			if(index%DeliveryAllMessagesTime == 0)	ClientEngine.deliverAllMessages();
			
			
			index++;
			
		}
		
	}
	
	private void checkContactList(){
		ArrayList<Contact> contactList = ContactListManager.getContactList();
		for(Contact contact : contactList){
			new ClientThreadTester(contact).start();
		}
	}


}
