/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHChecker.Model;

import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class SSHsTableSettingModel extends DefaultTableModel{
    public final static int LIVETIMEOUT_ROW            = 0;
    public final static int RECHECKINGWHEN_ROW         = 1;
    public final static int RECHECKINGCOUNT_ROW        = 2;
    public final static int BLACKLISTCHECKING_ROW      = 3;
    public final static int LIVECHECKING_ROW           = 4;
    public final static int FRESHCHECKING_ROW          = 5;
    public final static int BLCHECKINGTHREADCOUNT_ROW  = 6;
    public final static int LCHECKINGTHREADCOUNT_ROW   = 7;
    public final static int BITVISEPATH_ROW            = 8;
    public final static int BLTIMEOUT_ROW              = 0;
    public final static int FTIMEOUT_ROW               = 0;
    public final static int PROGRESSCHECKING_ROW       = 9;
    private String[] ColName = new String[]
    {
        "Setting", 
        "Value"
    };
    private Object[][] RowData = new Object[][]{
            {"TimeOut(ms)", null},
            {"Rechecking when", null},
            {"Rechecking count", null},
            {"Blacklist checking", null},
            {"Live checking", null},
            {"Fresh checking", null},
            {"BLChecking Thread count", null},
            {"LChecking  Thread count", null},
            {"Bitvise path", null},
            {"Progress", 0}
        };
     private boolean[] ColEditable = new boolean[]
    {
        false, 
        true
    };
    public SSHsTableSettingModel(Object[] defaultSetting){
        int i = 0;
        for(Object setting : defaultSetting){
            RowData[i][1] = setting;
            i++;
        }
        super.setDataVector(RowData, ColName);
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return ColEditable[column];
    }

}

