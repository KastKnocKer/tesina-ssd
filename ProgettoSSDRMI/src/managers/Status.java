package managers;
import chat.ChatStatusList;
import chat.Contact;


/**
 * Classe statica utilizzata per gestire lo stato dell'applicazione
 * (Configurazione - Contatti - Stato contatti)
 * 
 * @author Andrea Castelli
 */

public class Status {
	private static boolean LOGGED = false;
	private static boolean LOGGEDP2P = false;
	public final static boolean DEBUGINLAN = false;
	public final static boolean DEBUG = true;
	public final static boolean SUPER_DEBUG = true; 
	public final static int TYPE_SIP = 			0;
	public final static int TYPE_CLIENT = 		1;
	public final static int TYPE_SIPCLIENT = 	-1;
	public final static int P2P_TTL = 4;
	private static boolean SIPStatusOnline = true;
	private static String GlobalIP;
	private static String LocalIP;
	public static Contact localUser;
	
	private static int UserID = -1;
	private static String Nome = "***";
	private static String Cognome = "***";
	private static String Nickname = "***";
	private static String AvatarURL = ""; 
	private static ChatStatusList Stato = ChatStatusList.ONLINE;
	
	/////////////////////////////////////////////////
	//	DATI DA CARICARE DA CONF.XML
	////////////////////////////////////////////////
	private static int SIP_Port = 1101;
	private static int Client_Port = 1102;
	private static int RMIRegistryPort = 1099;
	private static int Type = 0;
	private static String PrivateKey = "";
	private static String PublicKey = "";
	private static String Email = "";
	private static String SIP_Address = "kastknocker.no-ip.biz";
	private static String LastLoginUsername = "biofrost88@gmail.com";
	private static String LastLoginPassword = "bio";
	
	// private static String SIP_Address = "192.168.1.113";

	
	public static String getAvatarURL() {				return AvatarURL;	}
	public static int getClient_Port() {				return Client_Port;	}
	public static String getCognome() {					return Cognome;	}
	public static String getEmail() {					return Email;	}
	public static String getGlobalIP() {				return GlobalIP;	}
	public static String getLastLoginPassword() {		return LastLoginPassword;}
	public static String getLastLoginUsername() {		return LastLoginUsername;}
	public static String getLocalIP() {					return LocalIP;	}
	public static Contact getLocalUser() {				return localUser;	}

	public static Contact getMyInfoIntoContact(){
		Contact contact = new Contact();
		contact = new Contact();
		contact.setGlobalIP(		Status.getGlobalIP());
		contact.setLocalIP(			Status.getLocalIP());
		contact.setStatus(			Status.getStato());
		contact.setNickname(		Status.getNickname());
		contact.setAvatarURL(		Status.getAvatarURL());
		contact.setEmail(			Status.getEmail());
		contact.setID(				Status.getUserID());
		contact.setClient_Port(		Status.getClient_Port());
		return contact;
	}

	public static String getNickname() {				return Nickname;	}
	public static String getNome() {					return Nome;	}
	public static int getP2pTtl() {						return P2P_TTL;}
	public static String getPrivateKey() {				return PrivateKey;	}
	public static String getPublicKey() {				return PublicKey;	}
	public static int getRMIRegistryPort() {			return RMIRegistryPort;	}
	public static String getSIP_Address() {				return SIP_Address;	}
	public static int getSIP_Port() {					return SIP_Port; }
	public static String getSIPAddress()	{			return SIP_Address;	}
	public static ChatStatusList getStato() {			return Stato;	}
	public static int getType() 			{			return Type; }
	public static int getTypeClient() {					return TYPE_CLIENT;	}
	public static int getTypeSip() {					return TYPE_SIP;	}
	public static int getTypeSipclient() {				return TYPE_SIPCLIENT;	}
	public static int getUserID() {						return UserID;	}
	
	public static boolean isDebug() {					return DEBUG;	}
	public static boolean isDebuginlan() {				return DEBUGINLAN;	}
	public static boolean isLOGGED() {					return LOGGED;	}
	public static boolean isSIPStatusOnline() {			return SIPStatusOnline;	}
	public static boolean isSuperDebug() {				return SUPER_DEBUG;	}
	
	
	public static void setAvatarURL(String avatarURL) {						AvatarURL = avatarURL;	}
	public static void setClient_Port(int cLIENT_PORT) {					Client_Port = cLIENT_PORT;	}
	public static void setCognome(String cognome) {							Cognome = cognome;	}
	public static void setEmail(String email) {								Email = email;	}
	public static void setGlobalIP(String globalIP) {						GlobalIP = globalIP;	}
	public static void setLastLoginPassword(String lastLoginPassword) {		LastLoginPassword = lastLoginPassword;	}	
	public static void setLastLoginUsername(String lastLoginUsername) {		LastLoginUsername = lastLoginUsername;	}
	public static void setLocalIP(String localIP) {							LocalIP = localIP;	}
	public static void setLocalUser(Contact localUser) {					Status.localUser = localUser;	}
	public static void setLOGGED(boolean lOGGED) {							LOGGED = lOGGED;	}
	public static void setNickname(String nickname) {						Nickname = nickname;	}
	public static void setNome(String nome) {								Nome = nome;	}
	public static void setPrivateKey(String privateKey) {					PrivateKey = privateKey;	}
	public static void setPublicKey(String publicKey) {						PublicKey = publicKey;	}
	public static void setRMIRegistryPort(int rMIRegistryPort) {			RMIRegistryPort = rMIRegistryPort;	}
	public static void setSIP_Address(String sIP_Address) {					SIP_Address = sIP_Address;	}
	public static void setSIP_Port(int sIP_PORT) {							SIP_Port = sIP_PORT;	}

	public static void setSIPStatusOnline(boolean sIPStatusOnline) {
		SIPStatusOnline = sIPStatusOnline;
		System.out.println("SIP ONLINE: "+sIPStatusOnline);
	}
	
	public static void setStato(ChatStatusList stato) {						Stato = stato;	}
	public static void setType(int type) {									Type = type;	}
	public static void setUserID(int userID) {								UserID = userID;	}


	/* Costruttore */
	public Status(){}
	public static boolean isLOGGEDP2P() {		return LOGGEDP2P;	}
	public static void setLOGGEDP2P(boolean lOGGEDP2P) {		LOGGEDP2P = lOGGEDP2P;	}


	
}
