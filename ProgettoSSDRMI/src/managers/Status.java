package managers;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import layout.homeframe.Home_Frame;
import client.thread.ClientThread;
import client.thread.ClientThread_SipRequestor;
import RMI.Client;
import RMI.ClientInterface;
import chat.ChatStatusList;
import chat.Contact;


/**
 * Classe statica utilizzata per gestire lo stato dell'applicazione
 * (Configurazione - Contatti - Stato contatti)
 * 
 * @author Andrea Castelli
 */

public class Status {
	
	private static Client client = null;
	
	
	private static boolean LOGGED = false;
	private static boolean LOGGEDP2P = false;
	public final static boolean DEBUGINLAN = true;
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
	private static int Type = 1;
	private static String PrivateKey = "";
	private static String PublicKey = "";
	private static String Email = "";
	private static String SIP_Address = "192.168.1.112";	//"kastknocker.no-ip.biz";
	private static String LastLoginUsername = "biofrost88@gmail.com";
	private static String LastLoginPassword = "bio";
	
	// private static String SIP_Address = "192.168.1.113";

	/**
	 * Metodo per restituire un avatar di Default nel caso 
	 * in cui non sia disponibile l'avatar di Default.
	 * 
	 * @return URL dell'avatar di default da utilizzare quando 
	 * un altro avatar è disponibile. 
	 */
	public static String getDefaultAvatarURL() {
		return "images/avatars/default.jpg"; 
	}
	
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
		System.out.println("SET SIP STATUS ONLINE: "+sIPStatusOnline);
	}
	
	public static void setStato(ChatStatusList stato) {						Stato = stato;	}
	public static void setType(int type) {									Type = type;	}
	public static void setUserID(int userID) {								UserID = userID;	}


	/* Costruttore */
	public Status(){}
	public static boolean isLOGGEDP2P() {		return LOGGEDP2P;	}
	public static void setLOGGEDP2P(boolean lOGGEDP2P) {		LOGGEDP2P = lOGGEDP2P;	}


	public static boolean startClient(){
		System.out.println("*** Client is starting ***");
		
		if(client == null){
			try {
	            client = new Client();
//	            ClientInterface stub = (ClientInterface) UnicastRemoteObject.exportObject(client, Status.getClient_Port());
//	            // Registro il SIP nel RMIREGISTRY
//	            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
//	            //System.out.println("Registry port "+registry.REGISTRY_PORT);
//	            registry.rebind("Client", stub);
	            System.out.println("*** Client obj ready ***");
			} catch (Exception e) {
	            System.out.println("Client obj exception:\n" + e.toString());
	            JOptionPane.showMessageDialog(null, e.getMessage(), "[CLIENT] Status.startClient() ClientInterface object exception", JOptionPane.ERROR_MESSAGE);
	            System.out.println("EXIT FORZATO");
	            System.exit(0);
			}
			// imposto visualizzazione con look and feel del sistema operativo in uso 
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (Exception ex) {
						System.err.println("Impossibile impostare L&F di sistema");
					}
					
					
					/**
					 * Mostro l'Home Frame
					 * @author Fabio Pierazzi 
					 */
					// places the application on the Swing Event Queue 
					SwingUtilities.invokeLater(new Runnable() {
				            public void run() {
				            	
				            	Home_Frame hf = new Home_Frame(); 
				            	hf.setVisible(true);
				            }
					});
			
			return true;
			
		}else{
			ClientInterface stub;
			try {
				stub = (ClientInterface) UnicastRemoteObject.exportObject(client, Status.getClient_Port());
	            // Registro il SIP nel RMIREGISTRY
	            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
	            //System.out.println("Registry port "+registry.REGISTRY_PORT);
	            registry.rebind("Client", stub);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
	}
	
	public static boolean bindClient(){
		if(client == null){
			client = new Client();
		}
		System.out.println("*** Client binding ***");
		unbindClient();
		try {
            ClientInterface stub = (ClientInterface) UnicastRemoteObject.exportObject(client, Status.getClient_Port());
            // Registro il SIP nel RMIREGISTRY
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            //System.out.println("Registry port "+registry.REGISTRY_PORT);
            registry.rebind("Client", stub);
            System.out.println("*** Client obj ready ***");
            return true;
		} catch (Exception e) {
            System.out.println("Client obj exception:\n" + e.toString());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Client obj exception", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
	
	public static boolean unbindClient(){
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry("localhost", 1099);
			registry.unbind("Client");
			return true;
		} catch (RemoteException | NotBoundException e1) {
			System.err.println("Exception catched: Unbinding Client");
		}
		return false;
	}
	
	public static boolean unbindSIP(){
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry("localhost", 1099);
			registry.unbind("SIP");
			return true;
		} catch (RemoteException | NotBoundException e1) {
			System.err.println("Exception catched: Unbinding Client");
		}
		return false;
	}
	
}
