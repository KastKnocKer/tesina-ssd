package layout.managers;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import layout.conversationframe.Conversation_Frame;
import managers.ContactListManager;
import chat.ChatStatusList;
import chat.Contact;

/**
 * Classe usata per gestire le finestre di conversazione. 
 * Questa classe viene usata per mostrarle, per nasconderle, 
 * e per fare che non vengano aperte due finestre relative
 * alla stessa conversazione. 
 * 
 * @author Fabio Pierazzi
 *
 */
public class ConversationWindowsManager {

	/** Lista dei frame di conversazione */
	private static ArrayList<Conversation_Frame> arrayList_conversationFrames = null;

//	/**
//	 * Restituisce l'array di finestre di conversazione con i vari utenti
//	 * @return Array contenente i frame di conversazione aperti coi vari utenti
//	 */
//	public static ArrayList<Conversation_Frame> getArrayList_conversationFrames() {
//		return arrayList_conversationFrames;
//	}
//
//	/**
//	 * Imposta un nuovo ArrayList di Frame di Conversazione. 
//	 * @param arrayList_conversationFrames
//	 */
//	public static void setArrayList_conversationFrames(
//			ArrayList<Conversation_Frame> arrayList_conversationFrames) {
//		ConversationWindowsManager.arrayList_conversationFrames = arrayList_conversationFrames;
//	}  
	
	
	/**
	 * 
	 * @param userID dell'utente (su database) di cui si vuole cercare la finestra di conversazione
	 * @return posizione del Frame di conversazione con l'utente relativo all'accountEmail fornito. (-1 se non trova nulla)
	 */
	private static int searchConversationWindow(int userID) {
		
		/* Se questa � la prima finestra di conversazione che viene aperta, 
		 * creo una nuova lista e restituisco il valore -1 per comunicare che
		 * la finestra di conversazione che si cercava non � presente */
		if(arrayList_conversationFrames == null) {
			arrayList_conversationFrames = new ArrayList<Conversation_Frame>();
			return -1; 
		}
		
		/* Scorro l'elenco di conversation frame per controllare se ce n'� 
		 * gi� uno aperto relativo all'userID passato come parametro in ingresso */
		
		int pos = 0; 
		try {
			for(Conversation_Frame conversationFrame : arrayList_conversationFrames) {
				
				
				if(conversationFrame == null) 
					continue; 
				
				if(conversationFrame.getContact() == null) 
					continue; 
				
				int temp_userID = conversationFrame.getContact().getID(); 
				System.out.println("Temp: " + temp_userID + "; userID: " + userID);
				if( temp_userID == userID)
					return pos; 
							
				pos++; 
			}
		} catch(Exception e) {
			System.err.println("Eccezione gestita per la ricerca di una finestra di conversazione.");
			e.printStackTrace(); 
		}
		
		/* Se arrivo in fondo, significa che non ho trovato
		 * quello che stavo cercando */
		return -1; 
	}
	
	/**
	 * Metodo che restituisce l'istanza del Conversation_Frame 
	 * della conversazione con un contatto dato l'id del contatto
	 * relativo a quella conversazione. 
	 * 
	 * @param userID
	 * @return Conversation_Frame, se lo trova; null, altrimenti. 
	 */
	public static Conversation_Frame getConversationFrame(int userID) {
		
		/* controllo che ci sia */
		if(arrayList_conversationFrames == null) {
			arrayList_conversationFrames = new ArrayList<Conversation_Frame>(); 
			return null;
		}
		
		int result = searchConversationWindow(userID); 
		
		if(result == -1) 
			return null; 
		
		return arrayList_conversationFrames.get(result); 
	}
	
