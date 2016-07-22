/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHChecker.Renderer;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Administrator
 */
public class FileChooseRenderer extends DefaultTableCellRenderer{
    public FileChooseRenderer(){
        super();
    }

    @Override
    protected void setValue(Object value) {
        if(value!=null)
            super.setValue((String)value);
        super.setValue(super.getText());
    }
    
    
}
