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
import RMIMessages.FriendshipRequest;
import RMIMessages.FriendshipRequestType;

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
		FileContactsManager.writeContactsXML();
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
//			try {
//				LayoutReferences.getFriendsListTable().updateTable();
//			} catch (Exception e) {
//				e.printStackTrace(); 
//			}
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
				
				if(found == true) 
					break; 
				
				if(contact.getID() == friendID) {
					try{
						contactList.remove(pos); 
						found = true; 
						break;
					} catch(Exception e) {
						System.err.println("Errore nel corso della rimozione di un contatto.");
						e.printStackTrace(); 
						return false; 
					}
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

				FriendshipRequest request = new FriendshipRequest(
						FriendshipRequestType.REMOVE_FRIEND, 
						myContact, 
						friendContact);
				
				ClientEngine.getSIP().removeFriendship(request);
				
			} catch (RemoteException e1) {
				System.err.println("Errore durante la richiesta al SIP.");
				e1.printStackTrace();
				return false; 
			} 

			/* Aggiorno graficamente la tabella con la lista amici */
			FriendsList_Table table = LayoutReferences.getFriendsListTable();

			/* Aggiorno la tabella */
			try {
				if(table!=null)
					table.updateTable();
			} catch(Exception e) {
				System.err.println("Errore nell'aggiornamento della tabella" +
						" a seguito della rimozione di un contatto.");
				return true; 
			}
			
			
			/* Mostro un messaggio di notifica dell'avvenuta rimozione con successo */
			JOptionPane.showMessageDialog(null, "Il contatto " + friendContact.getNickname() + " ( " + 
					friendContact.getEmail() + " )  è stato rimosso.", 
					"Rimozione contatto", JOptionPane.INFORMATION_MESSAGE);
			
			if(Status.DEBUG) 
				System.err.println("Contact removed.");
			
			
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
								client.receiveFriendshipRemovalNotificationFromContact(Status.getMyInfoIntoContact());
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
			
			return true; 
		}
		
		

		
		
	

	

}
