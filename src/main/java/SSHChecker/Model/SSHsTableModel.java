/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHChecker.Model;

import SSHChecker.SSH;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Administrator
 */
public class SSHsTableModel extends AbstractTableModel {
    public static final int COLUMN_NO              = 0;
    public static final int COLUMN_STATUS          = 1;
    public static final int COLUMN_HOST            = 2;
    public static final int COLUMN_USERNAME        = 3;
    public static final int COLUMN_PASSWORD        = 4;
    public static final int COLUMN_COUNTRY         = 5;
    public static final int COLUMN_BLPERCENT       = 6;
    public static final int COLUMN_ISFRESH         = 7;
    public static final int COLUMN_TIME            = 8;
    
    private String[] columnNames = new String[]
    {
        "NO #",
        "Status",
        "Host",
        "Username",
        "Password",
        "Country",
        "Blacklist%",
        "IsFresh",
        "Time(ms)"
    };
    private boolean[] columnEditable = new boolean[]
    {
        false,
        false,
        true,
        true,
        true,
        false,
        false,
        false,
        false
    };
    private List<SSH> sshs = new ArrayList<SSH>();
    
    public SSHsTableModel(List<SSH> sshs){
        this.sshs = sshs;
        
        int indexCount = 1;
        for(SSH ssh : sshs)
            ssh.setIndex(indexCount++);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnEditable[columnIndex];
    }
    
    @Override
    public int getRowCount() {
        return sshs.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(sshs.isEmpty())
            return Object.class;
        return getValueAt(0, columnIndex).getClass();
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SSH ssh = sshs.get(rowIndex);
        Object returnValue = null;
        switch(columnIndex){
            case COLUMN_NO:
                    returnValue = ssh.getIndex();
                    break;
            case COLUMN_STATUS:
                    returnValue = ssh.getStatus();
                    break;
            case COLUMN_HOST:
                    returnValue = ssh.getHost();
                    break;
            case COLUMN_USERNAME:
                    returnValue = ssh.getUsername();
                    break;
            case COLUMN_PASSWORD:
                    returnValue = ssh.getPassword();
                    break;
            case COLUMN_COUNTRY:
                    returnValue = ssh.getCountry();
                    break;
            case COLUMN_BLPERCENT:
                    returnValue = (int)ssh.getBLPercent();
                    break;
            case COLUMN_ISFRESH:
                    returnValue = (boolean)ssh.getIsFresh();
                    break;
            case COLUMN_TIME:
                    returnValue = (long)ssh.getTime();
                    break;
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
        return returnValue;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        SSH ssh = sshs.get(rowIndex);
        switch(columnIndex){
            case COLUMN_NO:
                    ssh.setIndex((int)aValue);
                    break;
            case COLUMN_STATUS:
                    ssh.setStatus(aValue.toString());
                    break;
            case COLUMN_HOST:
                    ssh.setHost(aValue.toString());
                    break;
            case COLUMN_USERNAME:
                    ssh.setUsername(aValue.toString());
                    break;
            case COLUMN_PASSWORD:
                    ssh.setPassword(aValue.toString());
                    break;
            case COLUMN_COUNTRY:
                    ssh.setStatus(aValue.toString());
                    break;
            case COLUMN_BLPERCENT:
                    ssh.setBLPercent((float)aValue);
                    break;
            case COLUMN_ISFRESH:
                    ssh.setIsFresh((boolean)aValue);
                    break;
            case COLUMN_TIME:
                    ssh.setTime((long)aValue);
                    break;        
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
        
    }
    public void updateRow(int rowIndex, int columnIndex){
        fireTableRowsUpdated(rowIndex, rowIndex);
    }
    @Override
    public void fireTableRowsDeleted(int firstRow, int lastRow) {
        
        for(int i = lastRow; i >= firstRow; i--){
            sshs.remove(i);
            super.fireTableRowsDeleted(i, i);
        }
        if(!sshs.isEmpty()){
            int indexCount = 1;
            for(SSH ssh : sshs)
                ssh.setIndex(indexCount++);
            super.fireTableRowsUpdated(0,getRowCount()-1);
        } 
    }
    
    public void removeAllRow(){
        fireTableRowsDeleted(0, getRowCount()-1);
        
        SSH nullSSH = new SSH("", "", "", "");
        nullSSH.setStatus("");
        sshs.add(nullSSH);
        fireTableRowsInserted(0, 0);
    }
    public void removeRow(int row) {
        if(getRowCount()==1)
            removeAllRow();
        else
            fireTableRowsDeleted(row, row);
    }
    public void addRow(SSH ssh) {
        if((int)getValueAt(0, 0)==0)
            removeRow(0);
        int indexCount = sshs.size();
        ssh.setIndex(indexCount+1);
        sshs.add(ssh);
        fireTableRowsInserted(indexCount,indexCount);// Tính từ 0
    }
    public List<SSH> getAllRow(){
        return sshs;
    }
    
}
