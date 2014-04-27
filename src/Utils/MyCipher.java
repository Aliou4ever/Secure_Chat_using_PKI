/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
/**
 *
 * @author khaled
 */
public class MyCipher {
    
    
    public static byte[] rsaEncrypt(byte [] msg, PublicKey pkey){
        byte [] chif=null;        
        try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, pkey);
            chif= c.doFinal(msg);
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("erreur de cryptage dans Mycipher: "+ex);
        } catch (NoSuchPaddingException ex) {
            System.err.println("erreur de cryptage dans Mycipher: "+ex);
        } catch (InvalidKeyException ex) {
            System.err.println("erreur de cryptage dans Mycipher: "+ex);
        } catch (IllegalBlockSizeException ex) {
            System.err.println("erreur de cryptage dans Mycipher: "+ex);
        } catch (BadPaddingException ex) {
            System.err.println("erreur de cryptage dans Mycipher: "+ex);
        }
        return chif;
    }
    
    public static byte [] rsaDecrypt(byte [] msg, PrivateKey key){
        byte[] dechif=null;
        try {            
            Security.addProvider(new BouncyCastleProvider());
            Cipher d = Cipher.getInstance("RSA");
            d.init(Cipher.DECRYPT_MODE, key);
            dechif = d.doFinal(msg);
            
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("erreur de decryptage rsa dans Mycipher: "+ex);
        } catch (NoSuchPaddingException ex) {
             System.err.println("erreur de decryptage rsa dans Mycipher: "+ex);
        } catch (InvalidKeyException ex) {
            System.err.println("erreur de decryptage rsa dans Mycipher: "+ex);
        } catch (IllegalBlockSizeException ex) {
            System.err.println("erreur de decryptage rsa dans Mycipher: "+ex);
        } catch (BadPaddingException ex) {
             System.err.println("erreur de decryptage rsa dans Mycipher: "+ex);
        }
        return dechif;
    }
    
    public byte[] desEncrypt(byte[] msg, Key key ){
        byte[] chif =null;
            try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher c = Cipher.getInstance("DES");
            c.init(Cipher.ENCRYPT_MODE, key);
            chif= c.doFinal(msg);
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("erreur de cryptage DES dans Mycipher: "+ex);
        } catch (NoSuchPaddingException ex) {
            System.err.println("erreur de cryptage DES dans Mycipher: "+ex);
        } catch (InvalidKeyException ex) {
            System.err.println("erreur de cryptage DES dans Mycipher: "+ex);
        } catch (IllegalBlockSizeException ex) {
           System.err.println("erreur de cryptage DES dans Mycipher: "+ex);
        } catch (BadPaddingException ex) {
           System.err.println("erreur de cryptage DES dans Mycipher: "+ex);
        }
        return chif;
    }
    
    public byte[] desDecrypt(byte[] msg, Key key){
         byte[] chif =null;
            try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher c = Cipher.getInstance("DES");
            c.init(Cipher.DECRYPT_MODE, key);
            chif= c.doFinal(msg);
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("erreur de decryptage DES dans Mycipher: "+ex);
        } catch (NoSuchPaddingException ex) {
            System.err.println("erreur de decryptage DES dans Mycipher: "+ex);
        } catch (InvalidKeyException ex) {
            System.err.println("erreur de decryptage DES dans Mycipher: "+ex);
        } catch (IllegalBlockSizeException ex) {
           System.err.println("erreur de decryptage DES dans Mycipher: "+ex);
        } catch (BadPaddingException ex) {
           System.err.println("erreur de decryptage DES dans Mycipher: "+ex);
        }
        return chif;
    
    }
}
