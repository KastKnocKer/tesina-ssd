package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import managers.ContactListManager;
import managers.Status;

import client.thread.ClientThread_MessageSender;

import chat.Message;

public class OUTChatMessageListManager {
	
	private static int progressiveNum = 0;
	private static ArrayList<Message> OUT_ChatMessageList = new ArrayList<Message>();
	
	public static synchronized void addMsgToSend(Message chatMsg){
		chatMsg.setProgressiveNum(progressiveNum++);
		OUT_ChatMessageList.add(chatMsg);
	}
	
	public static synchronized void removeSendedMsgs(ArrayList<Message> SendedMsgs){
		for(Message msg : SendedMsgs){
			OUT_ChatMessageList.remove(msg);
		}
	}
	
	public static synchronized boolean deliverAllMessages(){
		//Se la coda è vuota ritorno subito
		if(OUT_ChatMessageList.size() == 0) return true;
		
		//Ordino la lista dei messaggi in uscita
		Collections.sort(OUT_ChatMessageList, new ChatMessageComparator());
		
		//Gestisco l'invio dei messaggi considerando la lista ORDINATA
		int temporaryClientTarget = -1;
		boolean toNextUser = false;
		ArrayList<Message> MessagesToDeliver = new ArrayList<Message>();
		if(Status.DEBUG) System.out.println("*************************************************************");
		
		for(Message msg : OUT_ChatMessageList){
			if(Status.DEBUG) System.out.println("MSG TO: "+msg.getTo()+"\tNum: "+msg.getProgressiveNum());
			
			if(msg.getTo() != temporaryClientTarget){
				//Sto gestendo un nuovo destinatario
				if( (temporaryClientTarget > -1) && (MessagesToDeliver != null) && (MessagesToDeliver.size()>0)){
					//Se ho già considerato altri utenti invio i messaggi
					System.out.println("INVIO MESSAGGI VERSO: "+MessagesToDeliver.get(0).getTo());
					ClientThread_MessageSender cts = new ClientThread_MessageSender(MessagesToDeliver);
					cts.start();
					MessagesToDeliver = null;
				}
				MessagesToDeliver = new ArrayList<Message>();
				temporaryClientTarget = msg.getTo();
				
				//Verifico lo stato di connessione del contatto
				if(ContactListManager.searchContactById(temporaryClientTarget).isConnected()){
					//Contatto ONLINE
					toNextUser = false;
					MessagesToDeliver.add(msg);
				}else{
					//Contatto OFFLINE
					toNextUser = true;
					continue;
				}
				
				
			}else{
				//Sto gestendo un altro messaggio diretto al destinatario precedente
				
				if(toNextUser){
					//Contatto OFFLINE
					//Se il contatto non è raggiungibile passo al messaggio successivo
					continue;
				}else{
					//Contatto ONLINE
					//Se il contatto è raggiungibile aggiungo pure questo messaggio alla lista da inviare
					MessagesToDeliver.add(msg);
				}
				
			}
			
		}
		
		//Controllo che siano stati inviati tutti
		if( (temporaryClientTarget > -1) && (MessagesToDeliver != null) && (MessagesToDeliver.size()>0)){
			//Se ho già considerato altri utenti invio i messaggi
			ClientThread_MessageSender cts = new ClientThread_MessageSender(MessagesToDeliver);
			cts.start();
			MessagesToDeliver = null;
		}
		
		
		
//		while( (temporaryClientTarget < 0) || (OUT_ChatMessageList.size()>5) ){
//			temporaryClientTarget = -1;
//			MessagesToDeliver = new ArrayList<Message>();
//			for(int i=0; i<OUT_ChatMessageList.size(); i++){
//				Message msg = OUT_ChatMessageList.get(i);
//				if(temporaryClientTarget<0){
//					temporaryClientTarget = msg.getTo();
//					System.out.println("Servo i messaggi verso l'utente: "+temporaryClientTarget);
//				}
//				if(msg.getTo() == temporaryClientTarget){
//					System.out.println("In consegna: " +msg.getMessage());
//					MessagesToDeliver.add(msg);
//					//OUT_ChatMessageList.remove(msg);
//					i--;
//				}
//					
//				System.out.println("Deliver message - " + msg.getMessage());
//			}
//			
//			
//			
//			ClientThread_MessageSender cts = new ClientThread_MessageSender(MessagesToDeliver);
//			cts.start();
//			
//			System.err.println("Consegna di "+MessagesToDeliver.size()+" messaggi. Messaggi residui: "+OUT_ChatMessageList.size());
//		}
		
		
		return true;
	}

}

class ChatMessageComparator implements Comparator{
	   
    public int compare(Object msg1, Object msg2){
   
        int idDest1 = ((Message)msg1).getTo();  
        int progNum1 = ((Message)msg1).getProgressiveNum();
        int idDest2 = ((Message)msg2).getTo();
        int progNum2 = ((Message)msg2).getProgressiveNum();
       
        if(idDest1 > idDest2)
            return 1;
        else if(idDest1 < idDest2)
            return -1;
        
        if(progNum1 > progNum2)
            return 1;
        else if(progNum1 < progNum2)
            return -1;

        return 0;    
    }
   
}
