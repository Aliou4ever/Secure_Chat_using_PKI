/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ProtocolChat;

import Utils.Digester;
import Utils.MyCipher;
import data_base.MySQL_DB;
import java.math.BigInteger;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
 *
 * @author khaled
 */
public class ChatSecure {
    /*
        A -> B: {Na, login_a}KB

        B -> A: {Nb, h(Na)}KA

        A -> B: {h(Nb), keySession}KB
    */
    
    public static ObjectPassing AtoB_1(BigInteger nonceA, String loginA, PublicKey pbkeyB){        
        MsgAtoB_1 msg = new MsgAtoB_1(nonceA, loginA);        
        byte[] crypted = MyCipher.rsaEncrypt(MySerializer.serialize(msg), pbkeyB);
        ObjectPassing msg_ser = new ObjectPassing(crypted);
        return msg_ser;
    }
    
    public static ObjectPassing BtoA_1(ObjectPassing objReceived, BigInteger nonceB, PrivateKey pvKeyB, 
            MySQL_DB db){        
        byte[] decryped = MyCipher.rsaDecrypt(objReceived.objDataArray, pvKeyB);
        MsgAtoB_1 msg = (MsgAtoB_1) MySerializer.deserialize(decryped);
        db.connexion();
        X509Certificate cert = db.getCertificate(msg.loginA);
        db.connexion();
        MsgBtoA_1 msgTosend = new MsgBtoA_1(nonceB, Digester.hacher(msg.nonceA.toByteArray()));       
        byte[] crypted = MyCipher.rsaEncrypt(MySerializer.serialize(msgTosend), cert.getPublicKey());
        ObjectPassing msg_ser = new ObjectPassing(crypted);
        return msg_ser;
    }
    
    public static ObjectPassing AtoB_2 (ObjectPassing objReceived, BigInteger nonceA, PublicKey pbkeyB, 
            PrivateKey pvKeyA,Key sessionKey){
        byte[] decryped = MyCipher.rsaDecrypt(objReceived.objDataArray, pvKeyA);
        MsgBtoA_1 msg = (MsgBtoA_1) MySerializer.deserialize(decryped);
        if(Digester.digestVerify(msg.digestOfnaonceA, Digester.hacher(nonceA.toByteArray()))){
            MsgAtoB_2 toSend =  new MsgAtoB_2(Digester.hacher(msg.nonceB.toByteArray()), sessionKey);
            //crepter le semssage 
            byte[] crypted  = MyCipher.rsaEncrypt(MySerializer.serialize(toSend), pbkeyB);
            ObjectPassing msg_ser = new ObjectPassing(crypted);
            return msg_ser;
        }
        return null;        
    }
    
    public static Key BgetSessionKey(ObjectPassing objReceived,BigInteger nonceB, PrivateKey pvkeyB){
        byte[] decryped = MyCipher.rsaDecrypt(objReceived.objDataArray, pvkeyB);
        MsgAtoB_2 msg = (MsgAtoB_2) MySerializer.deserialize(decryped);
        if(Digester.digestVerify(msg.digestOfnaonceB, Digester.hacher(nonceB.toByteArray()))){
            return msg.sessionKey;       
        }
        return null;       
    }      
    
    
}
