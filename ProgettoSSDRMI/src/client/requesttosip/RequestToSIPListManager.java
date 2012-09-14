package client.requesttosip;

import java.util.ArrayList;

import RMI.SIPInterface;

import client.ClientEngine;

import managers.Status;

public class RequestToSIPListManager {
	
	private static int ProgressiveNum = 0;
	private static ArrayList<RequestToSIP> RequestsToSIP = new ArrayList<RequestToSIP>();
	
	/**
	 * Aggiunge alla lista una richiesta destinata al SIP
	 */
	public static synchronized void addRequest(RequestToSIP rtsip){
		if(Status.SUPER_DEBUG) System.out.println("Aggiungo richiesta: "+rtsip.getRequestType().toString());
		rtsip.setProgressiveNum(ProgressiveNum++);
		RequestsToSIP.add(rtsip);
	}
	
	/**
	 * Rimuove una richiesta destinata al SIP dalla lista delle richieste
	 */
	public static synchronized void removeRequest(RequestToSIP rtsip){
		if(Status.SUPER_DEBUG) System.out.println("Rimuovo richiesta: "+rtsip.getRequestType().toString());
		RequestsToSIP.remove(rtsip);
	}
	
	/**
	 * Risolve le richieste destinate al SIP
	 */
	public static synchronized void sendRequests(){
		if(Status.SUPER_DEBUG) System.out.println("Tentativo richieste al SIP");
		
		//Se la lista delle richieste � nulla ritorno
		if( RequestsToSIP.size() == 0)
			return;
		
		SIPInterface sip = ClientEngine.getSIP();

		for(RequestToSIP req : RequestsToSIP){
			
			try{
				if(req.getRequestType() == RequestToSIPTypeList.ATTIVA){
					
				} else if(req.getRequestType() == RequestToSIPTypeList.ATTIVA){
					
				} else if(req.getRequestType() == RequestToSIPTypeList.ATTIVA){
					
				} else if(req.getRequestType() == RequestToSIPTypeList.ATTIVA){
					
				} 
				
				
			}catch(Exception e){
				
			}
			
			
			
			
			
			
			
			
		}
	}
	
	

}



