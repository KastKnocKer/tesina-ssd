package managers;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import sun.swing.MenuItemLayoutHelper.LayoutResult;

import layout.friendslist.FriendsList_Table;
import layout.managers.ConversationWindowsManager;
import layout.managers.LayoutReferences;
import RMI.ClientInterface;
import RMIMessages.FriendshipRequest;
import RMIMessages.FriendshipRequestType;
import chat.ChatStatusList;
import chat.Contact;
import chat.FriendsList;
import client.ClientEngine;
import client.requesttosip.RequestToSIP;
import client.requesttosip.RequestToSIPListManager;
import client.requesttosip.RequestToSIPTypeList;

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
			
			System.err.println("ContactListManager: addToContactList: starting... ");
			
			if(newContact.getID() < 0)
				return; 
			
			Contact searchedContact = ContactListManager.searchContactById(newContact.getID()); 
			
			if(searchedContact != null) {
				System.err.println("Si sta cercando di aggiungere un contatto già presente");
				return; 
			}
			
			/* Aggiungo il contatto alla lista */
			ArrayList<Contact> contactList = getContactList(); 
			
			System.err.println("ContactListManager: addToContactList: adding contact... ");
			
			if(contactList != null)
				contactList.add(newContact); 
			
			/* Aggiorno graficamente la tabella */
			FriendsList_Table table = LayoutReferences.getFriendsListTable();
			if(table!=null) {
				System.err.println("Aggiorno graficamente la tabella.");
				table.updateTable();
			}
			
			/* Scrivo sul file XML */
			System.err.println("ContactListManager: addToContactList: writeContactsXML()");
			FileContactsManager.writeContactsXML();
			
			System.err.println("ContactListManager: addToContactList: ended.");
			
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
			
			/* Itero su una copia della lista */
			ArrayList<Contact> contactListCopy = (ArrayList<Contact>) contactList.clone(); 
			
			for(Contact contact : contactListCopy) {
				
				if(found == true) 
					break; 
				
				if(contact.getID() == friendID) {
					try{
						
						/* distruggo il frame di conversazione */
						ConversationWindowsManager.removeConversationFrame(contact);
						/* rimuovo il contatto dalla lista */
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
				System.err.println("Contatto " + friendContact.getEmail() + " non trovato. Rimozione dalla ContactList interrotta.");
				return false; 
			}
			
			

			/* Aggiorno graficamente la tabella con la lista amici */
			FriendsList_Table table = LayoutReferences.getFriendsListTable();

			/* Aggiorno la tabella */
			try {
				if(table!=null) {
					System.err.println("removeFromContactList: rimuovo il contatto " + friendContact.getEmail() + " dalla FriendsListTable!");
					table.updateTable();
				}
					
			} catch(Exception e) {
				System.err.println("Errore nell'aggiornamento della tabella" +
						" a seguito della rimozione di un contatto.");
				return true; 
			}
			
			/* Scrivo sul file XML */
			System.err.println("ContactListManager: removeFromContactList: writeContactsXML()");
			FileContactsManager.writeContactsXML();
			
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
							} 
						} else {
							System.err.println("Rimozione amico: " + friendContact.getNickname() + "; Problema nel reperimento del contatto.");
							
							/* restituisco true perché comunque sono riuscito a rimuoverlo sul SIP */
//							return true; 
						}
			}
			
			
			/* Preparo la richiesta di rimozione */
			FriendshipRequest request = new FriendshipRequest(
					FriendshipRequestType.REMOVE_FRIEND, 
					myContact, 
					friendContact);
			
			/* Rimuovo l'amico sul SIP */
			try {

				ClientEngine.getSIP().removeFriendship(request);
				
			} catch (RemoteException e1) {
				System.err.println("Errore durante la richiesta di rimozione amicizia al SIP.");
				System.err.println("Eccezione gestita: ");
				e1.printStackTrace();
				
				System.err.println("Accodo richiesta di rimozione per re-invio al SIP.");
				RequestToSIP rtsip = new RequestToSIP(RequestToSIPTypeList.FRIENDSHIP_REQUEST, request); 
				RequestToSIPListManager.addRequest(rtsip); 
//				return false; 
			} catch(Exception e2) {
				System.err.println("Errore durante la richiesta di rimozione amicizia al SIP.");
				System.err.println("Eccezione gestita: ");
				e2.printStackTrace();
				
				System.err.println("Accodo richiesta di rimozione per re-invio al SIP.");
				RequestToSIP rtsip = new RequestToSIP(RequestToSIPTypeList.FRIENDSHIP_REQUEST, request); 
				RequestToSIPListManager.addRequest(rtsip); 

				/* ritorno false se non sono riuscito a rimuoverlo sul SIP! */
				return true; 
			}
			
			return true; 
		}
		
		

	public static void addContactsFromSIPContactList(ArrayList<Contact> SIPContactList){
		if(SIPContactList == null)
			return;
		
		for(Contact contact : SIPContactList){
			Contact tmpContact = searchContactById(contact.getID());
			if(tmpContact == null){
				contact.setUpdatedFromSIP(true);
				contact.setTemporary(false);
				getContactList().add(contact);
			}else{
				tmpContact.setUpdatedFromSIP(true);
				tmpContact.setTemporary(false);
				//Se il contatto NON è ONLINE o RAGGIUNGIBILE
				if(!tmpContact.isConnected()){
					tmpContact.setNickname(	contact.getNickname());
					tmpContact.setGlobalIP(	contact.getGlobalIP());
					tmpContact.setLocalIP(	contact.getLocalIP());
				}
			}
		}
		
		//Elimino gli eventuali contatti che avevo ricevuto in passato dal SIP, ma di cui non ho ricevuto aggiornamento e che sono quindi da RIMUOVERE
		Contact contact = null;
		for(int i=0; i<contactList.size(); i++){
			contact = contactList.get(i);
			if((!contact.isTemporary()) && (!contact.isUpdatedFromSIP())){
				contactList.remove(contact);
				i--;
			}
		}
	}
		
	

	

}
