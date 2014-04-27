/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import CAclient.CAclient;
import java.awt.TextField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author khaled
 */
public class ServerChatCa {
    CAclient ca;
    int port;

    public ServerChatCa(CAclient ca, JTextField txtToSend, JTextArea txtToaffich, int port) {
        this.ca = ca;
        this.port = port;
    }
    
    public void run(){
        try {
            ServerSocket s = new ServerSocket(port);
            while(true){
                Socket socket = s.accept();
                ChatGUI chatGui = new ChatGUI();
                chatGui.setVisible(true);
                chatGui.setLocationRelativeTo(null);
                ObjectOutputStream  oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ios = new ObjectInputStream(socket.getInputStream());
                Thread t1 = new Thread(new ServerChatReceiver(ca,chatGui.getjTextArea1(),ios));
                t1.start();
                Thread t2 = new Thread(new ServerChatSender(ca,chatGui.getjTextField1(), chatGui.getjTextArea1(), oos,chatGui.getBtn_Send() ));
                t2.start();              
            }
        } catch (IOException ex) {
            System.err.println("erruer serveur Ca :"+ex);
        }
        
    }
    
    
}
