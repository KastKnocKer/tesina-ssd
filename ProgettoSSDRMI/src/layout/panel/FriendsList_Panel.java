package layout.panel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import layout.LayoutReferences;
import layout.frame.ChangeNickname_Frame;
import layout.frame.Home_Frame;
import layout.table.FriendsList_Table;
import layout.table.FriendsList_TableModel;

import chat.Status;
import chat.StatusList; 

/**
 * Pannello contenente la lista amici. Viene inserito
 * all'interno dell'Home Frame, e mostrato subito dopo il login. 
 * 
 * @author Fabio Pierazzi
 */
public class FriendsList_Panel extends JPanel {
	
	private Home_Frame homeFrame; 
	private JPanel mainPanel; 
	private JTable table_friendsList; 
	
	
	/* stato personale utente, mostrato nella parte alta del pannello */
	private JLabel nicknameLabel;
	private JLabel statusLabel; 
	private JLabel picture; 
	
	/* Costruttore */
	public FriendsList_Panel() {
		
		/* Imposto riferimento globale a questo pannello */
		LayoutReferences.setFriendsListPanel(this); 
		

		/* Modifico caratteristiche frame */
		LayoutReferences.getHomeFrame().setSize(300,550);
	
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
		
		/********************************************************
		 * AVATAR
		 ********************************************************/
		if(Status.getAvatarURL().equals("")) {
			System.out.println("No avatar found. Setting Default Avatar.");
			Status.setAvatarURL("images/avatars/tn_tate.jpg");
		}
		
		ImageIcon icon = new ImageIcon(Status.getAvatarURL());
		picture = new JLabel(); 
        picture.setIcon(icon);
        picture.setSize(150,150); 
        
//		ImagePanel imagePanel = new ImagePanel("Chrysanthemum.jpg"); 
//		mainPanel.add(imagePanel, BorderLayout.SOUTH);
//		imagePanel.setVisible(true); 
		
//		/* Avatar */
//		// JLabel avatarLabel = new JLabel("Avatar!");
//		ImagePanel imagePanel = new ImagePanel("Chrysanthemum.jpg");
//		
//		GridBagConstraints contraintsPicture = new GridBagConstraints(); 
//		contraintsPicture.fill = GridBagConstraints.NONE;
//		contraintsPicture.gridx = 0;
//		contraintsPicture.gridy = 0; 
//		contraintsPicture.gridwidth = 1; 
//		contraintsPicture.gridheight = 1; 
//		contraintsPicture.insets = new Insets(5, 5, 5, 5);  
		
//		
		JPanel tempContainerPanel = new JPanel(new BorderLayout());
//		imagePanel.setSize(150,150); 
		tempContainerPanel.setSize(150,150); 
		tempContainerPanel.add(picture, BorderLayout.CENTER);
		tempContainerPanel.setBorder( BorderFactory.createTitledBorder("Avatar") );
//		mainPanel.add(tempContainerPanel, contraintsPicture); 
		
		
		/********************************************************
		 * Nickname And Status
		 ********************************************************/

		/* Header del Pannello */
		JPanel nicknamePanel = new JPanel(new GridBagLayout()); 
		
		nicknameLabel = new JLabel(Status.getNickname()); 
		nicknameLabel.setFont(new Font("Arial", Font.BOLD, 13));
		
		statusLabel = new JLabel(); 
		
		if(Status.getStato() == StatusList.ONLINE) {
			statusLabel.setText("(Online)");
			System.out.println("ONLINE");
		} else if(Status.getStato() == StatusList.BUSY) {
			statusLabel.setText("(Occupato)");
			System.out.println("BUSY");
		} else if(Status.getStato() == StatusList.AWAY) {
			statusLabel.setText("(Non al computer)");
			System.out.println("AWAY");
		} else {
			System.out.println("NO STATUS");
			statusLabel.setText("(Online)");
		}
		
		statusLabel.setFont(new Font("Arial", Font.ITALIC, 13));

		GridBagConstraints nicknamePanelInsideconstraints = new GridBagConstraints(); 
		nicknamePanelInsideconstraints.fill = GridBagConstraints.NONE;
		nicknamePanelInsideconstraints.gridx = 0;
		nicknamePanelInsideconstraints.gridy = 1; 
		nicknamePanelInsideconstraints.gridwidth = 1; 
		nicknamePanelInsideconstraints.gridheight = 1; 
		nicknamePanelInsideconstraints.anchor = GridBagConstraints.CENTER;
		nicknamePanelInsideconstraints.insets = new Insets(2, 2, 2, 2);
		
		nicknamePanel.add(nicknameLabel, nicknamePanelInsideconstraints); 
		
		nicknamePanelInsideconstraints.gridx = 1; 
		
		nicknamePanel.add(statusLabel, nicknamePanelInsideconstraints); 
		
		nicknamePanelInsideconstraints.gridy = 0;
		nicknamePanelInsideconstraints.gridx = 0; 
		nicknamePanelInsideconstraints.gridwidth = 2;
		
		nicknamePanel.add(tempContainerPanel, nicknamePanelInsideconstraints);
		
		
		/* Inserisco il pannello con GridBagLayout */
		GridBagConstraints constraints = new GridBagConstraints(); 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 1;
		constraints.gridy = 0; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(5, 5, 5, 5);  
		
		nicknamePanel.setBorder(
				new CompoundBorder(
						BorderFactory.createTitledBorder("Nickname (stato)"),
						new EmptyBorder(5,5,5,5)
			       )
				);
		
		mainPanel.add(nicknamePanel, constraints); 
		
		/****************************************************
		 *  Listener per la pressione del mouse sul pannello con il nickname, 
		 * 	usato per aggiornare i propri nickname e status. 
		 * **************************************************/
		nicknamePanel.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
					System.out.println("Premuto il pannello");
					ChangeNickname_Frame cnf = new ChangeNickname_Frame(); 
					cnf.setVisible(true); 
				}
	
