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
public class FileNode {
    
    private String name,type;
    
    public FileNode(String name,String type){
        this.name = name;
        this.type = type;
    }
    
    public String getName(){
        return name;
    }
    
    public String getType(){
        return type;
    }
}
