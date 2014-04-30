/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import CAclient.CAclient;
import ProtocolChat.ObjectPassing;
import Utils.MyCipher;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author khaled
 */
class ServerChatSender implements Runnable {
    CA ca;
    JTextField txtToSend;
    JTextArea txtToaffich;
    ObjectOutputStream oos;
    JButton btn;
    Key sessionKey;
    public ServerChatSender(CA ca, JTextField txtToSend, JTextArea txtToaffich, ObjectOutputStream oos, 
            JButton btn, Key sessionKey) {
        this.btn = btn;
        this.ca = ca;
        this.txtToSend = txtToSend;
        this.txtToaffich = txtToaffich;
        this.oos = oos;
        this.sessionKey = sessionKey;
    }
   
    public void run() {        
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               if(!txtToSend.getText().equals("")){
                   try {
                       //crepter le message pui l'envoyer
                       String tosend = ca.ca_login+" :";
                       tosend   = tosend + txtToSend.getText();
                       txtToSend.setText("");
                       txtToaffich.setText(txtToaffich.getText()+"\n"+tosend);
                       byte [] encryptedmsg = MyCipher.desEncrypt(tosend.getBytes(), sessionKey);
                       ObjectPassing s = new ObjectPassing(encryptedmsg);
                       oos.writeObject(s);                       
                   } catch (IOException ex) {
                       System.err.println("l'envoi de message a echoué! "+ex);
                   }                   
               }          
            }            
        });        
    }
    
}
