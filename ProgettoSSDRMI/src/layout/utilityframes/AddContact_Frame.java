package layout.utilityframes;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import managers.FriendshipManager;
import managers.Status;

import RMIMessages.RMISIPBasicResponseMessage;
import chat.Contact;

/**
 * Frame contenente il pannello per l'aggiunta di nuovi 
 * contatti alla lista amici. 
 * 
 * @author Fabio Pierazzi
 */
public class AddContact_Frame extends JFrame {

	private JPanel addContact_Panel; 
	
	private JLabel addContactLabel;
	private JLabel addContactLabelDescription; 
	
	private JLabel textfield_label; 
	private JTextField textField_email; 
	
	private JButton addFriendButton; 
	private JButton quitButton; 
	
	/* Costruttore */
	public AddContact_Frame() {
			
			/* Imposto alcune proprietà del frame */
			setTitle("Always On - RMI Chat - Aggiungi Contatto");
			setSize(380,200);
			setLocationRelativeTo(null);
			// setDefaultCloseOperation(EXIT_ON_CLOSE);
	       
			/* Creo un pannello, e lo inserisco nella parte nord del frame */
			this.setLayout(new BorderLayout()); 
			addContact_Panel = new JPanel();
			this.add(addContact_Panel, BorderLayout.NORTH); 
			
			/* Imposto come Layout di Pannello il GridBagLayout */
			addContact_Panel.setLayout(new GridBagLayout()); 
        
			/* Label Titolo */
			addContactLabel = new JLabel("Aggiungi Contatto"); 
			addContactLabel.setFont(new Font("Arial Black", Font.BOLD, 15));
			
			GridBagConstraints constraints = new GridBagConstraints(); 
			constraints.fill = GridBagConstraints.NONE;
			constraints.gridx = 0;
			constraints.gridy = 0; 
			constraints.gridwidth = 3; 
			constraints.gridheight = 1; 
			constraints.ipadx = 10; 
			constraints.ipady = 10; 

			addContact_Panel.add(addContactLabel, constraints); 

			
			
			/* Label Descrizione di cosa fare */
			addContactLabelDescription = new JLabel("Indicare l'indirizzo email " +
					"dell'amico da aggiungere."); 
			
			constraints = new GridBagConstraints(); 
			constraints.fill = GridBagConstraints.NONE;
			constraints.gridx = 0;
			constraints.gridy = 1; 
			constraints.gridwidth = 3; 
			constraints.gridheight = 1; 
			constraints.insets = new Insets(10, 10, 10, 10);  
			
			addContact_Panel.add(addContactLabelDescription, constraints); 
			
			
			/* Label della textfield per la ricerca amico */
			textfield_label = new JLabel("Email: "); 
			
			constraints = new GridBagConstraints(); 
			constraints.fill = GridBagConstraints.NONE;
			constraints.gridx = 0;
			constraints.gridy = 2; 
			constraints.gridwidth = 1; 
			constraints.gridheight = 1; 
			constraints.insets = new Insets(5, 5, 5, 5);  
			
			addContact_Panel.add(textfield_label, constraints); 
			
			/* Textfield per inserire la mail dell'amico da cercare */
			textField_email = new JTextField(); 
			
			constraints = new GridBagConstraints(); 
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridx = 1;
			constraints.gridy = 2; 
			constraints.gridwidth = 2; 
			constraints.gridheight = 1; 
			constraints.insets = new Insets(5, 5, 5, 5);  
			
			/* Listener per l'aggiunta del contatto alla pressione del tasto INVIO */
			textField_email.addKeyListener
		      (new KeyAdapter() {
		         public void keyTyped(KeyEvent e) {
		           char key = e.getKeyChar(); 
		           if (key == KeyEvent.VK_ENTER) {
		        	   		addContact(); 
		              }
		           }
		         }
		      );
			
			addContact_Panel.add(textField_email, constraints);
			
			
			/* Bottone per aggiungere amico */
			addFriendButton = new JButton("Aggiungi"); 
			
			constraints = new GridBagConstraints(); 
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridx = 1;
			constraints.gridy = 3; 
			constraints.gridwidth = 2; 
			constraints.gridheight = 1; 
			constraints.insets = new Insets(2, 5, 2, 5); 
			
			/* Versione Kast: senza conferma amicizia */
//			addFriendButton.addActionListener(new ActionListener() {
//	            public void actionPerformed(ActionEvent event) {
////	                System.out.println("Premuto 'Aggiungi (Amico "+textField_email.getText()+")'");
//	                RMISIPBasicResponseMessage resp = ClientEngine.RequestFriendship(textField_email.getText());
//	                if(resp.isSUCCESS()){
//	                	JOptionPane.showMessageDialog(null, resp.getMESSAGE(), "Aggiungi contatto", JOptionPane.INFORMATION_MESSAGE);
//	                	/* aggiorno l'aspetto grafico della tabella */
//	                	LayoutReferences.getFriendsListTable().updateTable();
//	                }else{
//	                	JOptionPane.showMessageDialog(null, resp.getMESSAGE(), "Aggiungi contatto", JOptionPane.ERROR_MESSAGE);
//	                }
//	                
//	                /* Chiudo il frame */
//	                dispose(); 
//	            }
//	        });
			
			
			addFriendButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	            	addContact();
	            }
			});
			
			addContact_Panel.add(addFriendButton, constraints);
			
			
			/* Bottone per chiudere la finestra */
			quitButton = new JButton("Esci"); 
			
			constraints = new GridBagConstraints(); 
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridx = 1;
			constraints.gridy = 4; 
			constraints.gridwidth = 2; 
			constraints.gridheight = 1; 
			constraints.insets = new Insets(0, 5, 2, 5); 
			
			quitButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	            	dispose(); 
	            }
	        });
			
			addContact_Panel.add(quitButton, constraints);
			
	}
	
	/**
	 * Metodo privato che invoca le funzioni 
	 * necessarie a tentare l'aggiunta del contatto.
	 * 
	 * @author Fabio Pierazzi
	 */
	private void addContact() {
	
		if(textField_email.getText().equals(""))
			JOptionPane.showMessageDialog(null, "E' necessario specificare una mail nel campo opportuno", "Aggiungi contatto", JOptionPane.INFORMATION_MESSAGE);
		
		/* Invio richiesta di amicizia */
		RMISIPBasicResponseMessage answer = FriendshipManager.sendFriendshipRequestToContact(textField_email.getText()); 
    	
		if(answer.isSUCCESS() == true) {
			JOptionPane.showMessageDialog(null, answer.getMESSAGE(), "Aggiungi contatto", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, answer.getMESSAGE(), "Aggiungi contatto", JOptionPane.WARNING_MESSAGE);
		}
		
		// TODO: spostare il codice dove deve stare
		Contact myContact = Status.getMyInfoIntoContact(); 
		
		if(answer.isSUCCESS())
			FriendshipManager.showFriendshipRequestFrom(myContact); 
		
		
        /* Chiudo il frame */
//        dispose(); 
	}
}
