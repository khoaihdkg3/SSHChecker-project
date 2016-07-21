/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHChecker.Renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Administrator
 */
public class ProgressRenderer extends JProgressBar implements TableCellRenderer{
    public final static int TYPE_CHECKING_TEXT = -1;
    public final static int TYPE_WAITING_TEXT  = -2;
    public final static int TYPE_EMPTY_TEXT    = -3;
    
    public ProgressRenderer(){
        super();
        super.setMinimum(-3);
        super.setMaximum(100);
        super.setStringPainted(true);
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int percent = (int)value;
        if(percent < 50){
            setForeground(new Color(0, 153, 0));
        }else if(percent < 80 ){
            setForeground(new Color(255, 153,0));
        }
        else{
            setForeground(new Color(204, 0, 0));
        }
        switch (percent) {
            case TYPE_CHECKING_TEXT:
                setString("Checking");
                break;
            case TYPE_WAITING_TEXT:
                setString("Waiting");
                break;
            case TYPE_EMPTY_TEXT:
                setString("");
                break;
            default:
                setString(percent+"%");
                break;
        }
        setValue(percent);
        return this;
    }
    
}
