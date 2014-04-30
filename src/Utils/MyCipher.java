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
    
//    public static byte[] rsaEncrypt(byte [] msg, PublicKey pkey){
//        byte [] chif=null;        
//        try {
//            Security.addProvider(new BouncyCastleProvider());
//            Cipher c = Cipher.getInstance("RSA");
//            c.init(Cipher.ENCRYPT_MODE, pkey);
//            chif= c.doFinal(msg);
//        } catch (NoSuchAlgorithmException ex) {
//            System.err.println("erreur de cryptage dans Mycipher: "+ex);
//        } catch (NoSuchPaddingException ex) {
//            System.err.println("erreur de cryptage dans Mycipher: "+ex);
//        } catch (InvalidKeyException ex) {
//            System.err.println("erreur de cryptage dans Mycipher: "+ex);
//        } catch (IllegalBlockSizeException ex) {
//            System.err.println("erreur de cryptage dans Mycipher: "+ex);
//        } catch (BadPaddingException ex) {
//            System.err.println("erreur de cryptage dans Mycipher: "+ex);
//        }
//        return chif;
//    }
//    
//    public static byte [] rsaDecrypt(byte [] msg, PrivateKey key){
//        byte[] dechif=null;
//        try {            
//            Security.addProvider(new BouncyCastleProvider());
//            Cipher d = Cipher.getInstance("RSA");
//            d.init(Cipher.DECRYPT_MODE, key);
//            dechif = d.doFinal(msg);
//            
//        } catch (NoSuchAlgorithmException ex) {
//            System.err.println("erreur de decryptage rsa dans Mycipher: "+ex);
//        } catch (NoSuchPaddingException ex) {
//             System.err.println("erreur de decryptage rsa dans Mycipher: "+ex);
//        } catch (InvalidKeyException ex) {
//            System.err.println("erreur de decryptage rsa dans Mycipher: "+ex);
//        } catch (IllegalBlockSizeException ex) {
//            System.err.println("erreur de decryptage rsa dans Mycipher: "+ex);
//        } catch (BadPaddingException ex) {
//             System.err.println("erreur de decryptage rsa dans Mycipher: "+ex);
//        }
//        return dechif;
//    }
    
    public static  byte[] desEncrypt(byte[] msg, Key key ){
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
    
    public static byte[] desDecrypt(byte[] msg, Key key){
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
    
    public static byte[] rsaEncrypt(byte[] dataToEncrypt, PublicKey pubk){
        try{
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubk);
            byte[] encrypted = blockCipher(dataToEncrypt,Cipher.ENCRYPT_MODE, cipher);
            return encrypted;
        }catch(Exception e){
            System.err.println(e.toString());
            return null;
        }
    }
    
    
    public static byte[] rsaDecrypt(byte[] bytesIn, PrivateKey prvk){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, prvk);
            byte[] decrypted = blockCipher(bytesIn,Cipher.DECRYPT_MODE, cipher);
            return decrypted;
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }
    
    private static byte[] blockCipher(byte[] bytes, int mode,Cipher cipher ) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
	
        // string initialize 2 buffers.
	// scrambled will hold intermediate results
	byte[] scrambled = new byte[0];

	// toReturn will hold the total result
	byte[] toReturn = new byte[0];
	// if we encrypt we use 100 byte long blocks. Decryption requires 128 byte long blocks (because of RSA)
	int length = (mode == Cipher.ENCRYPT_MODE)? 100 : 128;

	// another buffer. this one will hold the bytes that have to be modified in this step
	byte[] buffer = new byte[length];

	for (int i=0; i< bytes.length; i++){

		// if we filled our buffer array we have our block ready for de- or encryption
		if ((i > 0) && (i % length == 0)){
			//execute the operation
			scrambled = cipher.doFinal(buffer);
			// add the result to our total result.
			toReturn = append(toReturn,scrambled);
			// here we calculate the length of the next buffer required
			int newlength = length;

			// if newlength would be longer than remaining bytes in the bytes array we shorten it.
			if (i + length > bytes.length) {
				 newlength = bytes.length - i;
			}
			// clean the buffer array
			buffer = new byte[newlength];
		}
		// copy byte into our buffer.
		buffer[i%length] = bytes[i];
	}

	// this step is needed if we had a trailing buffer. should only happen when encrypting.
	// example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
	scrambled = cipher.doFinal(buffer);

	// final step before we can return the modified data.
	toReturn = append(toReturn,scrambled);

	return toReturn;
}
    private static  byte[] append(byte[] prefix, byte[] suffix){
	byte[] toReturn = new byte[prefix.length + suffix.length];
	for (int i=0; i< prefix.length; i++){
		toReturn[i] = prefix[i];
	}
	for (int i=0; i< suffix.length; i++){
		toReturn[i+prefix.length] = suffix[i];
	}
	return toReturn;
}
}
