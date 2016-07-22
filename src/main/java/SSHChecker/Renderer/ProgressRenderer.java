package SSHChecker.Renderer;
import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class ProgressRenderer extends JProgressBar implements TableCellRenderer{
    public ProgressRenderer(){
        super();
        super.setMinimum(0);
        super.setMaximum(100);
        super.setStringPainted(true);
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int percent = (int)value;
        setValue(percent);
        return this;
    }
}
