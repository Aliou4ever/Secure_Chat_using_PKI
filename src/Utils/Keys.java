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
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author khaled
 */
public class Keys {
    
    
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
            
        } catch (IOException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
