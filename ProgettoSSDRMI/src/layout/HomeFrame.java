package layout;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class HomeFrame extends JFrame {
	
	public HomeFrame() {
		
		   setTitle("Always On - RMI Chat");
	       setSize(375,450);
	       setLocationRelativeTo(null);
	       setDefaultCloseOperation(EXIT_ON_CLOSE);
	       
	       
	       /* Mostro i pannelli all'interno di un card layout */
	       JPanel cardsPanel = new JPanel(new CardLayout());
	       
	       LoginPanel lp = new LoginPanel(); 
	       RegistrationPanel rp = new RegistrationPanel(); 
	       FriendsListPanel flp = new FriendsListPanel(); 
	       
	       LayoutReferences.setHomeFrame_CardPanel(cardsPanel); 

	       cardsPanel.add(lp, "Login"); 
	       cardsPanel.add(rp, "Register");
	       cardsPanel.add(flp, "FriendsList");
	       
	       /* Mostro una delle due cards */
	       CardLayout cl = (CardLayout)(cardsPanel.getLayout());
	       cl.show(cardsPanel, "Login");
	       
	       this.add(cardsPanel); 
	       
	       /* Aggiungo la Menu Bar */
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

	        setJMenuBar(menubar);
	}
}
