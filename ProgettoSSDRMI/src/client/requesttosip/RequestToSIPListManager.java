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
	public static synchronized void addRequest(RequestToSIP newRequestToSIP){
		if(Status.SUPER_DEBUG) 
			System.err.println("[CLIENT] Starting aggiunta richiesta in coda al SIP poichè offline: "+newRequestToSIP.getRequestType().toString());
		
		/* Controllo che non vengano aggiunte richieste di aggiunta/rimozione amicizia ridondanti. */
		if(newRequestToSIP.getRequestType() == RequestToSIPTypeList.FRIENDSHIP_REQUEST) {
				FriendshipRequest newFriendshipRequest = (FriendshipRequest) newRequestToSIP.getRequestMessage(); 
				
				/* Se è ADD_FRIEND, devo controllare che: 
				 * - la richiesta di aggiunta non sia già presente
				 * - non sia presente la richiesta di rimozione di quell'amicizia */
				if(newFriendshipRequest.getRequestType() == FriendshipRequestType.ADD_FRIEND) {
					ArrayList<RequestToSIP> RequestsToSIP_copy = (ArrayList<RequestToSIP>) RequestsToSIP.clone();
					
					/* Per tutte le richieste attualmente in coda... */
					for(RequestToSIP requestToSIP_temp : RequestsToSIP_copy) {
						
						/* ... se si tratta di richieste di amicizia... */
						if(requestToSIP_temp.getRequestType()  == RequestToSIPTypeList.FRIENDSHIP_REQUEST) {
							
							FriendshipRequest request_temp = (FriendshipRequest) requestToSIP_temp.getRequestMessage();
							
							/* Se la richiesta esistente ha tipo ADD_FRIEND ... */
							if(request_temp.getRequestType() == FriendshipRequestType.ADD_FRIEND) {
								/* Se mittente e destinatario coincidono con quelli della nuova richiesta che si cerca di aggiungere in coda... */
								if( (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoMittente().getID() 
										&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoDestinatario().getID()))
								{
										/* non faccio niente: la richiesta è già presente */
										System.out.println("[Aggiunta FriendshipRequest ADD_FRIEND]: Richiesta ADD_FRIEND già presente");
										return; 
								}
								
							} else if(request_temp.getRequestType() == FriendshipRequestType.FORCE_ADD_FRIEND) {
								
								if( (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoMittente().getID() 
										&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoDestinatario().getID())
									|| (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoDestinatario().getID() 
										&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoMittente().getID()))
								{
									/* non faccio niente: è già presente una richiesta "più forte" */
									System.out.println("[Aggiunta FriendshipRequest ADD_FRIEND]: Richiesta FORCE_ADD_FRIEND già presente, non è necessario aggiungere la nuova.");
									return;
								}
								
							} else if(request_temp.getRequestType() == FriendshipRequestType.REMOVE_FRIEND) {
								// TODO: cosa fare se voglio chiedere amicizia da A e B e prima c'era una richiesta di rimozione? 
								// TODO: garantire solo che le richieste al SIP vengano consegnate IN ORDINE! 
								
							}
						}
					}
					
				/********************************************************************************************************/
				} else if(newFriendshipRequest.getRequestType() == FriendshipRequestType.FORCE_ADD_FRIEND ) {
					
					ArrayList<RequestToSIP> RequestsToSIP_copy = (ArrayList<RequestToSIP>) RequestsToSIP.clone();
					
					/* Per tutte le richieste attualmente in coda... */
					for(RequestToSIP requestToSIP_temp : RequestsToSIP_copy) {
					
						FriendshipRequest request_temp = (FriendshipRequest) requestToSIP_temp.getRequestMessage();
						
						/* Se la richiesta esistente ha tipo ADD_FRIEND ... */
						if(request_temp.getRequestType() == FriendshipRequestType.ADD_FRIEND) {
							/* Se mittente e destinatario coincidono con quelli della nuova richiesta che si cerca di aggiungere in coda... */
							if( (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoMittente().getID() 
									&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoDestinatario().getID())
								|| (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoDestinatario().getID() 
									&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoMittente().getID()))
							{
									/* non faccio niente: la richiesta è già presente */
									System.out.println("[Aggiunta FriendshipRequest FORCE_ADD_FRIEND]: Rimuovo richiesta ADD_FRIEND 'più debole' già presente");
									RequestsToSIP.remove(requestToSIP_temp); 
							}
							
						} else if(request_temp.getRequestType() == FriendshipRequestType.FORCE_ADD_FRIEND) {
							
							if( (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoMittente().getID() 
									&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoDestinatario().getID())
								|| (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoDestinatario().getID() 
									&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoMittente().getID()))
							{
								/* non faccio niente: è già presente una richiesta "più forte" */
								System.out.println("[Aggiunta FriendshipRequest FORCE_ADD_FRIEND]: Richiesta FORCE_ADD_FRIEND già presente, non è necessario aggiungere la nuova.");
								return;
							}
							
						} else if(request_temp.getRequestType() == FriendshipRequestType.REMOVE_FRIEND) {
							
							if( (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoMittente().getID() 
									&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoDestinatario().getID())
								|| (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoDestinatario().getID() 
									&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoMittente().getID()))
							{
								/* Rimuovo le REMOVE_FRIEND ed anche la FORCE_ADD_FRIEND: l'amico sarà già stato aggiunto */
								// TODO : rifletti se può dare dei problemi: c'è rischio che di un'amicizia poi il SIP non venga notificato?
								System.out.println("[Aggiunta FriendshipRequest FORCE_ADD_FRIEND]: Richiesta REMOVE_FRIEND già presente, " +
										"la rimuovo in quanto non piùnecessaria e non aggiungo neanche la FORCE_ADD_FRIEND.");
								RequestsToSIP.remove(requestToSIP_temp);
								return; 
							}
							
						}
						
					}
					
				/********************************************************************************************************/
				} else if(newFriendshipRequest.getRequestType() == FriendshipRequestType.REMOVE_FRIEND) {
					
					ArrayList<RequestToSIP> RequestsToSIP_copy = (ArrayList<RequestToSIP>) RequestsToSIP.clone();
					
					/* Per tutte le richieste attualmente in coda... */
					for(RequestToSIP requestToSIP_temp : RequestsToSIP_copy) {
					
						FriendshipRequest request_temp = (FriendshipRequest) requestToSIP_temp.getRequestMessage();
						
						/* Se la richiesta esistente ha tipo ADD_FRIEND ... */
						if(request_temp.getRequestType() == FriendshipRequestType.ADD_FRIEND) {
							/* Se mittente e destinatario coincidono con quelli della nuova richiesta che si cerca di aggiungere in coda... */
							if( (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoMittente().getID() 
									&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoDestinatario().getID())
								|| (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoDestinatario().getID() 
									&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoMittente().getID()))
							{	
									// TODO: Può anche solo verificarsi una richiesta di rimozione di un'amicizia che ha richiesta parziale da inviare al SIP?
									// TODO: Se il SIP viene trovato offline, automaticamente faccio FORCE_ADD_FRIEND

									System.err.println("[Aggiunta FriendshipRequest REMOVE_FRIEND] [01]: NON SAREI DOVUTO ENTRARE QUI, ERRORE!");
									
									/* Ritorno senza aggiungere REMOVE, poiché non c'è bisogno */
									System.out.println("[Aggiunta FriendshipRequest REMOVE_FRIEND]: Trovato ADD_FRIEND: lo rimuovo, e non aggiuno il REMOVE.");
									RequestsToSIP.remove(requestToSIP_temp); 
									return; 
							}
							
						} else if(request_temp.getRequestType() == FriendshipRequestType.FORCE_ADD_FRIEND) {
							
							if( (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoMittente().getID() 
									&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoDestinatario().getID())
								|| (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoDestinatario().getID() 
									&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoMittente().getID()))
							{
								/* non faccio niente: è già presente una richiesta "più forte" */
								System.out.println("[Aggiunta FriendshipRequest REMOVE_FRIEND]: Trovato FORCE_ADD_FRIEND: lo rimuovo, e non aggiungo il REMOVE.");
								RequestsToSIP.remove(requestToSIP_temp); 
								return; 
							}
							
						} else if(request_temp.getRequestType() == FriendshipRequestType.REMOVE_FRIEND) {
							
							if( (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoMittente().getID() 
									&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoDestinatario().getID())
								|| (request_temp.getContattoMittente().getID() == newFriendshipRequest.getContattoDestinatario().getID() 
									&& request_temp.getContattoDestinatario().getID() == newFriendshipRequest.getContattoMittente().getID()))
							{
								/* Rimuovo le REMOVE_FRIEND ed anche la FORCE_ADD_FRIEND: l'amico sarà già stato aggiunto */
								System.out.println("[Aggiunta FriendshipRequest REMOVE_FRIEND]: Richiesta REMOVE_FRIEND già presente, esco e non faccio niente.");
								return; 
							}
							
						}
						
					}
				}
				/********************************************************************************************************/
			}
		
		/* Se sono arrivato fin qua senza ritornare, significa che 
		 * questa nuova richiesta POSSO aggiungerla */
		newRequestToSIP.setProgressiveNum(ProgressiveNum++);
		RequestsToSIP.add(newRequestToSIP);
		
		if(Status.SUPER_DEBUG) 
			System.err.println("[CLIENT] SUCCESSFULLY ENDED aggiunta richiesta in coda al SIP poichè offline: " + newRequestToSIP.getRequestType().toString());
	}
	
	/**
	 * Rimuove una richiesta destinata al SIP dalla lista delle richieste
	 */
	public static synchronized void removeRequest(RequestToSIP rtsip){
		
		if(Status.SUPER_DEBUG) 
			System.out.println("[CLIENT] Rimuovo richiesta dalla coda RICHIESTE OFFLINE del SIP: " +rtsip.getRequestType().toString());
		
		RequestsToSIP.remove(rtsip);
	}
	
	/**
	 * Risolve le richieste destinate al SIP
	 */
	public static synchronized void sendRequests(){
		if(Status.SUPER_DEBUG && RequestsToSIP.size()>0) System.out.println("[CLIENT] Tentativo richieste al SIP - Num richieste: "+RequestsToSIP.size());
		
		//Se la lista delle richieste è nulla ritorno
		if( RequestsToSIP.size() == 0)
			return;
		
		/* Reperisco l'interfaccia del SIP */
		SIPInterface sip = null; 
		
		try {
			sip = ClientEngine.getSIP();
		} catch (Exception e) {
			System.err.println("[CLIENT] RequestToSIPListManager: ClientEngine.getSIP() timeout");
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
						System.out.println("[CLIENT] Invio richieste al SIP offline: tentativo di rimozione amicizia");
						RMISIPBasicResponseMessage rmiResponse = sip.removeFriendship( (FriendshipRequest) req.getRequestMessage() );
						
						if(rmiResponse != null) {
							if(rmiResponse.isSUCCESS()) {
								RequestsToSIP.remove(req); 
							}
						}
					} else if(((FriendshipRequest) req.getRequestMessage()).getRequestType() == FriendshipRequestType.ADD_FRIEND ||
							((FriendshipRequest) req.getRequestMessage()).getRequestType() == FriendshipRequestType.FORCE_ADD_FRIEND) {
						System.out.println("[CLIENT] Invio richieste al SIP offline: tentativo di aggiunta amicizia");
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
				System.err.println("[CLIENT] Eccezione gestita: TENTATIVO INVIO RICHIESTE AL SIP OFFLINE");
				e.printStackTrace(); 
			}
			
			
		}
	}

	public static ArrayList<RequestToSIP> getRequestsToSIP() {
		return RequestsToSIP;
	}
	
	
	

}




