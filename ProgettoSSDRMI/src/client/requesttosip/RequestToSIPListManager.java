package client.requesttosip;

import java.util.ArrayList;

import managers.Status;
import RMI.SIPInterface;
import RMIMessages.FriendshipRequest;
import RMIMessages.FriendshipRequestType;
import RMIMessages.RequestLoginMessage;
import RMIMessages.ResponseLoginMessage;
import client.ClientEngine;
import RMIMessages.*;

public class RequestToSIPListManager {
	
	private static int ProgressiveNum = 0;
	private static ArrayList<RequestToSIP> RequestsToSIP = new ArrayList<RequestToSIP>();
	
	/**
	 * Aggiunge alla lista una richiesta destinata al SIP
	 */
	public static synchronized void addRequest(RequestToSIP rtsip){
		if(Status.SUPER_DEBUG) 
			System.out.println("Aggiungo richiesta: "+rtsip.getRequestType().toString());
		
		rtsip.setProgressiveNum(ProgressiveNum++);
		RequestsToSIP.add(rtsip);
	}
	
	/**
	 * Rimuove una richiesta destinata al SIP dalla lista delle richieste
	 */
	public static synchronized void removeRequest(RequestToSIP rtsip){
		if(Status.SUPER_DEBUG) 
			System.out.println("Rimuovo richiesta: "+rtsip.getRequestType().toString());
		
		RequestsToSIP.remove(rtsip);
	}
	
	/**
	 * Risolve le richieste destinate al SIP
	 */
	public static synchronized void sendRequests(){
		if(Status.SUPER_DEBUG && RequestsToSIP.size()>0) System.out.println("Tentativo richieste al SIP - Num richieste: "+RequestsToSIP.size());
		
		//Se la lista delle richieste è nulla ritorno
		if( RequestsToSIP.size() == 0)
			return;
		
		/* Reperisco l'interfaccia del SIP */
		SIPInterface sip = null; 
		
		try {
			sip = ClientEngine.getSIP();
		} catch (Exception e) {
			System.err.println("RequestToSIPListManager: ClientEngine.getSIP() timeout");
			return; 
		}
		
		if(sip == null){
			for(RequestToSIP req : RequestsToSIP)
				System.out.println("--> RequestToSIP : "+req.getRequestType());
			return; 
		}
			
		
		for(RequestToSIP req : RequestsToSIP){
			try{
				if(req.getRequestType() == RequestToSIPTypeList.LOGIN){
					
					RequestLoginMessage rlm = (RequestLoginMessage) req.getRequestMessage();
					ResponseLoginMessage resplm = ClientEngine.Login(rlm.getUsername(), rlm.getPassword());
					if(resplm != null && resplm.isSUCCESS()){
						RequestsToSIP.remove(req);
					}
					
				} else if(req.getRequestType() == RequestToSIPTypeList.FRIENDSHIP_REQUEST){
					
					if(((FriendshipRequest) req.getRequestMessage()).getRequestType() == FriendshipRequestType.REMOVE_FRIEND) {
						System.out.println("Invio richieste al SIP offline: tentativo di rimozione amicizia");
						RMISIPBasicResponseMessage rmiResponse = sip.removeFriendship( (FriendshipRequest) req.getRequestMessage() );
						
						if(rmiResponse != null) {
							if(rmiResponse.isSUCCESS()) {
								RequestsToSIP.remove(req); 
							}
						}
					} else if(((FriendshipRequest) req.getRequestMessage()).getRequestType() == FriendshipRequestType.ADD_FRIEND ||
							((FriendshipRequest) req.getRequestMessage()).getRequestType() == FriendshipRequestType.FORCE_ADD_FRIEND) {
						System.out.println("Invio richieste al SIP offline: tentativo di aggiunta amicizia");
						RMISIPBasicResponseMessage rmiResponse = sip.addFriendship( (FriendshipRequest) req.getRequestMessage() );
						
						if(rmiResponse != null) {
							if(rmiResponse.isSUCCESS()) {
								RequestsToSIP.remove(req); 
							}
						}
					}
					
					
					
				} else if(req.getRequestType() == RequestToSIPTypeList.YYY){
					//TODO rimuovere se non serve
				} else if(req.getRequestType() == RequestToSIPTypeList.ZZZ){
					//TODO rimuovere se non serve
				} 
				
				
			}catch(Exception e){
				System.err.println("Eccezione gestita: TENTATIVO INVIO RICHIESTE AL SIP OFFLINE");
				e.printStackTrace(); 
			}
			
			
		}
	}

	public static ArrayList<RequestToSIP> getRequestsToSIP() {
		return RequestsToSIP;
	}
	
	
	

}