	/**
	 * Apro la finestra di conversazione con un certo utente. 
	 * Se la finestra non � ancora stata aperta, la inizializzo; 
	 * altrimenti, la rendo nuovamente visibile. 
	 * 
	 * @param contact, istanza della classe Contact corrispondente al contatto invocato. 
	 */
	public static void showConversationFrame(Contact contact) {
		
		int result = -1;
		
		/* recupero tutte le informazioni sul contatto in base all'id */
		Contact completeContact = ContactListManager.searchContactById(contact.getID());
		contact = completeContact; 
		
		/* Verifico se c'� un ConversationFrame gi� aperto */
		try {
			result = searchConversationWindow(contact.getID());
		} catch(Exception e) {
			System.err.println("Errore durante l'apertura della finestra di conversazione.");
			return; 
		}

		switch(result) {
			/* apro una nuova finestra di conversazione */
			case -1: 
				Conversation_Frame cf = new Conversation_Frame(contact);
//				cf.setUserID_conversation(userID); 
				arrayList_conversationFrames.add(cf); 
				cf.setVisible(true);
				break;
			
			/* rendo visibile la finestra di conversazione che era stata solamente nascosta */
			default: 
				System.out.println("Conversazione con " + contact.getNickname() + " ( " + contact.getEmail() + " ) era gi� stata aperta");
//				arrayList_conversationFrames.get(result).setLocationRelativeTo(null); 
				arrayList_conversationFrames.get(result).setVisible(true); 
				break; 
		}
		
		
		/* Se il contatto � offline, avverto l'utente con una finestra di dialogo. */
		if(contact.getStatus() == ChatStatusList.OFFLINE) {
			JOptionPane.showMessageDialog(
					null, 
					"Attenzione!\n\n" +
					"Il contatto: \n" +
					"" + contact.getEmail() + "\n" +
							"al momento � offline. \n\n" +
							"I messaggi gli verranno recapitati\n" +
							"non appena torner� online.", 
					"Conversazione con " + contact.getNickname(),
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**
	 * Nasconde la finestra di conversazione con un certo utente.
	 * @param userID dell'utente (su database) di cui si vuole cercare la finestra di conversazione
	 */
	public static void hideConversationFrame(int userID) {
		
		/* Verifico se c'� un ConversationFrame gi� aperto */
		int result = searchConversationWindow(userID);
		
		switch(result) {
			/* non faccio niente */
			case -1: 
				break;
			
			/* rendo NON visibile la finestra di conversazione con l'utente che ha id userID */
			default: 
				arrayList_conversationFrames.get(result).setVisible(false); 
				arrayList_conversationFrames.get(result).setLocationRelativeTo(null); 
				break; 
		}
	}
	
	/**
	 * 
	 * @param userID destinatario del messaggio
	 * @param msg da mostrare in chat
	 */
	public static void writeToConversationFrame(int userID, String msg) {
		
		/* Verifico se c'� un ConversationFrame gi� aperto */
		int result = searchConversationWindow(userID);
		
		switch(result) {
			/* non faccio niente */
			case -1: 
				break;
			
			/* rendo NON visibile la finestra di conversazione con l'utente che ha id userID */
			default: 
				arrayList_conversationFrames.get(result).writeChatMsgInConversationWindow(msg);  
				break; 
		}
	}
	
	
	/**
	 * Metodo che aggiorna graficamente nickname, stato ed avatar
	 * in TUTTE le finestre di conversazione.
	 * Utile quando modifico i miei dati personali. 
	 */
	public static void updateMyContactInfos() {
		
		/* controllo che la lista non sia nulla */
		if(arrayList_conversationFrames == null) {
			arrayList_conversationFrames = new ArrayList<Conversation_Frame>(); 
			return; 
		}
		
		/* scorro tutta la lista, ... */
		for(Conversation_Frame frame : arrayList_conversationFrames) {
			/* aggiorno le MIE informazioni all'interno di tutti i frame */
			frame.updateMyContactInfos(); 
		}
		
		System.err.println("updateMyContactInfos()");
		
	}
	
	/**
	 * Metodo che aggiorna graficamente nickname, stato ed avatar di 
	 * TUTTI gli amici con cui ho aperto una finestra di conversazione. 
	 */
	public static void updateAllContactInfos() {
		
		/* controllo che la lista non sia nulla */
		if(arrayList_conversationFrames == null) {
			arrayList_conversationFrames = new ArrayList<Conversation_Frame>(); 
			return; 
		}

		/* per ogni frame all'interno dell'array,... */
		for(Conversation_Frame frame : arrayList_conversationFrames) {
			/* recupero la nuova versione del relativo contatto */
			Contact newContact = ContactListManager.searchContactById(frame.getContact().getID());  
			frame.updateHisContactInfos(newContact); 
		}
		
		System.err.println("updateAllContactInfos()");
	}
	
	/**
	 * Metodo che aggiorna graficamente nickname, stato ed avatar di UN
	 * amico con cui ho aperto finestra di conversazione. 
	 * 
	 * @param contact
	 */
	public static void updateOneContactInfos(Contact contact) {
		Conversation_Frame frame = ConversationWindowsManager.getConversationFrame(contact.getID());
		
		if(frame == null) {
			System.err.println("updateOneContactInfos: frame not found");
			return;
		}
			
		frame.updateHisContactInfos(contact); 
		
		System.err.println("updateOneContactInfos()");
	}
	
	/**
	 * Metodo per chiudere tutte le finestre di conversazione.
	 * Utile in caso di Logout. 
	 */
	public static void closeAllContactFrames() {
		if(arrayList_conversationFrames == null) {
			arrayList_conversationFrames = new ArrayList<Conversation_Frame>(); 
			return; 
		}
		

		/* li chiudo tutti */
		for(Conversation_Frame frame: arrayList_conversationFrames) {
			frame.setVisible(false); 
		}
		
		/* azzero la lista */
		arrayList_conversationFrames = new ArrayList<Conversation_Frame>(); 
		
	}
	
	/**
	 * Questo metodo chiude e rimuove la finestra di conversazione 
	 * relativa ad un 
	 * @param contact Contatto di cui chiudere la finestra di conversazione
	 */
	public static void removeConversationFrame(Contact contact) {
		
		Conversation_Frame frame = getConversationFrame(contact.getID());
		
		if(frame == null) {
			System.out.println("closeConversationFrame: frame relativo al contatto con id " + contact.getID() + " non trovato.");
			return; 
		}
		
		int pos = searchConversationWindow(contact.getID());
		
		frame.dispose(); 
		arrayList_conversationFrames.remove(pos);
		
	}
}
