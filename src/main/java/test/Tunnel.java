package test;


import SSHFunction.myHTTP;
import com.jcraft.jsch.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Tunnel {

    public static void main(String[] args) {
        Tunnel t = new Tunnel();
        try {
            t.go();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void go() throws Exception {
        String user = "installer";
        String password = "installer";
        String host = "69.198.11.157";
        int port = 22;
        
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setPassword(password);
        session.setUserInfo(new localUserInfo());

        session.connect();
        session.setPortForwardingL(8989, "www.ip-score.com", 80);
        System.out.println("connected");

        Socket s = new Socket("127.0.0.1", 8989);
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        DataInputStream in = new DataInputStream(s.getInputStream());
        
        String data = "GET / HTTP/1.1\r\n"+
                    "Host:www.ip-score.com\r\n\r\n";
        out.writeInt(data.length());
        out.write(data.getBytes());
        
        FileOutputStream fout = new FileOutputStream(new File("D:\\test\\test.html"));
        
        byte[] b = new byte[2048];
        int iread;
        while((iread=in.read())!=-1){
            fout.write(b, 0, iread);
            fout.flush();
            System.out.println("recv: "+iread);
        }
        fout.close();
        out.close();
        in.close();
    }

    class localUserInfo implements UserInfo {

        String passwd;

        public String getPassword() {
            return passwd;
        }

        public boolean promptYesNo(String str) {
            System.out.println("promptYesNo: "+str);
            return true;
        }

        public String getPassphrase() {
            return null;
        }

        public boolean promptPassphrase(String message) {
            System.out.println("promptPassphrase: "+message);
            return true;
        }

        public boolean promptPassword(String message) {
            System.out.println("promptPassword: "+message);
            return true;
        }

        public void showMessage(String message) {
            System.out.println("showMessage: "+message);
        }
    }
}
