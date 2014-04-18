/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data_base;
import java.security.cert.X509Certificate;
import java.sql.*;
/**
 *
 * @author khaled
 */
public class MySQL_DB {
    private String url;// localhost
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
            String url= "jdbc:mysql://"+this.url+"/PKI2014?user="+login+"&password="+password;
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
    
    public void insertCertificat(X509Certificate cert, String login){
        try {
            byte [] bytes = cert.getEncoded();
            String sql ="insert into Certificat(login,certificat)values(?,?)";
            PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(sql);
            pstmt.setObject(1, login);
            pstmt.setBytes(2, bytes);
            pstmt.execute();    
            pstmt.close();
        } catch (Exception ex) {
            System.err.println("Probeleme insert certificat dans la base: "+ex);
        } 
    }
    
    public X509Certificate getCertificate(String login){
        byte [] bytes =null;
        try {
            String sql2="select * from Certificat where login='"+login+"'";
            PreparedStatement ps=con.prepareStatement(sql2);
            ResultSet rs =ps.executeQuery();
            rs.next();
            bytes = rs.getBytes("certificat");
            ps.close();            
        } catch (Exception ex) {
            System.err.println("Probeleme recuperation de certificat a partir de la base: "+ex);
        }
        return Utils.Certificate.recreateCertFromBytes(bytes);
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
