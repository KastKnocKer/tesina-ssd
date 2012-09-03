package layout.managers;

import java.util.ArrayList;

import layout.conversationframe.Conversation_Frame;
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
		
		/* Se questa è la prima finestra di conversazione che viene aperta, 
		 * creo una nuova lista e restituisco il valore -1 per comunicare che
		 * la finestra di conversazione che si cercava non è presente */
		if(arrayList_conversationFrames == null) {
			arrayList_conversationFrames = new ArrayList<Conversation_Frame>();
			return -1; 
		}
		
		/* Scorro l'elenco di conversation frame per controllare se ce n'è 
		 * già uno aperto relativo all'userID passato come parametro in ingresso */
		
		int pos = 0; 
		
		for(Conversation_Frame conversationFrame : arrayList_conversationFrames) {
			
			int temp_userID = conversationFrame.getContact().getID(); 
			System.out.println("Temp: " + temp_userID + "; userID: " + userID);
			if( temp_userID == userID)
				return pos; 
						
			pos++; 
		}
		
		/* Se arrivo in fondo, significa che non ho trovato
		 * quello che stavo cercando */
		return -1; 
	}
	
	/**
	 * Apro la finestra di conversazione con un certo utente. 
	 * Se la finestra non è ancora stata aperta, la inizializzo; 
	 * altrimenti, la rendo nuovamente visibile. 
	 * 
	 * @param contact, istanza della classe Contact corrispondente al contatto invocato. 
	 */
	public static void showConversationFrame(Contact contact) {
		
//		int userID = contact.getID(); 
//		String nickname = contact.getNickname(); 
//		String email = contact.geteMail(); 
		
		/* Verifico se c'è un ConversationFrame già aperto */
		int result = searchConversationWindow(contact.getID());
		
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
				System.out.println("Conversazione con " + contact.getNickname() + " ( " + contact.geteMail() + " ) era già stata aperta");
//				arrayList_conversationFrames.get(result).setLocationRelativeTo(null); 
				arrayList_conversationFrames.get(result).setVisible(true); 
				break; 
		}
		
	}
	
	/**
	 * Nasconde la finestra di conversazione con un certo utente.
	 * @param userID dell'utente (su database) di cui si vuole cercare la finestra di conversazione
	 */
	public static void hideConversationFrame(int userID) {
		
		/* Verifico se c'è un ConversationFrame già aperto */
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
}
