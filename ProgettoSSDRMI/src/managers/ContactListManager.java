package managers;

import java.io.File;
import java.rmi.RemoteException;
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

import layout.friendslist.FriendsList_Table;
import layout.managers.LayoutReferences;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import RMI.ClientInterface;

import client.ClientEngine;

import chat.ChatStatusList;
import chat.Contact;
import chat.FriendsList;

/**
 * Classe per la gestione della ContactList globale.
 * 
 * @author Fabio Pierazzi
 *
 */
public class ContactListManager {

	static ArrayList<Contact> contactList = new ArrayList<Contact>();
	
	/**
	 * Metodo per impostare una nuova contactList. 
	 * Salva anche i contatti all'interno della FriendsList. 
	 * 
	 * @param contactList
	 */
	public static void setContactList(ArrayList<Contact> contactList) {
		//TODO aggiungere aggiornamento della tabella visuale
		//todo Aggiungere aggiornamento friendlist
		
		
		/* salva come nuova contactList globale */
		ContactListManager.contactList = contactList;
		
		/* copia nella friendsList globale (classe di interfaccia
		 * per mostrare la lista amici nella tabella visibile graficamente
		 * nella GUI.  */
		FriendsList temp_friendsList = new FriendsList(); 
		for(Contact contact : contactList){
			temp_friendsList.addFriend(contact.getFriend());
		}
		
		FriendsListManager.setFriendsList(temp_friendsList); 
		
		/* boh? */
		ContactListManager.writeContactsXML();
	}

