package layout.frame;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import layout.LayoutReferences;
import layout.panel.LoginPanel;
import layout.panel.RegistrationPanel;

/**
 * Frame principale dell'applicazione. Usato per contenere il pannello 
 * di Login, quello di Registrazione, e (una volta loggati correttamente nel sistema)
 * la FriendsList, cioé l'elenco dei contatti.
 *  
 * @author Fabio Pierazzi
 */
public class HomeFrame extends JFrame {
	
	/* Costruttore */
	public HomeFrame() {
		
			/* Imposto un riferimento globale statico a questo frame */   
			LayoutReferences.setHomeFrame(this);
			
			/* Imposto alcune proprietà del frame */
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
	       
	       /* *******************************************
	        * ActionListener: componentResized
	        *********************************************/
	       this.addComponentListener(new ComponentAdapter() {
	    	      @Override
	    	      public void componentResized(ComponentEvent e) {
	    	    	  /* Evento on-resize */
	    	      }
	       }); 

	}
	
	/** 
	 * Aggiunge all'HomeFrame la menu bar
	 * 
	 * @author Fabio Pierazzi
	 */
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
	
	/** Aggiunge all'HomeFrame la menu bar con tutte le opzioni che si hanno
	 * una volta effettuato il login nel sistema 
	 * 
	 * @author Fabio Pierazzi 
	 */
	public void show_HomeFrame_LoggedMenuBar() {
		
		setJMenuBar(null);
		
		JMenuBar menubar = new JMenuBar();

		/* File TopMenu */
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        /* LogOut */
        JMenuItem eMenuItem_logout = new JMenuItem("Logout");
        
        eMenuItem_logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	// TODO
            	System.out.println("Premuto bottone Logout...");
            }
        });
        
        file.add(eMenuItem_logout); 
        
        /* Exit */
        JMenuItem eMenuItem = new JMenuItem("Exit", null);
        eMenuItem.setMnemonic(KeyEvent.VK_C);
        // eMenuItem.setToolTipText("Exit application");
         
        
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
                AddContact_Frame frame_addContact = new AddContact_Frame(); 
                frame_addContact.setVisible(true); 
            }
        });

        contacts.add(contacts_add);
        
        menubar.add(contacts);
        
        setJMenuBar(menubar);
		
	}
}
