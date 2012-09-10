package chat;

import java.util.ArrayList;

public class StatusP2P {
	private static final int NUM_MAX_REQUEST_LIST = 30;
	private static ArrayList<int[]> requestList = new ArrayList<int[]>();

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
	

}
