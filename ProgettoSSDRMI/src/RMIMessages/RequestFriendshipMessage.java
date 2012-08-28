package RMIMessages;

/**
 * Classe Messaggio per la richiesta di amicizia presso il SIP
 */
public class RequestFriendshipMessage extends RMIBasicMessage{

	private String fromEmail,toEmail;
	public RequestFriendshipMessage(String toEmail){
		super();
		this.fromEmail = fromEmail;
		this.setToEmail(toEmail);
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
}
