/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data_base;
import java.sql.*;
/**
 *
 * @author khaled
 */
public class MySQL_DB {
    private String url;// /localhost/PKI2014
    private String login; //root
    private String password;// empty ""
    private Connection con ;

   

    public MySQL_DB(String path, String login, String password) {
        this.url = path;
        this.login = login;
        this.password = password;
    }
    public boolean connexion(){
        boolean retConn = false;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url= "jdbc:mysql:/"+this.url+"?user="+login+"&password="+password;
            con = DriverManager.getConnection(url,login,password);
            System.out.println("connexion a la base OK ");
            retConn = true;
        }catch(Exception ex){
            System.err.println("erreur de connexion a la base: "+ex);
            retConn = false;
        }
        return retConn;      
    }
    
    public boolean deconnexion(){
        boolean retDecon = false;
        try{
            con.close();
            System.out.println("deconnexion a la base OK ");
            retDecon = true;
        }catch(Exception ex){
        
        }
        return retDecon;
    }
    
    public void setPath(String path) {
        this.url = path;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPath() {
        return url;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
    
     public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
    
}
