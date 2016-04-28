/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp2;
import java.io.IOException;
import javax.swing.*;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
/**
 *
 * @author Kenny
 */
public class UI extends JFrame{
    
    public UI() throws IOException, IllegalArgumentException, InterruptedException, InvalidTelnetOptionException{
        String ip = JOptionPane.showInputDialog("Input IP Address");
        String port = JOptionPane.showInputDialog("Input Port Number (ex:23)");
        
        String []args = new String[2];
        args[0] = ip;
        args[1] = port;
        if(!new CheckAddress().isValid(args)){
            System.out.println("Error");
        }
        else{
        
        Coba.main(args);
        }
    }
}
