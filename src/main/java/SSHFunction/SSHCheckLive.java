/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHFunction;

import SSHChecker.Model.SSHsTableModel;
import static SSHChecker.Renderer.ProgressRenderer.TYPE_EMPTY_TEXT;
import static SSHChecker.Renderer.ProgressRenderer.TYPE_WAITING_TEXT;
import SSHChecker.SSH;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class SSHCheckLive extends SSHChecking implements Runnable {

    public SSHCheckLive(SSH ssh, int timeout, SSHsTableModel model) {
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
        boolean isFresh = false;
        long timeWaiting = 0;
        String status;
        String remoteHost = "ip-score.com";
        int remotePort = 80;
        String localHost = "127.0.0.1";
        int localPort = 8000 + ssh.getIndex();
        JSch jsch = new JSch();
        try {
            Session session = jsch.getSession(ssh.getUsername(), ssh.getHost(), 22);
            UserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);
            session.setPassword(ssh.getPassword());
            long startTime = System.currentTimeMillis();
            session.connect(getTimeout());
            isConnected = session.isConnected();
            timeWaiting = System.currentTimeMillis() - startTime;
            if (isConnected) {
                session.setPortForwardingL(localPort, remoteHost, remotePort);
                //System.out.println(localPort+" shell:");
                Channel channel = session.openChannel("shell");
                channel.setOutputStream(System.out);
                channel.connect();
                channel.disconnect();
                myHTTP http = new myHTTP();
                try {
                    int responseCode = http.GET("http://"+localHost+":"+localPort+"/");
                    //http.writetofile("D:\\test\\"+localPort+".html", false);
                    if (responseCode == 200) 
                        isFresh = true;
                } catch (IOException ex) {
                   //System.err.println(localPort+" -> "+ex);
                }
            }
            session.disconnect();
        } catch (JSchException ex) {
            //System.err.println(localPort+" -> "+ex);
            isConnected = false;
        }
        status = (isConnected?"Working":"Error");
        ssh.setIsFresh(isFresh);
        ssh.setTime(timeWaiting);
        ssh.setStatus(status);
        model.updateRow(ssh.getIndex() - 1, 1);

    }

    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public boolean promptYesNo(String str) {
            //System.out.println(str);
            return true;
        }

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public boolean promptPassphrase(String message) {
            //System.out.println(message);
            return true;
        }

        @Override
        public boolean promptPassword(String message) {
            //System.out.println(message);
            return false;
        }

        @Override
        public void showMessage(String message) {
            //System.out.println(message);
        }

        @Override
        public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
            //System.out.println(destination + "|" + name + "|" + instruction + "|" + Arrays.toString(prompt));
            return null;
        }

    }
}
