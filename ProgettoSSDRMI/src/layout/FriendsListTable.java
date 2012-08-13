package layout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import chat.FriendsList;
import chat.Status;

public class FriendsListTable extends JTable implements MouseListener,ActionListener {

	private JPopupMenu jPopupMenu;
	private JTableHeader jTableHeader;
	
	/** Mantengo separata la gestione della friendslist caricata in tabella rispetto
	 * a quella globale, perché potrei aver bisogno di applicare filtri (?)  */
	// TODO check if necessary
	private FriendsList friendsList; 
	
	
	/**
	 * Costruttore con parametro
	 * @param Table Model
	 */
	public FriendsListTable(FriendsListTableModel friendsListTableModel) {
		
		super(friendsListTableModel);
		
		/* Imposto il valore delle variabili static globali 
		 * con le quali farò successivamente riferimento alla 
		 * tabella contenente la lista amici */
		
		if(LayoutReferences.getFriendsListTable() == null)
			LayoutReferences.setFriendsListTable(this); 
		
		LayoutReferences.setFriendsListTableModel(friendsListTableModel); 

		/* Carico la FriendsList globale non filtrata */
		// TODO check if necessary
		friendsList = Status.getFriendsList(); 
		friendsListTableModel.setFriendsList(friendsList); 
		
		// TODO 
		Status.loadFriendsList();
		
		this.updateUI();
//		
//		if(LinkTabella==null) LinkTabella=this;
//		this.setName("JTableTabella");
//		this.addMouseListener(this);
//		
//		JTB=this.getTableHeader();
//		JTB.setName("JTableHeader");
//		JTB.addMouseListener(this);
//
//		JPM =new JPopupMenu();	//Creo il popup menu
//		JMenuItem JMIP_Play = new JMenuItem("Riproduci");
//		JMenuItem JMIP_Scheda = new JMenuItem("Scheda artista");
//		JMenuItem JMIP_Aggiorna = new JMenuItem("Aggiorna");
//		JMenuItem JMIP_Proprietà = new JMenuItem("Proprietà");
//		JMenuItem JMIP_Elimina = new JMenuItem("Elimina");
//		JMIP_Play.addActionListener(this);
//		JMIP_Scheda.addActionListener(this);
//		JMIP_Aggiorna.addActionListener(this);
//		JMIP_Proprietà.addActionListener(this);
//		JMIP_Elimina.addActionListener(this);
//		
//			JPM.add(JMIP_Play);
//			JPM.add(JMIP_Scheda);
//			JPM.add(JMIP_Aggiorna);
//			JPM.add(JMIP_Elimina);
//			JPM.add(JMIP_Proprietà);
//			
//		TableColumn colonna = null;
//		for(int i=0;i<LinkDataModel.getColumnCount();i++){
//				colonna = this.getColumnModel().getColumn(i);
//				switch(i){
//				case 0: {colonna.setPreferredWidth(50); break;}		//Colonna indice
//				case 5: {colonna.setPreferredWidth(60); break;}		//Colonna anno
//				case 6: {colonna.setPreferredWidth(30); break;}		//Colonna num traccia cd
//				case 7: {colonna.setPreferredWidth(100); break;}		//Colonna num traccia cd
//				default: {colonna.setPreferredWidth(200); break;}
//				
//				}
//		}
		
	}
	
	/**
	 * Aggiorna la tabella
	 */
	public void updateTable(){
		Status.loadFriendsList();
		this.updateUI();
	}
	
	/**
	 * Imposta la struttura dati contenente la lista amici
	 * e la associa alla tabella; aggiorna poi la GUI.
	 * @param FriendsList
	 */
	public void setFriendsList(FriendsList fl){
		
		// Status.setFriendsList(fl); 
		
		this.friendsList = fl; 
		LayoutReferences.getFriendsListTableModel().setFriendsList(fl); 
		/* Aggiorno la GUI */
		this.updateUI();
	}
	
	
	/*****************************************************************
	 * ACTION LISTENERS 
	 * ***************************************************************/
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
