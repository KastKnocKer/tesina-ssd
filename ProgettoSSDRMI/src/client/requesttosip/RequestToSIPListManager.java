package client.requesttosip;

import java.util.ArrayList;

import managers.Status;

public class RequestToSIPListManager {
	
	private static int ProgressiveNum = 0;
	private static ArrayList<RequestToSIP> RequestsToSIP = new ArrayList<RequestToSIP>();
	
	public static synchronized void addRequest(RequestToSIP rtsip){
		if(Status.SUPER_DEBUG) System.out.println("Aggiungo richiesta: "+rtsip.getRequestType().toString());
		rtsip.setProgressiveNum(ProgressiveNum++);
		RequestsToSIP.add(rtsip);
	}
	
	public static synchronized void removeRequest(RequestToSIP rtsip){
		if(Status.SUPER_DEBUG) System.out.println("Rimuovo richiesta: "+rtsip.getRequestType().toString());
		RequestsToSIP.remove(rtsip);
	}
	
	public static synchronized void removeRequest(){
		if(Status.SUPER_DEBUG) System.out.println("Tentativo richieste al SIP");

		for(RequestToSIP req : RequestsToSIP){
			
		}
	}
	
	

}




