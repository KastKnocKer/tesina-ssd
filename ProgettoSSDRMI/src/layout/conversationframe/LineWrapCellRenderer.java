package layout.conversationframe;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * TableCellRenderer per renderizzare una TextArea all'interno dei campi della
 * tabella di conversazione. 
 * 
 * @author Fabio Pierazzi
 *
 */
public class LineWrapCellRenderer  extends JTextArea implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
            this.setText((String)value);
            this.setWrapStyleWord(true);                    
            this.setLineWrap(true);                 
//            return this;
    	
    	   int fontHeight = this.getFontMetrics(this.getFont()).getHeight();
           int textLength = this.getText().length();
           int lines = textLength / this.getColumns() +1;//+1, cause we need at least 1 row.                       
           int height = fontHeight * lines;                        
           table.setRowHeight(row, height);
           System.out.println("height: " + height);
           return this;
    }

}