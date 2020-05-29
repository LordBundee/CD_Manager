/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdmanager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Troy
 */
public class ChatClient
{
     private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread client = null;
    private String serverName = "localhost";
    private int serverPort = 4444;
    private IChatHost myHost;
    
    
    
    
    public ChatClient()
    {
        //getParameters();
    }
    
    
     public boolean connect(String serverName, int serverPort, IChatHost host)
    {
        myHost = host;
        println("Establishing connection. Please wait ...");
        try
        {
            
            socket = new Socket(serverName, serverPort);
            println("Connected: " + socket);
            open();
            return true;
        }
        catch (UnknownHostException uhe)
        {
            println("Host unknown: " + uhe.getMessage());
            return false;
        }
        catch (IOException ioe)
        {
            println("Unexpected exception: " + ioe.getMessage());
            return false;
        }
    }

    public void send(String message)
    {
        try
        {
            streamOut.writeUTF(message);
            streamOut.flush();
        }
        catch (IOException ioe)
        {
            println("Sending error: " + ioe.getMessage());
            close();
        }
    }

    public void handle(String msg)
    {
        if(msg.contains(Integer.toString(socket.getLocalPort()))) 
        {
             return;
        }
            
        if (msg.equals(".bye"))
        {
            println("Good bye. Press EXIT button to exit ...");
            close();
        }
        else
        {
            println(msg);
            myHost.HandleReply(msg);
        }
    }

    public void open()
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            client = new ChatClientThread(this, socket);
        }
        catch (IOException ioe)
        {
            println("Error opening output stream: " + ioe);
        }
    }

    public void close()
    {
        try
        {
            if (streamOut != null)
            {
                streamOut.close();
            }
            if (socket != null)
            {
                socket.close();
            }
        }
        catch (IOException ioe)
        {
            println("Error closing ...");
        }
        client.close();
        client.stop();
    }

    void println(String msg)
    {
        //display.appendText(msg + "\n");
        //lblMessage.setText(msg);
        System.out.println(msg);
    }

    public void getParameters()
    {
//        serverName = getParameter("host");
//        serverPort = Integer.parseInt(getParameter("port"));
        
        //serverName = "localhost";
        //serverPort = 4444;        
    }
}
