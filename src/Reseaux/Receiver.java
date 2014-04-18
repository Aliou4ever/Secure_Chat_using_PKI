/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Reseaux;

/**
 *
 * @author khaled
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

public class Receiver {
    public static void main(String[] args) 
    throws IOException, ClassNotFoundException {
    	System.out.println("Receiver Start");

    	SocketChannel sChannel = SocketChannel.open();
    	sChannel.configureBlocking(true);
    	if (sChannel.connect(new InetSocketAddress("localhost", 12345))) {

    		ObjectInputStream ois = 
                     new ObjectInputStream(sChannel.socket().getInputStream());

    		CSRSerializer ob = (CSRSerializer) ois.readObject();
                PKCS10CertificationRequest csr = ob.getCsr();
    		System.out.println("csr is: '" + csr.toString() + "'");
    	}
    	System.out.println("End Receiver");
    }
}