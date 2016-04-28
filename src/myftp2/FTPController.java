/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author Kenny
 */
public class FTPController {
    private FTPModel x;
    private static FTPClient ftp;
    private FTPFile[] files;
    public FTPController(){
        x = new FTPModel();
    }
    private String currentWorkingDir;
    public FTPController(String hostname,String username,String password){
        x = new FTPModel(hostname,username,password);
    }
    
    public boolean doConnect() throws IOException{
        ftp = new FTPClient();
        
        ftp.connect(x.getHostname(), 21);
        
        showServerReply(ftp);
        
        int replyCode = ftp.getReplyCode();
        
        if(!FTPReply.isPositiveCompletion(replyCode)){
            System.out.println("connect failed");
            return false;
        }
        else{
            System.out.println("connect success");
        }
        
        boolean isSuccess = ftp.login(x.getUsername(), x.getPassword());
        
        if(!isSuccess){
            System.out.println("Could not login to server");
            return false;
        }
        
        else
            return true;
    }
    
    public ArrayList<FileNode> getListFiles(String path) throws IOException{
        files = ftp.listFiles(path);
        ArrayList<FileNode> data = new ArrayList<>();
        for(FTPFile file : files){
            if(file.isDirectory()){
                data.add(new FileNode(file.getName(),"dir"));
            }
            else{
                data.add(new FileNode(file.getName(),"file"));
            }
        }
        return data;
    }
    public ArrayList<FileNode> getListFiles2(String workDir) throws IOException{
        doConnect();
        boolean success = ftp.changeWorkingDirectory(workDir);
        ArrayList<FileNode> data = new ArrayList<>();
        if(success){
            this.currentWorkingDir = ftp.printWorkingDirectory();
            System.out.println("directory changed");
            files = ftp.listFiles();
            for(FTPFile file: files){
                if(file.isDirectory()){
                    data.add(new FileNode(file.getName(),"dir"));
                }
                else{
                    data.add(new FileNode(file.getName(),"file"));
                }
            }
            return data;
        }
        else{
            System.out.println("gagal");
            return null;
        }
    }
    public void berhenti() throws IOException{
        if(ftp.isConnected()){
            ftp.logout();
            ftp.disconnect();
            
        }
    }
    public String getWorkingDir(){
        return this.currentWorkingDir;
    }
    public String getCurrentDir() throws IOException{
        return ftp.printWorkingDirectory();
    }
    public boolean uploadFile(String path,String fileName) throws IOException{
        ftp.enterLocalPassiveMode();
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        File t = new File(path);
        String firstName = fileName;
        InputStream is = new FileInputStream(t);
        System.out.println("Start uploading file.....");
        boolean done = ftp.storeFile(firstName, is);
        if(!done){
            System.out.println("Failed to upload");
            return false;
        }
        else{
            System.out.println("Upload compelete");
        return true;
        }
        }
    
    public boolean uploadFile(String path, String fileName,String workingDir) throws IOException{
        boolean success = ftp.changeWorkingDirectory(workingDir);
        if(success){
            System.out.println("Berhasil diganti ke "+workingDir);
        ftp.enterLocalPassiveMode();
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        File t = new File(path);
        String firstName = fileName;
        InputStream is = new FileInputStream(t);
        System.out.println("Start uploading file.....");
        boolean done = ftp.storeFile(firstName, is);
        if(!done){
            System.out.println("Failed to upload");
            return false;
        }
        else{
            System.out.println("Upload compelete");
        return true;
        }
        }
        else
            return false;
    }
    public boolean renameFolder(String oldDir,String newDir) throws IOException{
        doConnect();
        boolean success = ftp.rename(oldDir, newDir);
        if(success){
            System.out.println("Berhasil ganti directory");
            return true;
        }
        else{
            System.out.println("Gagal ganti directory");
            return false;
        }
    }
    public boolean renameFile(String oldFile,String newFile) throws IOException{
        doConnect();
        boolean success = ftp.rename(oldFile,newFile);
        if(success){
            System.out.println("Berhasil rename file");
            return true;
            
        }
        else{
            System.out.println("Gagal rename file");
            return false;
        }
    }
    public boolean downloadFile(String remoteDir,String remoteFName,String targetDir,String targetFName) throws IOException{
        doConnect();
        String tmp = targetDir + "\\"+targetFName;
        System.out.println(tmp);
        String tmp2 = remoteDir+"/"+remoteFName;
        OutputStream ous = new FileOutputStream(tmp);
        boolean success = ftp.retrieveFile(tmp2, ous);
        if(success){
            System.out.println("Berhasil download file");
            return true;
        }
        else{
            System.out.println("Gagal download file");
            return false;
        }
            
    }
    public boolean createDirectory(String path, String dirName) throws IOException{
        boolean isCreated = ftp.makeDirectory(path+"/"+dirName);
        System.out.println("Start Making Directory");
        if(isCreated){
            System.out.println("Sukses");
            return true;
        }
        else{
            System.out.println("Gagal");
            return false;
        }
    }
    public boolean deleteDirectory(String path,String dirName) throws IOException{
        boolean isDeleted = ftp.removeDirectory(path+"/"+dirName);
        System.out.println("Start Deleting Directory");
        if(isDeleted){
            System.out.println("Sukses");
            return true;
        }
        else{
            //System.out.println("Dir is not empty,Not supported");
            removeDirectory(ftp,path+"/"+dirName,"");
            return false;
        }
    }
     public void removeDirectory(FTPClient ftpClient, String parentDir,
            String currentDir) throws IOException {
        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }
 
        FTPFile[] subFiles = ftpClient.listFiles(dirToList);
 
        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (currentFileName.equals(".") || currentFileName.equals("..")) {
                    // skip parent directory and the directory itself
                    continue;
                }
                String filePath = parentDir + "/" + currentDir + "/"
                        + currentFileName;
                if (currentDir.equals("")) {
                    filePath = parentDir + "/" + currentFileName;
                }
 
                if (aFile.isDirectory()) {
                    // remove the sub directory
                    removeDirectory(ftpClient, dirToList, currentFileName);
                } else {
                    // delete the file
                    boolean deleted = ftpClient.deleteFile(filePath);
                    if (deleted) {
                        System.out.println("DELETED the file: " + filePath);
                    } else {
                        System.out.println("CANNOT delete the file: "
                                + filePath);
                    }
                }
            }
 
            // finally, remove the directory itself
            boolean removed = ftpClient.removeDirectory(dirToList);
            if (removed) {
                System.out.println("REMOVED the directory: " + dirToList);
            } else {
                System.out.println("CANNOT remove the directory: " + dirToList);
            }
        }
    }
    public boolean deleteFile(String path,String fileName) throws IOException{
        boolean isDeleted = ftp.deleteFile(path+"/"+fileName);
        System.out.println("Start Deleting File");
        if(isDeleted){
            System.out.println("Sukses");
            return true;
        }
        else{
            System.out.println("Gagal");
            return false;
        }
    }
    public void showServerReply(FTPClient ftp){
        String[] replies = ftp.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }
}
