package layout.friendslist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import layout.managers.ConversationWindowsManager;
import layout.managers.LayoutReferences;
import managers.*;
import chat.Contact;
import chat.FriendsList;

public class FriendsList_Table extends JTable implements MouseListener,ActionListener {

	/* jPopUpMenu */
	private JPopupMenu jPopupMenu;
	private final String POPUPMENU_REMOVECONTACT = "Rimuovi Amico"; 
	private final String POPUPMENU_CONTACTINFO = "Info Contatto"; 
	private final String POPUPMENU_OPENCONVERSATION = "Apri Conversazione";
	
	private JTableHeader jTableHeader;

	/* mouse events */
	int clickedRow = -1; 
	int clickedColumn = -1; 
	int lastClickedRow = -1; 
	
	/** Mantengo separata la gestione della friendslist caricata in tabella rispetto
	 * a quella globale, perché potrei aver bisogno di applicare filtri (?)  */
	// TODO check if necessary
	private FriendsList friendsList; 
	
	
	/**
	 * Costruttore con parametro
	 * @param Table Model
	 */
	public FriendsList_Table(FriendsList_TableModel friendsListTableModel) {
		
		super(friendsListTableModel);
		

		/* Rimuovo la colonna con l'userID (solo alla vista) */
//		this.removeColumn(this.getColumnModel().getColumn(0));

		/* Aggiungo gli ascoltatori */
		this.setName("JTable_FriendsListTable");
		this.addMouseListener(this);
		
		JTableHeader jth = this.getTableHeader();
		jth.setName("JTable_FriendsListTableHeader");
		jth.addMouseListener(this);
		
		/* Imposto il valore delle variabili static globali 
		 * con le quali farò successivamente riferimento alla 
		 * tabella contenente la lista amici */
		if(LayoutReferences.getFriendsListTable() == null)
			LayoutReferences.setFriendsListTable(this); 
		
		LayoutReferences.setFriendsListTableModel(friendsListTableModel); 

		/* Carico la FriendsList globale non filtrata */
		// TODO check if necessary
		friendsList = FriendsListManager.getFriendsList(); 
		friendsListTableModel.setFriendsList(friendsList); 
		
		// TODO 
		//Status.loadFriendsList();
		
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
		
		/* Menù tasto destro */
		jPopupMenu =new JPopupMenu();	//Creo il popup menu
		JMenuItem menuItem_newConversation = new JMenuItem(POPUPMENU_OPENCONVERSATION);
		JMenuItem menuItem_contactInfo = new JMenuItem(POPUPMENU_CONTACTINFO);
		JMenuItem menuItem_removeContact = new JMenuItem(POPUPMENU_REMOVECONTACT);
		
		
		menuItem_newConversation.addActionListener(this);
		menuItem_removeContact.addActionListener(this);
		menuItem_contactInfo.addActionListener(this);
		
			jPopupMenu.add(menuItem_newConversation);
			jPopupMenu.add(menuItem_contactInfo);
			jPopupMenu.addSeparator();
			jPopupMenu.add(menuItem_removeContact);
			
		/* Imposto la dimensione preferita per le colonne */
		TableColumn colonna = null;
		
		for(int i=0; i< LayoutReferences.getFriendsListTableModel().getColumnCount();i++){
			
				colonna = this.getColumnModel().getColumn(i);
				switch(i){
					/* Colonna ID */
					case 0: 
						colonna.setPreferredWidth(5); 
						break;
					
					/* Colonna Nickname */
					case 1: 
						break;
					
					/* Colonna stato */
					case 2: 
						colonna.setPreferredWidth(50); 
						break;
					
					default: 
						break; 
				}
		}
		
	}
	
	/**
	 * Ricarica l'elenco contatti della lista amici (ricarica la ContactList 
	 * nella FriendsList) ed aggiorna graficamente la tabella
	 * 
	 * @author Fabio Pierazzi
	 */
	public synchronized void updateTable(){

		//TODO controllare
		if(LayoutReferences.getFriendsListTable() == null) {
			System.err.println("Tentativo di updateTable con tabella " +
					"non disponibile. ");
			return; 
		}
		
		/* ricarico la friendsList */
//		if(ContactListManager.getContactList() == null || ContactListManager.getContactList().size() == 0){
//			Status.loadFriendsList();
//		}
		
		try {
			FriendsListManager.loadFriendsList(); 
		
		/* re-imposto le nuove friendsList per la tabella e per il table model.
		 * Nota: vengono mantenute separate dalla friendsList globale per poter 
		 * così applicare eventuali filtraggi a parte. */
		
			this.setFriendsList(FriendsListManager.getFriendsList());
			LayoutReferences.getFriendsListTableModel().setFriendsList(FriendsListManager.getFriendsList());
			LayoutReferences.getFriendsListTableModel().fireTableDataChanged(); 
			this.updateUI();
		} catch(Exception e) {
			System.err.println("Si è verificato un errore durante l'aggiornamento della tabella della lista amici!");
			e.printStackTrace(); 
		}
		
//		this.revalidate();
//		this.repaint(); 
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
		LayoutReferences.getFriendsListTableModel().fireTableDataChanged();
		
		/* Aggiorno la GUI */
//		this.updateUI();
		
	}
	
	
	/*****************************************************************
	 * ACTION LISTENERS 
	 * ***************************************************************/
	
	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
		
