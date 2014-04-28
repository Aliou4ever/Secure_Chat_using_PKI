/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author khaled
 */
public class Digester {
    
    public static byte [] hacher(String s){        
        byte [] message_dg =null;
        try {
            MessageDigest msg_digest = MessageDigest.getInstance("SHA-1");
            msg_digest.update(s.getBytes());
            message_dg = msg_digest.digest();
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("erreur d'hachage du message: "+ex);
        }
        return message_dg; 
    }
    public static byte[] hacher(byte [] msg){
        byte [] message_dg =null;
        try {
            MessageDigest msg_digest = MessageDigest.getInstance("SHA-1");
            msg_digest.update(msg);
            message_dg = msg_digest.digest();
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("erreur d'hachage du message: "+ex);
        }
        return message_dg; 
    }
    public static boolean digestVerify( byte m1 [], byte m2 []){
        boolean ret =false;
        try {
            MessageDigest msg_digest = MessageDigest.getInstance("SHA-1");
            if(msg_digest.isEqual(m1,m2))ret=true;
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("erreur de hachés des messages: "+ex);
        }
        return ret;
    }
    
}
