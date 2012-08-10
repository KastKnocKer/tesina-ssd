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



}
