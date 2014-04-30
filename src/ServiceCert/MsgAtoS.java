/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ServiceCert;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

/**
 *
 * @author khaled
 */
public class MsgAtoS implements Serializable{
    String loginA;
    byte[] digestOfpass;
    byte[] dataArrayOfcsr;

    public MsgAtoS(String loginA, byte[] digestOfpass, PKCS10CertificationRequest dataArrayOfcsr) {
        try {
            this.loginA = loginA;
            this.digestOfpass = digestOfpass;
            this.dataArrayOfcsr = dataArrayOfcsr.getEncoded();
        } catch (IOException ex) {
            System.err.println(ex.toString());;
        }
    }

    public String getLoginA() {
        return loginA;
    }

    public void setLoginA(String loginA) {
        this.loginA = loginA;
    }

    public byte[] getDigestOfpass() {
        return digestOfpass;
    }

    public void setDigestOfpass(byte[] digestOfpass) {
        this.digestOfpass = digestOfpass;
    }

    public PKCS10CertificationRequest getDataArrayOfcsr() {
       PKCS10CertificationRequest csr =null;
        try {
            csr=   new PKCS10CertificationRequest(dataArrayOfcsr);
        } catch (IOException ex) {
            System.err.println(ex.toString());
        }
        return csr;
    }

    public void setDataArrayOfcsr(byte[] dataArrayOfcsr) {
        this.dataArrayOfcsr = dataArrayOfcsr;
    }
    
    
    
}
