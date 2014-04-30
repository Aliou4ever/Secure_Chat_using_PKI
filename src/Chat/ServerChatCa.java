/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import ProtocolChat.ChatSecure;
import ProtocolChat.ObjectPassing;
import Utils.MySQL_DB;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
public class ServerChatCa extends Thread{
    CA ca;
    int port;
    ServerSocket s;

    public ServerChatCa(CA ca, int port) {
        try {
            this.ca = ca;            
            s = new ServerSocket(port);
            this.port = s.getLocalPort();
            MySQL_DB db = new MySQL_DB(ca.getBD_url(), ca.getBD_login(), ca.getBD_pass());
            db.connexion();
            db.updatChatPort(ca.getCa_login(), s.getLocalPort());
            System.out.println("serverCaht: "+ca.ca_login+" port: "+port+" s_port: "+s.getLocalPort());
            db.deconnexion();
        } catch (IOException ex) {
            System.err.println("erruer serveur chat"+ex.toString());
        }
    }
    
    public void run(){
        try {
            
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
                MySQL_DB db = new MySQL_DB(ca.BD_url, ca.BD_login, ca.BD_pass);
                ObjectPassing obj2= ChatSecure.BtoA_1(obj, nonceB, ca.getCaKeyPair().getPrivate(), db);
                oos.writeObject(obj2);
                //A -> B: {h(Nb), keySession}KB
                ObjectPassing obj3=null;
                try {
                    //A -> B: {Na, login_a}KB
                    obj3 =(ObjectPassing) ios.readObject();                    
                } catch (ClassNotFoundException ex) {
                    System.err.println(ex.toString());
                }
                Key sessionKey = ChatSecure.BgetSessionKey(obj3, nonceB, ca.getCaKeyPair().getPrivate());
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
