/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.cert.X509CRLEntryHolder;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.BasicOCSPRespBuilder;
import org.bouncycastle.cert.ocsp.CertificateStatus;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.cert.ocsp.OCSPRespBuilder;
import org.bouncycastle.cert.ocsp.RevokedStatus;
import org.bouncycastle.cert.ocsp.SingleResp;
import org.bouncycastle.ocsp.CertificateID;
import org.bouncycastle.ocsp.OCSPException;
import org.bouncycastle.ocsp.OCSPReq;
import org.bouncycastle.ocsp.OCSPReqGenerator;
import org.bouncycastle.ocsp.Req;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

/**
 *
 * @author khaled
 */
public class OCSPchecker {
    public static org.bouncycastle.cert.ocsp.OCSPReq generateOCSPRequest(X509Certificate issuerCert, BigInteger serialNumber) throws OCSPException, OCSPException, CertificateEncodingException, OperatorCreationException, org.bouncycastle.cert.ocsp.OCSPException, IOException {
        /*
         * Generate an OCSP Request for the given serial.
         */

        // Generate the id for the certificate we are looking for
        org.bouncycastle.cert.ocsp.CertificateID id = new org.bouncycastle.cert.ocsp.CertificateID(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build().get(org.bouncycastle.cert.ocsp.CertificateID.HASH_SHA1), new X509CertificateHolder(issuerCert.getEncoded()), serialNumber);

        // basic request generation with nonce
        OCSPReqBuilder ocspGen = new OCSPReqBuilder();

        ocspGen.addRequest(id); //Add the serial to the request (could have made the possiblity to add multiples ones)

        //create a nonce to avoid replay attack
        BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());

        Extension ext = new Extension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce, true, new DEROctetString(nonce.toByteArray()));
        ocspGen.setRequestExtensions(new Extensions(new Extension[]{ext}));

        return ocspGen.build();//Notate thats the request is not signed
    }
    public static OCSPResp generateOCSPResponse(org.bouncycastle.cert.ocsp.OCSPReq request, X509Certificate caCert, PrivateKey privKey, X509CRLHolder crl) {

        int response = OCSPRespBuilder.INTERNAL_ERROR; // by default response as ERROR

        SubjectPublicKeyInfo keyinfo = SubjectPublicKeyInfo.getInstance(caCert.getPublicKey().getEncoded());
        BasicOCSPRespBuilder respGen;
        try {
            respGen = new BasicOCSPRespBuilder(keyinfo, new JcaDigestCalculatorProviderBuilder().setProvider("BC").build().get(org.bouncycastle.cert.ocsp.CertificateID.HASH_SHA1)); //Create builder
        } catch (Exception e) {
            return null;
        }

        Extension ext = request.getExtension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce);
        if (ext != null) {
            respGen.setResponseExtensions(new Extensions(new Extension[]{ext})); // Put the nonce back in the response
        }
        org.bouncycastle.cert.ocsp.Req[] requests = request.getRequestList();

        for (int i = 0; i != requests.length; i++) { //For all the Req in the Request

            org.bouncycastle.cert.ocsp.CertificateID certID = requests[i].getCertID();
            BigInteger serial = certID.getSerialNumber();

            if (serialNotInCRL(crl, serial)) { // If the certificate is not in the CRL
                respGen.addResponse(certID, CertificateStatus.GOOD); // Set the status to good
            } else {
                respGen.addResponse(certID, new RevokedStatus(new Date(), CRLReason.privilegeWithdrawn)); //Set status privilegeWithdrawn for the given ID
            }
        }

        try {
            ContentSigner contentSigner = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(privKey);
            BasicOCSPResp basicResp = respGen.build(contentSigner, new X509CertificateHolder[]{new X509CertificateHolder(caCert.getEncoded())}, new Date());
            response = OCSPRespBuilder.SUCCESSFUL; //Set response as successfull
            return new OCSPRespBuilder().build(response, basicResp); // build the reponse
        } catch (Exception e) {
            return null;
        }
    }
    public static String analyseResponse(OCSPResp response, org.bouncycastle.cert.ocsp.OCSPReq request, X509Certificate caCert) throws Exception {
        /*
         * Analyse the response send regarding the request the certificate that signed the response etc ..
         */
        BasicOCSPResp basicResponse = (BasicOCSPResp) response.getResponseObject(); // retrieve the Basic Resp of the Response

        // verify the response
        if (basicResponse.isSignatureValid(new JcaContentVerifierProviderBuilder().setProvider("BC").build(caCert.getPublicKey()))) {
            SingleResp[] responses = basicResponse.getResponses();

            byte[] reqNonce = request.getExtension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce).getExtnId().getEncoded();
            byte[] respNonce = basicResponse.getExtension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce).getExtnId().getEncoded();

            // validate the nonce if it is present
            if (reqNonce == null || Arrays.equals(reqNonce, respNonce)) { //If both nonce are equals

                String message = "";
                for (int i = 0; i != responses.length;) {
                    message += " certificate number " + responses[i].getCertID().getSerialNumber();
                    if (responses[i].getCertStatus() == CertificateStatus.GOOD) {
                        return message + " status: good";
                    } else {
                        return message + " status: revoked";
                    }
                }
                return message;
            } else {
                return "response nonce failed to validate";
            }
        } else {
            return "response failed to verify OCSP signature";
        }
    }
    public static void OCSPverify(X509Certificate caCert, X509Certificate clientCert){    
        try {
            org.bouncycastle.cert.ocsp.OCSPReq request= OCSPchecker.generateOCSPRequest(caCert, clientCert.getSerialNumber());
            
        } catch (IOException ex){
        
        } catch (OCSPException ex) {
            Logger.getLogger(OCSPchecker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateEncodingException ex) {
            Logger.getLogger(OCSPchecker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OperatorCreationException ex) {
            Logger.getLogger(OCSPchecker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (org.bouncycastle.cert.ocsp.OCSPException ex) {
            Logger.getLogger(OCSPchecker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static boolean serialNotInCRL(X509CRLHolder crl, BigInteger serial) {
		/*
		 * Return true if the serial is not in the crl, false otherwise
		 */
		X509CRLEntryHolder entry = crl.getRevokedCertificate(serial);
		if (entry == null) {
			return true;
		}
		else {
			System.out.println("Revocation Details:");
			System.out.println("Certificate number: " + entry.getSerialNumber());
			System.out.println("Issuer            : " +crl.getIssuer());
			if (entry.hasExtensions()) {
				Extension ext = entry.getExtension(X509Extension.reasonCode);
				if (ext != null) {
					DEREnumerated reasonCode;
					try {
						reasonCode = (DEREnumerated)X509ExtensionUtil.fromExtensionValue(ext.getExtnValue().getEncoded());
						System.out.println("Reason Code      : "+reasonCode.getValue());
					} catch (IOException e) {e.printStackTrace();	}
		        }
			}
			return false;
		}
	}

}
