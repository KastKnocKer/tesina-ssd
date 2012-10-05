package RMIMessages;

import chat.Contact;

public class ResponseHowAreYou extends RMIBasicMessage{

	private boolean Success;
	private Contact ResponseContact;
	private boolean AreYouMyFriend;		//In risposta indica la presenza dell'amicizia tra i contatti
	
	public ResponseHowAreYou(boolean Success, Contact ResponseContact,boolean AreYouMyFriend){
		super();
		this.setResponseContact(ResponseContact);
		this.setSuccess(Success);
		this.setAreYouMyFriend(AreYouMyFriend);
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

	public boolean isAreYouMyFriend() {
		return AreYouMyFriend;
	}

	public void setAreYouMyFriend(boolean areYouMyFriend) {
		AreYouMyFriend = areYouMyFriend;
	}


}
