/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ProtocolChat;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author khaled
 */
class MsgBtoA_1 implements Serializable{
    BigInteger nonceB;
    byte [] digestOfnaonceA;

    public MsgBtoA_1(BigInteger nonceB, byte[] digestOfnaonceA) {
        this.nonceB = nonceB;
        this.digestOfnaonceA = digestOfnaonceA;
    }
    
    
}
