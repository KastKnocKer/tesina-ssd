package client.thread;

import java.rmi.RemoteException;

import layout.friendslist.FriendsList_Table;
import layout.managers.LayoutReferences;
import managers.ContactListManager;
import managers.Status;
import utility.Counter;
import RMI.ClientInterface;
import RMIMessages.RequestHowAreYou;
import RMIMessages.ResponseHowAreYou;
import chat.ChatStatusList;
import chat.Contact;
import client.ClientEngine;

public class ClientThread_FriendsStatusTester extends Thread{
	private static int numClientThreadTester = 0;
	private Contact contactToTest;
	private Contact myContact;
	private static Counter counter = new Counter();
	private int counterNumber;

	
	public ClientThread_FriendsStatusTester(Contact contactToTest){
		this.contactToTest = contactToTest;
	}
	
	
	public void run() {
		counter.incr();
		System.err.println("Start to Test to: "+contactToTest.getNickname());
		
		try {
			ClientInterface client = ClientEngine.getClientWithoutOfflineControl(contactToTest.getID());
			if(client == null){
				System.err.println("Utente non raggiunto.");
				contactToTest.setStatus(ChatStatusList.OFFLINE);
				decrementCounter();
				System.out.println("ClientThreadTester: "+contactToTest.getNickname()+" "+contactToTest.getStatus());
				return;
			}
			ResponseHowAreYou rhay = client.howAreYou(new RequestHowAreYou(Status.getMyInfoIntoContact()));
			if(rhay.isSuccess()){
				//Se il client risponde correttamente, sostituisco le informazioni del contatto che io possiedo con quelle da lui comunicate
				contactToTest.updateInfoFromContact(rhay.getResponseContact());
				System.out.println("ClientThreadTester: "+contactToTest.getNickname()+" "+contactToTest.getStatus());
			}
			
		} catch (RemoteException e) {
			System.err.println("Utente non raggiunto.");
			contactToTest.setStatus(ChatStatusList.OFFLINE);
			//e.printStackTrace();
			System.out.println("ClientThreadTester: "+contactToTest.getNickname()+" "+contactToTest.getStatus());
		}
		decrementCounter();
		
		FriendsList_Table table = LayoutReferences.getFriendsListTable();
		
		if(table!=null) 
			table.updateTable(); 
	}
	
	private void decrementCounter(){
		counterNumber = counter.decr();
		if(counterNumber == 0){
			//TODO x Fabio -> AGGIORNARE TABELLA LISTA CONTATTI
			//System.out.println("AGGIORNAMENTO TABELLA - Ti prego fabio sistemami :(");
			//Salvo lo stato dei miei contatti
			ContactListManager.writeContactsXML();
//			LayoutReferences.getFriendsListTable().updateTable(); 
		}
	}


}
