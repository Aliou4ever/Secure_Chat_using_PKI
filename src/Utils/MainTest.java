/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;
import Chat.CA;
import Chat.ClientChat;
import Chat.ServerChatCa;
import ProtocolChat.*;
import ServiceCert.ServerCert;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
/**
 *
 * @author khaled
 */
public class MainTest {
    
    public static void main(String args[]) { 
       //debug CaListChat
        final MySQL_DB bd = new MySQL_DB("localhost", "root", "");
        bd.connexion();
        //Ca1
        final String ca1Login = "Ca1";
        KeyPair keyca1 =Keys.generateKeyPair();
        X509Certificate certCa1 = Certificate.generateCertForCAroot(keyca1);
        bd.insertCertificat(certCa1, ca1Login);
        CA ca1 = new CA(keyca1, bd.getPath(), bd.getPassword(), bd.getLogin(), ca1Login);
        ServerCert serverCertca1 = new  ServerCert(ca1, 0);
        serverCertca1.start();
        ServerChatCa serverChatca1 = new ServerChatCa(ca1, 0);
        serverChatca1.start();
        
        //Ca2
        final String ca2Login = "Ca2";
        KeyPair keyca2 =Keys.generateKeyPair();
        X509Certificate certCa2 = Certificate.generateCertForCAroot(keyca2);
        bd.insertCertificat(certCa2, ca2Login);
        CA ca2 = new CA(keyca2, bd.getPath(), bd.getPassword(), bd.getLogin(), ca2Login);
        ServerCert serverCertca2 = new  ServerCert(ca2, 0);
        serverCertca2.start();
        ServerChatCa serverChatca2 = new ServerChatCa(ca2, 0);
        serverChatca2.start();     
        
        int port = bd.getChatport(ca1Login);
        bd.deconnexion();
        System.out.println("lancer le chat...");
        ClientChat ask =new ClientChat(ca2, port, "localhost", certCa1.getPublicKey());
        ask.start();               
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("avant supression..");
                bd.connexion();
                bd.deleteCert(ca2Login);
                bd.deleteCert(ca1Login);
                bd.deconnexion();
                System.out.println("apres supression..");
            }
        });           
    }
    
    public static void testRsa(){
        MsgAtoB_1 msg= new MsgAtoB_1(new BigInteger(String.valueOf(System.currentTimeMillis())), "login");
        byte [] tab = MySerializer.serialize(msg);
        KeyPair key = Keys.generateKeyPair();
        byte [] crypt = MyCipher.rsaEncrypt(tab, key.getPublic());
        byte [] decrypt = MyCipher.rsaDecrypt(crypt,key.getPrivate() );
        for(int i =0; i<tab.length; i++) System.out.print(tab[i]);
        System.out.println("");
        for(int i =0; i<decrypt.length; i++) System.out.print(decrypt[i]);
        MsgAtoB_1 msg2 = (MsgAtoB_1) MySerializer.deserialize(decrypt);
        System.out.println(msg.loginA+" nonce"+ msg.nonceA);
        System.out.println(msg2.loginA+" nonce"+ msg2.nonceA);
    
    }
    public static void test1(){    
    //code pour inserer et recupercer un cerificat dans la base
        KeyPair pair = Keys.generateKeyPair();        
        X509Certificate cert = Certificate.generateCertForCAroot(pair); 
        String login = "CA4";
        MySQL_DB bd = new MySQL_DB("localhost","root","");
        bd.connexion();
        bd.insertCertificat(cert,login);
        X509Certificate cert3 = bd.getCertificate(login);
        bd.updatChatPort(login, 1024);
        bd.updatCertPort(login, 1025);
        System.out.println("chatPort: "+bd.getChatport(login)+" certPort: "+bd.getCertPort(login)
                               +" certTorevok: "+bd.certIsTorevok(login) );
        bd.revokCert(login);
        System.out.println(" cert revoked :"+bd.certIsTorevok(login));
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
    
    public static void test4(){
               //ajouter un user 
            MySQL_DB db = new MySQL_DB("localhost", "root", "");
            db.connexion();            
            byte [] pass =Digester.hacher("AdminPass");
            db.addUser("Admin2", pass);            
            System.out.println("user exists: "+db.userExists("Admin2"));
            db.deconnexion();   
    
    }
    
    public static void test5(){
        //ajouter un user et un mot de passe puis le recuperer et verifier si c'est le meme
        MySQL_DB db = new MySQL_DB("localhost", "root", "");
            db.connexion();            
            byte [] pass =Digester.hacher("myPassword2");
            db.addUser("Admin2", pass);  
            byte [] pass2= db.getPassword("Admin2");
            System.out.println("c'est le meme mot de passe: "+Digester.digestVerify(pass, pass2));
            db.deconnexion();
    }
    
}
