package layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {

	public LoginFrame() {
		
		  /* Imposto alcune proprietà del frame principale */
	       setTitle("Always On - RMI Chat");
	       setSize(280,250);
	       setLocationRelativeTo(null);
	       setDefaultCloseOperation(EXIT_ON_CLOSE);
	       
	       /* Border Layout Panel */
	       JPanel mainPanel = new JPanel(new BorderLayout());

	       /* Relative Layout Panel */
	       JPanel centerPanel_flowLayout = new JPanel(); 
	       centerPanel_flowLayout.setLayout(new FlowLayout(FlowLayout.CENTER) );
	       
	       /* Box Layout Panel */
	       JPanel centerPanel_boxLayout = new JPanel();
	       centerPanel_boxLayout.setLayout(new BoxLayout(centerPanel_boxLayout, BoxLayout.Y_AXIS));

	       
	       /* Aggiungo centerPanel a mainPanel */
	       getContentPane().add(mainPanel);
	       mainPanel.setBackground(Color.gray);
	       centerPanel_boxLayout.setAlignmentY(50);
	       
	       centerPanel_flowLayout.add(centerPanel_boxLayout);
	       mainPanel.add(centerPanel_flowLayout, BorderLayout.CENTER);

	       
	       /* Label LoginPanel */
	       JPanel subPanel_01 = new JPanel(new FlowLayout(FlowLayout.CENTER) ); 
	       centerPanel_boxLayout.add(subPanel_01); 
	       JLabel label_loginPanel = new JLabel("Login Screen"); 
	       subPanel_01.add(label_loginPanel, BorderLayout.CENTER);
	       
	       // label_loginPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
	       
	       /* Username & Password TextFields */
	       
	       JTextField textField_username = new JTextField("username");
	       JTextField textField_password = new JTextField("password"); 
	       
	       centerPanel_boxLayout.add(textField_username);
	       centerPanel_boxLayout.add(textField_password);
	       
	       /* Aggiungo Quit Button a Center Panel */
	       JPanel subPanel_02 = new JPanel(new FlowLayout(FlowLayout.CENTER) ); 
	       centerPanel_boxLayout.add(subPanel_02);
	       
	       JButton loginButton = new JButton("Login"); 
	       JButton quitButton = new JButton("Exit");
	       
	      
	       
	       quitButton.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent event) {
	               System.exit(0);
	          }
	       });
	       
	       subPanel_02.add(loginButton);
	       subPanel_02.add(quitButton); 

	     
	       
	       
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


//
//private JPanel p = new JPanel();
//private JLabel img = new JLabel();
//private JLabel img2 = new JLabel();
//private JLabel login = new JLabel();
//private Label remember = new Label("Password dimenticata?");
//private JLabel label = new JLabel("Accedi");
//private JLabel label2 = new JLabel("Accedi con il tuo ID.");
//private TextField email = new TextField("Esempio555@hotmail.com",15);
//private TextField pass = new TextField("Immetti la tua password",15);
//private JCheckBox check = new JCheckBox("Mem. profilo utente");
//private JCheckBox check2 = new JCheckBox("Memorizza la password");
//private JButton accedi = new JButton("Accedi");


//p.setLayout(new GridLayout(13,0));
//img.setIcon(new ImageIcon("img/cornice.png"));
//img2.setIcon(new ImageIcon("img/example.png"));
//p.add(img);
//p.add(img2);
//label.setForeground(Color.blue);
//p.add(label);
//p.add(label2);
//
//p.add(email);
//email.setForeground(Color.LIGHT_GRAY);
//p.add(pass);
//pass.setForeground(Color.LIGHT_GRAY);
//p.add(check);
//p.add(check2);
//p.add(remember);
//p.add(accedi);
//p.add(login);

/*
img.setBounds(100,0,141,141);
img2.setBounds(103,8,125,125);
label.setBounds(150,130,80,30);
label2.setBounds(80,150,200,30);
email.setBounds(60,180,220,25);
pass.setBounds(60,210,220,25);
check.setBounds(60,240,220,25);
check.setFocusPainted(false); 
check2.setBounds(60,260,220,25);
check2.setFocusPainted(false); 
remember.setBounds(60,290,180,25);
accedi.setBounds(140,330,80,25);
login.setBounds(100,390,120,25);
*/

//this.getContentPane().add(p,BorderLayout.CENTER);
//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
