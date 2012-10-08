package managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
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

import RMIMessages.FriendshipRequest;
import RMIMessages.FriendshipRequestType;

import client.requesttosip.RequestToSIP;
import client.requesttosip.RequestToSIPListManager;
import client.requesttosip.RequestToSIPTypeList;

import chat.ChatStatusList;
import chat.Contact;

public class FileContactsManager {

	/**
	 * Scrive l'elenco contatti su File XML
	 * @return
	 * @author Andrea Castelli
	 */
	public static boolean writeContactsXML(){
		ArrayList<Contact> contactList = ContactListManager.getContactList();
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Contacts");
			doc.appendChild(rootElement);
			
			//Aggiungo le mie informazioni personali
			Element rootElementInfo = doc.createElement("Info");
				Element username = doc.createElement("Username");
					username.setTextContent(	Status.getLastLoginUsername()	);
					rootElementInfo.appendChild(username);
				Element password = doc.createElement("Password");
					password.setTextContent(	Status.getLastLoginPassword()	);
					rootElementInfo.appendChild(password);
					
				Element mynickname = doc.createElement("Nickname");
					mynickname.setTextContent(	Status.getNickname()	);
					rootElementInfo.appendChild(mynickname);
						
				Element myid = doc.createElement("ID");
					myid.setTextContent(	Integer.toString(Status.getUserID())	);
					rootElementInfo.appendChild(myid);
					
				Element myemail = doc.createElement("Email");
					myemail.setTextContent(	Status.getEmail()	);
					rootElementInfo.appendChild(myemail);
							
					
			rootElement.appendChild(rootElementInfo);
				
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
						Element localIP = doc.createElement("LocalIP");
						Element IsTemporary = doc.createElement("IsTemporary");
						
						id.setTextContent(Integer.toString(contact.getID()));
						nome.setTextContent(contact.getNome());
						cognome.setTextContent(contact.getCognome());
						email.setTextContent(contact.getEmail());
						nickname.setTextContent(contact.getNickname());
						stato.setTextContent(contact.getStatus().toString());
						globalIP.setTextContent(contact.getGlobalIP());
						localIP.setTextContent(contact.getLocalIP());
						IsTemporary.setTextContent(Boolean.toString(contact.isTemporary()));
						
						contactElem.appendChild(id);
						contactElem.appendChild(nome);
						contactElem.appendChild(cognome);
						contactElem.appendChild(email);
						contactElem.appendChild(nickname);
						contactElem.appendChild(stato);
						contactElem.appendChild(globalIP);
						contactElem.appendChild(localIP);
						contactElem.appendChild(IsTemporary);
					
					rootElement.appendChild(contactElem);
			}	
				
				
				
				
			//Per ogni richiesta pendente verso il SIP
			for(RequestToSIP rts : RequestToSIPListManager.getRequestsToSIP()){
				
				if(rts.getRequestType() == RequestToSIPTypeList.FRIENDSHIP_REQUEST){
					FriendshipRequest fr = (FriendshipRequest) rts.getRequestMessage();
					Element friendshipElement = doc.createElement("FriendshipRequest");
					
					Element RequestType = doc.createElement("RequestType");
					RequestType.setTextContent(	fr.getRequestType().toString()	);
						friendshipElement.appendChild(RequestType);
					
					Element IDMittente = doc.createElement("IDMittente");
					IDMittente.setTextContent(	Integer.toString(fr.getContattoMittente().getID())	);
						friendshipElement.appendChild(IDMittente);
					
					Element eMailMittente = doc.createElement("eMailMittente");
					eMailMittente.setTextContent(	fr.getContattoMittente().getEmail()	);
						friendshipElement.appendChild(eMailMittente);
						
					Element IDDestinatario = doc.createElement("IDDestinatario");
					IDDestinatario.setTextContent(	Integer.toString(fr.getContattoDestinatario().getID())	);
						friendshipElement.appendChild(IDDestinatario);
						
					Element eMailDestinatario = doc.createElement("eMailDestinatario");
					eMailDestinatario.setTextContent(	fr.getContattoDestinatario().getEmail()	);
						friendshipElement.appendChild(eMailDestinatario);
						
						
						
					rootElement.appendChild(friendshipElement);
					System.out.println("Richiesta di amicizia scritta su contact.xml");
				}
				
			}
				
