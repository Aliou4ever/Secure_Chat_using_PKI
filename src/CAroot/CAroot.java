/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CAroot;

import java.security.*;
import java.security.KeyPair;
import java.security.cert.X509Certificate;

/**
 *
 * @author khaled
 */
public class CAroot {
    X509Certificate caRoot_Cert;    
    PublicKey caRoot_PublicKey;
    PrivateKey caRoot_PrivateKey;
    String BD_pass;

    public CAroot() {        
            //generer la pair de clé pour le CAroot
            KeyPair pair = Utils.Certificate.generateKeyPair();
            this.caRoot_PrivateKey = pair.getPrivate();
            this.caRoot_PublicKey = pair.getPublic();
            this.caRoot_Cert = Utils.Certificate.generateCertForCAroot(pair);
            this.BD_pass ="";
    }    
    public CAroot(String BDpass) {        
            //generer la pair de clé pour le CAroot
            KeyPair pair = Utils.Certificate.generateKeyPair();
            this.caRoot_PrivateKey = pair.getPrivate();
            this.caRoot_PublicKey = pair.getPublic();
            this.caRoot_Cert = Utils.Certificate.generateCertForCAroot(pair);
            this.BD_pass = BDpass; 
    }    

    public void setCaRoot_Cert(X509Certificate caRoot_Cert) {
        this.caRoot_Cert = caRoot_Cert;
    }

    public void setCaRoot_PublicKey(PublicKey caRoot_PublicKey) {
        this.caRoot_PublicKey = caRoot_PublicKey;
    }

    public void setCaRoot_PrivateKey(PrivateKey caRoot_PrivateKey) {
        this.caRoot_PrivateKey = caRoot_PrivateKey;
    }

    public void setBD_pass(String BD_pass) {
        this.BD_pass = BD_pass;
    }

    public X509Certificate getCaRoot_Cert() {
        return caRoot_Cert;
    }

    public PublicKey getCaRoot_PublicKey() {
        return caRoot_PublicKey;
    }

    public PrivateKey getCaRoot_PrivateKey() {
        return caRoot_PrivateKey;
    }

    public String getBD_pass() {
        return BD_pass;
    }
    
}
