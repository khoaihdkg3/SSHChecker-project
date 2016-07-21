package SSHFunction;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Củ Chuối
 */
public class myHTTP {
    public static final String FIREFOX_USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1";
    public static final String CHROME_USERAGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
    public static final String OPERA_USERAGENT = "Opera/9.80 (X11; Linux i686; Ubuntu/14.10) Presto/2.12.388 Version/12.16";
    public static final String INTERNET_EXLORER_USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; AS; rv:11.0) like Gecko";
    public static final String DEFAULT_USERAGENT = INTERNET_EXLORER_USERAGENT;
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";
    private final String CRLF = "\r\n";
    
    private HttpURLConnection urlcon = null;
    private InputStream in = null;
    private Proxy proxy = Proxy.NO_PROXY;
    private HashMap<String,String> HttpRequestHeaders = new HashMap<String,String>();
    private String HttpRquestUserAgent = DEFAULT_USERAGENT;
    private Authenticator authenticator;
    private Integer timeout = null;
    public void setHTTPProxy(String host, String port){
        setProxy(host,port,Proxy.Type.HTTP);
    }
    public void setSOCKSProxy(String host, String port){
        setProxy(host,port,Proxy.Type.SOCKS);
    }
    public void authenticatorProxy(String u, String p){
        authenticator = new Authenticator() {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return (new PasswordAuthentication(u,
                    p.toCharArray()));
        }
        };
        Authenticator.setDefault(authenticator);
    }
    private void setProxy(String host, String port, Type proxytype){
        InetSocketAddress host_port = new InetSocketAddress(host, Integer.parseInt(port));
        proxy = new Proxy(proxytype, host_port);
    }
    public int GET(String link) throws IOException{
        openRequest(link,GET_METHOD);
        return send();
    }
    public int POST(String link,String postdata) throws IOException{
        openRequest(link,POST_METHOD);
        DataOutputStream outdata = new DataOutputStream(urlcon.getOutputStream());
        outdata.writeBytes(postdata);
        outdata.flush();
        return send();
    }
    
    public int uploadFile(String link, String charset, 
            HashMap<String,String> fields_name,
            HashMap<String,String> fields_file){
        try {
            String Boundary = "---" + System.currentTimeMillis();
            StringBuilder data = new StringBuilder();
            addHeaderRequest("Content-Type","multipart/form-data; boundary="+Boundary);
            openRequest(link,POST_METHOD);
            OutputStream outputStream = urlcon.getOutputStream();
            DataOutputStream outdata = new DataOutputStream(outputStream);
            
            for(Map.Entry<String, String> field : fields_name.entrySet())
                data.append("--"+Boundary).append(CRLF)
                .append("Content-Disposition: form-data; name=\""+field.getKey()+"\"").append(CRLF)
                .append("Content-Type: text/plain; charset=" + charset).append(CRLF)
                .append(CRLF)
                .append(field.getValue()).append(CRLF);
            outdata.writeBytes(data.toString());
            outdata.flush();
            
            for(Map.Entry<String, String> field : fields_file.entrySet()){
                File fileupload = new File(field.getValue());
                String filename = fileupload.getName();
                data.append("--"+Boundary).append(CRLF) 
                .append("Content-Disposition: form-data; name=\""+field.getKey()+"\"; filename=\""+filename+"\"").append(CRLF)
                .append("Content-Type: "+URLConnection.guessContentTypeFromName(filename)).append(CRLF)
                .append(CRLF);
                outdata.writeBytes(data.toString());
                outdata.flush();
            //-------------------Writing file into datapost---------------------
            FileInputStream inputStream = new FileInputStream(fileupload);
            byte[] buffer = new byte[4096];
            int byteread = -1;
            while((byteread = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, byteread);
                }
            outputStream.flush();
            
            outdata.writeBytes(CRLF);
            outdata.flush();
        }
        outdata.writeBytes(CRLF+"--"+Boundary+CRLF);
        outdata.flush();
        
        outputStream.close();    
        outdata.close();    
        return send();
        } catch (IOException ex) {
            Logger.getLogger(myHTTP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    public void setTimeout(Integer ms){
        timeout = ms;
    }
    public void setUserAgent(String HttpRquestUserAgent){
        this.HttpRquestUserAgent = HttpRquestUserAgent;
    }
    public String getUserAgent(){
        return HttpRquestUserAgent;
    }
    public void setHeadersRequest(HashMap<String, String> HttpRequestHeaders){
        this.HttpRequestHeaders.clear();
        this.HttpRequestHeaders.putAll(HttpRequestHeaders);
    }
    public void addHeaderRequest(String key, String value){
        this.HttpRequestHeaders.put(key,value);
    }
    public String getHeadersResponse(){
        Map<String, List<String>> headers = urlcon.getHeaderFields();
            String headersResponse = null;
            for(Map.Entry<String, List<String>> header : headers.entrySet())
                headersResponse = headersResponse + header.getKey() + ":" 
                                                    + header.getValue()+ "\n";
            return headersResponse;
    }
    public String getHeadersResponse(String key){
            return "["+urlcon.getHeaderField(key)+"]";
    }
    private int send() throws IOException{
            //-------------Response-----------------------------------------------------
            int responseCode = urlcon.getResponseCode();
            //print("Reponse Code: "+responseCode);
            in = urlcon.getInputStream();
            return responseCode;
    }
    private void openRequest(String link, String method){
        try {
            //-------------------------------------------------------------------------
            URL url = new URL(link);
            URI uri = new URI(link);
            //-------------Open request------------------------------------------------
            String protocol = uri.getScheme();
            switch (protocol) {
                case "http":
                    {
                        urlcon = (HttpURLConnection) url.openConnection(proxy);
                        break;
                    }
                case "https":
                    {
                        urlcon = (HttpsURLConnection)url.openConnection(proxy);
                        break;
                    }
                default:
                    return;
            }
                //-------------Method-----------------------------------------------------
                switch (method) {
                case "GET":
                    {
                        urlcon.setDoInput(true);
                        break;
                    }
                case "POST":
                case "PUT":
                    {
                        urlcon.setDoOutput(true);
                        break;
                    }
                default:
                    return;
            }
                if(timeout!=null){
                    urlcon.setReadTimeout(timeout);
                    urlcon.setConnectTimeout(timeout);
                }
            urlcon.setRequestMethod(method);
            urlcon.setUseCaches(false);
            //-------------Headers-----------------------------------------------------
            
            String host = uri.getHost();
            urlcon.setRequestProperty("Host", host);
            urlcon.setRequestProperty("User-Agent", HttpRquestUserAgent);

            for(Map.Entry<String, String> header : HttpRequestHeaders.entrySet())
                urlcon.setRequestProperty(header.getKey(),header.getValue());
            
        } catch (URISyntaxException | MalformedURLException  ex) {
            System.err.print(myHTTP.class.getName()+" request : "+ex);
        } catch (IOException ex) {
            System.err.print(myHTTP.class.getName()+" request : "+ex);
        }
    }
    public String getContentPage() throws IOException{
        InputStream in = urlcon.getInputStream();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while((line = buffer.readLine())!=null)
            sb.append(line);
        
        return sb.toString();
        
    }
    public void writetofile(String path, boolean bytefile){
    
        print("Writing to file : "+path);
        try{
            File f = new File(path);
            if(!f.exists()){
                File dir = new File(f.getParent());
                if(!dir.exists()) 
                    dir.mkdirs();
                
                f.createNewFile();
            }
            FileOutputStream fout = new FileOutputStream(f);

            if(bytefile){
                byte[] b = new byte[1024];
                int c;
                while((c = in.read(b)) != -1)
                    fout.write(b,0, c);
            }else{
                OutputStreamWriter out = new OutputStreamWriter(fout,"UTF-8");
                BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
                String inline;
                while((inline = buffer.readLine()) != null){
                    out.write(inline);
                    out.flush();
                }
                out.close();
                buffer.close();
            
            }
            fout.close();
        
        } catch (IOException | NullPointerException ex ) {
            System.err.print(myHTTP.class.getName()+" writetofile : "+ex);
        }
    }
    public static void print(String msg){
        System.out.println(msg);
    }
    public static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }
}
