package RMIMessages;

import chat.Contact;

public class ResponseHowAreYou extends RMIBasicMessage{

	private boolean Success;
	private Contact ResponseContact;
	
	public ResponseHowAreYou(boolean Success, Contact ResponseContact){
		super();
		this.setResponseContact(ResponseContact);
		this.setSuccess(Success);
	}

	public Contact getResponseContact() {
		return ResponseContact;
	}

	public void setResponseContact(Contact responseContact) {
		ResponseContact = responseContact;
	}

	public boolean isSuccess() {
		return Success;
	}

	public void setSuccess(boolean success) {
		Success = success;
	}


}
