/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp2;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Kenny
 */
public class StringProc {
    private String path;
    
    public StringProc(String path){
        this.path = path;
    }
    
    public String getPrevPathftp(){
        String pecah[] = path.split("/");

Stack h = new Stack();

for(String c:pecah){
    h.push(c);
}
h.pop();
String data[] = new String[h.size()];
int i = data.length-1;
while(!h.isEmpty()){
    data[i--] = h.pop().toString();
}


String hasil = "";
for(int j=0;j<data.length;j++){
    if(j!=data.length-1){
        hasil+=data[j]+"/";
    }
    else
        hasil+=data[j];
}
return hasil;
    }
    public String getPrevLocalFile(){
     String b[] = this.path.split("\\\\");
        System.out.println(b.length);
 ArrayList<String> data = new ArrayList<>();
 
 for(String c:b){
     if(c.length()>0)
         data.add(c);
 }
 data.remove(data.size()-1);
 String hasil = "";
 for(int i=0;i<data.size();i++){
     if(i==0){
         hasil+=data.get(i)+"\\\\";
     }
     else if(i!=data.size()-1){
         hasil+=data.get(i)+"\\";
     }
     else{
         hasil+=data.get(i);
     }
 }
 return hasil;
    }
}
