package client;

import java.rmi.RemoteException;
import java.util.ArrayList;

import managers.ContactListManager;
import managers.Status;

import utility.Counter;

import RMI.ClientInterface;
import RMIMessages.RequestHowAreYou;
import RMIMessages.ResponseHowAreYou;

import chat.ChatStatusList;
import chat.Contact;
import chat.Message;

public class ClientThreadWhoIsRequestor extends Thread{
	private static int numClientThreadTester = 0;
	private Contact contactToTest;
	private Contact myContact;
	private static Counter counter = new Counter();
	private int counterNumber;

	private int requestorUserID;
	private String requestorGlobalIP;
	private int requestorNum;
	private int TTL;
	private String emailToSearch;
	
	public ClientThreadWhoIsRequestor(int requestorUserID, String requestorGlobalIP,int requestorNum, int TTL, String emailToSearch) {
		this.requestorUserID = requestorUserID;
		this.requestorGlobalIP = requestorGlobalIP;
		this.requestorNum = requestorNum;
		this.TTL = TTL;
		this.emailToSearch = emailToSearch;
		this.start();
	}


	public void run() {
		Contact contactToSearch = null;
		if( !requestorGlobalIP.equals(Status.getGlobalIP()) ){
			//Se non sono il richiedente originario controllo di avere il contatto nella mia lista contatti
			contactToSearch = ContactListManager.searchContactByEmail(emailToSearch);
			if(contactToSearch != null && contactToSearch.getStatus() != ChatStatusList.OFFLINE){
				//Se ho trovato il contatto ed � online rispondo al richiedente
				sendResponseToRequestor(contactToSearch);
				return;
			}
		}
		
		//Provo ad inoltrare la richiesta ai miei contatti
		//TODO Al momento ritengo che i contatti online siano assolutamente online, pena l'attesa del timeout
		
		ArrayList<Contact> contactList = ContactListManager.getContactList();
		for(Contact contact : contactList){
			if(contact.isConnected()){
				ClientInterface client = ClientEngine.getClient(contact.getID());
				try {
					client.whois(emailToSearch, TTL-1);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
//		
//		counter.incr();
//		System.err.println("Start to Test to: "+contactToTest.getNickname());
//		
//		try {
//			ClientInterface client = ClientEngine.getClient(contactToTest.getID());
//			if(client == null){
//				System.err.println("Utente non raggiunto.");
//				contactToTest.setStatus(StatusList.OFFLINE);
//				decrementCounter();
//				System.out.println("ClientThreadTester: "+contactToTest.getNickname()+" "+contactToTest.getStatus());
//				return;
//			}
//			ResponseHowAreYou rhay = client.howAreYou(new RequestHowAreYou(Status.getMyInfoIntoContact()));
//			if(rhay.isSuccess()){
//				//Se il client risponde correttamente, sostituisco le informazioni del contatto che io possiedo con quelle da lui comunicate
//				contactToTest.updateInfoFromContact(rhay.getResponseContact());
//				System.out.println("ClientThreadTester: "+contactToTest.getNickname()+" "+contactToTest.getStatus());
//			}
//			
//		} catch (RemoteException e) {
//			System.err.println("Utente non raggiunto.");
//			contactToTest.setStatus(StatusList.OFFLINE);
//			//e.printStackTrace();
//			System.out.println("ClientThreadTester: "+contactToTest.getNickname()+" "+contactToTest.getStatus());
//		}
//		decrementCounter();
	}
	
	private void sendResponseToRequestor(Contact contactToSearch){
		System.out.println("----->     RISPONDO AL RICHIEDENTE     <-----");
	}


}
