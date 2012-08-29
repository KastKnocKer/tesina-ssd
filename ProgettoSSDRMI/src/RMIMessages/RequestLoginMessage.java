package RMIMessages;

import chat.StatusList;

public class RequestLoginMessage extends RMIBasicMessage{

	private String Username = "";
	private String Password = "";
	private StatusList Stato = StatusList.OFFLINE;
	
	public RequestLoginMessage(String username, String password, StatusList stato){
		super();
		Username = username;
		Password = password;
		Stato = stato;
	}

}
