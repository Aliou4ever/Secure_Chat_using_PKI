/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ServiceCert;

import Chat.CA;
import ProtocolChat.MySerializer;
import ProtocolChat.ObjectPassing;
import Utils.CSRbuilder;
import Utils.Digester;
import Utils.MySQL_DB;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author khaled
 */
public class ServerCert extends Thread{
    CA ca;
    public int port;
    ServerSocket s;
    public ServerCert(CA ca, int port) {
        try {
            this.ca = ca;            
            this.s = new ServerSocket(port);
            this.port = s.getLocalPort();
            System.out.println("serveur Cert:"+ ca.getCa_login()+" en ecoute sur le port: "+this.port);
            MySQL_DB db = new MySQL_DB(ca.getBD_url(), ca.getBD_login(), ca.getBD_pass());
            db.connexion();
            db.updatCertPort(ca.getCa_login(), s.getLocalPort());
            db.deconnexion();
        } catch (IOException ex) {
            System.err.println("erruer serveur cert Ca :"+ex.toString());
        }
    }
    
    public void run(){
         try {
            
            while(true){
                Socket socket = s.accept();
                System.out.println("un client s'est connecté et demande la cerification");
                ObjectInputStream ios = new ObjectInputStream(socket.getInputStream());
                try {
                    //protocol cert: A -> S: {login, h(password), csr}KS
                    ObjectPassing obj= (ObjectPassing) ios.readObject();
                    MsgAtoS msg = (MsgAtoS) MySerializer.deserialize(obj.objDataArray);
                    MySQL_DB db = new MySQL_DB(ca.getBD_url(), ca.getBD_login(), ca.getBD_pass());
                    db.connexion();
                    byte[] passDigest = db.getPassword(msg.loginA);
                    if(Digester.digestVerify(passDigest, msg.digestOfpass)){
                        X509Certificate certCA = db.getCertificate(ca.getCa_login());
                        X509Certificate certClient = CSRbuilder.createCertOfCSR(msg.getDataArrayOfcsr(), ca.getCaKeyPair(),certCA );
                        db.insertCertificat(certClient, msg.loginA);
                    }
                    db.deconnexion();
                } catch (ClassNotFoundException ex) {
                    System.err.println(ex.toString());
                }
                
            }
         }catch(IOException ex){
             System.err.println("erruer serveur cert Ca :"+ex.toString());
         }
         
    }
    
}