				@Override
				public void mouseEntered(MouseEvent arg0) {}
	
				@Override
				public void mouseExited(MouseEvent arg0) {}
	
				@Override
				public void mousePressed(MouseEvent arg0) {}
	
				@Override
				public void mouseReleased(MouseEvent arg0) {}
			
		});
		
	}
	
	/**
	 * Mostra la FriendsListTable all'interno del 
	 * FriendsListPanel. La inserisce sfruttando i 
	 * constraints del GridBagLayout.
	 * 
	 * @author Fabio Pierazzi
	 */
	private void showFriendsListTable() {
		
		FriendsList_TableModel fl_tableModel = new FriendsList_TableModel();
		fl_tableModel.setFriendsList(Status.getFriendsList()); 
		FriendsList_Table fl_table = new FriendsList_Table(fl_tableModel); 
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
	
	/**
	 * Metodo che aggiorna graficamente l'aspetto grafico del pannello.
	 * In particolare, aggiorna nickname, avatar e stato dell'utente. 
	 * Aggiorna anche la tabella con la lista amici. 
	 */
	public void refreshPanel() {
		
		/* Aggiorno nickname */
		nicknameLabel.setText(Status.getNickname()); 
		
		/* Aggiorno stato */
		if(Status.getStato() == StatusList.ONLINE) {
			statusLabel.setText("(Online)");
			System.out.println("ONLINE");
		} else if(Status.getStato() == StatusList.BUSY) {
			statusLabel.setText("(Occupato)");
			System.out.println("BUSY");
		} else if(Status.getStato() == StatusList.AWAY) {
			statusLabel.setText("(Non al computer)");
			System.out.println("AWAY");
		} else {
			System.out.println("NO STATUS");
			statusLabel.setText("(Online)");
		}
		
		/* Aggiorno avatar */
		ImageIcon icon = new ImageIcon(Status.getAvatarURL());
		picture.setIcon(icon); 
		
		// TODO 
		// aggiorna tabella lista amici 
	}
}

