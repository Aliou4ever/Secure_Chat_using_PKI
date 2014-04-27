/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import CAclient.CAclient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
public class ClientChat {
    CAclient ca;
    int port ;
    String url;
    PublicKey pKeyOf_correspondent;

    public ClientChat(CAclient ca, int port, String url, PublicKey pKeyOf_correspondent) {
        this.ca = ca;
        this.port = port;
        this.url = url;
        this.pKeyOf_correspondent = pKeyOf_correspondent;
    }
    
    public void start(){
        try {
            Socket so = new Socket(url, port);
            ObjectOutputStream  oos = new ObjectOutputStream(so.getOutputStream());
            ObjectInputStream ios = new ObjectInputStream(so.getInputStream());
            ChatGUI chatGui = new ChatGUI();
            chatGui.setVisible(true);
            chatGui.setLocationRelativeTo(null);
            Thread t2 = new Thread(
                    new ClientChatSender(ca,chatGui.getjTextField1(), 
                            chatGui.getjTextArea1(), oos,chatGui.getBtn_Send() , 
                            pKeyOf_correspondent));
            t2.start();
            clientReciever(ios,
                    chatGui.getjTextArea1());
            
        } catch (IOException ex) {
            System.err.println("erreur connexion au serveur de chat :"+ex);
        }
    
    }
    public void clientReciever(ObjectInputStream ios, JTextArea txtToaffich){
        while(true){
            try {
                Object s =ios.readObject();
                //decrypter le message et l'ajouter au txtToaffich
                
            } catch (IOException ex) {
                Logger.getLogger(ClientChat.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
    }

    private static class ClientChatSender implements Runnable {
        CAclient ca;
        JTextField jTextField1;
        JTextArea jTextArea1; 
        ObjectOutputStream oos; 
        JButton btn_Send;
        PublicKey pKeyOf_correspondent;
        public ClientChatSender(CAclient ca, JTextField jTextField1, JTextArea jTextArea1, ObjectOutputStream oos, JButton btn_Send, PublicKey pKeyOf_correspondent) {
            this.ca = ca;
            this.jTextField1 = jTextField1;
            this.jTextArea1 = jTextArea1;
            this.oos = oos;
            this.btn_Send = btn_Send;
            this.pKeyOf_correspondent = pKeyOf_correspondent;
        }

        public void run() {
            btn_Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               if(!jTextField1.getText().equals("")){
                   try {
                       //crepter le message pui l'envoyer
                       oos.writeObject(jTextField1.getText());
                       
                       
                   } catch (IOException ex) {
                       System.err.println("l'envoi de message a echoué! "+ex);
                   }
               }            
                
            }            
        });
        }
    }
    
    
    
}
