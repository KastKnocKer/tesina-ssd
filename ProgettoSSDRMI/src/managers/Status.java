package managers;
import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	public final static boolean DEBUGINLAN = false;
	public final static boolean DEBUG = true;
	public final static boolean SUPER_DEBUG = true; 
	public final static int TYPE_SIP = 			0;
	public final static int TYPE_CLIENT = 		1;
	public final static int TYPE_SIPCLIENT = 	-1;
	public final static int P2P_TTL = 4;
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
	static int SIP_Port = 1101;
	static int Client_Port = 1102;
	private static int RMIRegistryPort = 1099;
	static int Type = 0;
	static String PrivateKey = "";
	static String PublicKey = "";
	private static String Email = "";
	static String SIP_Address = "kastknocker.no-ip.biz";
	private static String LastLoginUsername = "biofrost88@gmail.com";
	private static String LastLoginPassword = "bio";
	
	// private static String SIP_Address = "192.168.1.113";

	
	/* Costruttore */
	public Status(){}
	

	public boolean readConfXML(){
		try {
			File fXmlFile = new File("CONF.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Configuration");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					//LETTURA ELEMENTI DI CONFIGURAZIONE
					System.out.println("*** Lettura CONF.XML ***");
					try {	Type = Integer.parseInt(getTagValue("Type", eElement));					} catch (Exception e) {System.err.println("Read Type exception");}
					try {	SIP_Port = Integer.parseInt(getTagValue("SIP_Port", eElement));			} catch (Exception e) {}
					try {	Client_Port = Integer.parseInt(getTagValue("Client_Port", eElement));	} catch (Exception e) {}
					try {	PrivateKey = getTagValue("PrivateKey", eElement);						} catch (Exception e) {}
					try {	PublicKey = getTagValue("PublicKey", eElement);							} catch (Exception e) {}
					try {	SIP_Address = getTagValue("SIP_Address", eElement);						} catch (Exception e) {}
					try {	LastLoginUsername = getTagValue("LastLoginUsername", eElement);						} catch (Exception e) {}
					try {	LastLoginPassword = getTagValue("LastLoginPassword", eElement);						} catch (Exception e) {}
					
					
					
					//RISULTATI
					System.out.println("Type: " + Type);
					System.out.println("SIP_Port: " + SIP_Port);
					System.out.println("Client_Port: " + Client_Port);
					System.out.println("PrivateKey: " + PrivateKey);
					System.out.println("PublicKey: " + PublicKey);
					System.out.println("SIP_Address: " + SIP_Address);
					
					
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Client - readConfXML() exception FILE NOT FOUND");
			writeConfXML();
		} catch (Exception e) {
			System.out.println("Client - readConfXML() exception");
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean writeConfXML(){
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Configuration");
			doc.appendChild(rootElement);
			
			Element elemento;
			//TYPE
			elemento = doc.createElement("Type");
				elemento.setTextContent(Integer.toString(Type));
					rootElement.appendChild(elemento);
			//SIP ADDRESS
			elemento = doc.createElement("SIP_Address");
				elemento.setTextContent(SIP_Address);
					rootElement.appendChild(elemento);
			//SIP PORT
			elemento = doc.createElement("SIP_Port");
				elemento.setTextContent(Integer.toString(SIP_Port));
					rootElement.appendChild(elemento);
			//CLIENT PORT
			elemento = doc.createElement("Client_Port");
				elemento.setTextContent(Integer.toString(Client_Port));
					rootElement.appendChild(elemento);
			//PrivateKey
			elemento = doc.createElement("PrivateKey");
				elemento.setTextContent(PrivateKey);
					rootElement.appendChild(elemento);
			//PublicKey
			elemento = doc.createElement("PublicKey");
				elemento.setTextContent(PublicKey);
					rootElement.appendChild(elemento);
					
			//LastLoginUsername
			elemento = doc.createElement("LastLoginUsername");
				elemento.setTextContent(LastLoginUsername);
					rootElement.appendChild(elemento);
			
			//LastLoginPassword
			elemento = doc.createElement("LastLoginPassword");
				elemento.setTextContent(LastLoginPassword);
					rootElement.appendChild(elemento);
			
/*			

			// staff elements
			Element staff = doc.createElement("Staff");
			rootElement.appendChild(staff);

			// set attribute to staff element
			Attr attr = doc.createAttribute("id");
			attr.setValue("1");
			staff.setAttributeNode(attr);

			// shorten way
			// staff.setAttribute("id", "1");

			// firstname elements
			Element firstname = doc.createElement("firstname");
			firstname.appendChild(doc.createTextNode("yong"));
			staff.appendChild(firstname);

			// lastname elements
			Element lastname = doc.createElement("lastname");
			lastname.appendChild(doc.createTextNode("mook kim"));
			staff.appendChild(lastname);

			// nickname elements
			Element nickname = doc.createElement("nickname");
			nickname.appendChild(doc.createTextNode("mkyong"));
			staff.appendChild(nickname);

			// salary elements
			Element salary = doc.createElement("salary");
			salary.appendChild(doc.createTextNode("100000"));
			staff.appendChild(salary);
*/
			System.out.println("*** Scrittura CONF.XML ***");
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("CONF.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
			
			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
		return true;
	}
	

	public static String getSIPAddress()	{				return SIP_Address;	}
	public static int getType() 			{				return Type; }
	public static int getSIP_Port() {						return SIP_Port; }
	public static void setSIP_Port(int sIP_PORT) {			SIP_Port = sIP_PORT;	}
	public static int getClient_Port() {					return Client_Port;	}
	public static void setClient_Port(int cLIENT_PORT) {	Client_Port = cLIENT_PORT;	}
	public static String getPublicKey() {					return PublicKey;	}
	public static void setPublicKey(String publicKey) {		PublicKey = publicKey;	}
	public static String getGlobalIP() {					return GlobalIP;	}
	public static void setGlobalIP(String globalIP) {		GlobalIP = globalIP;	}
	public static String getEmail() {						return Email;	}
	public static void setEmail(String email) {				Email = email;	}
	public static String getNome() {						return Nome;	}
	public static void setNome(String nome) {				Nome = nome;	}
	public static String getCognome() {						return Cognome;	}
	public static void setCognome(String cognome) {			Cognome = cognome;	}
	public static String getNickname() {					return Nickname;	}
	public static void setNickname(String nickname) {		Nickname = nickname;	}
	public static ChatStatusList getStato() {					return Stato;	}
	public static void setStato(ChatStatusList stato) {			Stato = stato;	}
	public static int getUserID() {							return UserID;	}
	public static void setUserID(int userID) {				UserID = userID;	}
	
	public static String getAvatarURL() {
		return AvatarURL;
	}

	public static void setAvatarURL(String avatarURL) {
		AvatarURL = avatarURL;
	}

	//FUNZIONI DI UTILITY
	private String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = nlList.item(0);
		return nValue.getNodeValue();
	}

	public static int getRMIRegistryPort() {		return RMIRegistryPort;	}
	public static void setRMIRegistryPort(int rMIRegistryPort) {	RMIRegistryPort = rMIRegistryPort;	}
	public static String getLocalIP() {		return LocalIP;	}
	public static void setLocalIP(String localIP) {		LocalIP = localIP;	}



	public static String getLastLoginUsername() {
		return LastLoginUsername;
	}



	public static void setLastLoginUsername(String lastLoginUsername) {
		LastLoginUsername = lastLoginUsername;
	}



	public static String getLastLoginPassword() {
		return LastLoginPassword;
	}



	public static void setLastLoginPassword(String lastLoginPassword) {
		LastLoginPassword = lastLoginPassword;
	}
	
	public static boolean isDebuginlan() {
		return DEBUGINLAN;
	}


	public static boolean isLOGGED() {		return LOGGED;	}
	public static void setLOGGED(boolean lOGGED) {		LOGGED = lOGGED;	}


	public static Contact getMyInfoIntoContact(){
		Contact contact = new Contact();
		contact = new Contact();
		contact.setGlobalIP(		Status.getGlobalIP());
		contact.setLocalIP(			Status.getLocalIP());
		contact.setStatus(			Status.getStato());
		contact.setNickname(		Status.getNickname());
		contact.setAvatarURL(		Status.getAvatarURL());
		contact.seteMail(			Status.getEmail());
		contact.setID(				Status.getUserID());
		contact.setClient_Port(		Status.getClient_Port());
		return contact;
	}


	
}
