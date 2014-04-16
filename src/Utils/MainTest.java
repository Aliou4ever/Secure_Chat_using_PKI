/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import data_base.AccessBD;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author khaled
 */
public class MainTest {
    public static void main(String args[]) throws CertificateEncodingException, CertificateException, UnsupportedEncodingException, SQLException{        

        KeyPair pair = Utils.Certificate.generateKeyPair();        
        X509Certificate cert = Utils.Certificate.generateCertForCAroot(pair);       
        byte [] bytes = cert.getEncoded();
        String s = new String(bytes);
        System.out.println("cert 1 :"+s );
        X509Certificate cert2 = Utils.Certificate.recreateCertFromBytes(bytes);
        byte [] bytes2 = cert2.getEncoded();
        System.out.println("cert 2 :"+bytes2 );        
        if(cert2.equals(cert))System.out.println("c'est les meme yay :)");
        else System.err.println("NON KO#########################");
        if((new String(bytes2)).equals(s))
        System.out.println("yesssssssssssssssssssssssssssssssssssssssss");
        
        AccessBD bd = new AccessBD("C:\\Users\\khaled\\Desktop\\BDtest.accdb","","");
        bd.connexion();
        Connection con = bd.getCon();        
        PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(""
                + "insert into Client(nom,certificat)"
                + "values('+"+cert.getIssuerDN()+"','"+cert.getEncoded()+"')");
        pstmt.executeUpdate();
        pstmt.close();
        bd.deconnexion();
    }
}
