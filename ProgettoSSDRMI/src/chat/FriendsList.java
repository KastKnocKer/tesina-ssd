package chat;

import java.util.ArrayList;

/**
 * Classe che rappresenta una lista di oggetti Friend. Sfruttata
 * in FriendsListTable per mostrare l'elenco di amici. 
 * 
 * @author Fabio Pierazzi
 */
public class FriendsList {
	private ArrayList<Friend> friendsArrayList;
	
	/** Costruttore */
	public FriendsList() {
		/** Imposto il riferimento globale statico 
		 * a quest'oggetto FriendsList */
		Status.setFriendsList(this); 
	}

	public ArrayList<Friend> getFriendsArrayList() {
		return friendsArrayList;
	}

	public void setFriendsArrayList(ArrayList<Friend> friendsArrayList) {
		this.friendsArrayList = friendsArrayList;
	}
	
	/** 
	 * Aggiungo un amico alla mia lista
	 * @param friend
	 */
	public void addFriend(Friend friend) {
		this.friendsArrayList.add(friend);
	}
	
	/**
	 * Rimuovo un amico dalla mia lista
	 * @param userId dell'amico da eliminare
	 */
	public void removeFriend (int userId) {
		// TODO 
		System.out.println("FriendsList.removeFriend // TODO");
	}
	
	public void removeFriendByPosition(int position) {
		this.friendsArrayList.remove(position); 
	}
	
	/**
	 * Prendo un amico dalla lista, sulla base della sua posizione
	 * @param position: posizione dell'amico nella lista
	 */
	public Friend getFriendByPosition(int position) {
		return this.friendsArrayList.get(position); 
	}
	
	/**
	 * Prendo un amico dalla lista sulla base del suo userId
	 * @param userId
	 */
	public void getFriendByUserId(int userId) {
		// TODO 
		System.out.println("FriendsList.getFriendByUserId // TODO");
	}
	
	public int getLength() {
		return friendsArrayList.size(); 
	}
}
