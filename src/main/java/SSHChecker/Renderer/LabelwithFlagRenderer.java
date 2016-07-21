/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHChecker.Renderer;

import java.awt.Component;
import java.net.URL;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Administrator
 */
public class LabelwithFlagRenderer extends JLabel implements TableCellRenderer{
    public LabelwithFlagRenderer(){
        super();
        
        setOpaque(true);
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(isSelected)
            setBackground(table.getSelectionBackground());
        else
            setBackground(table.getBackground());
        
        String nameFlag = (String)value;
        if(nameFlag!=null
                &&nameFlag.length()>0
                &&!nameFlag.isEmpty()){
            int len = nameFlag.length();
            nameFlag = nameFlag.substring(len-3, len-1).toLowerCase();
            URL pathFlag = getClass().getResource("/ICON/Flag/"+nameFlag+".png");
            if(pathFlag == null)
                pathFlag = getClass().getResource("/ICON/unknow.png");
            setIcon(new javax.swing.ImageIcon(pathFlag));
            }
        setText((String)value);
        
        return this;
    }
    
}
