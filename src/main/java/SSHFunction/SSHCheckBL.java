/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHFunction;

import SSHChecker.Model.SSHsTableModel;
import static SSHChecker.Model.SSHsTableModel.COLUMN_BLPERCENT;
import SSHChecker.Model.SSHsTableSettingModel;
import static SSHChecker.Renderer.ProgressBLCheckingRenderer.TYPE_CHECKING_TEXT;
import SSHChecker.SSH;
import SSHChecker.SSHCheckerView;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class SSHCheckBL extends SSHChecking
        implements Runnable{
        
        private HashMap<String, String> headers=new HashMap<String,String>();
        private int countNotClear = 0;
        private String url = "http://ip-score.com/ajax_handler/get_bls";
        public SSHCheckBL(SSH ssh, int timeout, SSHsTableModel model){
            headers.put("X-Requested-With", "XMLHttpRequest");
            headers.put("If-Modified-Since", "*");
            super.setSSH(ssh);
            super.setTimeout(timeout);
            super.setModel(model);
        }
    @Override
    public void run() {
        ssh.setBLPercent(TYPE_CHECKING_TEXT);
        model.updateRow(ssh.getIndex()-1,COLUMN_BLPERCENT);
        
        ExecutorService pool = Executors.newFixedThreadPool(20);
        ArrayList<Future> al = new ArrayList<Future>();
        for(String site : SSHCheckBL_Site.SITE){

                al.add(pool.submit(new Runnable() {
                @Override
                public void run() {
                    myHTTP mhttp = new myHTTP();
                    //mhttp.setHTTPProxy("localhost", "8080");
                    //System.setProperty("javax.net.ssl.trustStore", "D:\\Software\\java lib\\FiddlerKeystore");
                    //System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
                    mhttp.setHeadersRequest(headers);
                    mhttp.setTimeout(getTimeout());
                    try {
                        mhttp.POST(url, "ip="+ssh.getHost()+"&server=" + site);
                        if(!mhttp.getContentPage().equals("{\""+site+"\":\"\"}"))
                            countNotClear++;
                    } catch (SocketTimeoutException ex) {
                    } catch (IOException ex) {
                    }
                }
            }));
            
        }
        for(Future f : al)
                try {
                    f.get();
                    ssh.setBLPercent((float)countNotClear/SSHCheckBL_Site.SITE.length*100);
                    model.updateRow(ssh.getIndex()-1,COLUMN_BLPERCENT);
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(SSHCheckerView.class.getName()).log(Level.SEVERE, null, ex);
                }
        
    }
}

