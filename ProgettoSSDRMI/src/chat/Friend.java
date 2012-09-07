package chat;

/**
 * Classe di interfaccia che rappresenta i dati di un singolo 
 * amico presente nella lista amici. 
 * A differenza della classe Contact, questa classe viene usata 
 * per contenere i dati di contatto utili a mostrare le informazioni
 * dei propri contatti all'interno dell'interfaccia grafica (più precisamente,
 * all'interno della FriendsListTable). 
 * 
 * @author Fabio Pierazzi
 */
public class Friend {
	/** Nickname dell'utente */
	private String nickname; 
	/** Status: occupato, libero, ... */
	private ChatStatusList status;
	/** Identificativo univoco dell'utente */
	private int userId; 
	/** Indirizzo email dell'utente */
	private String email; 
	/** URL dell'avatar dell'utente */
	private String avatarURL; 
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public ChatStatusList getStatus() {
		return status;
	}
	public void setStatus(ChatStatusList status) {
		this.status = status;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAvatarURL() {
		return avatarURL;
	}
	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	} 

}
