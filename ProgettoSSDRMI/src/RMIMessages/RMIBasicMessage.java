package RMIMessages;

import java.io.Serializable;

import chat.Status;
/**
 * Classe messaggio utilizzata per il maggior numero di richieste tra client-sip e client-client
 * Creata per offrire a tutti i messaggi il servizio di autenticazione del mittente\richiedente
 * Utilizzata in particolare nella sua forma non estesa per l'invio di richieste RMI senza il passaggio di parametri
 */
public class RMIBasicMessage implements Serializable {

	private int UserID;
	private String GlobalIP;
	private int ClientPort;
	private String PublicKey;
	private String Email;
	
	
	public RMIBasicMessage(){
		UserID = 		Status.getUserID();
		GlobalIP = 		Status.getGlobalIP();
		ClientPort = 	Status.getClient_Port();
		PublicKey = 	Status.getPublicKey();
		Email =			Status.getEmail();
	}


	public int getRequestorUserID() {						return UserID;	}
	public void setUserID(int userID) {				UserID = userID;	}
	public String getRequestorGlobalIP() {					return GlobalIP;	}
	public void setGlobalIP(String globalIP) {		GlobalIP = globalIP;	}
	public int getRequestorClientPort() {					return ClientPort;	}
	public void setClientPort(int clientPort) {		ClientPort = clientPort;	}
	public String getRequestorPublicKey() {					return PublicKey;	}
	public void setPublicKey(String publicKey) {	PublicKey = publicKey;	}
	public String getRequestorEmail() {						return Email;	}
	public void setEmail(String email) {			Email = email;	}
	
	

}
