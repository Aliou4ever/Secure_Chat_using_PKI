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

    public String getBd_pass() {
        return bd_pass;
    }

    public void setBd_pass(String bd_pass) {
        this.bd_pass = bd_pass;
    }

    public String getBd_login() {
        return bd_login;
    }

    public void setBd_login(String bd_login) {
        this.bd_login = bd_login;
    }

    public String getBd_url() {
        return bd_url;
    }

    public void setBd_url(String bd_url) {
        this.bd_url = bd_url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public PublicKey getCa_PublicKey() {
        return ca_PublicKey;
    }

    public void setCa_PublicKey(PublicKey ca_PublicKey) {
        this.ca_PublicKey = ca_PublicKey;
    }

    public PrivateKey getCa_PrivateKey() {
        return ca_PrivateKey;
    }

    public void setCa_PrivateKey(PrivateKey ca_PrivateKey) {
        this.ca_PrivateKey = ca_PrivateKey;
    }
    
    
    
    
}
