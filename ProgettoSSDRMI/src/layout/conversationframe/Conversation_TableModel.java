package layout.conversationframe;

import javax.swing.table.AbstractTableModel;

/**
 * TableModel della Conversation_Table in cui verranno mostrati
 * i messaggi delle conversazioni. 
 * 
 * @author Fabio Pierazzi
 *
 */
public class Conversation_TableModel extends AbstractTableModel {

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return 100;
	}

	@Override
	public Object getValueAt(int row, int column) {
		return "Prova";
	}

	/**
	 * Indica se la cella nella posizione
	 *  passata come parametro è editabile
	 */
	public boolean isCellEditable(int row,int col){
		return false;
	}

}
