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
public class MsgAtoB_1 implements Serializable{
    public BigInteger nonceA;
    public String loginA;

    public MsgAtoB_1(BigInteger nonceA, String loginA) {
        this.nonceA = nonceA;
        this.loginA = loginA;
    }
    
}
