package layout.homeframe;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.ClientEngine;

import RMI.ClientInterface;

import chat.Contact;
import managers.Status;

import layout.managers.ConversationWindowsManager;
import layout.managers.LayoutReferences;
import layout.utilityframes.*;

/**
 * Frame principale dell'applicazione. Usato per contenere il pannello 
 * di Login, quello di Registrazione, e (una volta loggati correttamente nel sistema)
 * la FriendsList, cioé l'elenco dei contatti.
 *  
 * @author Fabio Pierazzi
 */
public class Home_Frame extends JFrame {
	
	/* Costruttore */
	public Home_Frame() {
		
			/* Imposto un riferimento globale statico a questo frame */   
			LayoutReferences.setHomeFrame(this);
			
			/* Imposto alcune proprietà del frame */
			setTitle("Always On - RMI Chat");
	       setSize(375,450);
	       setLocationRelativeTo(null);
	       setDefaultCloseOperation(EXIT_ON_CLOSE);
	       
	       
	       /* Mostro i pannelli all'interno di un card layout */
	       JPanel cardsPanel = new JPanel(new CardLayout());
	       
	       Login_Panel lp = new Login_Panel(); 
	       Registration_Panel rp = new Registration_Panel(); 
	       
	       
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
            	System.out.println("Premuto bottone Logout...");
            	
            	/* Eseguo controllo con bottone */
            	
            	int result = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler effettuare Logout?", 
        						"Logout",
                        JOptionPane.YES_NO_OPTION);
        		
        		if(result == JOptionPane.YES_OPTION) {
        			System.err.println("Richiesta di Logout");
        			
        			//TODO: chiedi a kast
                	boolean logout_result = ClientEngine.Logout();
                	if(logout_result == true) {
                		/* chiudo tutte le finestre di conversazione */
                		ConversationWindowsManager.closeAllContactFrames(); 
                		/* rimostro la schermata di login */
     	        	   JPanel cardsPanel = LayoutReferences.getHomeFrame_CardPanel(); 
     	        	   ((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Login"); 
     	        	   /* informo l'utente che la cosa è avvenuta con successo */
     	        	   JOptionPane.showMessageDialog(null, "Logout avvenuto con successo!", "Logout", JOptionPane.INFORMATION_MESSAGE);
     	        	   Status.unbindClient();
                	}
        		}
        		else if(result == JOptionPane.NO_OPTION)  {
        			System.err.println("Ripensamento sul logout");
        		}
            		
            }
        });
        
        file.add(eMenuItem_logout); 
        
        file.addSeparator();
        
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
		
        addTestMenu(menubar);
	}

	private void addTestMenu(JMenuBar menubar){
		JMenu debug = new JMenu("[Test\\Debug]");
		JMenuItem eMenuItem_whoisp2p = new JMenuItem("whois P2P");
		eMenuItem_whoisp2p.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("TestWHOIS biofrost88@gmail.com");
				ClientEngine.whois("biofrost88@gmail.com");
			}
		});
		debug.add(eMenuItem_whoisp2p);
		
		JMenuItem eMenuItem_unbindsip = new JMenuItem("Unbind SIP");
		eMenuItem_unbindsip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Status.unbindSIP();
			}
		});
		debug.add(eMenuItem_unbindsip);
		
		JMenuItem eMenuItem_bindsip = new JMenuItem("Bind SIP");
		eMenuItem_bindsip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Status.bindSIP();
			}
		});
		debug.add(eMenuItem_unbindsip);
		
		JMenuItem eMenuItem_loadContactFromSIP = new JMenuItem("Load contact from SIP");
		eMenuItem_loadContactFromSIP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ClientEngine.LoadContactsFromSIP();
			}
		});
		debug.add(eMenuItem_loadContactFromSIP);
		
		menubar.add(debug);
	}
}
