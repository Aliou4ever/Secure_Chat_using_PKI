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
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

/**
 *
 * @author khaled
 */
public class ClientCert extends Thread{
    
    CA ca;
    public int port;
    String url;
    PublicKey serverpbKey;

    public ClientCert(CA ca, int port, String url, PublicKey serverpbKey) {
        this.ca = ca;
        this.port = port;
        this.url = url;
        this.serverpbKey = serverpbKey;
    }
    
    public void run(){
        PKCS10CertificationRequest csr = CSRbuilder.createCSR(ca.getCaKeyPair(), ca.getCa_login());
        MySQL_DB db = new MySQL_DB(ca.getBD_url(), ca.getBD_login(), ca.getBD_pass());
        db.connexion();
        byte[] passDigest = db.getPassword(ca.getCa_login());
        db.deconnexion();
        MsgAtoS msg = new MsgAtoS(ca.getCa_login(), passDigest, csr);
        byte[] objdata = MySerializer.serialize(msg);
        ObjectPassing obj = new ObjectPassing(objdata);               
        try {
            System.out.println("tentative de connexion au :"+url+"/"+port);
            Socket so = new Socket(url, port);
            ObjectOutputStream  oos = new ObjectOutputStream(so.getOutputStream());
            oos.writeObject(obj);            
        } catch (IOException ex) {
            System.err.println(ex.toString());
        }
    
    }
    
    
}
