package chat;

import java.io.Serializable;

public class Contact implements Serializable{
	public static String NONE = "";
	public int ID = 0;
	public String Nickname = NONE;
	public String Nome = NONE;
	public String Cognome = NONE;
	public String eMail = NONE;
	public String Password = NONE;

	public String GlobalIP = NONE;
	public String[][] LocalIPs = null;
	
	public Contact(String Nickname, String Nome, String Cognome, String eMail, String Password, String GlobalIP, String[][] LocalIPs){
		this.Nickname = Nickname;
		this.Nome = Nome;
		this.Cognome = Cognome;
		this.eMail = eMail;
		this.Password = Password;
		
		this.GlobalIP = GlobalIP;
		this.LocalIPs = LocalIPs;
	}
	
	 
	
	
}
