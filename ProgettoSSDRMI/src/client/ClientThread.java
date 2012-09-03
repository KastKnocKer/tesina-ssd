package client;

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
			
			index++;
			
		}
		
	}


}
