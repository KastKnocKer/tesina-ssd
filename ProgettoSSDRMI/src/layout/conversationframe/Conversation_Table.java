package layout.conversationframe;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import utility.DateUtils;

/**
 * Tabella in cui verranno mostrati (formattati) i messaggi
 * scambiati fra gli utenti della conversazione. 
 * 
 * @author Fabio Pierazzi
 */
public class Conversation_Table extends JTable {
	
	/* Costruttore */
	public Conversation_Table(DefaultTableModel model) {
		
		super(model); 
		
//		this.setDefaultRenderer(String.class, new LineWrapCellRenderer());
		
		model.addColumn("Time");
	    model.addColumn("Nickname");
	    model.addColumn("Messaggio");
	    
	    this.getColumn("Time").setPreferredWidth(60); 
	    this.getColumn("Time").setMaxWidth(60); 
	    
	    this.getColumn("Nickname").setPreferredWidth(70); 
	    this.getColumn("Nickname").setMaxWidth(200); 
	    
//	    this.setRowHeight(500); 

//	    String[] socrates = {  DateUtils.now_time(), "Socrates", "Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus  " +
//	    		"Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus " +
//	    		"Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus " +
//	    		"Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus Caius magnus "};
//	    
//	    for(int i=0; i<100; i++) 
//	    	model.addRow(socrates);
	    
	    

	}
	
	public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == 2 ) {
            return new MultilineTableCell(); 
        }
        else {
            return super.getCellRenderer(row, column);
        }
    }


}
