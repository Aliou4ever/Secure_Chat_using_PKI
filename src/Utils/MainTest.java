/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;
import data_base.MySQL_DB;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
/**
 *
 * @author khaled
 */
public class MainTest {
    public static void main(String args[]) {       
     
       
        
    }
    public static void test1(){    
    //code pour inserer et recupercer un cerificat dans la base
        KeyPair pair = Keys.generateKeyPair();        
        X509Certificate cert = Certificate.generateCertForCAroot(pair);       
        MySQL_DB bd = new MySQL_DB("localhost","root","");
        bd.connexion();
        bd.insertCertificat(cert,"CAroot3");
        X509Certificate cert3 = bd.getCertificate("CAroot3");
        bd.deconnexion();        
        if(cert3.equals(cert)) System.out.println("yes :) c'est le meme certificat on a bien réussi a l'avoir");
        else System.err.println("ça n'a pas marché :( Koooooooooooooooo");
        
    }
    public static void test2(){
        //code pour generer csr et creer son certificat
        KeyPair pairCAroot = Keys.generateKeyPair();                 
        X509Certificate CAcert = Certificate.generateCertForCAroot(pairCAroot);
        
        KeyPair pairCA1 = Keys.generateKeyPair();
        PKCS10CertificationRequest csr = CSRbuilder.createCSR(pairCA1, "CA1");
        
        X509Certificate cert = CSRbuilder.createCertOfCSR(csr, pairCAroot, CAcert);
        
        System.out.println(cert.toString());        
    }
    
    public static void test3(){
        //tester les fonction de hachage 
        String s = "Saliou est fort :)";
        byte []tab1 =Digester.hacher(s);
        byte []tab2 =Digester.hacher(s);
        System.out.println("c'est les meme: "+Digester.digestVerify(tab1, tab2));
    
    }
    
}
