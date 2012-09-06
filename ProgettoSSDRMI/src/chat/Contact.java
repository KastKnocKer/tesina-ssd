package chat;

import java.io.Serializable;

public class Contact implements Serializable{
	public static String NONE = "";
	private int UserID = -1;
	private String Nickname = NONE;
	private String Nome = NONE;
	private String Cognome = NONE;
	private String eMail = NONE;
	private String Password = NONE;
	private String AvatarURL = NONE;
	

	public String getAvatarURL() {
		return AvatarURL;
	}

	public void setAvatarURL(String avatarURL) {
		AvatarURL = avatarURL;
	}

	private ChatStatusList Status = ChatStatusList.OFFLINE;
	private int Client_Port = 1102;
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
	 * Metodo che restituisce una versione semplificata (cio�, con meno parametri) 
	 * della classe Contact, utilizzata per mostare informazioni all'interno della 
	 * lista amici 
	 * 
	 * @return un'istanza della classe Friend, versione semplificata 
	 * (cio�, con meno parametri) della classe Contact.
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
	public ChatStatusList getStatus() {		
		return Status;	
	}

	public void setStatus(ChatStatusList status) {		
		Status = status;	
	}
	
	public void setStatus(String status) {		
		if(	status.equals(ChatStatusList.ONLINE.toString())	)				this.Status = ChatStatusList.ONLINE;
		else if(	status.equals(ChatStatusList.BUSY.toString())	)		this.Status = ChatStatusList.BUSY;
		else if(	status.equals(ChatStatusList.AWAY.toString())	)		this.Status = ChatStatusList.AWAY;
		else if(	status.equals(ChatStatusList.OFFLINE.toString())	)	this.Status = ChatStatusList.OFFLINE;
	}

	public int getClient_Port() {
		return Client_Port;
	}

	public void setClient_Port(int client_Port) {
		Client_Port = client_Port;
	}

	public String getLocalIP() {
		return LocalIP;
	}

	public void setLocalIP(String localIP) {
		LocalIP = localIP;
	}

	public void updateInfoFromContact(Contact contact){
		if(UserID != contact.getID()){
			System.out.println("Si sta aggiornando un contatto sbagliato.");
			return;
		}
		GlobalIP 	= 	contact.getGlobalIP();
		LocalIP		=	contact.getLocalIP();
		Status		=	contact.getStatus();
		Nickname	=	contact.getNickname();
		AvatarURL	=	contact.getAvatarURL();
		eMail		=	contact.geteMail();
	}
	
	public boolean isConnected(){
		if(GlobalIP == null || GlobalIP.length() == 0) return false;
		if(Status == ChatStatusList.OFFLINE) return false;
		return true;
	}
	
}
