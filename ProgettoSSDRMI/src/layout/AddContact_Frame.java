package layout;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import RMIMessages.RMISIPBasicResponseMessage;

import client.ClientEngine;

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
			setSize(450,250);
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
			
			addFriendButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	            	// TODO INSERIRE RICHIESTA DI AGGIUNTA CONTATTO
	                System.out.println("Premuto 'Aggiungi (Amico "+textField_email.getText()+")'");
	                RMISIPBasicResponseMessage resp = ClientEngine.RequestFriendship(textField_email.getText());
	                if(resp.isSUCCESS()){
	                	JOptionPane.showMessageDialog(null, resp.getMESSAGE(), "Aggiungi contatto", JOptionPane.INFORMATION_MESSAGE);
	                }else{
	                	JOptionPane.showMessageDialog(null, resp.getMESSAGE(), "Aggiungi contatto", JOptionPane.ERROR_MESSAGE);
	                }
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
	                System.out.println("Premuto 'Quit'"); 
	            }
	        });
			
			addContact_Panel.add(quitButton, constraints);
			
			//        
//		pane.setLayout(new GridBagLayout());
//		GridBagConstraints c = new GridBagConstraints();
//		if (shouldFill) {
//		//natural height, maximum width
//		c.fill = GridBagConstraints.HORIZONTAL;
//		}
//	
//		button = new JButton("Button 1");
//		if (shouldWeightX) {
//		c.weightx = 0.5;
//		}
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.gridx = 0;
//		c.gridy = 0;
//		pane.add(button, c);
//	
//		button = new JButton("Button 2");
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.weightx = 0.5;
//		c.gridx = 1;
//		c.gridy = 0;
//		pane.add(button, c);
//	
//		button = new JButton("Button 3");
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.weightx = 0.5;
//		c.gridx = 2;
//		c.gridy = 0;
//		pane.add(button, c);
//	
//		button = new JButton("Long-Named Button 4");
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.ipady = 40;      //make this component tall
//		c.weightx = 0.0;
//		c.gridwidth = 3;
//		c.gridx = 0;
//		c.gridy = 1;
//		pane.add(button, c);
//	
//		button = new JButton("5");
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.ipady = 0;       //reset to default
//		c.weighty = 1.0;   //request any extra vertical space
//		c.anchor = GridBagConstraints.PAGE_END; //bottom of space
//		c.insets = new Insets(10,0,0,0);  //top padding
//		c.gridx = 1;       //aligned with button 2
//		c.gridwidth = 2;   //2 columns wide
//		c.gridy = 2;       //third row
//		pane.add(button, c);
        
	}
	
}
