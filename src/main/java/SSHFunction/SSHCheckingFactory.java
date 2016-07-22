/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHFunction;

import SSHChecker.Model.SSHsTableModel;
import SSHChecker.SSH;

/**
 *
 * @author Administrator
 */
public class SSHCheckingFactory {
    public static final String LIVEID       = "Live";
    public static final String BLACKLISTID  = "Blacklist";
    
    public static SSHChecking getSSHChecking(String ID,SSH ssh, int timeout, SSHsTableModel model){
        switch(ID){
            case LIVEID:
                return new SSHCheckLive(ssh, timeout, model);
            case BLACKLISTID:
                return new SSHCheckBL(ssh, timeout, model);
            default:
                return null;
        }
        
    }
    public static boolean isAvaliable(String ID){
        return getSSHChecking(ID, null, 0, null)!=null;
    }
}
