/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
/**
 *
 * @author khaled
 */
public class CSRbuilder {    
    public static PKCS10CertificationRequest createCSR(KeyPair keys , String login){
        PKCS10CertificationRequestBuilder csrgen = null;
        ContentSigner contentSigner = null;
        try {
            Security.addProvider(new BouncyCastleProvider());
            X500Name subjectName = new X500Name("cn="+login);
            SubjectPublicKeyInfo keyInfo = SubjectPublicKeyInfo.getInstance(keys.getPublic().getEncoded());
            csrgen = new PKCS10CertificationRequestBuilder(subjectName, keyInfo);
            contentSigner = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(keys.getPrivate());//Un peu étrange qu'on utilise notre clé privée
            return csrgen.build(contentSigner);
        } catch (Exception ex) {
            System.err.println("Probeleme de creartion csr: "+ex);
        }
          return csrgen.build(contentSigner);
    }    
    
    public static X509Certificate createCertOfCSR(PKCS10CertificationRequest csr, KeyPair caKeys, X509Certificate caCert){
        X509Certificate cert=null;
        try {
            BigInteger bigInt = new BigInteger(String.valueOf(System.currentTimeMillis()));
            Security.addProvider(new BouncyCastleProvider());
            AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA1withRSA");
            AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);            
            AsymmetricKeyParameter parameterCa = PrivateKeyFactory.createKey(caKeys.getPrivate().getEncoded());
            SubjectPublicKeyInfo keyInfo = csr.getSubjectPublicKeyInfo();
            Calendar cal = Calendar.getInstance();
            Date notbefore = cal.getTime();
            cal.add(Calendar.YEAR, 2); // Define the validity of 2 years
            Date notafter = cal.getTime();
            X509v3CertificateBuilder myCertificateGenerator = new X509v3CertificateBuilder(new X500Name(caCert.getSubjectDN().getName()), bigInt, notbefore, notafter, csr.getSubject(), keyInfo);            
            ContentSigner sigGen = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(parameterCa);          
            myCertificateGenerator.addExtension(X509Extension.basicConstraints, true, new BasicConstraints(false));            
            myCertificateGenerator.addExtension(X509Extension.authorityKeyIdentifier, false, new JcaX509ExtensionUtils().createAuthorityKeyIdentifier(caCert));
            SubjectKeyIdentifier subjectKeyIdentifier = new JcaX509ExtensionUtils().createSubjectKeyIdentifier(keyInfo);
            myCertificateGenerator.addExtension(X509Extension.subjectKeyIdentifier, false, subjectKeyIdentifier);
            KeyUsage keyUsage = new KeyUsage(KeyUsage.digitalSignature | KeyUsage.nonRepudiation | KeyUsage.keyEncipherment | KeyUsage.dataEncipherment | KeyUsage.digitalSignature);
            myCertificateGenerator.addExtension(X509Extension.keyUsage, true, keyUsage);
            X509CertificateHolder holder = myCertificateGenerator.build(sigGen);
            cert = (X509Certificate) java.security.cert.CertificateFactory.getInstance("X.509", "BC").generateCertificate(new ByteArrayInputStream(holder.getEncoded()));
        } catch (Exception ex) {
            System.err.println("Probeleme de creartion de certificat pour le client a partir du csr: "+ex);
        }
        return cert;

    }
    
}
