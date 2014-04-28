/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ProtocolChat;

import java.io.Serializable;

/**
 *
 * @author khaled
 */
public class ObjectPassing implements  Serializable {
    public byte[] objDataArray;

    public ObjectPassing(byte[] obj) {
        this.objDataArray = obj;
    }    
    
    
}
