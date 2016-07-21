/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHChecker.Renderer;

import java.text.NumberFormat;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Administrator
 */
public class IntegerRenderer extends DefaultTableCellRenderer{
    public IntegerRenderer(){
        super();
        
    }
    
    @Override
    protected void setValue(Object value) {
        if((value != null) && (value instanceof Number)){
            Number numberValue = (Number)value;
            NumberFormat formater = NumberFormat.getNumberInstance();
            value = formater.format(numberValue.doubleValue());
        }
        super.setValue(value); //To change body of generated methods, choose Tools | Templates.
    }
    
}
