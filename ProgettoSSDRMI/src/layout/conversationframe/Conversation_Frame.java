package layout.conversationframe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
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

import managers.Status;

import utility.DateUtils;
import chat.Contact;
import chat.Message;
import client.ClientEngine;

/**
 * Classe che rappresenta il frame della finestra di
 * conversazione. In questa prima versione, per semplicit�, 
 * ogni finestra sar� relativa ad una diversa chat. 
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
	private JScrollPane scrollPane_textAreaSendMessage;
	
	
	private JButton buttonSendMessage; 
	
	private JTextArea textAreaProvaShowMessage; 
	private JScrollPane scrollPane_textAreaProvaShowMessage;
	
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
		
		// TODO : per semplicit� per ora uso lo stesso avatar
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
		
		scrollPane_textAreaSendMessage = new JScrollPane(textAreaSendMessage); 
		scrollPane_textAreaSendMessage.setAutoscrolls(true);
		
		
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
	         public void keyTyped(KeyEvent e) {
//	           int key = e.getKeyCode();
	           char key = e.getKeyChar(); 
	           /* Pressione tasto invio: mando messaggio di chat */
	           if (key == KeyEvent.VK_ENTER) {
	        	   
//	        	   	Toolkit.getDefaultToolkit().beep();
	        	   
	        	   /* se c'� meno di un carattere, ritorna */
	        	    if(textAreaSendMessage.getText().length() <= 1) {
	        	    	textAreaSendMessage.setText(""); 
	        	    	return; 
	        	    }
	        	    	
	        	    
	        	    /* Rimuovo il ritorno a capo legato alla pressione del tasto invio */
	        	    String msg = textAreaSendMessage.getText(); 
	        	    textAreaSendMessage.setText(msg.substring(0, msg.length() - 1));
	        	    
	        	    
	        	    
	        	    sendChatMsgToRemoteContact();
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
	    		   sendChatMsgToRemoteContact(); 
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
		scrollPane_textAreaProvaShowMessage = new JScrollPane(textAreaProvaShowMessage);
		
//		scrollPane_textAreaProvaShowMessage
//		scrollPane_textAreaProvaShowMessage.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
//	        public void adjustmentValueChanged(AdjustmentEvent e) {  
//	            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
//	        }
//	    });
		
		
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
//		model = new DefaultTableModel() {
//
//		    @Override
//		    public boolean isCellEditable(int row, int column) {
//		       // all cells false
//		       return false;
//		    }
//		}; 
//		
//		conversation_Table = new Conversation_Table(model); 
//		
////		conversation_TableModel = new Conversation_TableModel(); 
////		
////		model = new DefaultTableModel();
////		
//////		model.addColumn("Username");
//////		model.addColumn("Message"); 
//////		model.addColumn("Time"); 
////		
//////		model.insertRow(0, rowData)
////		
////		conversation_Table = new Conversation_Table(conversation_TableModel); 
////		
//		JScrollPane conversationTable_scrollPane = new JScrollPane(conversation_Table);
//		
//		/* Imposto un listener affinch� la scrollbar sia sempre in basso, man mano che la chat prosegue */
//		conversationTable_scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
//	        public void adjustmentValueChanged(AdjustmentEvent e) {  
//	            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
//	        }
//	    });
//		
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
//		mainPanel.add(conversationTable_scrollPane, constraints); 
	}
	
	/**
	 *  Funzione per inviare il messaggio dall'host locale all'host remoto,
	 *  e mostrarlo all'interno della propria finestra di conversazione.
	 *  @author Fabio Pierazzi
	 */
	private void sendChatMsgToRemoteContact() {
		
		/* Versione TextArea */
		String msg = textAreaSendMessage.getText(); 
		textAreaProvaShowMessage.append("[" + DateUtils.now_time() + "] " + Status.getNickname() + ": " +  msg + "\n"); 
		textAreaSendMessage.setText("");
		
		/* sposto in basso la scrollbar */
		int max_value = scrollPane_textAreaProvaShowMessage.getVerticalScrollBar().getMaximum(); 
		scrollPane_textAreaProvaShowMessage.getVerticalScrollBar().setValue(max_value); 
		
		ClientEngine.sendMessageToContact(new Message(Status.getUserID(),contact.getID(), msg));
		
		/* Versione Tabella */
//		String msg = textAreaSendMessage.getText();
//		
//		String[] text = { 
//				DateUtils.now_time(), 
//				Status.getNickname(), 
//				msg };
//		
//		model.addRow(text); 
//		
//		ClientEngine.sendMessageToContact(new Message((int) Status.getUserID(), (int) contact.getID(), (String) msg)); 
//		
//		textAreaSendMessage.setText(""); 
		
	}


	/** 
	 * Funzione per mostrare un messaggio ricevuto da un determinato contatto
	 * all'interno della fienstra di conversazione. 
	 * 
	 * @param msg , il messaggio da mostrare all'interno della finestra di conversazione
	 */
	public void writeChatMsgInConversationWindow(String msg) {
		/* Versione TextArea */
		
		textAreaProvaShowMessage.append("[" + DateUtils.now_time() + "] " + contact.getNickname() + ": " +  msg + "\n"); 
		
		/* sposto in basso la scrollbar */
		int max_value = scrollPane_textAreaProvaShowMessage.getVerticalScrollBar().getMaximum(); 
		scrollPane_textAreaProvaShowMessage.getVerticalScrollBar().setValue(max_value); 
		
		/* Versione tabella */
//		String[] text = { 
//				DateUtils.now_time(), 
//				contact.getNickname(), 
//				msg };
//		
//		model.addRow(text); 
	}

	public Contact getContact() {
		return contact;
	}


	public void setContact(Contact contact) {
		this.contact = contact;
	}
}
