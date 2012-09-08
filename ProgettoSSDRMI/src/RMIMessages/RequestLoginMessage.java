package RMIMessages;

import chat.ChatStatusList;

public class RequestLoginMessage extends RMIBasicMessage{

	private String Username = "";
	private String Password = "";
	private ChatStatusList Stato = ChatStatusList.OFFLINE;
	
	public RequestLoginMessage(String username, String password, ChatStatusList stato){
		super();
		Username = username;
		Password = password;
		Stato = stato;
	}

	public String getUsername() {					
		return Username;	
	}
	
	public void setUsername(String username) {		
		Username = username;	
	}
	
	public String getPassword() {					
		return Password;	
	}
	
	public void setPassword(String password) {		
		Password = password;	
	}
	
	public ChatStatusList getStato() {					
		return Stato;	
	}
	
	public void setStato(ChatStatusList stato) {		
		Stato = stato;	
	}

}
