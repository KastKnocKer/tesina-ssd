package client;

import java.rmi.RemoteException;
import java.util.ArrayList;

import managers.Status;

import RMI.ClientInterface;

import chat.Message;

public class ClientThreadSender extends Thread{

	private ArrayList<Message> MessagesToDeliver;
	
	public ClientThreadSender(ArrayList<Message> MessagesToDeliver){
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}
