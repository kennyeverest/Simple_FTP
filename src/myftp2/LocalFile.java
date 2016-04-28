/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp2;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Kenny
 */
public class LocalFile {
 /**
  * 
  * @param directoryName 
  */
    public void listFilesAndFolders(String directoryName){
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList){
            System.out.println(file.getName());
        }
    }
 /**
  * 
  * @param directoryName
  * @return 
  */   
    public ArrayList<FileNode> getLocalFiles(String directoryName){
        File directory = new File(directoryName);
        //get all the files from a directory
        ArrayList<FileNode> data = new ArrayList<>();
        File[] fList = directory.listFiles();
        for (File file : fList){
            //System.out.println(file.getName());
            if(file.isDirectory()){
                data.add(new FileNode(file.getName(),"dir"));
            }
            else{
                data.add(new FileNode(file.getName(),"file"));
            }
        }
        return data;
    }
    
    public ArrayList<String> getDriveLetter(){
        //File directory = new File(directoryName);
        //get all the files from a directory
        ArrayList<String> data = new ArrayList<>();
        File[] fList = File.listRoots();
        for (File file : fList){
            //System.out.println(file.getName());
            data.add(file.toString());
            //System.out.println(file);
        }
        //System.out.println(data.size());
        return data;
    }

}
