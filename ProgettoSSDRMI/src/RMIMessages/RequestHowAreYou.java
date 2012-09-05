package RMIMessages;

import chat.Contact;

public class RequestHowAreYou extends RMIBasicMessage{

	private Contact SenderContact;
	
	public RequestHowAreYou(Contact SenderContact){
		super();
		this.SenderContact = SenderContact;
	}

	public Contact getSenderContact() {
		return SenderContact;
	}

	public void setSenderContact(Contact senderContact) {
		SenderContact = senderContact;
	}

}
