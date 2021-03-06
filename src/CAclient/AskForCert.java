/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CAclient;

import CAroot.CAroot;
import Chat.CaListChat;
import Chat.CA;
import Chat.ServerChatCa;
import ServiceCert.ClientCert;
import ServiceCert.ServerCert;
import Utils.Keys;
import Utils.FileChooser;
import Utils.MySQL_DB;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static java.lang.Thread.sleep;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JList;

/**
 *
 * @author khaled
 */
public class AskForCert extends javax.swing.JFrame {
    /**
     * Creates new form AskForCert
     */
    CAclient caClient;
    
    public AskForCert( CAclient ca) {
        initComponents();        
        this.caClient = ca;
        MyMouseListenr mouseListener = new MyMouseListenr(this.caClient, this);
        jList1.addMouseListener(mouseListener);
        MySQL_DB db = new MySQL_DB(ca.bd_url, ca.bd_login, ca.bd_pass);
        db.connexion();
        String listCA[] = db.getListCAclient();
        jList1.setListData(listCawithoutRoot(listCA));
        db.deconnexion();        
    }
    public static String[] listCawithoutRoot(String [] calist){
        ArrayList<String> l = new ArrayList<String>();
        for(int i=0; i<calist.length;i++){
            if(!calist[i].equals(CAroot.CaRootlogin)){
                l.add(calist[i]);
            }     
        }
        return (l.toArray(new String[l.size()]));
    }
    
    private class MyMouseListenr implements MouseListener{
        CAclient caClient;
        JFrame frame;
        public MyMouseListenr( CAclient ca,  JFrame frame){
            this.caClient = ca;
            this.frame = frame;
        }
        public void mouseClicked(MouseEvent me) {
            JList theList = (JList) me.getSource();
                if (me.getClickCount() == 2) {
                    int index = theList.locationToIndex(me.getPoint());
                    if (index >= 0) {
                        Object o = theList.getModel().getElementAt(index);                        
                        MySQL_DB db = new MySQL_DB(caClient.bd_url, caClient.bd_login, caClient.bd_pass);
                        db.connexion();
                        int port = db.getCertPort(o.toString());
                        X509Certificate cert = db.getCertificate(o.toString());                        
                        CA caThclientCert = new CA(caClient.keyPair, caClient.bd_url, caClient.bd_pass, caClient.bd_login, caClient.login);
                        ClientCert ask =new ClientCert(caThclientCert, port, "localhost", cert.getPublicKey());
                        ask.start();
                        //attente certification
                        boolean attente = true;
                        while (attente) {
                            if (db.getCertificate(caClient.login) != null) {
                                attente = false;
                            } else {
                                try {
                                    sleep(1000);
                                } catch (InterruptedException ex) {
                                    System.err.println("erreur attente certification" + ex.toString());
                                }
                            }
                        }
                        db.deconnexion();
                        CA caThservertCert = new CA(caClient.keyPair, caClient.bd_url, caClient.bd_pass, caClient.bd_login, caClient.login);
                        ServerCert serveurCert = new  ServerCert(caThservertCert, 0);
                        serveurCert.start();
                        CA caThservertChat = new CA(caClient.keyPair, caClient.bd_url, caClient.bd_pass, caClient.bd_login, caClient.login);
                        ServerChatCa serveurChat = new ServerChatCa(caThservertChat, 0);
                        serveurChat.start();
                        CA caChatList = new CA(caClient.keyPair, caClient.bd_url, caClient.bd_pass, caClient.bd_login, caClient.login);  
                        JFrame chat = new CaListChat(caChatList);
                        chat.setVisible(true);
                        chat.setLocationRelativeTo(null);
                        frame.setVisible(false);
                        System.out.println("Double-clicked on: " + o.toString());
                    }                }
        }
        public void mousePressed(MouseEvent me) {}
        public void mouseReleased(MouseEvent me) {}
        public void mouseEntered(MouseEvent me) {}
        public void mouseExited(MouseEvent me) {}   
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("CA Client: demande certification");

        jButton1.setText("Demande au CAroot");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Demande a un CA deja certifié:");

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String path = FileChooser.openFile();
        if(path != null) {
            PublicKey caRootPkey = Keys.recreatePublicKey(path);
            //se connecter au CAroot et lui envoyer la demande de certification
            //cryptée avec sa clé publique
            
            MySQL_DB db = new MySQL_DB(caClient.bd_url, caClient.bd_login, caClient.bd_pass);
            db.connexion();
            X509Certificate cert= db.getCertificate(CAroot.CaRootlogin);
            int port = db.getCertPort(CAroot.CaRootlogin);
            
            CA caClientcert = new CA(caClient.keyPair, caClient.bd_url, caClient.bd_pass, caClient.bd_login, caClient.login);
            ClientCert ask =new ClientCert(caClientcert, port, "localhost", cert.getPublicKey());
            ask.start();
            //attente certification
            boolean attente =true;
            while(attente){
                if(db.getCertificate(caClient.login)!=null){
                    attente =false;
                }else{
                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        System.err.println("erreur attente certification"+ex.toString());
                    }
                }            
            }            
            db.deconnexion();
            CA caServerCert = new CA(caClient.keyPair, caClient.bd_url, caClient.bd_pass, caClient.bd_login, caClient.login);
            ServerCert serveurCert = new  ServerCert(caServerCert, 0);
            serveurCert.start();
            CA caServerChat = new CA(caClient.keyPair, caClient.bd_url, caClient.bd_pass, caClient.bd_login, caClient.login);
            ServerChatCa serveurChat = new ServerChatCa(caServerChat, 0);
            serveurChat.start();
            CA ca = new CA(caClient.keyPair, caClient.bd_url, caClient.bd_pass, caClient.bd_login, caClient.login);
            JFrame frame = new CaListChat(ca);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            this.setVisible(false);
        }        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AskForCert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AskForCert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AskForCert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AskForCert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CAclient ca = new CAclient("", "root", "localhost", "Admin", "Admin");
                JFrame frame = new AskForCert(ca);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
