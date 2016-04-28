/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author Kenny
 */
public class MyFTP2 {
//
//    /**
//     * @param args the command line arguments
//     */
public static void main(String[] args) throws IOException {
//        // TODO code application logic here
//
////FTPController t = new FTPController();
////t.doConnect();
////t.uploadFile("a");
////    FTPController t = new FTPController();
////    t.doConnect();
////    //t.downloadFile("/", "ftp.txt", "C:\\", "kenny.txt");
////    t.deleteDirectory("/", "test");
////    }
////String a = "/1/halo/hai";
////String b[] = a.split("/");
////
////Stack h = new Stack();
////for(String c:b){
////    h.push(c);
////}
//////while(!h.isEmpty()){
//////    System.out.println(h.pop());
//////}
////
////
////
////h.pop();
////String data[] = new String[h.size()];
////int i = data.length-1;
////while(!h.isEmpty()){
////    data[i--] = h.pop().toString();
////}
////
////
////String hasil = "";
////for(int j=0;j<data.length;j++){
////    if(j!=data.length-1){
////        hasil+=data[j]+"/";
////    }
////    else
////        hasil+=data[j];
////}
////        System.out.println(hasil);
//
//String a = "C:\\\\Android\\sdk";
//        System.out.println(a);
// String b[] = a.split("\\\\");
//        System.out.println(b.length);
// ArrayList<String> data = new ArrayList<>();
// 
// for(String c:b){
//     if(c.length()>0)
//         data.add(c);
// }
//        System.out.println(data);
//        data.remove(data.size()-1);
//        System.out.println(data);
//    }
//
//   FTPClient ftp = new FTPClient();
//        
//        ftp.connect(192.168.0.4, 21);
//        
//        //showServerReply(ftp);
//        
//        int replyCode = ftp.getReplyCode();
//        
//        if(!FTPReply.isPositiveCompletion(replyCode)){
//            System.out.println("connect failed");
//            //return false;
//        }
//        else{
//            System.out.println("connect success");
//        }
//        
//        boolean isSuccess = ftp.login(coba, );
//        
//        if(!isSuccess){
//            System.out.println("Could not login to server");
//            return false;
//        }
//        
//        else
//            return true;
//new FTPView().setVisible(true);
new MainMenu().setVisible(true);
}    
}
