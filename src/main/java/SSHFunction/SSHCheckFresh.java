/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHFunction;

import SSHChecker.Model.SSHsTableModel;
import static SSHChecker.Renderer.ProgressBLCheckingRenderer.TYPE_EMPTY_TEXT;
import static SSHChecker.Renderer.ProgressBLCheckingRenderer.TYPE_WAITING_TEXT;
import SSHChecker.SSH;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 *
 * @author Administrator
 */
public class SSHCheckFresh extends SSHChecking implements Runnable{
    public SSHCheckFresh(SSH ssh, int timeout, SSHsTableModel model) {
        super.setSSH(ssh);
        super.setTimeout(timeout);
        super.setModel(model);
    }
    @Override
    public void run() {
        SSH ssh = getSSH();
        SSHsTableModel model = getModel();
        ssh.setStatus("Checking");
        model.updateRow(ssh.getIndex() - 1, 1);

        boolean isConnected = false;
        long timeWaiting = 0;

        JSch jsch = new JSch();
        try {
            Session session = jsch.getSession(ssh.getUsername(), ssh.getHost(), 22);
            UserInfo ui = new SSHCheckLive.MyUserInfo();
            session.setUserInfo(ui);
            session.setPassword(ssh.getPassword());
            long startTime = System.currentTimeMillis();
            session.connect(getTimeout());
            isConnected = session.isConnected();
            timeWaiting = System.currentTimeMillis() - startTime;
            session.disconnect();
        } catch (JSchException ex) {
        }
        
        String status;
        if (!isConnected) {
            status = "Error";
            ssh.setBLPercent(TYPE_EMPTY_TEXT);
        } else {
            status = "Working";
            ssh.setBLPercent(TYPE_WAITING_TEXT);
        }
        ssh.setTime(timeWaiting);
        ssh.setStatus(status);
        model.updateRow(ssh.getIndex() - 1, 1);
    }
    
}
