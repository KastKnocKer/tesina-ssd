package layout;

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

import chat.Status;
import chat.StatusList; 

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
	
	private JButton addFriendButton; 
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
		
		if(Status.getStato() == StatusList.ONLINE) {
			System.out.println("ONLINE");
			combobox_newStato.setSelectedIndex(0);
		} else if(Status.getStato() == StatusList.BUSY) {
			System.out.println("BUSY");
			combobox_newStato.setSelectedIndex(1);
		} else if(Status.getStato() == StatusList.AWAY) {
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
		
		
		
		
		
		
		
		/* Bottone per aggiungere amico */
		addFriendButton = new JButton("Applica Modifiche"); 
		
		constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 5; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(10, 5, 2, 5); 
		
		addFriendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	// TODO ? 
            	Status.setNickname(textfield_newNickname.getText()); 
            	
            	switch(combobox_newStato.getSelectedIndex()) {
            		case 0: Status.setStato(StatusList.ONLINE);
            				System.out.println("ComboBox: Selezionato online");
            				break;
            		case 1: Status.setStato(StatusList.BUSY); 
            				System.out.println("ComboBox: Selezionato busy");
            				break; 
            		case 2: Status.setStato(StatusList.AWAY); 
            				System.out.println("ComboBox: Selezionato away");
            				break; 
            		default: 
            				System.out.println("Errore: nessuno stato selezionato."); 
            				break; 
            	}
            	
            	Status.setAvatarURL(textfield_newAvatar.getText()); 
            	
            	LayoutReferences.getFriendsListPanel().refreshPanel();
            	
            	dispose(); 
            }
        });
		
		changeNickname_Panel.add(addFriendButton, constraints);
		
		
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
