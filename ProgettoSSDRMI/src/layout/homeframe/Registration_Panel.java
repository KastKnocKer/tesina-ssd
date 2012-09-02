package layout.homeframe;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.ClientEngine;

public class Registration_Panel extends JPanel {
	
	public Registration_Panel() {
		   
		   

		   
		   JPanel boxPanel = new JPanel();
		   boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
		   
		   JPanel p = new JPanel();
		   
		   JLabel label_firstName = new JLabel("Nome:");
		   JLabel label_lastName = new JLabel("Cognome:");
		   JLabel label_email = new JLabel("Email:"); 
		   JLabel label_nickname= new JLabel("Nickname:");
		   JLabel label_password= new JLabel("Password:");
		   JLabel label_passwordConfirm= new JLabel("Conferma Password:");
		   
		   final JTextField textField_firstName = new JTextField(20);
		   final JTextField textField_lastName = new JTextField(20);
		   final JTextField textField_email = new JTextField(20);
		   final JTextField textField_nickname = new JTextField(20);
		   final JTextField textField_password = new JTextField(20);
		   final JTextField textField_passwordConfirm = new JTextField(20);
		   
		   p.setLayout(new GridLayout(0,2));
		   
		  
		   
		   /* Aggiungo campi del form */
		   p.add(label_firstName);
		   p.add(textField_firstName);
		   p.add(label_lastName);
		   p.add(textField_lastName);
		   p.add(label_email);
		   p.add(textField_email);
		   p.add(label_nickname);
		   p.add(textField_nickname); 
		   p.add(label_password); 
		   p.add(textField_password); 
		   p.add(label_passwordConfirm);
		   p.add(textField_passwordConfirm);
		   
		   
		   /* Aggiungo bottoni per submittare e tornare indietro */
		   JButton buttonSubmit = new JButton("Submit");
		   buttonSubmit.addActionListener(new ActionListener() {
			   
			   
			   
			   public void actionPerformed(ActionEvent e) {
				   if(  (textField_firstName == null ||	textField_firstName.getText().length()==0) 	||
						(textField_lastName == null || 	textField_lastName.getText().length()==0) 	||
						(textField_email == null || 	textField_email.getText().length()==0) 		||
						(textField_nickname == null || 	textField_nickname.getText().length()==0) 	||
						(textField_password == null || 	textField_password.getText().length()==0) ){
					   JOptionPane.showMessageDialog(null, "Ci sono campi mancanti!", "Registrazione", JOptionPane.WARNING_MESSAGE);
					   return;
				   }
				   
				   if(!textField_password.getText().equals(textField_passwordConfirm.getText())){
					   JOptionPane.showMessageDialog(null, "Le password inserite non coincidono.", "Registrazione", JOptionPane.WARNING_MESSAGE);
					   return;
				   }
				   
				   if( ClientEngine.RegisterNewAccount(textField_firstName.getText(), textField_lastName.getText(), textField_email.getText(), textField_nickname.getText(), textField_password.getText())){
					   JOptionPane.showMessageDialog(null, "Account inserito con successo.", "Registrazione", JOptionPane.WARNING_MESSAGE);
					   
					 //TODO per fabio :) INSERISCI IL RITORNO AL PANNELLO DI LOGIN
				   }else{
					   JOptionPane.showMessageDialog(null, "Account non inserito.", "Registrazione", JOptionPane.WARNING_MESSAGE);
				   }
			   }
		   });
		   
		   p.add(new JLabel());
		   p.add(new JLabel());
		   p.add(new JLabel());
		   p.add(buttonSubmit);
		   
		   JButton buttonGoBack = new JButton("Go Back to Login"); 
		   
		   /* Action Listener per riaprire la schermata di Login */
		   buttonGoBack.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent event) {
	        	   System.out.println("Premuto GoBackToLogin Button");
	        	   
	        	   /* Recupero la sorgente dell'evento */
	        	   JButton button = (JButton) event.getSource();
	        	   Login_Panel lp = new Login_Panel(); 

	        	   /* risalendo la gerarchia di pannelli, arrivo fino al cardsPanel */
	        	   JPanel cardsPanel = (JPanel) button.getParent().getParent().getParent().getParent(); 

	        	   ((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Login"); 
	        	   
	          }
	       });

		   
		   
		   p.add(new JLabel());
		   p.add(buttonGoBack);
		   
		   /* Aggiungo label di titolo */
		   
		   JPanel subPanel_01 = new JPanel(new FlowLayout(FlowLayout.CENTER) ); 
		   JLabel lbl = new JLabel("Registrazione");
		   lbl.setFont(new Font("Arial Black", Font.BOLD, 15));

		   subPanel_01.add(lbl);
		   boxPanel.add(subPanel_01);
		   
		   /* */
		   boxPanel.add(p);
		   this.add(boxPanel); 
		   
		   this.setSize(350,200);
		   this.setVisible(true);
	}
}
