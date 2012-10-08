package managers;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import layout.friendslist.FriendsList_Table;
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
	public static synchronized void setContactList(ArrayList<Contact> contactList) {
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
	public static synchronized ArrayList<Contact> getContactList() {
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
	public static synchronized Contact searchContactById(int contactID) {
		
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
	
		public static synchronized Contact searchContactByEmail(String email) {
		
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
		public static synchronized void addToContactList(Contact newContact) {
			
			/* Controllo che il contatto non sia già presente nella lista */
			
			if(newContact.getID() < 0)
				return; 
			
			Contact searchedContact = ContactListManager.searchContactById(newContact.getID()); 
			
			if(searchedContact != null) {
				System.err.println("Si sta cercando di aggiungere un contatto già presente; verrà eventualmente aggiornato");
				if(!searchedContact.isConnected()){
					searchedContact.setLocalIP(newContact.getLocalIP());
					searchedContact.setGlobalIP(newContact.getGlobalIP());
					searchedContact.setNickname(newContact.getNickname());
				}
				return; 
			}
			
			/* Aggiungo il contatto alla lista */
			ArrayList<Contact> myContactList = getContactList(); 
			myContactList.add(newContact); 
			
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
		public static synchronized boolean removeFromContactList(Contact friendContactToRemove) {
			
			System.err.println("ContactListManager.removeFromContactList: begin...");
			
			/* Verifico che il parametro in ingresso non sia null */
			if(friendContactToRemove == null) {
				System.err.println("[error] ContactListManager.removeFromContactList: null parameter!");
				return false; 
			}
			
			
			/* Recupero i contatti */
			Contact myContact = Status.getMyInfoIntoContact(); 
			
			int friendID = friendContactToRemove.getID(); 
			
			if(Status.DEBUG) 
				System.err.println("ContactListManager.removeFromContactList: starting contact removal...");
			
			/* Cerco il contatto e lo rimuovo */
			ArrayList<Contact> myContactList = ContactListManager.getContactList(); 
			
			int pos = 0; 
			boolean found = false; 
			
			if(Status.DEBUG) 
				System.err.println("ContactListManager.removeFromContactList: Beginning contact removal iteration...");
			
			ArrayList<Contact> contactListCopy = (ArrayList<Contact>) myContactList.clone();
			
			for(Contact contact : contactListCopy) {
				
				if(found == true) 
					break; 
				
				if(contact.getID() == friendID) {
					try{
						System.err.println("ContactListManager.removeFromContactList: eseguo metodo remove...");
						myContactList.remove(pos); 
						System.err.println("ContactListManager.removeFromContactList: metodo remove eseguito.");
						found = true; 
						break;
					} catch(Exception e) {
						System.err.println("Eccezione gestita: ContactListManager.removeFromContactList - Errore nel corso della rimozione di un contatto.");
						e.printStackTrace(); 
						return false; 
					}
				}
				
				pos++; 
			}
			
			System.out.println("ContactListManager.removeFromContactList: uscito con successo dal ciclo di rimozione. ");
			
			/* Se non l'ho trovato, ritorno */
			if(found == false) {
				System.err.println("Contatto " + friendContactToRemove.getEmail() + " non trovato. Rimozione interrotta.");
				return false; 
			}
			

			/* Aggiorno graficamente la tabella con la lista amici */
			FriendsList_Table table = LayoutReferences.getFriendsListTable();

			System.err.println("ContactListManager.removeFromContactList: aggiorno tabella... ");
			
			/* Aggiorno la tabella */
			try {
				if(table!=null)
					table.updateTable();
			} catch(Exception e) {
				System.err.println("Errore nell'aggiornamento della tabella" +
						" a seguito della rimozione di un contatto.");
				return true; 
			}
			
			System.err.println("ContactListManager.removeFromContactList: tabella aggiornata. ");
			
			System.err.println("ContactListManager.removeFromContactList: mostro finestra di dialogo. ");
			
			/* mostro joptionpane nel thread della GUI */
			final Contact friendContactToRemove_copy = friendContactToRemove;
			
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	JOptionPane.showMessageDialog(null, "Il contatto " + friendContactToRemove_copy.getNickname() + " ( " + 
	            			friendContactToRemove_copy.getEmail() + " )  è stato rimosso.", 
	    					"Rimozione contatto", JOptionPane.INFORMATION_MESSAGE);
	            }
        	});
			
			
			System.out.println("ContactListManager.removeFromContactList: finestra di dialogo mostrata. ");
			
			if(Status.DEBUG) 
				System.err.println("Contact removed.");
			
			/* Preparo la richiesta di rimozione */
			FriendshipRequest request = new FriendshipRequest(
					FriendshipRequestType.REMOVE_FRIEND, 
					myContact, 
					friendContactToRemove);
			
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
//				return false; 
			}
			
			/* Notifico il client rimosso della rimozione, 
			 * di modo che non mi veda più online.
			 * Lo notifico solo se lui non è OFFLINE. */
			if(friendContactToRemove.getStatus() != ChatStatusList.OFFLINE) {
				
				ClientInterface client = null;
				
				/* se sono in LAN */
				if(Status.getGlobalIP().equals(friendContactToRemove.getGlobalIP())) {
					/* reperisco il client da rimuovere */
					client = ClientEngine.getClient(friendContactToRemove.getLocalIP()); 
				/* se invece NON sono in LAN */
				} else {
					/* reperisco il client da rimuovere */
					client = ClientEngine.getClient(friendContactToRemove.getGlobalIP()); 
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
							System.err.println("Rimozione amico: " + friendContactToRemove.getNickname() + "; Problema nel reperimento del contatto.");
							
							/* restituisco true perché comunque sono riuscito a rimuoverlo sul SIP */
//							return true; 
						}
			}
			
			System.err.println("ContactListManager.removeFromContactList: ended successfully.");
			return true; 
		}

		/**
		 * Aggiunge alla propria lista locale dei contatti gli aggiornamenti che giungono dal SIP
		 * @param SIPContactList
		 */
		public static synchronized void addContactsFromSIPContactList(ArrayList<Contact> SIPContactList) {
			//Setto tutti i contatti locali come non aggiornati dal SIP
			for(Contact contact : contactList){
				contact.setUpdatedFromSIP(false);
			}
			
			//Per ogni contatto ricevuto dal SIP aggiorno la mia lista
			for(Contact contact : SIPContactList){
				Contact localContact = searchContactById(contact.getID());
				
				if(localContact == null){
					//Non ho trovato il contatto
					addToContactList(contact);
				}else{
					//Ho trovato il contatto
					if(!localContact.isConnected()){
						// Se il contatto è nella mia lista e non è raggiungibile aggiorno i suoi dati
						localContact.setLocalIP(contact.getLocalIP());
						localContact.setGlobalIP(contact.getGlobalIP());
						localContact.setNickname(contact.getNickname());
						localContact.setUpdatedFromSIP(true);
						localContact.setTemporary(false);
						
					}
				}
			}
			
			//Elimino i contatti che avevo ricevuto dal SIP in passato, ma di cui non ho ricevuto aggiornamenti (=Contatti rimossi)
			ArrayList<Contact> contactListCopy = (ArrayList<Contact>) contactList.clone();
			for(Contact contact : contactListCopy){
				if(!contact.isTemporary() && !contact.isUpdatedFromSIP())
//					contactList.remove(contact);
					removeFromContactList(contact); 
			}
		}

		
		

		
		
	

	

}
