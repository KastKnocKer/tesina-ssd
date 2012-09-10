package layout.friendslist;

import javax.swing.table.AbstractTableModel;

import layout.managers.LayoutReferences;
import managers.Status;

import chat.ChatStatusList;
import chat.Friend;
import chat.FriendsList;

/**
 * Modello che definisce i dati da mostrare nella FriendsListTable
 * @author Fabio Pierazzi
 */
public class FriendsList_TableModel extends AbstractTableModel {

	/** Uso una variabile privata per gestire manualmente
	 * l'aggiornamento grafico della FriendsListTable */
	private FriendsList friendsList; 
	private Object obj; 
	
	/**
	 *  Costruttore
	 */
	public FriendsList_TableModel() {
		super();
		LayoutReferences.setFriendsListTableModel(this); 
	}
	
	/**
	 * Restituisce il numero di colonne
	 * @return numero di colonne
	 */
	public int getColumnCount() {
		return 4;
	}

	/**
	 * Restituisce il numero di righe
	 * @return numero di righe
	 */
	public int getRowCount() {
		return this.friendsList.getLength();
	}

	/**
	 * Restituisce l'oggetto da visualizzare nella
	 * posizione "row, col"
	 * @return oggetto da mostrare nella posizione "row, col"
	 */
	public Object getValueAt(int row, int col) {
		
//		System.err.println("getValueAt(row " + row + ", col " + col + ")");
		
		/* Recupero un amico in base alla posizione */
		obj = friendsList.getFriendByPosition(row); 
		
		int id = ((Friend) obj).getUserId(); 
		String nickname =  ((Friend) obj).getNickname(); 
		String status =  ((Friend) obj).getStatus().toString(); 
		String email = ((Friend) obj).getEmail(); 
		
//		System.err.println("value: id [ " + id + " ]; nickname [ " + nickname + " ]; status [ " + status + " ]; email [ " + email + "]");
		
		if(id < 0 || nickname == null || status == null || email == null)
			return "***"; 
		
		if(obj == null) 
			return "***"; 
		
		switch(col){
			case 0: return id; //  return row+1;
			case 1: return nickname; 
			case 2: return status;
			case 3: return email; 
			default: return "***";
		}
	}
	
	
	/**
	 * Dato l'indice di colonna ritorna il nome corrispondente
	 * @return nome da mostrare per la colonna "col"
	 */
	public String getColumnName(int col){
		switch(col){
			case 0: return "ID"; 
			case 1: return "Nickname"; 
			case 2: return "Status";
			case 3: return "Email";
			default: return "***";
		}
	}
	
	/**
	 * Indica se la cella nella posizione
	 *  passata come parametro è editabile
	 */
	public boolean isCellEditable(int row,int col){
		return false;
	}

	/**
	 * Setta a questo modello la friends list
	 * @param FriendsList
	 */
	public void setFriendsList(FriendsList fl){
		this.friendsList = fl; 
	}

	
}
