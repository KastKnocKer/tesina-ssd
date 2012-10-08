package layout.conversationframe;

import java.awt.BorderLayout;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import managers.Status;
import utility.DateUtils;
import chat.ChatStatusList;
import chat.Contact;
import chat.Message;
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
	
	private JPanel hisAvatarPanel;
	private JLabel hisNicknameImageLabel;
	private ImageIcon hisNicknameImage; 
	private JLabel hisStatusLabel; 
	
	private JPanel myAvatarPanel; 
	private JLabel myNicknameImageLabel; 
	private ImageIcon myNicknameImage; 
	private JLabel myStatusLabel; 
	
	
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
		setTitle("Conversazione con " + contact.getNickname() + " [" + contact.getStatus() + "] ( " + contact.getEmail() + " ) "); 
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
		
		/* Avatar, Nickname, Status */
		hisAvatarPanel = new JPanel(new BorderLayout()); 
		
		/* Se non è disponibile un avatar, uso quello di default */
		if(contact.getAvatarURL().equals("") || contact.getAvatarURL() == null) {
			System.err.println("Avatar not found. Using default avatar.");
			contact.setAvatarURL(Status.getDefaultAvatarURL());
		}
		
		hisNicknameImage = new ImageIcon(contact.getAvatarURL()); 
		hisNicknameImageLabel = new JLabel(); 
		hisNicknameImageLabel.setIcon(hisNicknameImage); 
		hisNicknameImageLabel.setBorder( BorderFactory.createTitledBorder(contact.getNickname()) );
		
		hisStatusLabel = new JLabel(contact.getStatus().toString(), JLabel.CENTER);
		
		hisStatusLabel.setBorder( BorderFactory.createTitledBorder("Status") );
		
		hisAvatarPanel.add(hisNicknameImageLabel, BorderLayout.NORTH); 
		hisAvatarPanel.add(hisStatusLabel, BorderLayout.SOUTH); 

		hisAvatarPanel.setVisible(true); 
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(2, 2, 2, 2);
		
		mainPanel.add(hisAvatarPanel, constraints);
		
//		/* Vecchia versione Avatar & Co */
//		hisNicknameImage = new ImageIcon(Status.getAvatarURL()); 
//		hisNicknameImageLabel = new JLabel("Prova bla bla", hisNicknameImage, JLabel.CENTER);
//		hisNicknameImageLabel.setBorder( BorderFactory.createTitledBorder(contact.getNickname() + " [" + contact.getStatus() + "]") );
//	
////		hisNicknameImageLabel.setIcon(hisNicknameImage); 
//		
//		GridBagConstraints constraints = new GridBagConstraints(); 
//		constraints.fill = GridBagConstraints.NONE;
//		constraints.gridx = 0;
//		constraints.gridy = 1; 
//		constraints.gridwidth = 1; 
//		constraints.gridheight = 1; 
//		constraints.insets = new Insets(2, 2, 2, 2);
//		
//		mainPanel.add(hisNicknameImageLabel, constraints);

		
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
		
		myAvatarPanel = new JPanel(new BorderLayout()); 
		
		/* Se non è disponibile un avatar, uso quello di default */
		if(Status.getAvatarURL().equals("") || Status.getAvatarURL() == null) {
			System.err.println("Avatar not found. Using default avatar.");
			contact.setAvatarURL(Status.getDefaultAvatarURL());
		}
		
		myNicknameImage = new ImageIcon(Status.getAvatarURL()); 
		myNicknameImageLabel = new JLabel(); 
		myNicknameImageLabel.setIcon(myNicknameImage); 
		myNicknameImageLabel.setBorder( BorderFactory.createTitledBorder(Status.getNickname()) );
		
		myStatusLabel = new JLabel(Status.getStato().toString(), JLabel.CENTER);
		
		myStatusLabel.setBorder( BorderFactory.createTitledBorder("Status") );
		
		myAvatarPanel.add(myNicknameImageLabel, BorderLayout.NORTH); 
		myAvatarPanel.add(myStatusLabel, BorderLayout.SOUTH); 

		myAvatarPanel.setVisible(true); 
		
		/* Fine pezzo copiato */
		
