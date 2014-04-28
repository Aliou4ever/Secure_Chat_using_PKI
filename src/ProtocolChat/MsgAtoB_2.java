/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ProtocolChat;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.Key;

/**
 *
 * @author khaled
 */
class MsgAtoB_2 implements Serializable{
    public byte [] digestOfnaonceB;
    public Key sessionKey;

    public MsgAtoB_2(byte[] digestOfnaonceB, Key sessionKey) {
        this.digestOfnaonceB = digestOfnaonceB;
        this.sessionKey = sessionKey;
    }
    
}
