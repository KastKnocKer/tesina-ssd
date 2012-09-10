package client.thread;

import java.rmi.RemoteException;
import java.util.ArrayList;

import client.ClientEngine;

import layout.friendslist.FriendsList_Table;
import layout.managers.LayoutReferences;
import managers.ContactListManager;
import managers.Status;

import RMI.ClientInterface;

import chat.ChatStatusList;
import chat.Message;
/**
 * Thread che si occupa dell'invio dei messaggi di chat agli altri nodi client
 */
public class ClientThread_MessageSender extends Thread{

	private ArrayList<Message> MessagesToDeliver;
	
	public ClientThread_MessageSender(ArrayList<Message> MessagesToDeliver){
		this.MessagesToDeliver = MessagesToDeliver;
	}
	
	
	public void run() {
		System.err.println("Start to send to: "+MessagesToDeliver.get(0).getTo());
		Message[] messagesToDeliver = new Message[MessagesToDeliver.size()];
		for(int i=0; i<MessagesToDeliver.size(); i++){
			messagesToDeliver[i] = MessagesToDeliver.get(i);
		}
		try {
			ClientInterface client = ClientEngine.getClient(messagesToDeliver[0].getTo());
			if(client == null){
				System.err.println("Messaggi non consegnati - (getClient() == null)");
				return;
			}
			client.sendMessageToContact(messagesToDeliver,Status.getGlobalIP(),Status.getLocalIP());
		} catch (RemoteException e) {
			System.err.println("ClientThread_MessageSender.run() - Il contatto "+messagesToDeliver[0].getTo()+" è OFFLINE");
			//e.printStackTrace();
			ContactListManager.searchContactById(messagesToDeliver[0].getTo()).setStatus(ChatStatusList.OFFLINE);
			FriendsList_Table table = LayoutReferences.getFriendsListTable();
			if(table!=null) 
				table.updateTable(); 
		}
	}

	

}