//		myNicknameImageLabel.setBorder( BorderFactory.createTitledBorder(Status.getNickname()) );
//		myNicknameImage = new ImageIcon(Status.getAvatarURL()); 
//		myNicknameImageLabel.setIcon(myNicknameImage); 
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;
		constraints.gridy = 4; 
		constraints.anchor = GridBagConstraints.LAST_LINE_START;
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(2, 2, 2, 2);
		
		mainPanel.add(myAvatarPanel, constraints); 
		
		
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
	        	   
	        	   /* se c'è meno di un carattere, ritorna */
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
//		/* Imposto un listener affinché la scrollbar sia sempre in basso, man mano che la chat prosegue */
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
		//Se il messaggio è nullo aborto
		if(msg == null || msg.length() == 0) return;
		
		textAreaProvaShowMessage.append("[" + DateUtils.now_time() + "] " + Status.getNickname() + ": " +  msg + "\n"); 
		textAreaSendMessage.setText("");
		
		/* sposto in basso la scrollbar */
		int max_value = scrollPane_textAreaProvaShowMessage.getVerticalScrollBar().getMaximum(); 
		scrollPane_textAreaProvaShowMessage.getVerticalScrollBar().setValue(max_value); 
		
		ClientEngine.sendMessageToContact(new Message(Status.getUserID(),contact.getID(), msg));
		
		/* Se il contatto a cui sto inviando il messaggio è offline, 
		 * mostro una finestra di dialogo per avvisare che i messaggi
		 * verranno recapitati quando tornerà disponibile. */
		if(contact.getStatus() == ChatStatusList.OFFLINE) {
			
			textAreaProvaShowMessage.append("\n*** ATTENZIONE *** Il contatto " + contact.getNickname() + " è offline. " +
					"I messaggi verrano recapitati non appena tornerà online. \n\n"); 
			
			/* sposto in basso la scrollbar */
			max_value = scrollPane_textAreaProvaShowMessage.getVerticalScrollBar().getMaximum(); 
			scrollPane_textAreaProvaShowMessage.getVerticalScrollBar().setValue(max_value); 
			
//			JOptionPane.showMessageDialog(
//					null, 
//					"Attenzione!\n\n" +
//					"Il contatto: \n" +
//					"" + contact.getEmail() + "\n" +
//							"al momento è offline. \n\n" +
//							"I messaggi gli verranno recapitati\n" +
//							"non appena tornerà online.", 
//					"Conversazione con " + contact.getNickname(),
//					JOptionPane.INFORMATION_MESSAGE);
		}
		
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
	 * all'interno della finestra di conversazione. 
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
	
	/**
	 * Metodo per aggiornare (graficamente) lo stato 
	 * del contatto con cui si sta parlando (status, nickname,...)
	 * nell'ambito della finestra di conversazione
	 * 
	 * @param contact: nuova istanza della classe Contact 
	 * contenente le informazioni sul contatto con cui si sta
	 * conversando.
	 */
	public void updateHisContactInfos(Contact contact) {

		/* imposto il nuovo contatto */
		this.setContact(contact); 
		
		/* Aggiorno il titolo della finestra di conversazione */
		this.setTitle("Conversazione con " + contact.getNickname() + " [" + contact.getStatus() + "] ( " + contact.getEmail() + " ) "); 
		
		/* Aggiorno status, nickname, ed avatar del contatto con cui sto parlando */
		
		/* Se non è disponibile un avatar, uso quello di default */
		if(contact.getAvatarURL().equals("") || contact.getAvatarURL() == null) {
			contact.setAvatarURL(Status.getDefaultAvatarURL());
		}
		
		hisNicknameImage = new ImageIcon(contact.getAvatarURL()); 
		hisNicknameImageLabel.setIcon(hisNicknameImage); 
		
		hisNicknameImageLabel.setBorder( BorderFactory.createTitledBorder(contact.getNickname()) );
		
		hisStatusLabel.setText(contact.getStatus().toString());
			
	}
	
	
	/**
	 * Metodo per aggiornare graficamente le MIE informazioni
	 * nella finestra di contatto (status, nickname, avatar).
	 * Questo aggiornamento avviene quando modifico i miei dati personali. 
	 */
	public void updateMyContactInfos() {
	
		/* Se non è disponibile un avatar, uso quello di default */
		if(Status.getAvatarURL().equals("") || Status.getAvatarURL() == null) {
			System.err.println("Avatar not found. Using default avatar.");
			contact.setAvatarURL(Status.getDefaultAvatarURL());
		}
		
		myNicknameImage = new ImageIcon(Status.getAvatarURL()); 
		myNicknameImageLabel.setIcon(myNicknameImage); 
		myNicknameImageLabel.setBorder( BorderFactory.createTitledBorder(Status.getNickname()) );
		
		myStatusLabel.setText(Status.getStato().toString());
		
	}
}
