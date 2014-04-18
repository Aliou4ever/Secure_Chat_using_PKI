/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseaux;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

/**
 *
 * @author khaled
 */
public class CSRSerializer implements Serializable {
    
    byte [] dataCSR;
    
    public CSRSerializer(PKCS10CertificationRequest csr){
        try {
            this.dataCSR = csr.getEncoded();
        } catch (IOException ex) {
            Logger.getLogger(CSRSerializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public PKCS10CertificationRequest getCsr() {
        PKCS10CertificationRequest csr =null;
        try {
            csr=   new PKCS10CertificationRequest(dataCSR);
        } catch (IOException ex) {
            Logger.getLogger(CSRSerializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return csr;
    }
    public void setCsr(PKCS10CertificationRequest csr) throws IOException {
       this.dataCSR= csr.getEncoded();       
    }
    
    
}
