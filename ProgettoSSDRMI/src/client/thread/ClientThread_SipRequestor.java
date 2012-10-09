package client.thread;

import java.rmi.RemoteException;
import java.util.ArrayList;

import layout.friendslist.FriendsList_Table;
import layout.managers.LayoutReferences;
import managers.ContactListManager;
import managers.Status;
import RMI.ClientInterface;
import client.ClientEngine;
import client.OUTChatMessageListManager;
import client.requesttosip.RequestToSIPListManager;

import chat.ChatStatusList;
import chat.Message;

/**
 * Thread che controlla se ci sono richieste da inviare al SIP.
 * Ad esempio: rimuovi amico, invia richiesta di amicizia. 
 * 
 * @author Fabio Pierazzi
 */
public class ClientThread_SipRequestor extends Thread {
	private int index;
	
	public ClientThread_SipRequestor(){
		
	}
	
	
	public void run() {
//		index = 0;
		while(true){
			
			System.out.println("*** ClientThread_SipRequestor *** running...");
			
			try {
				/* Ogni 5 secondi */
				Thread.sleep(15000);
			} catch (InterruptedException e) {	
				e.printStackTrace(); System.out.println("ClientThread Exception");
			}
			
			// TODO: se non sono loggato e skippo, significa che se anche torna online non gli mando neanche la richiesta di LOGIN
//			// Se non sono loggato skippo
//			if(!Status.isLOGGED()) 
//				continue;	

			// Invio le richieste al SIP ogni 60 secondi
//			if(index%12 == 0)		
				RequestToSIPListManager.sendRequests();
			
//			index++;
		}
	}

}
