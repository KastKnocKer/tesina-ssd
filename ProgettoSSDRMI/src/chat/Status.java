package chat;
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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 
 * @author Andrea Castelli
 * Classe statica utilizzata per gestire lo stato dell'applicazione
 *
 *	Configurazione - Contatti - Stato contatti
 */

public class Status {
	
	public final static int TYPE_SIP = 	0;
	public final static int TYPE_CLIENT = 	1;
		
	public static Contact localUser;
	
	
	/////////////////////////////////////////////////
	//DATI DA CARICARE DA CONF.XML
	////////////////////////////////////////////////
	private static int SIP_Port = 1101;
	private static int Client_Port = 1102;
	private static int Type = 0;
	private static String PrivateKey = "";
	private static String PublicKey = "";
	//private static String SIP_Address = "kastknocker.no-ip.biz";
	private static String SIP_Address = "192.168.1.113";

	
	
	public Status(){}
	
	public boolean readConfXML(){
		try {
			File fXmlFile = new File("CONF.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

//			System.out.println("Root element :"
//					+ doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("Configuration");
//			System.out.println("-----------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					System.out.println("*** Lettura CONF.XML ***");
					System.out.println("Type: " + getTagValue("Type", eElement));
					System.out.println("SIP_ADDRESS: " + getTagValue("SIPA_ddress", eElement));

				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("FILE NOT FOUND SAD");
			writeConfXML();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("WAAAAAAAA");
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
			elemento = doc.createElement("SIPA_ddress");
				elemento.setTextContent(SIP_Address);
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

	
	public static String getSIPAddress()	{	return SIP_Address;	}
	public static int getType() 			{	return Type; }
	
	//FUNZIONI DI UTILITY
	private String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = nlList.item(0);
		return nValue.getNodeValue();
	}

	public static int getSIP_Port() {
		return SIP_Port;
	}

	public static void setSIP_Port(int sIP_PORT) {
		SIP_Port = sIP_PORT;
	}

	public static int getClient_Port() {
		return Client_Port;
	}

	public static void setClient_Port(int cLIENT_PORT) {
		Client_Port = cLIENT_PORT;
	}
}
