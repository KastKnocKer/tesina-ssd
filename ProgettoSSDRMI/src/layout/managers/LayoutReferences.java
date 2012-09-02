package layout.managers;

import javax.swing.JPanel;

import layout.friendslist.FriendsList_Panel;
import layout.friendslist.FriendsList_Table;
import layout.friendslist.FriendsList_TableModel;
import layout.homeframe.Home_Frame;

/**
 * Classe statica contenente i riferimenti ad alcune delle istanze
 * degli elementi più significativi del layout. 
 * @author Fabio Pierazzi
 */
public class LayoutReferences {

	/* Frame contenente: login, registrazione, lista amici */
	private static Home_Frame homeFrame; 
	/* Pannello contenente CardLayout: usato per switchare fra i pannelli */
	private static JPanel homeFrame_cardPanel; 

	private static FriendsList_Table friendsListTable; 
	private static FriendsList_TableModel friendsListTableModel; 
	
	private static FriendsList_Panel friendsListPanel; 
	
	public static void setHomeFrame(Home_Frame hf) {
		homeFrame = hf; 
		System.out.println("Frame settato");
	}
	
	public static Home_Frame getHomeFrame() {
		System.out.println("Frame recuperato");
		return homeFrame; 
	}

	public static void setHomeFrame_CardPanel(JPanel hcp) {
		homeFrame_cardPanel = hcp;
	}

	public static JPanel getHomeFrame_CardPanel() {
		return homeFrame_cardPanel;
	}

	public static FriendsList_Table getFriendsListTable() {
		return friendsListTable;
	}

	public static void setFriendsListTable(FriendsList_Table friendsListTable) {
		LayoutReferences.friendsListTable = friendsListTable;
	}

	public static FriendsList_TableModel getFriendsListTableModel() {
		return friendsListTableModel;
	}

	public static void setFriendsListTableModel(FriendsList_TableModel frameListTableModel) {
		LayoutReferences.friendsListTableModel = frameListTableModel;
	}

	public static FriendsList_Panel getFriendsListPanel() {
		return friendsListPanel;
	}

	public static void setFriendsListPanel(FriendsList_Panel friendsListPanel) {
		LayoutReferences.friendsListPanel = friendsListPanel;
	}



}
