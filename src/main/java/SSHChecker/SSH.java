/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHChecker;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class SSH implements Serializable{
    private String host, username, password, country, status = "Waiting";
    private int index;
    private long time = 0;
    private float BLPercent = -3;
    private boolean isFresh = false;
    public SSH(){
        
    }
    public SSH(String host, String username, String password, String country){
        this.host = host;
        this.username = username;
        this.password = password;
        this.country = country;
    }
    
    public String getHost(){
        return host;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getCountry(){
        return country;
    }
    public String getStatus(){
        return status;
    }
    public int getIndex(){
        return index;
    }
    public float getBLPercent(){
        return BLPercent;
    }
    public boolean getIsFresh(){
        return isFresh;
    }
    public long getTime(){
        return time;
    }
    public void setHost(String host){
        this.host = host;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setIndex(int index){
        this.index = index;
    }
    public void setBLPercent(float p){
        this.BLPercent = p;
    }
    public void setIsFresh(boolean is){
        this.isFresh = is;
    }
    public void setTime(long time){
        this.time = time;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getStatus()).append("|");
        sb.append(getHost()).append("|");
        sb.append(getUsername()).append("|");
        sb.append(getPassword()).append("|");
        sb.append(getCountry()).append("|");
        sb.append(getBLPercent()).append("|");
        sb.append((getIsFresh()?"Fresh":"NotFresh")).append("|");
        sb.append(getTime()+"ms");
        return sb.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
