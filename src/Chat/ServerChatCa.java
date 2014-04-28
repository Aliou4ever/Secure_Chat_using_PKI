/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import CAclient.CAclient;
import ProtocolChat.ChatSecure;
import ProtocolChat.ObjectPassing;
import data_base.MySQL_DB;
import java.awt.TextField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
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
                ObjectPassing obj=null;
                try {
                    //A -> B: {Na, login_a}KB
                    obj =(ObjectPassing) ios.readObject();                    
                } catch (ClassNotFoundException ex) {
                    System.err.println(ex.toString());
                }
                //B -> A: {Nb, h(Na)}KA
                BigInteger nonceB = new BigInteger(String.valueOf(System.currentTimeMillis()));
                MySQL_DB db = new MySQL_DB(ca.getBd_url(), ca.getBd_login(), ca.getBd_pass());
                ObjectPassing obj2= ChatSecure.BtoA_1(obj, nonceB, ca.getCa_PrivateKey(), db);
                oos.writeObject(obj2);
                //A -> B: {h(Nb), keySession}KB
                ObjectPassing obj3=null;
                try {
                    //A -> B: {Na, login_a}KB
                    obj3 =(ObjectPassing) ios.readObject();                    
                } catch (ClassNotFoundException ex) {
                    System.err.println(ex.toString());
                }
                Key sessionKey = ChatSecure.BgetSessionKey(obj3, nonceB, ca.getCa_PrivateKey());
                Thread t1 = new Thread(new ServerChatReceiver(ca,chatGui.getjTextArea1(),ios,sessionKey));
                t1.start();
                Thread t2 = new Thread(new ServerChatSender(ca,chatGui.getjTextField1(), 
                        chatGui.getjTextArea1(), oos,chatGui.getBtn_Send(), sessionKey ));
                t2.start();              
            }
        } catch (IOException ex) {
            System.err.println("erruer serveur Ca :"+ex);
        }
        
    }
    
    
}
