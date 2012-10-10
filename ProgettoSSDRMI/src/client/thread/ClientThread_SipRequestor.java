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
		System.out.println("*** ClientThread_SipRequestor *** starting...");
		index = 0;
		
		while(true){
			
			try {
				//Ogni 10 secondi
				Thread.sleep(10000);
			} catch (InterruptedException e) {	
				e.printStackTrace(); System.out.println("ClientThread Exception");
			}
			
			System.out.println("*** ClientThread_SipRequestor *** running...");
			
			//Invio le richieste in coda al SIP
			RequestToSIPListManager.sendRequests();
			
			
			
			index++;
		}
	}

}
