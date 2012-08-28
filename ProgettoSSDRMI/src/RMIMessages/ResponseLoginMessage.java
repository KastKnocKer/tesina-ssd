package RMIMessages;

import chat.Contact;
import utility.RMIResponse;

public class ResponseLoginMessage extends RMISIPBasicResponseMessage{

	private Contact contact;
	public ResponseLoginMessage(boolean success, String message, Contact contact){
		super(success,message);
		this.contact=contact;
	}
	public Contact getLoggedContact() {
		return contact;
	}
	public void setLoggedContact(Contact contact) {
		this.contact = contact;
	}

}
