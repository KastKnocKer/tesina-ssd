package layout.utilityframes;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layout.managers.LayoutReferences;
import managers.FileContactsManager;
import managers.Status;
import chat.ChatStatusList;
import chat.Contact;
import client.ClientEngine;
import client.thread.ClientThread;

import layout.managers.*; 

/**
 * Frame usato dall'utene per cambiare i propri nickname
 * e status (Occupato, Online,...). 
 * Questa finestra viene mostrata cliccando sul nicknamePanel 
 * all'interno del FriendsListPanel. 
 * 
 * @author Fabio Pierazzi
 */
public class ChangeNickname_Frame extends JFrame {
	
	private JPanel changeNickname_Panel; 
	
	private JLabel changeNickname_titleLabel;
	private JLabel changeNickname_titleDescription; 
	
	private JLabel label_newNickname; 
	private JTextField textfield_newNickname; 
	
	private JLabel label_newStato; 
	private JComboBox combobox_newStato; 
	
	private JLabel label_newAvatar; 
	private JTextField textfield_newAvatar; 
	
	private JButton updateInfosButton; 
	private JButton quitButton; 
	
	
	private JButton button_applyChanges; 
	
//	private JComboBox<E>; 
	
	/* costruttore */
	public ChangeNickname_Frame() {
		
		/* Imposto alcune proprietà del frame */
		setTitle("Always On - RMI Chat - Change Personal Infos");
		setSize(380,300);
		setLocationRelativeTo(null);
		// setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		/* Creo un pannello, e lo inserisco nella parte nord del frame */
		this.setLayout(new BorderLayout()); 
		changeNickname_Panel = new JPanel();
		this.add(changeNickname_Panel, BorderLayout.NORTH); 
		
		/* Imposto come Layout di Pannello il GridBagLayout */
		changeNickname_Panel.setLayout(new GridBagLayout()); 
    
		/* Label Titolo */
		changeNickname_titleLabel = new JLabel("Change Personal Infos"); 
		changeNickname_titleLabel.setFont(new Font("Arial Black", Font.BOLD, 15));
		
		GridBagConstraints constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;
		constraints.gridy = 0; 
		constraints.gridwidth = 3; 
		constraints.gridheight = 1; 
		constraints.ipadx = 10; 
		constraints.ipady = 10; 

		changeNickname_Panel.add(changeNickname_titleLabel, constraints); 

		
		/* Label Descrizione di cosa fare */
		changeNickname_titleDescription = new JLabel("Di seguito è possibile modificare le proprie impostazioni personali."); 
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;
		constraints.gridy = 1; 
		constraints.gridwidth = 3; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(10, 10, 10, 10);  
		
		changeNickname_Panel.add(changeNickname_titleDescription, constraints); 
		
		
		/* Label della textfield per la ricerca amico */
		label_newNickname = new JLabel("Nickname: "); 
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(5, 5, 5, 5);  
		
		changeNickname_Panel.add(label_newNickname, constraints); 
		
		/* Textfield per inserire la mail dell'amico da cercare */
		textfield_newNickname = new JTextField(); 
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 2; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(5, 5, 5, 5);  
		
		textfield_newNickname.setText(Status.getNickname());
		changeNickname_Panel.add(textfield_newNickname, constraints);
		
		
		
		
		/* Label della combobox per cambiare il proprio stato */
		label_newStato = new JLabel("Status: "); 
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;
		constraints.gridy = 3; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(5, 5, 5, 5);  
		
		changeNickname_Panel.add(label_newStato, constraints); 
		
		/* ComboBox per cambiare il proprio stato */
		String[] statusStrings = { "Online", "Occupato", "Non al computer"};

		combobox_newStato = new JComboBox<String>(statusStrings);
		
		if(Status.getStato() == ChatStatusList.ONLINE) {
			System.out.println("ONLINE");
			combobox_newStato.setSelectedIndex(0);
		} else if(Status.getStato() == ChatStatusList.BUSY) {
			System.out.println("BUSY");
			combobox_newStato.setSelectedIndex(1);
		} else if(Status.getStato() == ChatStatusList.AWAY) {
			System.out.println("AWAY");
			combobox_newStato.setSelectedIndex(2);
		} else {
			System.out.println("NO STATUS");
			combobox_newStato.setSelectedIndex(0);
		}
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 3; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(5, 5, 5, 5); 
		
		changeNickname_Panel.add(combobox_newStato, constraints); 
		
		
		
		
		
		
		/* Label del textfield per aggiornare l'url dell'avatar */
		label_newAvatar = new JLabel("Avatar URL: "); 
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;
		constraints.gridy = 4; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(5, 5, 5, 5);  
		
		changeNickname_Panel.add(label_newAvatar, constraints);
		
		
		/* TextField per specificare l'URL del nuovo avatar */
		textfield_newAvatar = new JTextField(); 
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 4; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(5, 5, 5, 5);
		
		textfield_newAvatar.setText(Status.getAvatarURL());
		changeNickname_Panel.add(textfield_newAvatar, constraints);
		
		
		/* Bottone per confermare le modifiche */
		updateInfosButton = new JButton("Applica Modifiche"); 
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 5; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(10, 5, 2, 5); 
		
		updateInfosButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

            	Status.setNickname(textfield_newNickname.getText()); 
            	
            	
            	switch(combobox_newStato.getSelectedIndex()) {
            		case 0: Status.setStato(ChatStatusList.ONLINE);
            				System.out.println("ComboBox: Selezionato stato online");
            				break;
            		case 1: Status.setStato(ChatStatusList.BUSY); 
            				System.out.println("ComboBox: Selezionato stato busy");
            				break; 
            		case 2: Status.setStato(ChatStatusList.AWAY); 
            				System.out.println("ComboBox: Selezionato stato away");
            				break; 
            		default: 
            				System.err.println("Errore: nessuno stato selezionato."); 
            				break; 
            	}

            	Status.setAvatarURL(textfield_newAvatar.getText()); 
            	
            	//Modifica dei parametri
            	Contact myContact = new Contact();
            	myContact.setID(Status.getUserID());
            	myContact.setNickname(Status.getNickname());
            	myContact.setAvatarURL(Status.getAvatarURL());
            	myContact.setStatus(Status.getStato());
            	ClientEngine.ModifyMyInfos(myContact);
            	FileContactsManager.writeContactsXML();
            	ClientThread.setModifiedInfos(true);
            	LayoutReferences.getFriendsListPanel().refreshPanel();
            	
            	/* Invoco un metodo che aggiorna graficamente il mio stato
            	 * all'interno delle finestre di conversazione */
            	ConversationWindowsManager.updateMyContactInfos(); 
            	
            	/* Chiudo la finestra */
            	dispose(); 
            }
        });
		
		changeNickname_Panel.add(updateInfosButton, constraints);
		
		
		/* Bottone per chiudere la finestra */
		quitButton = new JButton("Esci"); 
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 6; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(5, 5, 5, 5); 
		
		quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	dispose(); 
            }
        });
		
		changeNickname_Panel.add(quitButton, constraints);
		
	}
}
