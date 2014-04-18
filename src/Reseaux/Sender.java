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
import Utils.CSRbuilder;
import Utils.Keys;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.KeyPair;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

public class Sender {
    public static void main(String[] args) throws IOException {
    	System.out.println("Sender Start");

    	ServerSocketChannel ssChannel = ServerSocketChannel.open();
    	ssChannel.configureBlocking(true);
    	int port = 12345;
    	ssChannel.socket().bind(new InetSocketAddress(port));
        KeyPair pairCA1 = Keys.generateKeyPair(); 
    	PKCS10CertificationRequest csr = CSRbuilder.createCSR(pairCA1, "CA1");
        CSRSerializer obj = new CSRSerializer(csr);
    	while (true) {
    		SocketChannel sChannel = ssChannel.accept();

    		ObjectOutputStream  oos = new 
                      ObjectOutputStream(sChannel.socket().getOutputStream());
    		oos.writeObject(obj);
    		oos.close();

    		System.out.println("Connection ended");
    	}
    }
}