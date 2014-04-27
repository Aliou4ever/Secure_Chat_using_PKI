/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CAclient;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 * @author khaled
 */
public class CAclient {
    String bd_pass;
    String bd_login;
    String bd_url;
    String password;
    String login;
    PublicKey ca_PublicKey;
    PrivateKey ca_PrivateKey;

    public CAclient(String bd_pass, String bd_login, String bd_url, String password, String login){
        //generer la pair de clé pour le CAroot
        KeyPair pair = Utils.Keys.generateKeyPair();
        this.ca_PrivateKey = pair.getPrivate();
        this.ca_PublicKey = pair.getPublic();
        this.bd_pass = bd_pass;
        this.bd_login = bd_login;
        this.bd_url = bd_url;
        this.password = password;
        this.login = login;
    }
    
    
    
}