		/* Seleziono la riga cliccata */
		clickedRow = rowAtPoint(mouseEvent.getPoint());
		clickedColumn = columnAtPoint(mouseEvent.getPoint());
			
		/* Se non seleziono nessuna vera riga, ritorno */
		if(clickedRow == -1 || clickedColumn == -1) 
			return; 
		
		/* Verifico il doppio click */
		if(mouseEvent.getClickCount() == 2 && !mouseEvent.isConsumed() && clickedRow == lastClickedRow) {
			mouseEvent.consume(); 

			/* Apro il frame di conversazione con l'utente */
			Contact contact = ContactListManager.searchContactById((int) this.getValueAt(clickedRow, 0)); 
			openConversationFrame(contact); 
			
//			System.out.println("Doppio click :)");
		}
		
		/* salvo in una variabile la penultima riga cliccata */
		lastClickedRow = clickedRow; 
		
		
		
		
		
		this.setRowSelectionInterval(clickedRow, clickedRow);
		
		/* In base al bottone premuto del mouse... */
		switch(mouseEvent.getButton()) {
			/* Tasto sinistro */
			case MouseEvent.BUTTON1: 
				break;
		
			/* Tasto Centrale (Rotella) */
			case MouseEvent.BUTTON2: 
				break;
				
			/* Tasto Destro */
			case MouseEvent.BUTTON3:
				/* Mostro il PopUp Menu */
				jPopupMenu.show(this, mouseEvent.getX(), mouseEvent.getY());
				break; 
		}
		
		
		
//			if(me.getComponent().getName().equals("JTableTabella") ){
//				
//				if( me.getButton() == MouseEvent.BUTTON1) {
//					if( me.getClickCount()==1 ) return;	
//					JPanelArea_Play JPAP=Ctrl_Componenti.getLinkJPanelArea_Play();
//					JPAP.Play( (Mp3)LibMp3.getObjPos(ClickRiga) );
//					return;
//				}
//					
//				if( me.getButton() == MouseEvent.BUTTON2){/*evento per tasto rotella mouse*/return;}
//				
//				if( me.getButton() == MouseEvent.BUTTON3){
//					JPM.show(this, me.getX(), me.getY());	//Mostro il PopupMenu
//					return;
//					}
//			
//				return;
//				}
//			
//			if(me.getComponent().getName().equals("JTableHeader") ){
//				Libreria Lib_Supporto=LibMp3;
//				switch(ClickColonna){
//				case 0: this.Aggiorna(); break;
//				case 1: LibMp3=new Libreria(); LibMp3.getComparator().setMode(Mp3Comparator.Key_Sort_Artista); break;
//				case 2: LibMp3=new Libreria(); LibMp3.getComparator().setMode(Mp3Comparator.Key_Sort_Titolo); break;
//				case 3: LibMp3=new Libreria(); LibMp3.getComparator().setMode(Mp3Comparator.Key_Sort_Album); break;
//				case 4: LibMp3=new Libreria(); LibMp3.getComparator().setMode(Mp3Comparator.Key_Sort_Genere); break;
//				case 5: LibMp3=new Libreria(); LibMp3.getComparator().setMode(Mp3Comparator.Key_Sort_Anno); break;
//				case 6: LibMp3=new Libreria(); LibMp3.getComparator().setMode(Mp3Comparator.Key_Sort_NTraccia); break;
//				case 7: LibMp3=new Libreria(); LibMp3.getComparator().setMode(Mp3Comparator.Key_Sort_TagId); break;
//				case 8: LibMp3=new Libreria(); LibMp3.getComparator().setMode(Mp3Comparator.Key_Sort_Dir); break;
//				}
//				LibMp3.caricaLibreria( Lib_Supporto );
//				
//				this.setLibreria(LibMp3);
//				return;
		
	}
	
	/**
	 * Funzione per aprire un nuovo frame di conversazione
	 */
	public void openConversationFrame(Contact contact) {
		ConversationWindowsManager.showConversationFrame(contact); 
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		
		/* Recupero l'evento */
		String event = actionEvent.getActionCommand();
		
		int contactId = (int) this.getValueAt(clickedRow, 0); 
		Contact friendContact = ContactListManager.searchContactById(contactId); 
	
		/* Specifico le azioni da intraprendere nel momento
		 * in cui viene selezionata una voce del jPopUpMenu */
		if (event.equals(POPUPMENU_OPENCONVERSATION)) {
			openConversationFrame(friendContact); 
		} else if (event.equals(POPUPMENU_REMOVECONTACT)) {
			
			int result = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler rimuovere: \n" +
					"" + friendContact.getNickname() + " ( " + friendContact.getEmail() + " ) \n\n" +
							"Desideri accettare?", 
							"Aggiungi Contatto",
	                JOptionPane.YES_NO_OPTION);
			
			if(result == JOptionPane.YES_OPTION) {
				System.out.println("CONFERMATA - Rimozione contatto " + friendContact.getEmail() + "");
				FriendshipManager.removeFriend(friendContact); 
			}
			else if(result == JOptionPane.NO_OPTION)  {
				if(Status.DEBUG)
					System.out.println("RIFIUTATA - Rimozione contatto " + friendContact.getEmail() + "");
			}
			
			 
		}
	}

}
