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

public class FileConfManager {
	
	public static boolean readConfXML(){
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
					try {	Status.setType(		Integer.parseInt(getTagValue("Type", eElement))			);	} catch (Exception e) {System.err.println("Read Type exception");}
					try {	Status.setSIP_Port(	Integer.parseInt(getTagValue("SIP_Port", eElement))		);	} catch (Exception e) {}
					try {	Status.setClient_Port(Integer.parseInt(getTagValue("Client_Port", eElement)));	} catch (Exception e) {}
					try {	Status.setSIP_Address(getTagValue("SIP_Address", eElement)					);	} catch (Exception e) {}
					try {	Status.setLastLoginUsername(getTagValue("LastLoginUsername", eElement)		);	} catch (Exception e) {}
					try {	Status.setLastLoginPassword(getTagValue("LastLoginPassword", eElement)		);	} catch (Exception e) {}
					
					
					//try {	PrivateKey = getTagValue("PrivateKey", eElement);						} catch (Exception e) {}
					//try {	PublicKey = getTagValue("PublicKey", eElement);							} catch (Exception e) {}
					
					//RISULTATI
//					System.out.println("Type: " + Type);
//					System.out.println("SIP_Port: " + SIP_Port);
//					System.out.println("Client_Port: " + Client_Port);
//					System.out.println("PrivateKey: " + PrivateKey);
//					System.out.println("PublicKey: " + PublicKey);
//					System.out.println("SIP_Address: " + SIP_Address);
					
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
				elemento.setTextContent(Integer.toString(Status.getType()));
					rootElement.appendChild(elemento);
			//SIP ADDRESS
			elemento = doc.createElement("SIP_Address");
				elemento.setTextContent(Status.getSIP_Address());
					rootElement.appendChild(elemento);
			//SIP PORT
			elemento = doc.createElement("SIP_Port");
				elemento.setTextContent(Integer.toString(Status.getSIP_Port()));
					rootElement.appendChild(elemento);
			//CLIENT PORT
			elemento = doc.createElement("Client_Port");
				elemento.setTextContent(Integer.toString(Status.getClient_Port()));
					rootElement.appendChild(elemento);
			//PrivateKey
//			elemento = doc.createElement("PrivateKey");
//				elemento.setTextContent(PrivateKey);
//					rootElement.appendChild(elemento);
			//PublicKey
//			elemento = doc.createElement("PublicKey");
//				elemento.setTextContent(PublicKey);
//					rootElement.appendChild(elemento);
					
			//LastLoginUsername
			elemento = doc.createElement("LastLoginUsername");
				elemento.setTextContent(Status.getLastLoginUsername());
					rootElement.appendChild(elemento);
			
			//LastLoginPassword
			elemento = doc.createElement("LastLoginPassword");
				elemento.setTextContent(Status.getLastLoginPassword());
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
	
	/**
	 * Funzione di utility per l'estrazione dei valori
	 */
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = nlList.item(0);
		return nValue.getNodeValue();
	}

}
