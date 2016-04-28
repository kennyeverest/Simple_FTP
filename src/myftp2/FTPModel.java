/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp2;

import java.util.HashMap;

/**
 *
 * @author Kenny
 */
public class FTPModel {
    private static HashMap<String,String> x;
    
    public FTPModel(){
       x = new HashMap();
       
       x.put("hostname", "192.168.43.184");
       x.put("username", "FTP-User");
       x.put("password", "1234");
    }
    
    public FTPModel(String hostname,String username,String password){
        x = new HashMap();
        x.put("hostname", hostname);
        x.put("username", username);
        x.put("password", password);
    }
    
    public HashMap getLoginInfo(){
        return x;
    }
    
    public String getHostname(){
        return x.get("hostname");
    }
    public String getUsername(){
        return x.get("username");
    }
    public String getPassword(){
        return x.get("password");
    }
}
