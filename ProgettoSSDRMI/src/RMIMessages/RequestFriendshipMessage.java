package RMIMessages;

/**
 * Classe Messaggio per la richiesta di amicizia presso il SIP
 */
public class RequestFriendshipMessage extends RMIBasicMessage{

	private String fromEmail,toEmail;
	public RequestFriendshipMessage(String fromEmail, String toEmail){
		super();
		this.fromEmail = fromEmail;
		this.setToEmail(toEmail);
	}
	
	public String getFromEmail() {
		return fromEmail;
	}
	
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
}
