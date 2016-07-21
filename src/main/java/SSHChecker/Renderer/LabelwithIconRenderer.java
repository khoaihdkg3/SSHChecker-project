/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHChecker.Renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Administrator
 */
public class LabelwithIconRenderer extends JLabel implements TableCellRenderer{
    private HashMap<String,String> TextwithIcon_HashMap;
    private HashMap<String,Color> TextwithColor_HashMap;
    
    public LabelwithIconRenderer(HashMap TextwithIcon_HashMap, HashMap TextwithColor_HashMap){
        super();
        this.TextwithIcon_HashMap = new HashMap(TextwithIcon_HashMap);
        this.TextwithColor_HashMap = new HashMap(TextwithColor_HashMap);
        super.setOpaque(true);
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(isSelected)
            setBackground(table.getSelectionBackground());
        else
            setBackground(table.getBackground());
        
        String text = (String) value;
            if(text!=null){
                setForeground(TextwithColor_HashMap.get(text));
                setFont(new Font("Tahoma", 1, 11));
                setText(text);
                String nameIcon = TextwithIcon_HashMap.get(text);
                if(nameIcon!=null)
                    setIcon(new javax.swing.ImageIcon(getClass().getResource(nameIcon)));
            }
        return this;
    }
    
}
