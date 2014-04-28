/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
/**
 *
 * @author khaled
 */
public class Keys {
    public static KeyPair generateKeyPair(){
        KeyPair pair = null;
        try {
            KeyPairGenerator key = KeyPairGenerator.getInstance("RSA");
            pair = key.generateKeyPair();           
        } catch (NoSuchAlgorithmException ex) {
             System.err.println("erreur generation pair de cle :"+ex);
        }
        return pair;
    }
    
    public static PublicKey recreatePublicKey(String path){
        FileInputStream file = null;
        RSAPublicKey key_public =null;
        try {            
            file = new FileInputStream(path);
            ObjectInputStream obj_cle = new ObjectInputStream(file);
            BigInteger modulus = (BigInteger) obj_cle.readObject();
            BigInteger exponent =  (BigInteger) obj_cle.readObject();
            RSAPublicKeySpec cle_reconst = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            key_public = (RSAPublicKey) keyFactory.generatePublic(cle_reconst);
            
        }catch (Exception ex) {
            System.err.println("Probeleme de recreartion cle public: "+ex);
        } finally {
            try {
                file.close();
            }catch (Exception ex) {
                System.err.println("Probeleme de fermiture de fichier de la cle public: "+ex);
        }
        }
        return key_public;
    }
    
    
    public static void savePublicKeyInFile(String path, PublicKey key){
        try {            
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec RSAspec = fac.getKeySpec(key, RSAPublicKeySpec.class);
            FileOutputStream file3 = new FileOutputStream (path);
            ObjectOutputStream obj_stream = new ObjectOutputStream(file3);
            obj_stream.writeObject(RSAspec.getModulus());
            obj_stream.writeObject(RSAspec.getPublicExponent());        
        } catch (Exception ex) {
            System.err.println("Probeleme de sauvegarde de la cle public dans un fichier: "+ex);
        }
    }
    
    public static Key generateSessionKey(){
        Key key =null;
        try {
            KeyGenerator keys = KeyGenerator.getInstance("DES");
            key = keys.generateKey();
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("erreur generation de clé de session: "+ex);
        }
        return key;
    }
}
