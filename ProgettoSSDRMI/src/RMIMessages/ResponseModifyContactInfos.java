package RMIMessages;

import chat.Contact;

public class ResponseModifyContactInfos extends RMISIPBasicResponseMessage{
	Contact contact;
	public ResponseModifyContactInfos(boolean success, String msg, Contact contact){
		super(success,msg);
		this.contact = contact;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}

}
