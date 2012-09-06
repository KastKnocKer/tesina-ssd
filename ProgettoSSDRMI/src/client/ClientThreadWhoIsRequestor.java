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

public class ClientThreadWhoIsRequestor extends Thread{
	private static int numClientThreadTester = 0;
	private Contact contactToTest;
	private Contact myContact;
	private static Counter counter = new Counter();
	private int counterNumber;

	
	public ClientThreadWhoIsRequestor(Contact contactToTest){
		this.contactToTest = contactToTest;
	}
	
	
	public ClientThreadWhoIsRequestor(int userID, String globalIP,int randomNum, int TTL) {
		// TODO Auto-generated constructor stub
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
			contactToTest.setStatus(StatusList.OFFLINE);
			//e.printStackTrace();
			System.out.println("ClientThreadTester: "+contactToTest.getNickname()+" "+contactToTest.getStatus());
		}
		decrementCounter();
	}
	
	private void decrementCounter(){
		counterNumber = counter.decr();
		if(counterNumber == 0){
			//TODO x Fabio -> AGGIORNARE TABELLA LISTA CONTATTI
			//System.out.println("AGGIORNAMENTO TABELLA - Ti prego fabio sistemami :(");
			//Salvo lo stato dei miei contatti
			Status.writeContactsXML();
		}
	}


}
