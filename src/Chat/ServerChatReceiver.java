/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import CAclient.CAclient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author khaled
 */
class ServerChatReceiver implements Runnable {
    CAclient ca;
    JTextArea txtToaffich;
    ObjectInputStream ios;

    public ServerChatReceiver(CAclient ca,JTextArea txtToaffich, ObjectInputStream ios) {
        this.ca = ca;
        this.txtToaffich = txtToaffich;
        this.ios = ios;
    }



    public void run() {
        while(true){
            try {
                Object s =ios.readObject();
                //decrepter le message et l'ajouet au txtToaffich
                
                
            } catch (IOException ex) {
                System.err.println("erreur lecture objet reçu du chat: "+ex);
            } catch (ClassNotFoundException ex) {
                System.err.println("erreur lecture objet reçu du chat: "+ex);
            }
        }
            
    }
    
}
