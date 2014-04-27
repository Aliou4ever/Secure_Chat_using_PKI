/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import CAclient.CAclient;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
    CAclient ca;
    JTextField txtToSend;
    JTextArea txtToaffich;
    ObjectOutputStream oos;
    JButton btn;

    public ServerChatSender(CAclient ca, JTextField txtToSend, JTextArea txtToaffich, ObjectOutputStream oos, JButton btn) {
        this.btn = btn;
        this.ca = ca;
        this.txtToSend = txtToSend;
        this.txtToaffich = txtToaffich;
        this.oos = oos;        
    }
   
    public void run() {        
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               if(!txtToSend.getText().equals("")){
                   try {
                       //crepter le message pui l'envoyer
                       oos.writeObject(txtToSend.getText());
                       
                   } catch (IOException ex) {
                       System.err.println("l'envoi de message a echoué! "+ex);
                   }
               }            
                
            }            
        });        
    }
    
}
