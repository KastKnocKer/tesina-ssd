package chat;

import java.io.Serializable;

public class Contact implements Serializable{
	public static String NONE = "";
	private int ID = 0;
	private String Nickname = NONE;
	private String Nome = NONE;
	private String Cognome = NONE;
	private String eMail = NONE;
	private String Password = NONE;

	private String GlobalIP = NONE;
	private String[][] LocalIPs = null;
	
	public Contact(){
		super();
	}
	
	public Contact(String Nickname, String Nome, String Cognome, String eMail, String Password, String GlobalIP, String[][] LocalIPs){
		this.Nickname = Nickname;
		this.Nome = Nome;
		this.Cognome = Cognome;
		this.eMail = eMail;
		this.Password = Password;
		
		this.GlobalIP = GlobalIP;
		this.LocalIPs = LocalIPs;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getNickname() {
		return Nickname;
	}

	public void setNickname(String nickname) {
		Nickname = nickname;
	}

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public String getCognome() {
		return Cognome;
	}

	public void setCognome(String cognome) {
		Cognome = cognome;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getGlobalIP() {
		return GlobalIP;
	}

	public void setGlobalIP(String globalIP) {
		GlobalIP = globalIP;
	}

	public String[][] getLocalIPs() {
		return LocalIPs;
	}

	public void setLocalIPs(String[][] localIPs) {
		LocalIPs = localIPs;
	}

	public void printInfo(){
		System.out.println(ID +" - "+ eMail+ " - "+ Nickname);
	}
	
	 
	
	
}
