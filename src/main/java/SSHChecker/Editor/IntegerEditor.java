/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHChecker.Editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author Administrator
 */
public class IntegerEditor extends DefaultCellEditor{
        JFormattedTextField jformattextfield;
        NumberFormat integerFormat;
        private Integer min, max;
    public IntegerEditor(int min, int max) {
        super(new JFormattedTextField()); //super phải đc gọi đầu tiên trong hàm xây dựng
        jformattextfield = (JFormattedTextField)getComponent(); //giờ mới có thể gán component cho biến
        this.min = min;
        this.max = max;
        
        integerFormat = NumberFormat.getIntegerInstance();
        NumberFormatter integerFormatter = new NumberFormatter(integerFormat);
        integerFormatter.setMinimum(min);
        integerFormatter.setMaximum(max);
        
        jformattextfield.setFormatterFactory(new DefaultFormatterFactory(integerFormatter));
        jformattextfield.setValue(min);
        jformattextfield.setToolTipText(
                "The value must be integer between "+min+" and "+max
        );
        
        jformattextfield.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                "Enter"
        );
        jformattextfield.getActionMap().put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopCellEditing();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }
    
    @Override
    public Object getCellEditorValue() {
        Object value = jformattextfield.getValue();
        if(value instanceof Integer)
            return value;
        else if(value instanceof Number)
            return ((Number) value).intValue();
        else
            try {
                return integerFormat.parseObject((String) value);
            } catch (ParseException ex) {
                return null;
            }
        
    }

    @Override
    public boolean stopCellEditing() {
        if(jformattextfield.isEditValid())
            try {
                jformattextfield.commitEdit();
        } catch (ParseException ex) {
            System.err.println(IntegerEditor.class.getName() + ": " + ex);
        }
        else{
            try{
                Integer checkInteger = Integer.parseInt(jformattextfield.getText());
                if(checkInteger<min)
                    jformattextfield.setValue(min);
                else
                    jformattextfield.setValue(max);
            }
            catch(NumberFormatException e){
                jformattextfield.setValue(max);
            }
            //System.err.println("Edit invalid : "+jformattextfield.getText());
            return false;
        }
        return super.stopCellEditing(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
