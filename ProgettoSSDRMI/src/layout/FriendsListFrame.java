package layout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class FriendsListFrame extends JFrame {
	public FriendsListFrame() {
		friendsListMenuBarCreator(this); 
	}
	
	/* Metodo che crea la Menu Bar della finestra con la lista amici */
	private void friendsListMenuBarCreator(JFrame frame) {
		
		 	JMenuBar menubar = new JMenuBar();

	        JMenu file = new JMenu("File");
	        file.setMnemonic(KeyEvent.VK_F);

	        JMenuItem eMenuItem = new JMenuItem("Exit", null);
	        eMenuItem.setMnemonic(KeyEvent.VK_C);
	        eMenuItem.setToolTipText("Exit application");
	        
	        eMenuItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	                System.exit(0);
	            }

	        });

	        file.add(eMenuItem);

	        menubar.add(file);

	        frame.setJMenuBar(menubar);
		
	}
}

