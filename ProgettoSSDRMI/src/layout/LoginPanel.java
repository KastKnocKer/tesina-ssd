package layout;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.ClientEngine;

public class LoginPanel extends JPanel {

	private JPanel mainPanel;
	private JPanel centerPanel_flowLayout;
	private JPanel centerPanel_boxLayout;
	private JPanel subPanel_01; 
	private JPanel subPanel_02; 
	private JPanel subPanel_03; 
	
	private JLabel label_loginPanel;
	private JTextField textField_username; 
	private JPasswordField textField_password;
	
	private JButton loginButton; 
	private JButton quitButton; 
	private JButton registerButton;
	
	public LoginPanel() {
		
		  /* Imposto alcune proprietà del frame principale */
//		   setTitle("Always On - RMI Chat");
//	       setSize(280,250);
//	       setLocationRelativeTo(null);
//	       setDefaultCloseOperation(EXIT_ON_CLOSE);
	       
	       /* Border Layout Panel */
	       mainPanel = new JPanel(new BorderLayout());
	       
	       this.add(mainPanel); 

	       /* Relative Layout Panel */
	       centerPanel_flowLayout = new JPanel(); 
	       centerPanel_flowLayout.setLayout(new FlowLayout(FlowLayout.CENTER) );
	       
	       /* Box Layout Panel */
	       centerPanel_boxLayout = new JPanel();
	       centerPanel_boxLayout.setLayout(new BoxLayout(centerPanel_boxLayout, BoxLayout.Y_AXIS));

	       
	       /* Aggiungo centerPanel a mainPanel */
//	       getContentPane().add(mainPanel);
	       this.add(mainPanel);
	       mainPanel.setBackground(Color.gray);
	       centerPanel_boxLayout.setAlignmentY(50);
	       
	       centerPanel_flowLayout.add(centerPanel_boxLayout);
	       mainPanel.add(centerPanel_flowLayout, BorderLayout.CENTER);

	       /* Label LoginPanel */
	       subPanel_01 = new JPanel(new FlowLayout(FlowLayout.CENTER) ); 
	       centerPanel_boxLayout.add(subPanel_01); 
	       label_loginPanel = new JLabel("Login Screen"); 
	       subPanel_01.add(label_loginPanel, BorderLayout.CENTER);
	       
	       // label_loginPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
	       
	       /* Username & Password TextFields */
	       textField_username = new JTextField("email@yourdomain.com");
	       textField_password = new JPasswordField("password"); 

	       centerPanel_boxLayout.add(textField_username);
	       centerPanel_boxLayout.add(textField_password);
	       
	       /* Aggiungo Login & Exit Button */
	       subPanel_02 = new JPanel(new FlowLayout(FlowLayout.CENTER) ); 
	       centerPanel_boxLayout.add(subPanel_02);
	       
	       loginButton = new JButton("Login");
	       loginButton.addActionListener(new ActionListener() {
	    	   public void actionPerformed(ActionEvent arg0) {
	    		   //ClientEngine.Login(textField_username.getText(), new String(textField_password.getPassword()));
	    		   System.out.println(textField_username.getText()+" "+new String(textField_password.getPassword()));
	    	   }
	       });
	       
	       
	       quitButton = new JButton("Exit");
	       
	       quitButton.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent event) {
	               System.exit(0);
	          }
	       });
	       
	       subPanel_02.add(loginButton);
	       subPanel_02.add(quitButton); 

	       /* Aggiungo Registration Button */
	       subPanel_03 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	       centerPanel_boxLayout.add(subPanel_03); 
	       
	       registerButton = new JButton("Register");
	       
	       // Mostro il Registration Frame
	       registerButton.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent event) {
	        	   System.out.println("Premuto Register Button");
	        	   
	        	   /* Recupero la sorgente dell'evento */
	        	   JButton button = (JButton) event.getSource();
	        	   RegistrationPanel rp = new RegistrationPanel(); 
	        	   
	        	   
	        	   /* risalendo la gerarchia di pannelli, arrivo fino al cardsPanel */
	        	   JPanel cardsPanel = (JPanel) button.getParent().getParent().getParent().getParent().getParent().getParent(); 

	        	   ((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Register"); 
	        	   
	          }
	       });
	       
	       subPanel_03.add(registerButton); 


	}
	

}

