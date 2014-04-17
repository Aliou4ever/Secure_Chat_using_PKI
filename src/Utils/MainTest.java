/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;
import Utils.*;
import data_base.MySQL_DB;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;

/**
 *
 * @author khaled
 */
public class MainTest {
    public static void main(String args[]) throws CertificateEncodingException, CertificateException, UnsupportedEncodingException, SQLException{        

        KeyPair pair = Certificate.generateKeyPair();        
        X509Certificate cert = Certificate.generateCertForCAroot(pair);       
        byte [] bytes = cert.getEncoded();
       
        X509Certificate cert2 = Utils.Certificate.recreateCertFromBytes(bytes); 
        byte [] bytes2 = cert2.getEncoded();
        
        if(cert2.equals(cert))System.out.println("0K**************:)");
        else System.err.println("KO000#########################:(");      
        
        MySQL_DB bd = new MySQL_DB("/localhost/PKI2014","root","");
        bd.connexion();
        Connection con = bd.getCon();  
        
        String sql ="insert into Certificat(nom,certificat)values(?,?)";
        PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(sql);
        pstmt.setObject(1, "CAroot");
        pstmt.setBytes(2, bytes);
        pstmt.execute();
        pstmt.close();        
        
        String sql2="select * from Certificat where nom='CAroot'";        
        PreparedStatement ps=con.prepareStatement(sql2);
        ResultSet rs =ps.executeQuery();
        rs.next();
        String s = rs.getString("nom");
        System.out.println(s);
        //rs.next();
        byte [] bytes3 = rs.getBytes("certificat");        
        ps.close();
        System.out.println("length bytes: "+bytes.length+"  length bytes2: "
                +bytes2.length+ " length bytes3: "+bytes3.length);
        
        bd.deconnexion();
        
        X509Certificate cert3 = Utils.Certificate.recreateCertFromBytes(bytes3);

        if(cert3.equals(cert)) System.out.println("yes :) c'est le meme certificat on a bien réussi a l'avoir");
        else System.err.println("ça n'a pas marché :( Koooooooooooooooo");
        
                     
        
    }
}
