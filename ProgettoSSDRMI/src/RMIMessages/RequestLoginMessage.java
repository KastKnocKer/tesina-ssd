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

	public String getUsername() {					return Username;	}
	public void setUsername(String username) {		Username = username;	}
	public String getPassword() {					return Password;	}
	public void setPassword(String password) {		Password = password;	}
	public StatusList getStato() {					return Stato;	}
	public void setStato(StatusList stato) {		Stato = stato;	}

}
