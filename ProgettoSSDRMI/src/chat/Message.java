package chat;

import java.io.Serializable;

public class Message implements Serializable{
	
	private int From = -1;
	private int To = -1;
	private String Message = "";
	
	public Message(int FromUserId, int ToUserId, String message){
		From = FromUserId;
		To	= ToUserId;
		Message = message;
	}

	public int getFrom() {
		return From;
	}

	public void setFrom(int from) {
		From = from;
	}

	public int getTo() {
		return To;
	}

	public void setTo(int to) {
		To = to;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}
	
	
	

}
