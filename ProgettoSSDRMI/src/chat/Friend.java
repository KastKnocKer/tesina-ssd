package chat;

/**
 * Classe di interfaccia che rappresenta i dati di un singolo 
 * amico presente nella lista amici. 
 * A differenza della classe Contact, questa classe viene usata 
 * per contenere i dati di contatto utili a mostrare le informazioni
 * dei propri contatti all'interno dell'interfaccia grafica (pi� precisamente,
 * all'interno della FriendsListTable). 
 * 
 * @author Fabio Pierazzi
 */
public class Friend {
	/** Nickname dell'utente */
	private String nickname; 
	/** Status: occupato, libero, ... */
	private StatusList status;
	/** Identificativo univoco dell'utente */
	private int userId; 
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public StatusList getStatus() {
		return status;
	}
	public void setStatus(StatusList status) {
		this.status = status;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	} 

}
