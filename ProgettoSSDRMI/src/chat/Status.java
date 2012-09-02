package chat;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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

import client.ClientEngine;


/**
 * Classe statica utilizzata per gestire lo stato dell'applicazione
 * (Configurazione - Contatti - Stato contatti)
 * 
 * @author Andrea Castelli
 */

public class Status {
	private static boolean LOGGED = false;
	public final static boolean DEBUG = true;
	public final static boolean SUPER_DEBUG = false; 
	public final static int TYPE_SIP = 			0;
	public final static int TYPE_CLIENT = 		1;
	public final static int TYPE_SIPCLIENT = 	-1;
	private static String GlobalIP;
	private static String LocalIP;
	private static FriendsList friendsList = null;
	private static ArrayList<Contact> contactList = null;
		
	public static Contact localUser;
	
	private static int UserID = -1;
	private static String Nome = "***";
	private static String Cognome = "***";
	private static String Nickname = "***";
	private static String AvatarURL = ""; 
	private static StatusList Stato = StatusList.OFFLINE;
	
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

	
	/* Costruttore */
	public Status(){}
	

	/** Metodo per reperire la lista amici globale 
	 * @return lista amici
	 */
	public static FriendsList getFriendsList() {
		return friendsList;
	}
	
	/**
	 * Metodo per impostare il riferimento globale alla lista amici 
	 * dell'utente.
	 * @param lista amici
	 */
	public static void setFriendsList(FriendsList fl) {
		friendsList = fl; 
	}
	
	/** 
	 * Mediante questo metodo, il sistema carica da file di configurazione
	 * la lista amici (senza filtri), ed essa viene mostrata all'interno
	 * della tabella lista amici dell'interfaccia grafica.
	 * 
	 * @author Fabio Pierazzi
	 */
	// TODO da perfezionare
	public static void loadFriendsList() {
		
		// TODO Controllare che esista il file CONTACTS.xml
		// TODO Se esiste, caricare i contatti dal file altrimenti richiedere al sip
		
		ClientEngine.LoadContactsFromSIP();
	}
	
	
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
	

	public static boolean writeContactsXML(){
		if(contactList == null) contactList = new ArrayList<Contact>();
			contactList.add(new Contact("Nickname1", "Nome1", "Cognome1", "eMail1", "Password1", null, null));
			contactList.add(new Contact("Nickname2", "Nome2", "Cognome2", "eMail2", "Password2", null, null));
			contactList.add(new Contact("Nickname3", "Nome3", "Cognome3", "eMail3", "Password3", null, null));
		
		
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Contacts");
			doc.appendChild(rootElement);
			
			//Per ogni contatto
			//for(int i=0; i<contactList.size(); i++){
			
			for(Contact contact : contactList){
				//Contact contact = contactList.get(i);
				Element contactElem = doc.createElement("Contact");
					Element id = doc.createElement("ID");
					Element nome = doc.createElement("Nome");
					Element cognome = doc.createElement("Cognome");
					Element email = doc.createElement("Email");
					Element nickname = doc.createElement("Nickname");
					Element stato = doc.createElement("Stato");
					Element globalIP = doc.createElement("GlobalIP");
					id.setTextContent(Integer.toString(contact.getID()));
					nome.setTextContent(contact.getNome());
					cognome.setTextContent(contact.getCognome());
					email.setTextContent(contact.geteMail());
					nickname.setTextContent(contact.getNickname());
					stato.setTextContent(contact.getStatus().toString());
					globalIP.setTextContent(contact.getGlobalIP());
					
					contactElem.appendChild(id);
					contactElem.appendChild(nome);
					contactElem.appendChild(cognome);
					contactElem.appendChild(email);
					contactElem.appendChild(nickname);
					contactElem.appendChild(stato);
					contactElem.appendChild(globalIP);
					
				
				rootElement.appendChild(contactElem);
			}	
			
			
			
			
			
			
			
			
			
			
			
			
			Element el1 = doc.createElement("kiss1");
			Element el2 = doc.createElement("kiss2");
			Element el3 = doc.createElement("kiss3");
			Element el4 = doc.createElement("kiss4");
			Element el5 = doc.createElement("kiss5");
			rootElement.appendChild(el1);
			el1.appendChild(el2);
			el2.appendChild(el3);
			el3.appendChild(el4);
			el4.appendChild(el5);
			
			
			
			
			Element elemento;
			//TYPE
			elemento = doc.createElement("Type");
				elemento.setTextContent(Integer.toString(Type));
					rootElement.appendChild(elemento);
			//SIP ADDRESS
			Node newNode = doc.createTextNode("asd");
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
			System.out.println("*** Scrittura CONTACTS.XML ***");
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("CONTACTS.xml"));

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
	public static StatusList getStato() {					return Stato;	}
	public static void setStato(StatusList stato) {			Stato = stato;	}
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

	public static ArrayList<Contact> getContactList() {
		return contactList;
	}
	public static void setContactList(ArrayList<Contact> contactList) {
		//TODO aggiungere aggiornamento della tabella visuale
		//todo Aggiungere aggiornamento friendlist
		
		
		/* salva come nuova contactList globale */
		Status.contactList = contactList;
		
		/* copia nella friendsList globale (classe di interfaccia
		 * per mostrare la lista amici nella tabella visibile graficamente
		 * nella GUI.
		 */
		FriendsList temp_friendsList = new FriendsList(); 
		for(Contact contact : contactList){
			temp_friendsList.addFriend(contact.getFriend());
		}
		
		Status.setFriendsList(temp_friendsList); 
		
		/* boh? */
		Status.writeContactsXML();
	}

	/**
	 *  Ricerco un contatto in base all'ID.
	 * 
	 * @param contactID (su database) del contatto da cercare
	 * @return il contatto, se viene trovato; null, altrimenti.
	 * 
	 *  @author Fabio Pierazzi
	 */
	public static Contact searchContactById(int contactID) {
		
		/* Se la contactList è vuota quando si cerca di fare una ricerca... */
		if(contactList == null) {
			System.out.println("Status.searchContactById: la lista contatti non esiste; ne creo una. ");
			contactList = new ArrayList<Contact>(); 
			return null;
		}
		
		/* Ricerco il contatto all'interno della lista */
		for(Contact contact : contactList) {
			if(contact.getID() == contactID)
				return contact; 
		}
			
		/* Se arrivo fin qua, significa che non è stato trovato, 
		 * quindi ritorno null */
		return null; 
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



	public static boolean isLOGGED() {
		return LOGGED;
	}



	public static void setLOGGED(boolean lOGGED) {
		LOGGED = lOGGED;
	}




	
}
