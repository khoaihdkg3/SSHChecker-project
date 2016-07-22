/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHChecker;

import java.util.prefs.Preferences;

/**
 *
 * @author Administrator
 */
public class SSHCheckerPreferences {
    private Preferences JFramePrefs = Preferences.userRoot().node("JFrame"); 
    private Preferences JTableSettingPrefs = Preferences.userRoot().node("JTableSetting");
    public int JFramePrefsW, JFramePrefsH;
    public int JFramePrefsDivider1, JFramePrefsDivider2;
    public String JTableSettingRCWhen;
    public int JTableSettingTimeout,JTableSettingRCCount,JTableSettingBLCTCount,
            JTableSettingLCTCount;
    public String JTableSettingBitvisePath;
    public boolean JTableSettingIsBLC, JTableSettingIsLC, JTableSettingIsFC;
    
    public SSHCheckerPreferences(){
        JFramePrefsW = JFramePrefs.getInt("W", 600);
        JFramePrefsH = JFramePrefs.getInt("H", 550);
        JFramePrefsDivider1 = JFramePrefs.getInt("Divider1", 300);
        JFramePrefsDivider2 = JFramePrefs.getInt("Divider2", 270);
        
        JTableSettingRCWhen = JTableSettingPrefs.get("RCWhen", "not-using");
        
        JTableSettingTimeout = JTableSettingPrefs.getInt("Timeout", 30000);
        JTableSettingRCCount = JTableSettingPrefs.getInt("RCCount", 5);
        JTableSettingBLCTCount = JTableSettingPrefs.getInt("BLCTCount", 10);
        JTableSettingLCTCount = JTableSettingPrefs.getInt("LCTCount", 10);
        JTableSettingBitvisePath = JTableSettingPrefs.get("BitvisePath","C:\\BvSsh.exe");
        
        JTableSettingIsBLC = JTableSettingPrefs.getBoolean("IsBLC", true);
        JTableSettingIsLC = JTableSettingPrefs.getBoolean("IsLC", true);
        JTableSettingIsFC = JTableSettingPrefs.getBoolean("IsFC", true);
    }
    public void setJFramePrefs(int W, int H, int dvder1, int dvder2){
        JFramePrefs.putInt("W", W);
        JFramePrefs.putInt("H", H);
        JFramePrefs.putInt("Divider1", dvder1);
        JFramePrefs.putInt("Divider2", dvder2);
        
    }
    
    public void setJTableSettingPrefs(int timeout, String rcwhen, int rccount, int blctcount, int lctcount,
            String bitvisepath, boolean isblc, boolean islc, boolean isfc){
        JTableSettingPrefs.putInt("Timeout", timeout);
        JTableSettingPrefs.put("RCWhen", rcwhen);
        JTableSettingPrefs.putInt("RCCount", rccount);
        JTableSettingPrefs.putInt("BLCTCount", blctcount);
        JTableSettingPrefs.putInt("LCTCount", lctcount);
        JTableSettingPrefs.put("BitvisePath", bitvisepath);
        
        JTableSettingPrefs.putBoolean("IsBLC", isblc);
        JTableSettingPrefs.putBoolean("IsLC", islc);
        JTableSettingPrefs.putBoolean("IsFC", isfc);
    }
}
