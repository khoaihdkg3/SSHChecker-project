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
public abstract class SSHChecking implements Runnable{
    protected int numSSH = 0;
    protected int timeout = 30000;
    protected SSH ssh = null;
    protected SSHsTableModel model = null;
    public void setTimeout(int timeout){
        this.timeout = timeout;
    };
    public void setSSH(SSH ssh){
        this.ssh = ssh;
    };
    public void setModel(SSHsTableModel model){
        this.model = model;
    };
    public void setNumSSH(int numSSH){
        this.numSSH = numSSH;
    };
    public int getTimeout(){
        return this.timeout;
    };
    public SSH getSSH(){
        return this.ssh;
    };
    public SSHsTableModel getModel(){
        return this.model;
    };
    public int getNumSSH(){
        return this.numSSH;
    }
}
