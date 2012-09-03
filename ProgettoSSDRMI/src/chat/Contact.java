package chat;

import java.io.Serializable;

public class Contact implements Serializable{
	public static String NONE = "";
	private int UserID = 0;
	private String Nickname = NONE;
	private String Nome = NONE;
	private String Cognome = NONE;
	private String eMail = NONE;
	private String Password = NONE;
	

	private StatusList Status = StatusList.OFFLINE;
	private static int Client_Port = 1102;
	private String GlobalIP = NONE;
	private String LocalIP = NONE;
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
	
	/** 
	 * Metodo che restituisce una versione semplificata (cioè, con meno parametri) 
	 * della classe Contact, utilizzata per mostare informazioni all'interno della 
	 * lista amici 
	 * 
	 * @return un'istanza della classe Friend, versione semplificata 
	 * (cioè, con meno parametri) della classe Contact.
	 */
	public Friend getFriend(){
		Friend friend = new Friend();
		friend.setUserId(UserID);
		friend.setNickname(Nickname);
		friend.setStatus(Status);
		friend.setEmail(eMail); 
		return friend;
	}
	
	public int getID() {
		return UserID;
	}

	public void setID(int iD) {
		UserID = iD;
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
		System.out.println("Contact Info: " + UserID +" - "+ eMail+ " - "+ Nickname +" - "+ GlobalIP);
	}
	public StatusList getStatus() {		
		return Status;	
	}

	public void setStatus(StatusList status) {		
		Status = status;	
	}
	
	public void setStatus(String status) {		
		if(	status.equals(StatusList.ONLINE.toString())	)				this.Status = StatusList.ONLINE;
		else if(	status.equals(StatusList.BUSY.toString())	)		this.Status = StatusList.BUSY;
		else if(	status.equals(StatusList.AWAY.toString())	)		this.Status = StatusList.AWAY;
		else if(	status.equals(StatusList.OFFLINE.toString())	)	this.Status = StatusList.OFFLINE;
	}

	public static int getClient_Port() {
		return Client_Port;
	}

	public static void setClient_Port(int client_Port) {
		Client_Port = client_Port;
	}

	public String getLocalIP() {
		return LocalIP;
	}

	public void setLocalIP(String localIP) {
		LocalIP = localIP;
	}

	
	
}
