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
		index = 0;
		while(true){
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {	
				e.printStackTrace(); System.out.println("ClientThread Exception");
			}
			
			//Se non sono loggato skippo
			if(!Status.isLOGGED()) 
				continue;	

			//Invio le richieste al SIP
			if(index%1 == 0)		
				RequestToSIPListManager.sendRequests();
			
			index++;
			
		}
	}

}

//package client;
//
//import java.rmi.RemoteException;
//import java.util.ArrayList;
//
//import managers.Status;
//
//import RMI.ClientInterface;
//
//import chat.Message;
//
//public class ClientThreadSender extends Thread{
//
//	private ArrayList<Message> MessagesToDeliver;
//	
//	public ClientThreadSender(ArrayList<Message> MessagesToDeliver){
//		this.MessagesToDeliver = MessagesToDeliver;
//	}
//	
//	
//	public void run() {
//		System.err.println("Start to send to: "+MessagesToDeliver.get(0).getTo());
//		Message[] messagesToDeliver = new Message[MessagesToDeliver.size()];
//		for(int i=0; i<MessagesToDeliver.size(); i++){
//			messagesToDeliver[i] = MessagesToDeliver.get(i);
//		}
//		try {
//			ClientInterface client = ClientEngine.getClient(messagesToDeliver[0].getTo());
//			if(client == null){
//				System.err.println("Messaggi non consegnati - (getClient() == null)");
//				return;
//			}
//			client.sendMessageToContact(messagesToDeliver,Status.getGlobalIP(),Status.getLocalIP());
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	
//
//}
