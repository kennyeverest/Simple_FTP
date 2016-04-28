/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;
import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.SimpleOptionHandler;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetNotificationHandler;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;

/**
 *
 * @author Kenny
 */
public class Coba implements Runnable,TelnetNotificationHandler{
    private static TelnetClient tc;
    private static byte []buff;
     public static void main(String[] args) throws IOException, IllegalArgumentException, InterruptedException, InvalidTelnetOptionException  {
         //args = new String[2];
   
    
        FileOutputStream fout = null;

        
        String remoteip = args[0];

        int remoteport;

        
            remoteport = (new Integer(args[1])).intValue();
        
        

        try
        {
            fout = new FileOutputStream ("spy.log", true);
        }
        catch (IOException e)
        {
            System.err.println(
                "Exception while opening the spy file: "
                + e.getMessage());
        }

        tc = new TelnetClient();

      setHandler();

        

        while (true)
        {
            boolean end_loop = false;
            try
            {
                tc.connect(remoteip, remoteport);


                Thread reader = new Thread (new Coba());
                
                tc.registerNotifHandler(new Coba());
                
                showStandardCommand();
                
                reader.start();
                OutputStream outstr = tc.getOutputStream();

                 buff = new byte[1024];
                int ret_read = 0;

                do
                {
                    try
                    {
                        ret_read = System.in.read(buff);
                        if(ret_read > 0)
                        {
                            final String line = new String(buff, 0, ret_read); // deliberate use of default charset
                            if(line.startsWith("AYT"))
                            {
                                AYT();
                            }
                            else if(line.startsWith("OPT"))
                            {
                                 OPT();
                            }
                            else if(line.startsWith("REGISTER"))
                            {
                                register();
                            }
                            else if(line.startsWith("UNREGISTER"))
                            {
                                unRegister();
                            }
                            else if(line.startsWith("SPY"))
                            {
                                tc.registerSpyStream(fout);
                            }
                            else if(line.startsWith("UNSPY"))
                            {
                                tc.stopSpyStream();
                            }
                            else if(line.matches("^\\^[A-Z^]\\r?\\n?$"))
                            {
                                byte toSend = buff[1];
                                if (toSend == '^') {
                                    outstr.write(toSend);
                                } else {
                                    outstr.write(toSend - 'A' + 1);
                                }
                                outstr.flush();
                            }
                            else
                            {
                                try
                                {
                                        outstr.write(buff, 0 , ret_read);
                                        
                                        outstr.flush();
                                }
                                catch (IOException e)
                                {
                                        end_loop = true;
                                }
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        System.err.println("Exception while reading keyboard:" + e.getMessage());
                        end_loop = true;
                    }
                }
                while((ret_read > 0) && (end_loop == false));

                try
                {
                    tc.disconnect();
                }
                catch (IOException e)
                {
                          System.err.println("Exception while connecting:" + e.getMessage());
                }
            }
            catch (IOException e)
            {
                    System.err.println("Exception while connecting:" + e.getMessage());
                    System.exit(1);
            }
        }
     }
     public static  void setHandler() throws InvalidTelnetOptionException, IOException{
           TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("cmd", false, false, true, false);
        
        EchoOptionHandler echoopt = new EchoOptionHandler(true, false, true, false);
        
        SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true, true, true, true);
         tc.addOptionHandler(ttopt);
            tc.addOptionHandler(echoopt);
            tc.addOptionHandler(gaopt);
     }
     public static void AYT() throws IllegalArgumentException, InterruptedException{
         try
                                {
                                    System.out.println("Sending AYT");

                                    System.out.println("AYT response:" + tc.sendAYT(5000));
                                }
                                catch (IOException e)
                                {
                                    System.err.println("Exception waiting AYT response: " + e.getMessage());
                                }
     }
     public static void OPT(){
         System.out.println("Status of options:");
                                 for(int ii=0; ii<25; ii++) {
                                     System.out.println("Local Option " + ii + ":" + tc.getLocalOptionState(ii) +
                                                        " Remote Option " + ii + ":" + tc.getRemoteOptionState(ii));
                                 }
     }
     
     public static void register(){
         StringTokenizer st = new StringTokenizer(new String(buff));
                                try
                                {
                                    st.nextToken();
                                    int opcode = Integer.parseInt(st.nextToken());
                                    boolean initlocal = Boolean.parseBoolean(st.nextToken());
                                    boolean initremote = Boolean.parseBoolean(st.nextToken());
                                    boolean acceptlocal = Boolean.parseBoolean(st.nextToken());
                                    boolean acceptremote = Boolean.parseBoolean(st.nextToken());
                                    SimpleOptionHandler opthand = new SimpleOptionHandler(opcode, initlocal, initremote,
                                                                    acceptlocal, acceptremote);
                                    tc.addOptionHandler(opthand);
                                }
                                catch (Exception e)
                                {
                                    if(e instanceof InvalidTelnetOptionException)
                                    {
                                        System.err.println("Error registering option: " + e.getMessage());
                                    }
                                    else
                                    {
                                        System.err.println("Invalid REGISTER command.");
                                        System.err.println("Use REGISTER optcode initlocal initremote acceptlocal acceptremote");
                                        System.err.println("(optcode is an integer.)");
                                        System.err.println("(initlocal, initremote, acceptlocal, acceptremote are boolean)");
                                    }
                                }
     }
     
     public static void unRegister(){
         StringTokenizer st = new StringTokenizer(new String(buff));
                                try
                                {
                                    st.nextToken();
                                    int opcode = (new Integer(st.nextToken())).intValue();
                                    tc.deleteOptionHandler(opcode);
                                }
                                catch (Exception e)
                                {
                                    if(e instanceof InvalidTelnetOptionException)
                                    {
                                        System.err.println("Error unregistering option: " + e.getMessage());
                                    }
                                    else
                                    {
                                        System.err.println("Invalid UNREGISTER command.");
                                        System.err.println("Use UNREGISTER optcode");
                                        System.err.println("(optcode is an integer)");
                                    }
                                }
     }
     public static void showStandardCommand(){
         System.out.println("TelnetClientExample");
                System.out.println("Type AYT to send an AYT telnet command");
                System.out.println("Type OPT to print a report of status of options (0-24)");
                System.out.println("Type REGISTER to register a new SimpleOptionHandler");
                System.out.println("Type UNREGISTER to unregister an OptionHandler");
                System.out.println("Type SPY to register the spy (connect to port 3333 to spy)");
                System.out.println("Type UNSPY to stop spying the connection");
                System.out.println("Type ^[A-Z] to send the control character; use ^^ to send ^");

     }
     public void receivedNegotiation(int negotiation_code, int option_code)
    {
        String command = null;
        switch (negotiation_code) {
            case TelnetNotificationHandler.RECEIVED_DO:
                command = "DO";
                break;
            case TelnetNotificationHandler.RECEIVED_DONT:
                command = "DONT";
                break;
            case TelnetNotificationHandler.RECEIVED_WILL:
                command = "WILL";
                break;
            case TelnetNotificationHandler.RECEIVED_WONT:
                command = "WONT";
                break;
            case TelnetNotificationHandler.RECEIVED_COMMAND:
                command = "COMMAND";
                break;
            default:
                command = Integer.toString(negotiation_code); // Should not happen
                break;
        }
        System.out.println("Received " + command + " for option code " + option_code);
   }

    /***
     * Reader thread.
     * Reads lines from the TelnetClient and echoes them
     * on the screen.
     ***/
//    @Override
    public void run()
    {
        InputStream instr = tc.getInputStream();

        try
        {
            byte[] buff = new byte[1024];
            int ret_read = 0;

            do
            {
                ret_read = instr.read(buff);
                if(ret_read > 0)
                {
                    System.out.print(new String(buff, 0, ret_read));
                }
            }
            while (ret_read >= 0);
        }
        catch (IOException e)
        {
            System.err.println("Exception while reading socket:" + e.getMessage());
        }

        try
        {
            tc.disconnect();
        }
        catch (IOException e)
        {
            System.err.println("Exception while closing telnet:" + e.getMessage());
        }
    }
}