				System.out.println("*** Scrittura CONTACTS.XML ***");
				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("CONTACTS_"+Status.getEmail()+".xml"));
	
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
	
	
	
	public static boolean readContactsXML(){
		String fileUsername = "";
		String filePassword = "";
		try {
			System.out.println("*** LETTURA DI CONTACTS_"+Status.getLastLoginUsername()+".xml ***");
			File fXmlFile = new File("CONTACTS_"+Status.getLastLoginUsername()+".xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Contacts");
			
			NodeList nList2 = nList.item(0).getChildNodes();
			
			for (int temp = 0; temp < nList2.getLength(); temp++) {
				Node nNode = nList2.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if(eElement.getNodeName().equals("Info")){
						//Carico le info del file
						try {
							fileUsername = getTagValue("Username", eElement);
							filePassword = getTagValue("Password", eElement);
							//Controllo che il file sia coerente con le mie credenziali
							if( !(Status.getLastLoginUsername().equals(fileUsername) && Status.getLastLoginPassword().equals(filePassword)) ){
								JOptionPane.showMessageDialog(null, "Le credenziali con cui si cerca di connettersi\nnon sono valide!!", "Tentativo di Login P2P", JOptionPane.ERROR_MESSAGE);
								return false;
							}
						} catch (Exception e) {
							System.err.println("FileContactsManager.readContactsXML() - Fallimento caricamento INFOs");
						}
						
						try {	Status.setUserID(Integer.parseInt(getTagValue("ID", eElement)));					} catch (Exception e) {}
						try {	Status.setNickname(getTagValue("Nickname", eElement));								} catch (Exception e) {}
						try {	Status.setEmail(getTagValue("Email", eElement));									} catch (Exception e) {}
						
						
					}else if(eElement.getNodeName().equals("Contact")){
						//Carico i contatti
						Contact contact = new Contact();
						
						try {	contact.setID(Integer.parseInt(getTagValue("ID", eElement)));						} catch (Exception e) {}
						try {	contact.setNome(getTagValue("Nome", eElement));										} catch (Exception e) {}
						try {	contact.setCognome(getTagValue("Cognome", eElement));								} catch (Exception e) {}
						try {	contact.setEmail(getTagValue("Email", eElement));									} catch (Exception e) {}
						try {	contact.setNickname(getTagValue("Nickname", eElement));								} catch (Exception e) {}
						try {	contact.setGlobalIP(getTagValue("GlobalIP", eElement));								} catch (Exception e) {}
						try {	contact.setLocalIP(getTagValue("LocalIP", eElement));								} catch (Exception e) {}
						try {	contact.setTemporary(Boolean.parseBoolean(getTagValue("IsTemporary", eElement)));	} catch (Exception e) {}
						contact.setStatus(ChatStatusList.OFFLINE);
						ContactListManager.addToContactList(contact);
					
					
					}else if(eElement.getNodeName().equals("FriendshipRequest")){
						System.out.println("Carico richiesta di amicizia da effettuare verso il SIP");
						//Aggiungo alla lista delle richieste del sip la richiesta di amicizia
						FriendshipRequest friendReq = null;
						Contact mitt = null;
						Contact dest = null;
						try {
							mitt = new Contact();
							dest = new Contact();
							mitt.setID(Integer.parseInt(getTagValue("IDMittente", eElement)));
							dest.setID(Integer.parseInt(getTagValue("IDDestinatario", eElement)));
							mitt.setEmail(getTagValue("eMailMittente", eElement));
							dest.setEmail(getTagValue("eMailDestinatario", eElement));
						} catch (Exception e) {}
						
						if( mitt != null && dest != null){
							if(FriendshipRequestType.ADD_FRIEND.toString().equals( getTagValue("RequestType", eElement)) ){
								friendReq = new FriendshipRequest(FriendshipRequestType.ADD_FRIEND,mitt,dest);
							}else if(FriendshipRequestType.FORCE_ADD_FRIEND.toString().equals( getTagValue("RequestType", eElement)) ){
								friendReq = new FriendshipRequest(FriendshipRequestType.FORCE_ADD_FRIEND,mitt,dest);
							}else if(FriendshipRequestType.REMOVE_FRIEND.toString().equals( getTagValue("RequestType", eElement)) ){
								friendReq = new FriendshipRequest(FriendshipRequestType.REMOVE_FRIEND,mitt,dest);
							}
							
							RequestToSIPListManager.addRequest( new RequestToSIP(RequestToSIPTypeList.FRIENDSHIP_REQUEST, friendReq));
						}
						
					}
					
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File CONTACTS.XML non trovato.");
			return false;
//			System.out.println("Client - readConfXML() exception FILE NOT FOUND");
//			writeConfXML();
		} catch (Exception e) {
//			System.out.println("Client - readConfXML() exception");
			e.printStackTrace();
		}
		System.out.println("*** FINE LETTURA DI CONTACTS_"+Status.getLastLoginUsername()+".xml ***");
		return true;
	}
	
	
	/**
	 * Funzione di utility per l'estrazione dei valori
	 */
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = nlList.item(0);
		return nValue.getNodeValue();
	}
}