	/**
	 * Metodo per reperire la lista globale dei contatti. 
	 * @return ArrayList dei contatti
	 */
	public static ArrayList<Contact> getContactList() {
		if(contactList == null) 
			contactList = new ArrayList<Contact>(); 
		
		return contactList;
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
	
	/**
	 * Ricerco un contatto in base all'indirizzo email
	 * 
	 * @param email del contatto
	 * @return il contatto, se viene trovato; null, altrimenti
	 */
	
		public static Contact searchContactByEmail(String email) {
		
		/* Se la contactList è vuota quando si cerca di fare una ricerca... */
		if(contactList == null) {
			System.out.println("Status.searchContactById: la lista contatti non esiste; ne creo una. ");
			contactList = new ArrayList<Contact>(); 
			return null;
		}
		
		/* Ricerco il contatto all'interno della lista */
		for(Contact contact : contactList) {
			if(contact.getEmail().equals(email))
				return contact; 
		}
			
		/* Se arrivo fin qua, significa che non è stato trovato, 
		 * quindi ritorno null */
		return null; 
	}
		
		
		/**
		 * Aggiungo un nuovo contatto alla ContactList.
		 * ATTENZIONE: Esegue anche un update della FriendsListTable, 
		 * che deve quindi essere stata creata. 
		 * 
		 * @param newContact da aggiungere
		 * @author Fabio Pierazzi
		 */
		public static void addToContactList(Contact newContact) {
			
			/* Controllo che il contatto non sia già presente nella lista */
			
			if(newContact.getID() < 0)
				return; 
			
			Contact searchedContact = ContactListManager.searchContactById(newContact.getID()); 
			
			if(searchedContact != null) {
				System.err.println("Si sta cercando di aggiungere un contatto già presente");
				return; 
			}
			
			/* Aggiungo il contatto alla lista */
			ArrayList<Contact> contactList = getContactList(); 
			contactList.add(newContact); 
			
//			/* Aggiungo il nuovo contatto anche alla FriendsList */
//			FriendsList friendsList = FriendsListManager.getFriendsList(); 
//			friendsList.addFriend(newContact.getFriend()); 
			
			/* Aggiorno graficamente la tabella della lista amici */
			try {
				LayoutReferences.getFriendsListTable().updateTable();
			} catch (Exception e) {
				e.printStackTrace(); 
			}
		}
		
		/**
		 * Rimuove (anche graficamente) un amico dalla Lista Contatti.
		 * Inoltre, lo rimuovo anche dal SIP ed avviso anche l'altro 
		 * contatto, se non OFFLINE. 
		 * 
		 * @param removeId
		 */
		public static boolean removeFromContactList(Contact friendContact) {
			
			Contact myContact = Status.getMyInfoIntoContact(); 
			
			int friendID = friendContact.getID(); 
			
			if(Status.DEBUG) 
				System.err.println("Starting contact removal...");
			
			/* Cerco il contatto e lo rimuovo */
			ArrayList<Contact> contactList = ContactListManager.getContactList(); 
			
			int pos = 0; 
			boolean found = false; 
			
			for(Contact contact : contactList) {
				if(contact.getID() == friendID) {
					try{
						contactList.remove(pos); 
						found = true; 
					} catch(Exception e) {
						System.err.println("Errore nel corso della rimozione di un contatto.");
						e.printStackTrace(); 
						return false; 
					}
					
					break; 
				}
				pos++; 
			}
			
			/* Se non l'ho trovato, ritorno */
			if(found == false) {
				System.err.println("Contatto " + friendContact.getEmail() + " non trovato. Rimozione interrotta.");
				return false; 
			}
			
			/* Rimuovo l'amico sul SIP */
			try {
				ClientEngine.getSIP().removeFriendship(myContact, friendContact);
			} catch (RemoteException e1) {
				System.err.println("Errore durante la richiesta al SIP.");
				e1.printStackTrace();
				return false; 
			} 
			
			/* Notifico il client rimosso della rimozione, 
			 * di modo che non mi veda più online.
			 * Lo notifico solo se lui non è OFFLINE. */
			if(friendContact.getStatus() != ChatStatusList.OFFLINE) {
				
				
				ClientInterface client = null;
				
				/* se sono in LAN */
				if(Status.getGlobalIP().equals(friendContact.getGlobalIP())) {
					/* reperisco il client da rimuovere */
					client = ClientEngine.getClient(friendContact.getLocalIP()); 
				/* se invece NON sono in LAN */
				} else {
					/* reperisco il client da rimuovere */
					client = ClientEngine.getClient(friendContact.getGlobalIP()); 
				}
				
					/* se ho reperito un client */
						if(client!= null) {
							
							try {
								/* gli notifico la rimozione */
								client.receiveFriendshipRemovalNotificationFromContact(friendContact);
							} catch (RemoteException e) {
								System.err.println("Non è stato possibile notificare il client della rimozione " +
										"del contatto.");
								e.printStackTrace();
								
								/* restituisco true perché comunque sono riuscito a rimuoverlo sul SIP */
								return true;
							} 
						} else {
							System.err.println("Problema nel reperimento del contatto.");
							
							/* restituisco true perché comunque sono riuscito a rimuoverlo sul SIP */
							return true; 
						}
			}
			
			
			/* Aggiorno graficamente la tabella con la lista amici */
			FriendsList_Table table = LayoutReferences.getFriendsListTable();

			/* Se non c'è la tabella, non la aggiorno */
			if(table == null) {
				return true; 
			} else {
				try {
					table.updateTable();
				} catch(Exception e) {
					System.err.println("Errore nell'aggiornamento della tabella" +
							" a seguito della rimozione di un contatto.");
					return true; 
				}
				 
			}
			
			
			/* Mostro un messaggio di notifica dell'avvenuta rimozione con successo */
			JOptionPane.showMessageDialog(null, "Il contatto " + friendContact.getNickname() + " ( " + 
					friendContact.getEmail() + " )  è stato rimosso.", 
					"Aggiungi contatto", JOptionPane.INFORMATION_MESSAGE);
			
			if(Status.DEBUG) 
				System.err.println("Contact removed.");
			
			return true; 
		}
		
		

		
		
	/**
	 * Scrive l'elenco contatti su File XML
	 * @return
	 * @author Andrea Castelli
	 */
	public static boolean writeContactsXML(){
			if(contactList == null) contactList = new ArrayList<Contact>();
	//			contactList.add(new Contact("Nickname1", "Nome1", "Cognome1", "eMail1", "Password1", null, null));
	//			contactList.add(new Contact("Nickname2", "Nome2", "Cognome2", "eMail2", "Password2", null, null));
	//			contactList.add(new Contact("Nickname3", "Nome3", "Cognome3", "eMail3", "Password3", null, null));
			
			
			
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
						email.setTextContent(contact.getEmail());
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
					elemento.setTextContent(Integer.toString(Status.Type));
						rootElement.appendChild(elemento);
				//SIP ADDRESS
				Node newNode = doc.createTextNode("asd");
					elemento.setTextContent(Status.SIP_Address);
						rootElement.appendChild(elemento);
				//SIP PORT
				elemento = doc.createElement("SIP_Port");
					elemento.setTextContent(Integer.toString(Status.SIP_Port));
						rootElement.appendChild(elemento);
				//CLIENT PORT
				elemento = doc.createElement("Client_Port");
					elemento.setTextContent(Integer.toString(Status.Client_Port));
						rootElement.appendChild(elemento);
				//PrivateKey
				elemento = doc.createElement("PrivateKey");
					elemento.setTextContent(Status.PrivateKey);
						rootElement.appendChild(elemento);
				//PublicKey
				elemento = doc.createElement("PublicKey");
					elemento.setTextContent(Status.PublicKey);
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

	

}
