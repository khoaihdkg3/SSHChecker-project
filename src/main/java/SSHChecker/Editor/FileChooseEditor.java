/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHChecker.Editor;

import SSHChecker.ChooseFile;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Administrator
 */
public class FileChooseEditor extends DefaultCellEditor{
    private File file = null;
    private JCheckBox Button;
    public FileChooseEditor(ChooseFile choosefile){
        super(new JCheckBox());
        Button = (JCheckBox)super.getComponent();
        Button.setVisible(false);
        Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                file = choosefile.OpenFileDialog(
                new FileNameExtensionFilter("Bitvise exe", "exe"));
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return super.getTableCellEditorComponent(table, value, isSelected, row, column); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Object getCellEditorValue() {
        
        if(file!=null)
            return file.getAbsolutePath();
        return "";
    }

    
    
}
