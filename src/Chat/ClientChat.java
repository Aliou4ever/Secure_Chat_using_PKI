/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import ProtocolChat.ChatSecure;
import ProtocolChat.ObjectPassing;
import Utils.Keys;
import Utils.MyCipher;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.System.exit;
import java.math.BigInteger;
import java.net.Socket;
import java.security.Key;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author khaled
 */
public class ClientChat extends Thread{
    CA ca;
    int port ;
    String url;
    PublicKey pKeyOf_correspondent;
    BigInteger nonceA;
    Key sessionKey;

    public ClientChat(CA ca, int port, String url, PublicKey pKeyOf_correspondent) {
        this.ca = ca;
        this.port = port;
        this.url = url;
        this.pKeyOf_correspondent = pKeyOf_correspondent;
        this.nonceA = new BigInteger(String.valueOf(System.currentTimeMillis()));
        this.sessionKey = Keys.generateSessionKey();
    }
    
    public void run(){
        try {
            Socket so = new Socket(url, port);
            ObjectOutputStream  oos = new ObjectOutputStream(so.getOutputStream());
            ObjectInputStream ios = new ObjectInputStream(so.getInputStream());            
            //protocol chat
            // A -> B: {Na, login_a}KB
            ObjectPassing obj= ChatSecure.AtoB_1(nonceA, ca.ca_login, pKeyOf_correspondent);
            oos.writeObject(obj);
             //B -> A: {Nb, h(Na)}KA
            ObjectPassing obj2 = null;
            try {               
                obj2 = (ObjectPassing) ios.readObject();
            } catch (ClassNotFoundException ex) {
               System.err.println(ex.toString()); 
            }
            // A -> B: {h(Nb), keySession}KB
            ObjectPassing obj3= ChatSecure.AtoB_2(obj2, nonceA, pKeyOf_correspondent, ca.caKeyPair.getPrivate(), sessionKey);
            oos.writeObject(obj3);
            ChatGUI chatGui = new ChatGUI();
            chatGui.setVisible(true);
            chatGui.setTitle(ca.ca_login);
            chatGui.setLocationRelativeTo(null);
            chatGui.getjTextArea1().setEditable(false);
            ClientChatSender sender = new ClientChatSender(ca,chatGui.getjTextField1(), 
                            chatGui.getjTextArea1(), oos,chatGui.getBtn_Send(), sessionKey);
            sender.start();
            clientReciever(ios, chatGui.getjTextArea1());
            
        } catch (IOException ex) {
            System.err.println("erreur connexion au serveur de chat :"+ex);
        }
    
    }
    public void clientReciever(ObjectInputStream ios, JTextArea txtToaffich){
        while(true){
            try {
                ObjectPassing s =(ObjectPassing) ios.readObject();
                if(s == null){
                    ios.close();
                    break;
                }
                String msg = new String( MyCipher.desDecrypt(s.objDataArray, sessionKey)); 
                txtToaffich.setEditable(true);
                txtToaffich.setText(txtToaffich.getText()+"\n"+msg);
                txtToaffich.setEditable(false);
            } catch (IOException ex) {
                System.err.println("erreur de reception client chat "+ex.toString());
            } catch (ClassNotFoundException ex) {
                System.err.println("erreur de reception client chat "+ex.toString());
            }
        
        }
    }

    private static class ClientChatSender extends Thread {
        CA ca;
        JTextField jTextField1;
        JTextArea jTextArea1; 
        ObjectOutputStream oos; 
        JButton btn_Send;
        Key sessionKey;
        public ClientChatSender(CA ca, JTextField jTextField1, JTextArea jTextArea1, ObjectOutputStream oos, JButton btn_Send, Key sessionKey) {
            this.ca = ca;
            this.jTextField1 = jTextField1;
            this.jTextArea1 = jTextArea1;
            this.oos = oos;
            this.btn_Send = btn_Send;
            this.sessionKey = sessionKey;
        }

        public void run() {
            btn_Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               if(!jTextField1.getText().equals("")){
                   try {
                        //crepter le message pui l'envoyer
                       String tosend = ca.ca_login+" :";
                       tosend   = tosend + jTextField1.getText();
                       jTextField1.setText("");
                       jTextArea1.setEditable(true);
                       jTextArea1.setText(jTextArea1.getText()+"\n"+tosend);
                       jTextArea1.setEditable(false);
                       byte [] encryptedmsg = MyCipher.desEncrypt(tosend.getBytes(), sessionKey);
                       ObjectPassing s = new ObjectPassing(encryptedmsg);
                       oos.writeObject(s);  
                   } catch (IOException ex) {
                       System.err.println("l'envoi de message a echoué! "+ex.toString());
                   }
               }            
                
            }            
        });
        }
    }
    
    
    
}
