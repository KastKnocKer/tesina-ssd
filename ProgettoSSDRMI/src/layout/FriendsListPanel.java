package layout;

import javax.swing.JPanel;

public class FriendsListPanel extends JPanel {
	public FriendsListPanel() {
		/* Cambio la menu bar */
		
		// LayoutReferences.getHomeFrame().show_HomeFrame_LoggedMenuBar(); 
		HomeFrame hf = LayoutReferences.getHomeFrame();
		hf.show_HomeFrame_LoggedMenuBar(); 
	}
	
	/* Metodo che crea la Menu Bar della finestra con la lista amici */
	private void friendsListMenuBarCreator() {
		
//		 	
//
//	        JMenu file = new JMenu("FileNew");
//	        file.setMnemonic(KeyEvent.VK_F);
//
//	        JMenuItem eMenuItem = new JMenuItem("ExitNew", null);
//	        eMenuItem.setMnemonic(KeyEvent.VK_C);
//	        
//	        eMenuItem.addActionListener(new ActionListener() {
//	            public void actionPerformed(ActionEvent event) {
//	                System.exit(0);
//	            }
//	        });
//
//	        
//	        file.add(eMenuItem);
//
//	        menubar_alternate.add(file);
//	        
//	        frame.validate();
//
//	         
//	        
//	        frame.setJMenuBar(menubar_alternate);
//	        frame.validate();
		
	}
}

