package client;

import java.rmi.RemoteException;
import java.util.ArrayList;

import utility.Counter;

import RMI.ClientInterface;
import RMIMessages.RequestHowAreYou;
import RMIMessages.ResponseHowAreYou;

import chat.Contact;
import chat.Message;
import chat.Status;
import chat.StatusList;

public class ClientThreadTester extends Thread{
	private static int numClientThreadTester = 0;
	private Contact contactToTest;
	private Contact myContact;
	private static Counter counter = new Counter();
	private int counterNumber;

	
	public ClientThreadTester(Contact contactToTest){
		this.contactToTest = contactToTest;
		//Inizializzo le informazioni del mio contatto che devo inviare al client
		myContact = new Contact();
		myContact.setGlobalIP(Status.getGlobalIP());
		myContact.setLocalIP(Status.getLocalIP());
		myContact.setStatus(Status.getStato());
		myContact.setNickname(Status.getNickname());
		myContact.setAvatarURL(Status.getAvatarURL());
	}
	
	
	public void run() {
		counter.incr();
		System.err.println("Start to Test to: "+contactToTest.getNickname());
		
		try {
			ClientInterface client = ClientEngine.getClient(contactToTest.getID());
			if(client == null){
				System.err.println("Utente non raggiunto.");
				contactToTest.setStatus(StatusList.OFFLINE);
				decrementCounter();
				return;
			}
			ResponseHowAreYou rhay = client.howAreYou(new RequestHowAreYou(myContact));
			if(rhay.isSuccess()){
				//Se il client risponde correttamente, sostituisco le informazioni del contatto che io possiedo con quelle da lui comunicate
				Status.getContactList().remove(contactToTest);
				Status.getContactList().add(rhay.getResponseContact());
			}
			
		} catch (RemoteException e) {
			System.err.println("Utente non raggiunto.");
			contactToTest.setStatus(StatusList.OFFLINE);
			//e.printStackTrace();
		}
		decrementCounter();
	}
	
	private void decrementCounter(){
		counterNumber = counter.decr();
		if(counterNumber == 0){
			//TODO AGGIORNARE TABELLA
			System.out.println("AGGIORNAMENTO TABELLA");
		}
	}


}
