package chat;

public class WhoisResponse {
	
	private int RequestFromID;
	private int RandomNum;
	private int ResponseFromID;
	private Contact contact;
	
	public WhoisResponse(int RequestFromID, int RandomNum, int ResponseFromID, Contact contact){
		this.RequestFromID = RequestFromID;
		this.RandomNum = RandomNum;
		this.ResponseFromID = ResponseFromID;
		this.contact = contact;
	}

	public int getRequestFromID() {
		return RequestFromID;
	}

	public void setRequestFromID(int requestFromID) {
		RequestFromID = requestFromID;
	}

	public int getResponseFromID() {
		return ResponseFromID;
	}

	public void setResponseFromID(int responseFromID) {
		ResponseFromID = responseFromID;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public int getRandomNum() {
		return RandomNum;
	}

	public void setRandomNum(int randomNum) {
		RandomNum = randomNum;
	}
	
	
}
