/*
 * Project: e-CODEX Connector - Container Services/DSS
 * Contractor: ARHS-Developments
 *
 * $HeadURL: http://forge.aris-lux.lan/svn/dgmarktdss/ecodex/src/main/java/eu/ecodex/dss/model/SignatureParameters.java $
 * $Revision: 1879 $
 * $Date: 2013-04-18 09:39:53 +0200 (jeu., 18 avr. 2013) $
 * $Author: meyerfr $
 */
package eu.ecodex.dss.model;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;

import eu.europa.esig.dss.x509.CertificateToken;


/**
 * Holds attributes to create a signature.
 * This is a simple POJO - no checks are done.
 * 
 * <p>DISCLAIMER: Project owner e-CODEX</p>
 *
 * @author <a href="mailto:eCodex.Project-DSS@arhs-developments.com">ARHS Developments</a>
 * @version $Revision: 1879 $ - $Date: 2013-04-18 09:39:53 +0200 (jeu., 18 avr. 2013) $
 */
public class SignatureParameters {

	/**
	 * used for getting the identity of the signer; business-wise mandatory (but not in this pojo)
	 */
	private CertificateToken certificate;

	/**
	 * the private key of the signer; relates to the {@link #certificate}; business-wise mandatory (but not in this pojo)
	 */
	private PrivateKey privateKey;

	/**
	 * the chain of certificates from the signer up to his root; must include the signer's {@link #certificate} as the first one; optional
	 */
	private List<CertificateToken> certificateChain;

	/**
	 * the algorithm used for signing; business-wise mandatory (but not in this pojo)
	 */
	private String signatureAlgorithm;

	/**
	 * the algorithm used for the digest; business-wise mandatory (but not in this pojo)
	 */
	private String digestAlgorithm;

	/**
	 * returns the certificate; used for getting the identity of the signer; business-wise mandatory (but not in this pojo)
	 *
	 * @return the value
	 */
	public CertificateToken getCertificate() {
		return certificate;
	}

	/**
	 * sets the certificate; used for getting the identity of the signer; business-wise mandatory (but not in this pojo)
	 *
	 * @param certificate the value
	 * @return this class' instance for chaining
	 */
	public SignatureParameters setCertificate(final CertificateToken certificate) {
		this.certificate = certificate;
		return this;
	}

	/**
	 * returns the private key of the signer; relates to the {@link #certificate}; business-wise mandatory (but not in this pojo)
	 *
	 * @return the value
	 */
	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * sets the private key of the signer; relates to the {@link #certificate}; business-wise mandatory (but not in this pojo)
	 *
	 * @param privateKey the value
	 * @return this class' instance for chaining
	 */
	public SignatureParameters setPrivateKey(final PrivateKey privateKey) {
		this.privateKey = privateKey;
		return this;
	}

	/**
	 * the chain of certificates from the signer up to his root; must include the signer's {@link #certificate} as the first one; optional
	 *
	 * @return the value
	 */
	public List<CertificateToken> getCertificateChain() {
		return certificateChain;
	}

	/**
	 * the chain of certificates from the signer up to his root; must include the signer's {@link #certificate} as the first one; optional
	 *
	 * @param certificateChain the value
	 * @return this class' instance for chaining
	 */
	public SignatureParameters setCertificateChain(final List<CertificateToken> certificateChain) {
		this.certificateChain = certificateChain;
		return this;
	}

	/**
	 * the algorithm used for signing; business-wise mandatory (but not in this pojo)
	 *
	 * @return the value
	 */
	public String getSignatureAlgorithm() {
		return signatureAlgorithm;
	}

	/**
	 * the algorithm used for signing; business-wise mandatory (but not in this pojo)
	 *
	 * @param encryptionAlgorithm the value
	 * @return this class' instance for chaining
	 */
	public SignatureParameters setSignatureAlgorithm(final String encryptionAlgorithm) {
		this.signatureAlgorithm = encryptionAlgorithm;
		return this;
	}

	/**
	 * the algorithm used for the digest; business-wise mandatory (but not in this pojo)
	 *
	 * @return the value
	 */
	public String getDigestAlgorithm() {
		return digestAlgorithm;
	}

	/**
	 * the algorithm used for the digest; business-wise mandatory (but not in this pojo)
	 *
	 * @param digestAlgorithm the value
	 * @return this class' instance for chaining
	 */
	public SignatureParameters setDigestAlgorithm(final String digestAlgorithm) {
		this.digestAlgorithm = digestAlgorithm;
		return this;
	}
}
