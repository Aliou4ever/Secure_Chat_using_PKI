/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 * @author khaled
 */
public class CA {
    KeyPair caKeyPair;
    String BD_url;
    String BD_pass;
    String BD_login;
    String ca_login;

    public CA(KeyPair caKeyPair, String BD_url, String BD_pass, String BD_login, String ca_login) {
        this.caKeyPair = caKeyPair;
        this.BD_url = BD_url;
        this.BD_pass = BD_pass;
        this.BD_login = BD_login;
        this.ca_login = ca_login;
    }

    public KeyPair getCaKeyPair() {
        return caKeyPair;
    }
    

    public String getBD_url() {
        return BD_url;
    }

    public void setBD_url(String BD_url) {
        this.BD_url = BD_url;
    }

    public String getBD_pass() {
        return BD_pass;
    }

    public void setBD_pass(String BD_pass) {
        this.BD_pass = BD_pass;
    }

    public String getBD_login() {
        return BD_login;
    }

    public void setBD_login(String BD_login) {
        this.BD_login = BD_login;
    }

    public String getCa_login() {
        return ca_login;
    }

    public void setCa_login(String ca_login) {
        this.ca_login = ca_login;
    }
    
    
}
