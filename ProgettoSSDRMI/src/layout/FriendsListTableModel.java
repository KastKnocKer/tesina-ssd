package layout;

import javax.swing.table.AbstractTableModel;

import chat.Friend;
import chat.FriendsList;
import chat.Status;

/**
 * Modello che definisce i dati da mostrare nella FriendsListTable
 * @author Fabio Pierazzi
 */
public class FriendsListTableModel extends AbstractTableModel {

	/** Uso una variabile privata per gestire manualmente
	 * l'aggiornamento grafico della FriendsListTable */
	private FriendsList friendsList; 
	private Object obj; 
	
	/**
	 *  Costruttore
	 */
	public FriendsListTableModel() {
		super();
		LayoutReferences.setFriendsListTableModel(this); 
		// this.setFriendsList(Status.getFriendsList());
	}
	
	/**
	 * Restituisce il numero di colonne
	 * @return numero di colonne
	 */
	public int getColumnCount() {
		return 3;
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
		
		/* Recupero un amico in base alla posizione */
		obj = friendsList.getFriendByPosition(row); 
		
		switch(col){
			case 0: return row+1;
			case 1: return ((Friend) obj).getNickname(); 
			case 2: return ((Friend) obj).getStatus();
			default: return "***";
		}
	}
	
	
	/**
	 * Dato l'indice di colonna ritorna il nome corrispondente
	 * @return nome da mostrare per la colonna "col"
	 */
	public String getColumnName(int col){
		switch(col){
			case 0: return "N."; 
			case 1: return "Nickname"; 
			case 2: return "Status";
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
