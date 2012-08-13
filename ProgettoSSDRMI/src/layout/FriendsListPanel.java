package layout;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class FriendsListPanel extends JPanel {
	
	private JPanel mainPanel; 
	private JTable table_friendsList; 
	
	public FriendsListPanel() {
		
		/* recupero l'home frame */
		HomeFrame hf = LayoutReferences.getHomeFrame(); 
		
		mainPanel = new JPanel(new BorderLayout()); 
		
		/* Cambio la menu bar del frame */
		hf.show_HomeFrame_LoggedMenuBar(); 
		
		/* In alto, metto un pannello */
		drawHeaderPanel();
		
		/* Disegno la Table */
		drawFriendsListTable(); 
		
		ImageIcon myIcon = new ImageIcon("images/myPic.gif");

		this.add(mainPanel); 
		mainPanel.setSize(800,800); 
		mainPanel.setVisible(true); 
	}
	
	/* Disegno il pannello sopra con avatar ecc. */
	private void drawHeaderPanel() {
		ImagePanel imagePanel = new ImagePanel("Chrysanthemum.jpg"); 
		mainPanel.add(imagePanel, BorderLayout.CENTER);
		imagePanel.setVisible(true); 
	}
	
	/* Disegno la tabella di amici */
	private void drawFriendsListTable() {
		
		FriendsListTableModel fl_tableModel = new FriendsListTableModel(); 
		FriendsListTable fl_table = new FriendsListTable(fl_tableModel); 
		
		fl_table.updateTable();
//		 
//		Object rowData[][] = {  {"Online", "Fabio Pierazzi"}, 
//								{"Online", "Andrea Castelli"},
//								{"Offline", "Matteina al mare"},
//								{"Offline", "Marco Guerri"}
//							};
//		Object columnNames[] = {"Status", "Nome"}; 
//		
//		TableModel tm = new DefaultTableModel(rowData, columnNames);
//		
//		table_friendsList = new JTable(tm);
//		
//		/* Aggiungo la tabella al pannello */ 
//		JScrollPane scrollPane = new JScrollPane(table_friendsList);
//		table_friendsList.setFillsViewportHeight(true);
//		
//		table_friendsList.setSize(500, 200); 
		mainPanel.setLayout(new BorderLayout());
		
		// mainPanel.add(table_friendsList.getTableHeader(), BorderLayout.PAGE_START);
		
		JPanel pannello = new JPanel(); 
		pannello.add(fl_table);
		// pannello.setSize(500,350); 
		mainPanel.add(pannello, BorderLayout.NORTH);
	}
}

