/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import static sun.security.krb5.Confounder.bytes;

/**
 *
 * @author khaled
 */
public class Certificate {
    
    public static KeyPair generateKeyPair(){
        KeyPair pair = null;
        try {
            KeyPairGenerator key = KeyPairGenerator.getInstance("RSA");
            pair = key.generateKeyPair();
            //KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA", "BC");
            //kpGen.initialize(1024, new SecureRandom());
            //pair = kpGen.generateKeyPair();            
        } catch (NoSuchAlgorithmException ex) {
             System.err.println("erreur generation pair de cle CAroot :"+ex);
        }
        return pair;
    }
    public static X509Certificate generateCertForCAroot(KeyPair pair){        

        // Generate self-signed certificate
        X509Certificate cert=null;
        Security.addProvider(new BouncyCastleProvider());

        String subject = "CAroot";
        KeyPair keyPair = pair;
        String issuerName = "CAroot"; // Issuer same as subject
        BigInteger serialNumber = BigInteger.ONE;

        Calendar cal = Calendar.getInstance();
        Date notBefore = cal.getTime();
        cal.add(Calendar.YEAR, 1);
        Date notAfter = cal.getTime();

        JcaX509v3CertificateBuilder builder;

        X500Name subjectFormated = new X500NameBuilder(BCStyle.INSTANCE).addRDN(BCStyle.CN, subject).build();
        X500Name issuerFormated = new X500NameBuilder(BCStyle.INSTANCE).addRDN(BCStyle.CN, issuerName).build();
        builder = new JcaX509v3CertificateBuilder(issuerFormated, serialNumber, notBefore, notAfter, subjectFormated, keyPair.getPublic());
        try{
        //Signer will be the same ourselves
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(keyPair.getPrivate());//our own key

        //------------------------- Extensions ------------------------
        builder.addExtension(org.bouncycastle.asn1.x509.X509Extension.basicConstraints, true, new BasicConstraints(1)); //Should be critics

        SubjectKeyIdentifier subjectKeyIdentifier = new JcaX509ExtensionUtils().createSubjectKeyIdentifier(keyPair.getPublic());
        builder.addExtension(org.bouncycastle.asn1.x509.X509Extension.subjectKeyIdentifier, false, subjectKeyIdentifier);

        KeyUsage keyUsage = new KeyUsage(KeyUsage.keyCertSign);
        builder.addExtension(org.bouncycastle.asn1.x509.X509Extension.keyUsage, true, keyUsage); //KeyUsage must be critic

        ExtendedKeyUsage extendedKeyUsage = new ExtendedKeyUsage(KeyPurposeId.anyExtendedKeyUsage);
        builder.addExtension(org.bouncycastle.asn1.x509.X509Extension.extendedKeyUsage, false, extendedKeyUsage);
        X509CertificateHolder holder = builder.build(contentSigner);
        cert = (X509Certificate) java.security.cert.CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(holder.getEncoded()));
        }catch(Exception ex){
            System.err.println("erreur generation certificat auto singé CAroot :"+ex);
        }
        return cert;
    }
    
    public static X509Certificate recreateCertFromBytes(byte [] certBytes){
        X509Certificate cert= null;
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            InputStream in = new ByteArrayInputStream(certBytes);
            cert = (X509Certificate)certFactory.generateCertificate(in);            
        } catch (CertificateException ex) {
            Logger.getLogger(Certificate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cert;
    
    
    }
    
}
