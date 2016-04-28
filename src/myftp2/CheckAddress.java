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
public class CheckAddress implements CheckInterface{

    @Override
    public boolean isValid(Object x) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       String []a = (String[])x;
       return !a[0].equals("") && !a[1].equals("");
    }
    
}
