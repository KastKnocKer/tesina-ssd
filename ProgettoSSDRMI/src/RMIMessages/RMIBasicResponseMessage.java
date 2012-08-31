package RMIMessages;

import java.io.Serializable;
/**
 * Classe messaggio di risposta utilizzata per le richieste del client verso altri clients
 * Per comunicare la riuscita esecuzione delle operazioni remote ed un eventuale messaggio
 */
public class RMIBasicResponseMessage implements Serializable{

	private boolean SUCCESS = false;
	private String MESSAGE = "";
	
	public RMIBasicResponseMessage(boolean Success, String msg){
		SUCCESS = Success;
		MESSAGE = msg;
	}

	public boolean isSUCCESS() {
		return SUCCESS;
	}

	public void setSUCCESS(boolean sUCCESS) {
		SUCCESS = sUCCESS;
	}

	public String getMESSAGE() {
		return MESSAGE;
	}

	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}

}
