/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import ProtocolChat.ChatSecure;
import ProtocolChat.ObjectPassing;
import Utils.MyCipher;
import Utils.MySQL_DB;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import javax.swing.JButton;
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
            System.out.println("serverCaht: "+ca.ca_login+"s_port: "+s.getLocalPort());
            db.deconnexion();
        } catch (IOException ex) {
            System.err.println("erruer serveur chat"+ex.toString());
        }
    }
    
    public void run(){
        try {
            
            while(true){
                Socket socket = s.accept();
                System.out.println("##########un client s'est connecté au chat: ####################");
                System.err.println("*****le port du chat est: "+socket.getLocalPort());
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
                ChatGUI chatGui = new ChatGUI();
                chatGui.setVisible(true);
                chatGui.setTitle(ca.ca_login);
                chatGui.setLocationRelativeTo(null);
                chatGui.getjTextArea1().setEditable(false);
                ServerChatReceiver receiver = new ServerChatReceiver(ca,chatGui.getjTextArea1(),ios,sessionKey);
                receiver.start();
                ServerChatSender sender = new ServerChatSender(ca,chatGui.getjTextField1(), 
                        chatGui.getjTextArea1(), oos,chatGui.getBtn_Send(), sessionKey );
                sender.start();              
            }
        } catch (IOException ex) {
            System.err.println("erruer serveur Ca :"+ex.toString());
        }
        
    }

    /**
     *
     * @author khaled
     */
    private class ServerChatReceiver extends Thread {

        CA ca;
        JTextArea txtToaffich;
        ObjectInputStream ios;
        Key sessionKey;

        public ServerChatReceiver(CA ca, JTextArea txtToaffich, ObjectInputStream ios, Key sessionKey) {
            super();
            this.ca = ca;
            this.txtToaffich = txtToaffich;
            this.ios = ios;
            this.sessionKey = sessionKey;
        }

        public void run() {
            while (true) {
                try {
                    ObjectPassing s = (ObjectPassing) ios.readObject();
                    if (s == null) {
                        ios.close();
                        break;
                    }
                    String msg = new String(MyCipher.desDecrypt(s.objDataArray, sessionKey));
                    txtToaffich.setEditable(true);
                    txtToaffich.setText(txtToaffich.getText() + "\n" + msg);
                    txtToaffich.setEditable(false);
                } catch (IOException ex) {
                    System.err.println("erreur lecture objet re\u00e7u du chat: " + ex.toString());
                } catch (ClassNotFoundException ex) {
                    System.err.println("erreur lecture objet re\u00e7u du chat: " + ex.toString());
                }
            }
        }
    }

    /**
     *
     * @author khaled
     */
    private class ServerChatSender extends Thread {

        CA ca;
        JTextField txtToSend;
        JTextArea txtToaffich;
        ObjectOutputStream oos;
        JButton btn;
        Key sessionKey;

        public ServerChatSender(CA ca, JTextField txtToSend, JTextArea txtToaffich, ObjectOutputStream oos, JButton btn, Key sessionKey) {
            super();
            this.btn = btn;
            this.ca = ca;
            this.txtToSend = txtToSend;
            this.txtToaffich = txtToaffich;
            this.oos = oos;
            this.sessionKey = sessionKey;
        }

        public void run() {
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (!txtToSend.getText().equals("")) {
                        try {
                            //crepter le message pui l'envoyer
                            String tosend = ca.ca_login + " :";
                            tosend = tosend + txtToSend.getText();
                            txtToSend.setText("");
                            txtToaffich.setEditable(true);
                            txtToaffich.setText(txtToaffich.getText() + "\n" + tosend);
                            txtToaffich.setEditable(false);
                            byte[] encryptedmsg = MyCipher.desEncrypt(tosend.getBytes(), sessionKey);
                            ObjectPassing s = new ObjectPassing(encryptedmsg);
                            oos.writeObject(s);
                        } catch (IOException ex) {
                            System.err.println("l'envoi de message a echou\u00e9! " + ex);
                        }
                    }
                }
            });
        }
    }
    
    
}
