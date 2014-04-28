/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ProtocolChat;
    import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author khaled
 */
public class MySerializer {

    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o;
        try {
             o = new ObjectOutputStream(b);
             o.writeObject(obj);
            return b.toByteArray();
        } catch (IOException ex) {
            System.err.println("probleme serialisation: "+ex);
            return null;
        }
       
    }
    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o;
        try {
            o = new ObjectInputStream(b);
            return o.readObject();
        } catch (IOException ex) {
            System.err.println("probleme deserialisation: "+ex);
            return null;
        } catch (ClassNotFoundException ex) {
            System.err.println("probleme deserialisation: "+ex);
            return null;
        }
        
    }

}
