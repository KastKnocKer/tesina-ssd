package client.thread;

import java.rmi.RemoteException;
import java.util.ArrayList;

import client.ClientEngine;

import managers.ContactListManager;
import managers.Status;
import managers.StatusP2P;

import utility.Counter;

import RMI.ClientInterface;
import RMIMessages.RequestHowAreYou;
import RMIMessages.ResponseHowAreYou;

import chat.ChatStatusList;
import chat.Contact;
import chat.Message;

/**
 * Thread che si occupa dell'invio delle richieste per il whois P2P
 */
public class ClientThread_WhoisRequestor extends Thread{
	private static int numClientThreadTester = 0;
	private Contact contactToTest;
	private Contact myContact;
	private static Counter counter = new Counter();
	private int counterNumber;

	private int requestorUserID;
	private String requestorIP;
	private int requestorNum;
	private int TTL;
	private String emailToSearch;
	
	
	public ClientThread_WhoisRequestor(int requestorUserID, String requestorIP,int requestorNum, int TTL, String emailToSearch) {
		this.requestorUserID = requestorUserID;
		this.requestorIP = requestorIP;
		this.requestorNum = requestorNum;
		this.TTL = TTL;
		this.emailToSearch = emailToSearch;
		this.start();
		
	}


	public void run() {
		Contact contactToSearch = null;
		
		//Controllo di non avere gi� ricevuto la richiesta
		if(!StatusP2P.addRequest(requestorUserID, requestorNum)){
			//Ho gi� ricevuto la richiesta quindi ritorno
			if(Status.SUPER_DEBUG) System.out.println("[CLIENT] ClientThread_WhoisRequestor: Richiesta gi� ricevuta!!");
			return;
		}
		
		//if( !requestorGlobalIP.equals(Status.getGlobalIP()) ){ //Ottimizzazione che non funziona in LAN
			//Se non sono il richiedente originario controllo di avere il contatto nella mia lista contatti
			contactToSearch = ContactListManager.searchContactByEmail(emailToSearch);
			if(contactToSearch != null && contactToSearch.getStatus() != ChatStatusList.OFFLINE){
				//Se ho trovato il contatto ed � online rispondo al richiedente
				System.out.println("[Whois P2P] Contatto trovato!!");
				ClientInterface client = ClientEngine.getClient(requestorIP);
				try {
					client.whoisResponse(requestorUserID, Status.getUserID(), requestorNum, contactToSearch);
				} catch (RemoteException e) {
					System.err.println("[CLIENT] ClientThread_WhoisRequestor.run() - RemoteException: "+e.toString());
					//e.printStackTrace();
				}
				return;
			}
		//}
		
		//Se il Time To Live � 0 non interrogo i miei contatti
		if(TTL == 0) return;
		
		//Provo ad inoltrare la richiesta ai miei contatti
		//TODO Al momento ritengo che i contatti online siano assolutamente online, pena l'attesa del timeout
		
		ArrayList<Contact> contactList = ContactListManager.getContactList();
		for(Contact contact : contactList){
			if(contact.isConnected()){
				ClientInterface client = ClientEngine.getClient(contact.getID());
				try {
					client.whois(requestorUserID, requestorIP, requestorNum, TTL-1, emailToSearch);
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
	

}
