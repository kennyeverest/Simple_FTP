/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp2;

/**
 *
 * @author Kenny
 */
public class HostNameCheck {
    private String hostName;
    public HostNameCheck(String hostName){
        this.hostName = hostName;
    }
    
    public boolean isIpAddress(){
        String tmp[] = this.hostName.split(".");
        if(tmp.length==32)
            return true;
        else
            return false;
    }
    
}
