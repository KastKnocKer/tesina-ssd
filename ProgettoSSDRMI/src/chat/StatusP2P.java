package chat;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public class StatusP2P {
	private static final int NUM_MAX_REQUEST_LIST = 30;
	private static ArrayList<int[]> requestList = new ArrayList<int[]>();
	//ID RICHIEDENTE, ID RISPOSTA, CONTATTO TROVATO
	private static ArrayList<WhoisResponse> whoisResponsesList = new ArrayList<WhoisResponse>();

	public StatusP2P(){
		
	}
	
	/**
	 * Aggiunge la traccia di una richiesta ricevuta.
	 * Ritorna true se riesce ad aggiungere la richiesta ricevuta altrimenti torna false
	 */
	public static boolean addRequest(int userid, int numquery){
		if(!checkRequest(userid, numquery)){
			requestList.add(new int[]{userid,numquery});
			if(requestList.size()>NUM_MAX_REQUEST_LIST)
				requestList.remove(0);	//Rimuovo la query più vecchia
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Controlla se la richiesta è già presente nella lista delle richieste
	 */
	public static boolean checkRequest(int userid, int numquery){
		for(int[] array : requestList){
			if( (array[0] == userid) && (array[1] == numquery) ){
				//Query già ricevuta
				return true;
			}
		}
		//Query mai ricevuta
		return false;
	}
	
	/**
	 * Aggiunge alla lista una risposta ricevuta dal whois
	 */
	public static boolean addWhoisResponse(int RequestFromID, int RandomNum, int ResponseFromID, Contact contact){
		whoisResponsesList.add(new WhoisResponse(RequestFromID, RandomNum, ResponseFromID, contact));
		return true;
	}
	
	/**
	 * Ritorna in modo sicuro il contatto
	 */
	public static Contact getWhoisResponse(int RequestFromID, int RandomNum){
		Contact contact = null;
		for(WhoisResponse whoisResp : whoisResponsesList){
			if(whoisResp.getRequestFromID() == RequestFromID && whoisResp.getRandomNum() == RandomNum){
				if(contact == null){
					contact = whoisResp.getContact();
				}else{
					//Controllo che le informazioni siano le stesse
					boolean a = contact.getID() == whoisResp.getContact().getID();
					boolean b = contact.getGlobalIP().equals(whoisResp.getContact().getGlobalIP());
					boolean c = contact.getLocalIP().equals(whoisResp.getContact().getLocalIP());
					boolean d = contact.getEmail().equals(whoisResp.getContact().getEmail());
					
					if(a&&b&&c&&d){
						//OK
					}else{
						//Il risultato è errato!
						System.out.println("Hai ricevuto risposte inerenti lo stesso contatto diverse!!");
						return null;
					}
				}
			}
		}
		return contact;
	}
	
	/**
	 * Rimuove tutte le risposte di una determinata richiesta
	 */
	public static void removeWhoisResponse(int RequestFromID, int RandomNum){
		for(WhoisResponse whoisResp : whoisResponsesList){
			if(whoisResp.getRequestFromID() == RequestFromID && whoisResp.getRandomNum() == RandomNum){
				whoisResponsesList.remove(whoisResp);
			}
		}
		return;
	}

}
