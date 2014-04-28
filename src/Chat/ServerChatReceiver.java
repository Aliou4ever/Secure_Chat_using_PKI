/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import CAclient.CAclient;
import ProtocolChat.ObjectPassing;
import Utils.MyCipher;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.Key;
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
    Key sessionKey;
    public ServerChatReceiver(CAclient ca,JTextArea txtToaffich, ObjectInputStream ios, Key sessionKey) {
        this.ca = ca;
        this.txtToaffich = txtToaffich;
        this.ios = ios;
        this.sessionKey = sessionKey;
    }

    public void run() {
        while(true){
            try {
                ObjectPassing s =(ObjectPassing) ios.readObject();
                String msg = new String( MyCipher.desDecrypt(s.objDataArray, sessionKey));                
                txtToaffich.setText(txtToaffich.getText()+"\n"+msg);                
            } catch (IOException ex) {
                System.err.println("erreur lecture objet reçu du chat: "+ex.toString());
            } catch (ClassNotFoundException ex) {
                System.err.println("erreur lecture objet reçu du chat: "+ex.toString());
            }
        }
            
    }
    
}
