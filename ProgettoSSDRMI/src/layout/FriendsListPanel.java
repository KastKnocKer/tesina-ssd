package layout;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import chat.Status;

public class FriendsListPanel extends JPanel {
	
	private HomeFrame homeFrame; 
	private JPanel mainPanel; 
	private JTable table_friendsList; 
	
	public FriendsListPanel() {
		
		/* recupero l'home frame */
		homeFrame = LayoutReferences.getHomeFrame(); 
		
		this.setLayout(new BorderLayout());
		
		mainPanel = new JPanel(new BorderLayout()); 

		/* Cambio la menu bar del frame */
		homeFrame.show_HomeFrame_LoggedMenuBar(); 
		
		/* In alto, metto un pannello */
		// drawHeaderPanel();
		
		/* Disegno la Table */
		drawFriendsListTable(); 
		
		// ImageIcon myIcon = new ImageIcon("images/myPic.gif");

		this.add(mainPanel, BorderLayout.CENTER); 
		// mainPanel.setSize(800,800); 
		mainPanel.setVisible(true); 
	}
	
	/* Disegno il pannello sopra con avatar ecc. */
	private void drawHeaderPanel() {
		ImagePanel imagePanel = new ImagePanel("Chrysanthemum.jpg"); 
		mainPanel.add(imagePanel, BorderLayout.SOUTH);
		imagePanel.setVisible(true); 
	}
	
	/* Disegno la tabella di amici */
	private void drawFriendsListTable() {
		
		FriendsListTableModel fl_tableModel = new FriendsListTableModel();
		fl_tableModel.setFriendsList(Status.getFriendsList()); 
		FriendsListTable fl_table = new FriendsListTable(fl_tableModel); 
		fl_table.setFriendsList(Status.getFriendsList()); 
		
		fl_table.updateTable();
		fl_table.setVisible(true); 
		
		/* Aggiungo la tabella al pannello */ 
		JScrollPane scrollPane = new JScrollPane(fl_table);
		fl_table.setFillsViewportHeight(true);
		
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		
	}
}

