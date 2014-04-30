/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.security.cert.X509Certificate;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author khaled
 */
public class MySQL_DB {

    private String url;// localhost
    private String login; //root
    private String password;// empty ""
    private Connection con;

    public MySQL_DB(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }

    public boolean connexion() {
        boolean retConn = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + this.url + "/PKI2014?user=" + login + "&password=" + password;
            con = DriverManager.getConnection(url, login, password);
            System.out.println("connexion a la base OK ");
            retConn = true;
        } catch (Exception ex) {
            System.err.println("erreur de connexion a la base: " + ex);
            retConn = false;
        }
        return retConn;
    }

    public boolean deconnexion() {
        boolean retDecon = false;
        try {
            con.close();
            System.out.println("deconnexion a la base OK ");
            retDecon = true;
        } catch (Exception ex) {

        }
        return retDecon;
    }

    public void insertCertificat(X509Certificate cert, String login) {
        try {
            byte[] bytes = cert.getEncoded();
            String sql = "insert into Certificat(login,certificat,certToRevok)values(?,?,?)";
            PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(sql);
            pstmt.setObject(1, login);
            pstmt.setBytes(2, bytes);
            pstmt.setBoolean(3, false);
            pstmt.execute();
            pstmt.close();
        } catch (Exception ex) {
            System.err.println("Probeleme insert certificat dans la base: " + ex);
        }
    }
    public  void updatChatPort(String login, int port){
        try{
            // create the java mysql update preparedstatement
            String query = "update Certificat set chatPort = ? where login = ?";
            PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
            preparedStmt.setInt   (1, port);
            preparedStmt.setString(2, login);
            // execute the java preparedstatement
            preparedStmt.executeUpdate();        
        }catch(Exception ex){
            System.err.println(ex.toString());
        }    
    }
    public void updatCertPort(String login, int port){
         try{
            // create the java mysql update preparedstatement
            String query = "update Certificat set certPort = ? where login = ?";
            PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
            preparedStmt.setInt   (1, port);
            preparedStmt.setString(2, login);
            // execute the java preparedstatement
            preparedStmt.executeUpdate();        
        }catch(Exception ex){
            System.err.println(ex.toString());
        }    
    }
    public boolean certIsTorevok(String login){
        boolean isto;
        try {
            String sql2 = "select * from Certificat where login='" + login + "'";
            PreparedStatement ps = con.prepareStatement(sql2);
            ResultSet rs = ps.executeQuery();
            rs.next();
            isto = rs.getBoolean("certToRevok");
            ps.close();
            return isto;
        } catch (Exception ex) {
            System.err.println("Probeleme recuperation certToRevok partir de la base: " + ex.toString());
            return false;
        }
    
    }
    public void revokCert(String login ){
            try{
            
            String query = "update Certificat set certToRevok = ? where login = ?";
            PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
            preparedStmt.setBoolean(1, true);
            preparedStmt.setString(2, login);
            // execute the java preparedstatement
            preparedStmt.executeUpdate();        
        }catch(Exception ex){
            System.err.println(ex.toString());
        }    
    }
    public int getChatport(String login){
        int port;
        try {
            String sql2 = "select * from Certificat where login='" + login + "'";
            PreparedStatement ps = con.prepareStatement(sql2);
            ResultSet rs = ps.executeQuery();
            rs.next();
            port = rs.getInt("chatPort");
            ps.close();
            return port;
        } catch (Exception ex) {
            System.err.println("Probeleme recuperation chatPort partir de la base: " + ex.toString());
            return 0;
        }
    }
    public int getCertPort(String login){
        int port;
        try {
            String sql2 = "select * from Certificat where login='" + login + "'";
            PreparedStatement ps = con.prepareStatement(sql2);
            ResultSet rs = ps.executeQuery();
            rs.next();
            port = rs.getInt("certPort");
            ps.close();
            return port;
        } catch (Exception ex) {
            System.err.println("Probeleme recuperation certPort partir de la base: " + ex.toString());
            return 0;
        }    
    }

    public X509Certificate getCertificate(String login) {
        byte[] bytes = null;
        try {
            String sql2 = "select * from Certificat where login='" + login + "'";
            PreparedStatement ps = con.prepareStatement(sql2);
            ResultSet rs = ps.executeQuery();
            rs.next();
            bytes = rs.getBytes("certificat");
            ps.close();
        } catch (Exception ex) {
            System.err.println("Probeleme recuperation de certificat a partir de la base: " + ex);
            return null;
        }
        return Utils.Certificate.recreateCertFromBytes(bytes);
    }

    public void addUser(String userLogin, byte[] password) {
        try {
            String insert = "insert into User(login,password)values(?,?)";
            PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(insert);
            pstmt.setObject(1, userLogin);
            pstmt.setBytes(2, password);
            pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            System.err.println("erreur d'ajout d'un user: "+ex);
        }
    }
    public boolean userExists(String login){
        boolean ret = false;
        try {
            String sql2 = "select * from User where login='" + login + "'";
            PreparedStatement ps = con.prepareStatement(sql2);
            ResultSet rs = ps.executeQuery();            
            if(rs.next())ret=true;
            ps.close();
        } catch (Exception ex) {
            System.err.println("Probeleme recherche user dans la base: " + ex);
        }
        return ret;
    }
    public byte[] getPassword(String login){
        byte[] ret = null;
        try {
            String sql2 = "select * from User where login='" + login + "'";
            PreparedStatement ps = con.prepareStatement(sql2);
            ResultSet rs = ps.executeQuery();            
            if(rs.next())ret= rs.getBytes("password");
            ps.close();
        } catch (Exception ex) {
            System.err.println("Probeleme recherche user dans la base: " + ex);
        }
        return ret;    
    }
    
    public String[] getListCAclient(){
        ArrayList<String> caList = new ArrayList<String>();
        try {
            String sql2 = "select * from Certificat where login!='CAroot'";
            PreparedStatement ps = con.prepareStatement(sql2);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                caList.add((String) rs.getObject("login"));
            }            
            ps.close();
        } catch (Exception ex) {
            System.err.println("Probeleme recherche user dans la base: " + ex);
        }        
        return (caList.toArray(new String[caList.size()]));
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
