package layout;

/**
 * Classe statica contenente i riferimenti ad alcune delle istanze
 * degli elementi più significativi del layout. 
 * @author Fabio Pierazzi
 */
public class LayoutReferences {

	private static HomeFrame homeFrame; 
	private static FriendsListFrame friendsListFrame; 
	
	public static void setHomeFrame(HomeFrame hf) {
		homeFrame = hf; 
	}
	
	public static HomeFrame getHomeFrame() {
		return homeFrame; 
	}

	public static FriendsListFrame getFriendsListFrame() {
		return friendsListFrame; 
	}
	
	public static void setFriendsListFrame(FriendsListFrame flf) {
		friendsListFrame = flf; 
	}
}
