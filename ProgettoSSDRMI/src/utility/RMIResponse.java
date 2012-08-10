package utility;

import java.io.Serializable;

/**
 *	Classe utilizzata per comunicare una risposta più completa al client tramite RMI
 */
public class RMIResponse implements Serializable{
	private boolean result;
	private String message;
	
	public RMIResponse(boolean result, String message){
		this.setResult(result);
		this.setMessage(message);
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
