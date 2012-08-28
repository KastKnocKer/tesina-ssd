package layout;

import javax.swing.JPanel;

/**
 * Classe statica contenente i riferimenti ad alcune delle istanze
 * degli elementi più significativi del layout. 
 * @author Fabio Pierazzi
 */
public class LayoutReferences {

	/* Frame contenente: login, registrazione, lista amici */
	private static HomeFrame homeFrame; 
	/* Pannello contenente CardLayout: usato per switchare fra i pannelli */
	private static JPanel homeFrame_cardPanel; 

	private static FriendsListTable friendsListTable; 
	private static FriendsListTableModel friendsListTableModel; 
	
	private static FriendsListPanel friendsListPanel; 
	
	public static void setHomeFrame(HomeFrame hf) {
		homeFrame = hf; 
		System.out.println("Frame settato");
	}
	
	public static HomeFrame getHomeFrame() {
		System.out.println("Frame recuperato");
		return homeFrame; 
	}

	public static void setHomeFrame_CardPanel(JPanel hcp) {
		homeFrame_cardPanel = hcp;
	}

	public static JPanel getHomeFrame_CardPanel() {
		return homeFrame_cardPanel;
	}

	public static FriendsListTable getFriendsListTable() {
		return friendsListTable;
	}

	public static void setFriendsListTable(FriendsListTable friendsListTable) {
		LayoutReferences.friendsListTable = friendsListTable;
	}

	public static FriendsListTableModel getFriendsListTableModel() {
		return friendsListTableModel;
	}

	public static void setFriendsListTableModel(FriendsListTableModel frameListTableModel) {
		LayoutReferences.friendsListTableModel = frameListTableModel;
	}

	public static FriendsListPanel getFriendsListPanel() {
		return friendsListPanel;
	}

	public static void setFriendsListPanel(FriendsListPanel friendsListPanel) {
		LayoutReferences.friendsListPanel = friendsListPanel;
	}



}
