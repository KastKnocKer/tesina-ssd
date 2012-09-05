package client;

import java.util.ArrayList;

import layout.managers.LayoutReferences;

import chat.Contact;
import chat.Message;
import chat.Status;

public class ClientThread extends Thread{

	private int index;
	public void run() {
		index = 0;
		while(true){
			try {Thread.sleep(1000);} catch (InterruptedException e) {	e.printStackTrace(); System.out.println("ClientThread Exception");}
			if(!Status.isLOGGED()) continue;	//Se non sono loggato skippo


//			int sendTo = (int)(Math.random() * 10)+2;
//			ClientEngine.sendMessageToContact(new Message(Status.getUserID(),sendTo,"\t\t["+Status.getNickname()+"]Msg n° "+index));
			
			if(index%2 == 0){
				//try {Thread.currentThread().sleep(500);} catch (InterruptedException e) {	e.printStackTrace(); System.out.println("ClientThread Exception");}
				ClientEngine.deliverAllMessages();
			}
			
			if(index%60 == 0) checkContactList();
			
			index++;
			
		}
		
	}
	
	private void checkContactList(){
		ArrayList<Contact> contactList = Status.getContactList();
		for(Contact contact : contactList){
			new ClientThreadTester(contact).start();
		}
	}


}
