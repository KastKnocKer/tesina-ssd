package layout.conversationframe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import chat.Contact;
import chat.Message;
import chat.Status;
import client.ClientEngine;

/**
 * Classe che rappresenta il frame della finestra di
 * conversazione. In questa prima versione, per semplicità, 
 * ogni finestra sarà relativa ad una diversa chat. 
 * 
 * @author Fabio Pierazzi
 *
 */
public class Conversation_Frame extends JFrame {
	
	/* Salvo il riferimento all'istanza della classe contact del contatto
	 * con cui sto conversando */
	private Contact contact; 
	
	private JPanel mainPanel; 
	
	private JLabel hisNicknameImageLabel;
	private ImageIcon hisNicknameImage; 
	
	private JLabel myNicknameImageLabel; 
	private ImageIcon myNicknameImage; 
	
	private JTextArea textAreaSendMessage;
	
	private JButton buttonSendMessage; 
	
	private JTextArea textAreaProvaShowMessage; 
	
	private Conversation_TableModel conversation_TableModel; 
	private DefaultTableModel model; 
	private Conversation_Table conversation_Table; 
	
	
	/**
	 * Costruttore con parametri di Conversation_Frame
	 * 
	 * @param userID dell'utente con cui iniziare la conversazione
	 * @param nickname dell'utente con cui iniziare la conversazione
	 * @param email dell'utente con cui iniziare la conversazione
	 */
	public Conversation_Frame(Contact contact) {
		
		/* Modifico impostazioni del frame */
		setSize(400,500); 
		setTitle("Conversazione con " + contact.getNickname() + " ( " + contact.geteMail() + " ) "); 
//		setLocationRelativeTo(null);

		this.setContact(contact); 

		/* *******************************************
		 * Inizio parte grafica 
		 * *******************************************/
		mainPanel = new JPanel(new GridBagLayout()); 
		this.add(mainPanel); 
		
		/* *******************************************
		 * Avatar dell'utente con cui sto parlando 
		 * *******************************************/
		
		/* Nickname */
//		JLabel hisNicknameLabel = new JLabel(nickname);
//		
//		GridBagConstraints constraints = new GridBagConstraints(); 
//		constraints.fill = GridBagConstraints.NONE;
//		constraints.gridx = 0;
//		constraints.gridy = 0; 
//		constraints.gridwidth = 1; 
//		constraints.gridheight = 1; 
//		constraints.insets = new Insets(2, 2, 2, 2);
//		
//		mainPanel.add(hisNicknameLabel, constraints); 
		
		/* Avatar */
		hisNicknameImageLabel = new JLabel(); 
		hisNicknameImageLabel.setBorder( BorderFactory.createTitledBorder(contact.getNickname()) );
		
		// TODO : per semplicità per ora uso lo stesso avatar
		hisNicknameImage = new ImageIcon(Status.getAvatarURL()); 
		hisNicknameImageLabel.setIcon(hisNicknameImage); 
		
		GridBagConstraints constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(2, 2, 2, 2);
		
		mainPanel.add(hisNicknameImageLabel, constraints);
		
		/* *******************************************
		 * Mio Avatar personale 
		 * *******************************************/
		
		/* Nickname */
//		JLabel myNicknameLabel = new JLabel(Status.getNickname());
//		
//		constraints = new GridBagConstraints(); 
//		constraints.fill = GridBagConstraints.NONE;
//		constraints.gridx = 0;
//		constraints.gridy = 3; 
//		constraints.anchor = GridBagConstraints.LAST_LINE_START;
//		constraints.gridwidth = 1; 
//		constraints.gridheight = 1; 
//		constraints.insets = new Insets(2, 2, 2, 2);
//		
//		mainPanel.add(myNicknameLabel, constraints); 
		
		/* Avatar */
		myNicknameImageLabel = new JLabel(); 
		
		myNicknameImageLabel.setBorder( BorderFactory.createTitledBorder(Status.getNickname()) );
		myNicknameImage = new ImageIcon(Status.getAvatarURL()); 
		myNicknameImageLabel.setIcon(myNicknameImage); 
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;
		constraints.gridy = 4; 
		constraints.anchor = GridBagConstraints.LAST_LINE_START;
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(2, 2, 2, 2);
		
		mainPanel.add(myNicknameImageLabel, constraints); 
		
		
		/* *******************************************
		 * TextArea per inviare messaggi 
		 * *******************************************/
		textAreaSendMessage = new JTextArea();
		textAreaSendMessage.setWrapStyleWord(true); 
		textAreaSendMessage.setLineWrap(true); 
		
		JScrollPane scrollPane_textAreaSendMessage = new JScrollPane(textAreaSendMessage); 
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 3; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 2; 
		constraints.weightx = 0.9; 
		constraints.insets = new Insets(2, 2, 2, 2);
		
		mainPanel.add(scrollPane_textAreaSendMessage, constraints); 
		
		textAreaSendMessage.requestFocus(); 
		
		/* Listener textArea */
		textAreaSendMessage.addKeyListener
	      (new KeyAdapter() {
	         public void keyReleased(KeyEvent e) {
	           int key = e.getKeyCode();
	           /* Pressione tasto invio: mando messaggio di chat */
	           if (key == KeyEvent.VK_ENTER) {
//	        	   	Toolkit.getDefaultToolkit().beep();   
	        	   	sendChatMsg(); 
	              }
	           }
	         }
	      );
		
		
		/* *******************************************
		 * Bottone per inviare messaggi 
		 * *******************************************/
		buttonSendMessage = new JButton("Send"); 
		
		buttonSendMessage.setSize(400, 10); 
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 2;
		constraints.gridy = 3; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 2; 
		constraints.weightx = 0.1; 
		constraints.insets = new Insets(2, 2, 2, 2);
		
		mainPanel.add(buttonSendMessage, constraints); 
		
		/* Listener per inviare messaggio */
		buttonSendMessage.addActionListener(new ActionListener() {
	    	   public void actionPerformed(ActionEvent event) {
	    		   sendChatMsg(); 
	    	   }
	       });
		
		/* *******************************************
		 * Tabella contenente l'elenco messaggi scambiati 
		 * *******************************************/
		
		/* Versione TextArea */
		textAreaProvaShowMessage = new JTextArea(); 
		textAreaProvaShowMessage.setEditable(false); 
		textAreaProvaShowMessage.setLineWrap(true); 
		textAreaProvaShowMessage.setWrapStyleWord(true); 
		JScrollPane scrollPane_textAreaProvaShowMessage = new JScrollPane(textAreaProvaShowMessage); 
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1; 
		constraints.weighty = 1; 
		constraints.gridx = 1;
		constraints.gridy = 0; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 3; 
		constraints.insets = new Insets(2, 2, 2, 2);
		
		mainPanel.add(scrollPane_textAreaProvaShowMessage, constraints); 
		
		/* Versione Tabella */
		
//		model = new DefaultTableModel();
//		conversation_Table = new Conversation_Table(model); 
		
//		conversation_TableModel = new Conversation_TableModel(); 
//		
//		model = new DefaultTableModel();
//		
////		model.addColumn("Username");
////		model.addColumn("Message"); 
////		model.addColumn("Time"); 
//		
////		model.insertRow(0, rowData)
//		
//		conversation_Table = new Conversation_Table(conversation_TableModel); 
//		
//		JScrollPane scrollPane = new JScrollPane(conversation_Table);
//		conversation_Table.setFillsViewportHeight(true);
//		
//		constraints = new GridBagConstraints(); 
//		constraints.fill = GridBagConstraints.BOTH;
//		constraints.weightx = 1; 
//		constraints.weighty = 1; 
//		constraints.gridx = 1;
//		constraints.gridy = 0; 
//		constraints.gridwidth = 2; 
//		constraints.gridheight = 3; 
//		constraints.insets = new Insets(2, 2, 2, 2);
//		
//		mainPanel.add(scrollPane, constraints); 
	}
	
	/* Funzione che mostra nella finestra di chat il messaggio contenuto al momento
	 * della pressione all'interno della JTextArea
	 */
	private void sendChatMsg() {
		
		/* Versione TextArea */
		String msg = textAreaSendMessage.getText(); 
		textAreaProvaShowMessage.append(Status.getNickname() + ": " +  msg); 
		textAreaSendMessage.setText("");
		
		ClientEngine.sendMessageToContact(new Message(Status.getUserID(),contact.getID(),msg));
		
		
		/* Versione Tabella */
//		String msg = textAreaSendMessage.getText();
//		
//		conversation_TableModel.setValueAt(msg, conversation_TableModel.getRowCount(), 0); 
//		
//		textAreaSendMessage.setText(""); 
		
	}


	public void writeChatMsg(String msg) {
		textAreaProvaShowMessage.append(contact.getNickname() + ": " +  msg); 
	}

	public Contact getContact() {
		return contact;
	}


	public void setContact(Contact contact) {
		this.contact = contact;
	}
}
