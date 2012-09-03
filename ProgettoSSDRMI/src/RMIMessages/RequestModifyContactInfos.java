package RMIMessages;

import chat.Contact;

public class RequestModifyContactInfos extends RMIBasicMessage{
	private Contact contact;
	
	public RequestModifyContactInfos(Contact contact){
		super();
		this.contact = contact;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
}
