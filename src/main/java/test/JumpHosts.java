package test;

import com.jcraft.jsch.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.swing.*;

public class JumpHosts {
  public static void main(String[] argr){
String host = "195.68.111.242";
    String user = "admin";
    String password = "default";
    int port = 22;

    String remoteHost = "ip-score.com";
    int remotePort = 80;

    int localPort = 8888;
    int assignedPort;
    String localHost = "127.0.0.1";
    
    try { 
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setPassword(password);
        session.setUserInfo(new MyUserInfo());
        session.connect();
        assignedPort = session.setPortForwardingL(localPort, remoteHost, remotePort);
        Channel channel = session.openChannel("direct-tcpip");  
        
        System.out.println(assignedPort);
        ((ChannelDirectTCPIP)channel).setHost(localHost);
        ((ChannelDirectTCPIP)channel).setPort(assignedPort);

        String cmd = "GET / HTTP/1.0\r\n\r\n";

        InputStream in = channel.getInputStream();
        OutputStream out = channel.getOutputStream();
        ((ChannelDirectTCPIP)channel).connect();

        byte[] bytes = cmd.getBytes();          
        InputStream is = new ByteArrayInputStream(cmd.getBytes("UTF-8"));

        int numRead;

        while ((numRead = is.read(bytes)) >= 0)
              out.write(bytes, 0, numRead);

        out.flush();


        System.out.println("Request supposed to have been sent");
        
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            for (String line; (line = reader.readLine()) != null;){
                System.out.println(line);
            }
        } catch (java.io.IOException exc) {
            System.out.println(exc.toString());
        }
        channel.disconnect();
        session.disconnect();
    } catch (Exception e){
        System.out.println(e.toString());
    }
  }

  public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{
    public String getPassword(){ return passwd; }
    public boolean promptYesNo(String str){
      Object[] options={ "yes", "no" };
      int foo=JOptionPane.showOptionDialog(null, 
             str,
             "Warning", 
             JOptionPane.DEFAULT_OPTION, 
             JOptionPane.WARNING_MESSAGE,
             null, options, options[0]);
       return foo==0;
    }
  
    String passwd;
    JTextField passwordField=(JTextField)new JPasswordField(20);

    public String getPassphrase(){ return null; }
    public boolean promptPassphrase(String message){ return true; }
    public boolean promptPassword(String message){
      Object[] ob={passwordField}; 
      int result=
	  JOptionPane.showConfirmDialog(null, ob, message,
					JOptionPane.OK_CANCEL_OPTION);
      if(result==JOptionPane.OK_OPTION){
	passwd=passwordField.getText();
	return true;
      }
      else{ return false; }
    }
    public void showMessage(String message){
      JOptionPane.showMessageDialog(null, message);
    }
    final GridBagConstraints gbc = 
      new GridBagConstraints(0,0,1,1,1,1,
                             GridBagConstraints.NORTHWEST,
                             GridBagConstraints.NONE,
                             new Insets(0,0,0,0),0,0);
    private Container panel;
    public String[] promptKeyboardInteractive(String destination,
                                              String name,
                                              String instruction,
                                              String[] prompt,
                                              boolean[] echo){
      panel = new JPanel();
      panel.setLayout(new GridBagLayout());

      gbc.weightx = 1.0;
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.gridx = 0;
      panel.add(new JLabel(instruction), gbc);
      gbc.gridy++;

      gbc.gridwidth = GridBagConstraints.RELATIVE;

      JTextField[] texts=new JTextField[prompt.length];
      for(int i=0; i<prompt.length; i++){
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.weightx = 1;
        panel.add(new JLabel(prompt[i]),gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        if(echo[i]){
          texts[i]=new JTextField(20);
        }
        else{
          texts[i]=new JPasswordField(20);
        }
        panel.add(texts[i], gbc);
        gbc.gridy++;
      }

      if(JOptionPane.showConfirmDialog(null, panel, 
                                       destination+": "+name,
                                       JOptionPane.OK_CANCEL_OPTION,
                                       JOptionPane.QUESTION_MESSAGE)
         ==JOptionPane.OK_OPTION){
        String[] response=new String[prompt.length];
        for(int i=0; i<prompt.length; i++){
          response[i]=texts[i].getText();
        }
	return response;
      }
      else{
        return null;  // cancel
      }
    }
  }
}