package client;

import chat.Message;

public class ClientThread implements Runnable{

	private int index;
	public void run() {
		index = 0;
		while(true){
			
			int sendTo = (int)(Math.random() * 10)+2;
			ClientEngine.sendMessageToContact(new Message(1,sendTo,"\t\tMsg n° "+index));
			
			if(index%20 == 0){
				//try {Thread.currentThread().sleep(500);} catch (InterruptedException e) {	e.printStackTrace(); System.out.println("ClientThread Exception");}
				ClientEngine.deliverAllMessages();
			}
			
			index++;
			try {Thread.currentThread().sleep(1000);} catch (InterruptedException e) {	e.printStackTrace(); System.out.println("ClientThread Exception");}
		}
		
	}


}
