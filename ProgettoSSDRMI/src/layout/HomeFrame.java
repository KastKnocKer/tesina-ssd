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
		
		/* Imposto un riferimento globale statico a questo frame */   
		LayoutReferences.setHomeFrame(this);
		
		setTitle("Always On - RMI Chat");
	       setSize(375,450);
	       setLocationRelativeTo(null);
	       setDefaultCloseOperation(EXIT_ON_CLOSE);
	       
	       
	       /* Mostro i pannelli all'interno di un card layout */
	       JPanel cardsPanel = new JPanel(new CardLayout());
	       
	       LoginPanel lp = new LoginPanel(); 
	       RegistrationPanel rp = new RegistrationPanel(); 
	       
	       
	       LayoutReferences.setHomeFrame_CardPanel(cardsPanel); 

	       cardsPanel.add(lp, "Login"); 
	       cardsPanel.add(rp, "Register");
	       
	       
	       /* Mostro una delle due cards */
	       CardLayout cl = (CardLayout)(cardsPanel.getLayout());
	       cl.show(cardsPanel, "Login");
	       
	       this.add(cardsPanel); 
	       
	       /* Aggiungo la Menu Bar */
	       show_HomeFrame_LoginMenuBar();
	}
	
	/* Aggiunge la menu bar all'Home Frame */
	public void show_HomeFrame_LoginMenuBar() {
		
		setJMenuBar(null);
		
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
	
	/* Aggiunge la menu bar con tutte le opzioni che si hanno
	 * una volta effettuato il login nel sistema */
	public void show_HomeFrame_LoggedMenuBar() {
		
		setJMenuBar(null);
		
		JMenuBar menubar = new JMenuBar();

		/* File */
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

        /* Contacts */ 
        JMenu contacts = new JMenu("Contacts");

        JMenuItem contacts_add = new JMenuItem("Add Contact...", null);
        
        contacts_add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.out.println("Premuto 'Add Contact..'");
            	//System.exit(0);
            }
        });

        contacts.add(contacts_add);
        
        menubar.add(contacts);
        
        setJMenuBar(menubar);
		
	}
}
