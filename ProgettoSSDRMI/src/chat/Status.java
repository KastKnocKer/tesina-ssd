package chat;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Vector;

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

import RMIMessages.RMIBasicMessage;


/**
 * 
 * @author Andrea Castelli
 * Classe statica utilizzata per gestire lo stato dell'applicazione
 *
 *	Configurazione - Contatti - Stato contatti
 */

public class Status {
	public final static boolean DEBUG = true; 
	public final static int TYPE_SIP = 	0;
	public final static int TYPE_CLIENT = 	1;
	private static String GlobalIP;
	private static FriendsList friendsList;
	private static ArrayList<Contact> contactList;
		
	



	public static Contact localUser;
	
	
	
	/////////////////////////////////////////////////
	//DATI DA CARICARE DA CONF.XML
	////////////////////////////////////////////////
	private static int SIP_Port = 1101;
	private static int Client_Port = 1102;
	private static int Type = 0;
	private static String PrivateKey = "";
	private static String PublicKey = "";
	public static String getPublicKey() {
		return PublicKey;
	}



	public static void setPublicKey(String publicKey) {
		PublicKey = publicKey;
	}



	private static String SIP_Address = "kastknocker.no-ip.biz";
	//private static String SIP_Address = "192.168.1.113";

	
	
	public Status(){}
	
	

/** Metodo per reperire la lista amici globale 
	 * @return lista amici
	 */
	public static FriendsList getFriendsList() {
		return friendsList;
	}
	
	public static void loadFriendListFromSIP(){
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
	 * Metodo per caricare la lista amici non filtrata 
	 */
	// TODO da perfezionare
	public static void loadFriendsList() {
		
		// TODO Controllare che esista il file CONTACTS.xml
		// TODO Se esiste, caricare i contatti dal file altrimenti richiedere al sip
		ClientEngine.LoadContactsFromSIP();
		FriendsList temp_friendsList = new FriendsList(); 
		
		/* Primo amico */
		Friend f1 = new Friend(); 
		f1.setNickname("Fabio"); 
		f1.setStatus(StatusList.ONLINE);
		f1.setUserId(1); 
		System.out.println( f1.getUserId() + ". " + f1.getNickname() + ": " + f1.getStatus());
		
		/* Secondo amico */
		Friend f2 = new Friend(); 
		f2.setNickname("Kast"); 
		f2.setStatus(StatusList.BUSY);
		f2.setUserId(2); 
		System.out.println( f2.getUserId() + ". " + f2.getNickname() + ": " + f2.getStatus());
		
		/* terzo amico */
		Friend f3 = new Friend(); 
		f3.setNickname("Marco"); 
		f3.setStatus(StatusList.BUSY);
		f3.setUserId(3); 
		System.out.println( f3.getUserId() + ". " + f3.getNickname() + ": " + f3.getStatus());
		
		temp_friendsList.addFriend(f1); 
		temp_friendsList.addFriend(f2); 
		temp_friendsList.addFriend(f3); 
		
		Status.setFriendsList(temp_friendsList); 
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
					try {	Type = Integer.parseInt(getTagValue("Type", eElement));					} catch (Exception e) {}
					try {	SIP_Port = Integer.parseInt(getTagValue("SIP_Port", eElement));			} catch (Exception e) {}
					try {	Client_Port = Integer.parseInt(getTagValue("Client_Port", eElement));	} catch (Exception e) {}
					try {	PrivateKey = getTagValue("PrivateKey", eElement);						} catch (Exception e) {}
					try {	PublicKey = getTagValue("PublicKey", eElement);							} catch (Exception e) {}
					try {	SIP_Address = getTagValue("SIP_Address", eElement);						} catch (Exception e) {}
					
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
			System.out.println("readConfXML() exception FILE NOT FOUND");
			writeConfXML();
		} catch (Exception e) {
			System.out.println("readConfXML() exception");
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean writeConfXML(){
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
	

	public boolean writeContactsXML(){
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
					id.setTextContent(Integer.toString(contact.getID()));
					nome.setTextContent(contact.getNome());
					cognome.setTextContent(contact.getCognome());
					email.setTextContent(contact.geteMail());
					nickname.setTextContent(contact.getNickname());
					stato.setTextContent("stato");	/////
				
					contactElem.appendChild(id);
					contactElem.appendChild(nome);
					contactElem.appendChild(cognome);
					contactElem.appendChild(email);
					contactElem.appendChild(nickname);
					contactElem.appendChild(stato);
					
				
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
	
	//FUNZIONI DI UTILITY
	private String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = nlList.item(0);
		return nValue.getNodeValue();
	}



	public static String getGlobalIP() {
		return GlobalIP;
	}
	public static void setGlobalIP(String globalIP) {
		GlobalIP = globalIP;
	}

	public static ArrayList<Contact> getContactList() {
		return contactList;
	}
	public static void setContactList(ArrayList<Contact> contactList) {
		//TODO aggiungere aggiornamento della tabella visuale
		//todo Aggiungere aggiornamento friendlist
		Status.contactList = contactList;
	}
}
