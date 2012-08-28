package layout;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import chat.Status;

/**
 * Pannello contenente la lista amici. Viene inserito
 * all'interno dell'Home Frame, e mostrato subito dopo il login. 
 * 
 * @author Fabio Pierazzi
 */
public class FriendsListPanel extends JPanel {
	
	private HomeFrame homeFrame; 
	private JPanel mainPanel; 
	private JTable table_friendsList; 
	
	
	/* Costruttore */
	public FriendsListPanel() {
		
		this.setLayout(new BorderLayout());
		mainPanel = new JPanel(new GridBagLayout()); 
		this.add(mainPanel, BorderLayout.CENTER); 
		
		/* Cambio la menu bar del frame */
		homeFrame = LayoutReferences.getHomeFrame(); 
		homeFrame.show_HomeFrame_LoggedMenuBar(); 

		/* Mostro i pezzi del layout */
		showHeaderPanel(); 
		showFriendsListTable();

		/* Mostro il mainPanel */
		mainPanel.setVisible(true); 
	}
	
	/**
	 * Mostra la parte superiore della lista amici:
	 * avatar, nome dell'utente, e messaggio di stato. 
	 * Inserita sfruttando i constraints del GridBagLayout.
	 * 
	 * @author Fabio Pierazzi
	 */
	private void showHeaderPanel() {
//		ImagePanel imagePanel = new ImagePanel("Chrysanthemum.jpg"); 
//		mainPanel.add(imagePanel, BorderLayout.SOUTH);
//		imagePanel.setVisible(true); 
		
		
		/* Avatar */
		// JLabel avatarLabel = new JLabel("Avatar!");
//		ImagePanel imagePanel = new ImagePanel("Chrysanthemum.jpg");
//		
//		GridBagConstraints constraints = new GridBagConstraints(); 
//		constraints.fill = GridBagConstraints.BOTH;
//		constraints.gridx = 0;
//		constraints.gridy = 0; 
//		constraints.gridwidth = 1; 
//		constraints.gridheight = 1; 
//		constraints.insets = new Insets(5, 5, 5, 5);  
//		
//		JPanel tempContainerPanel = new JPanel(new BorderLayout());
//		tempContainerPanel.setSize(150,150); 
//		tempContainerPanel.add(imagePanel, BorderLayout.CENTER);
//		tempContainerPanel.setBorder( BorderFactory.createTitledBorder("Avatar") );
//		mainPanel.add(tempContainerPanel, constraints); 
		
		
		/* Titolo */
		JPanel nicknamePanel = new JPanel(); 
		
		JLabel nicknameLabel = new JLabel("Febio"); 
		nicknameLabel.setFont(new Font("Arial", Font.BOLD, 13));
		JLabel statusLabel = new JLabel("(Occupato)");
		statusLabel.setFont(new Font("Arial", Font.ITALIC, 13));
		
		nicknamePanel.add(nicknameLabel); 
		nicknamePanel.add(statusLabel); 
		
//		JLabel titleLabel = new JLabel("Febio (Occupato)"); 
		
		GridBagConstraints constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 0; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(5, 5, 5, 5);  
		
//		titleLabel.setFont(new Font("Arial", Font.BOLD, 13));
		
		nicknamePanel.setBorder(
				new CompoundBorder(
						BorderFactory.createTitledBorder("Nickname (stato)"),
						new EmptyBorder(5,5,5,5)
			       )
				);
				
		mainPanel.add(nicknamePanel, constraints); 
	}
	
	/**
	 * Mostra la FriendsListTable all'interno del 
	 * FriendsListPanel. La inserisce sfruttando i 
	 * constraints del GridBagLayout.
	 * 
	 * @author Fabio Pierazzi
	 */
	private void showFriendsListTable() {
		
		FriendsListTableModel fl_tableModel = new FriendsListTableModel();
		fl_tableModel.setFriendsList(Status.getFriendsList()); 
		FriendsListTable fl_table = new FriendsListTable(fl_tableModel); 
		fl_table.setFriendsList(Status.getFriendsList()); 
		
		fl_table.updateTable();
		fl_table.setVisible(true); 
		
		/* Aggiungo la tabella al pannello */ 
		JScrollPane scrollPane = new JScrollPane(fl_table);
		fl_table.setFillsViewportHeight(true);
		
		/* Inserisco la tabella all'interno del GridBagLayout */
		GridBagConstraints constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 1; 
		constraints.gridwidth = 3; 
		constraints.gridheight = 3; 
		constraints.weightx = 1;
		constraints.weighty = 1; 
		constraints.insets = new Insets(5, 5, 5, 5);  
		
		JPanel nestedPanel = new JPanel(new BorderLayout()); 
		nestedPanel.setBorder( BorderFactory.createTitledBorder("Elenco Amici") );
		nestedPanel.add(scrollPane, BorderLayout.CENTER); 
		mainPanel.add(nestedPanel, constraints);
		
	}
}

