/*
 * Project: e-CODEX Connector - Container Services/DSS
 * Contractor: ARHS-Developments
 *
 * $HeadURL: http://forge.aris-lux.lan/svn/dgmarktdss/ecodex/src/main/java/eu/ecodex/dss/service/impl/dss/SigningUtil.java $
 * $Revision: 1879 $
 * $Date: 2013-04-18 09:39:53 +0200 (jeu., 18 avr. 2013) $
 * $Author: meyerfr $
 */
package eu.ecodex.dss.service.impl.dss;

import java.io.File;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.ecodex.dss.model.SignatureParameters;
import eu.europa.esig.dss.ASiCContainerType;
import eu.europa.esig.dss.AbstractSerializableSignatureParameters;
import eu.europa.esig.dss.AbstractSignatureParameters;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.EncryptionAlgorithm;
import eu.europa.esig.dss.InMemoryDocument;
import eu.europa.esig.dss.MimeType;
import eu.europa.esig.dss.SignatureAlgorithm;
import eu.europa.esig.dss.SignatureLevel;
import eu.europa.esig.dss.SignaturePackaging;
import eu.europa.esig.dss.SignatureValue;
import eu.europa.esig.dss.ToBeSigned;
import eu.europa.esig.dss.signature.AbstractSignatureService;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.validation.CertificateVerifier;
//import eu.europa.ec.markt.dss.DSSUtils;
//import eu.europa.ec.markt.dss.DigestAlgorithm;
//import eu.europa.ec.markt.dss.EncryptionAlgorithm;
//import eu.europa.ec.markt.dss.signature.DSSDocument;
//import eu.europa.ec.markt.dss.signature.DocumentSignatureService;
//import eu.europa.ec.markt.dss.signature.SignatureLevel;
//import eu.europa.ec.markt.dss.signature.SignaturePackaging;
//import eu.europa.ec.markt.dss.signature.asic.ASiCService;
//import eu.europa.ec.markt.dss.signature.pades.PAdESService;
//import eu.europa.ec.markt.dss.signature.xades.XAdESService;
//import eu.europa.ec.markt.dss.validation102853.CertificateVerifier;
//import eu.europa.ec.markt.dss.validation102853.CommonCertificateVerifier;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;
import eu.europa.esig.dss.asic.ASiCWithXAdESSignatureParameters;
import eu.europa.esig.dss.asic.signature.ASiCWithXAdESService;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;

/**
 * a utility class used for signing documents in specific flavours
 * <p/>
 * <p>
 * DISCLAIMER: Project owner e-CODEX
 * </p>
 *
 * @author <a href="mailto:eCodex.Project-DSS@arhs-developments.com">ARHS Developments</a>
 * @version $Revision: 1879 $ - $Date: 2013-04-18 09:39:53 +0200 (jeu., 18 avr. 2013) $
 */
class SigningUtil {
	/**
	 * utility constructor
	 */
	private SigningUtil() {
	}

	/**
	 * signs a document with an ASiC_S_BES/DETACHED signature
	 *
	 * @param signingParameters the signing parameters (algorithms, certificates and private key)
	 * @param document          the to be signed document
	 * @return a new document based on the input and signed
	 * @throws java.security.NoSuchAlgorithmException from the underlying classes
	 */
	static DSSDocument signASiC(final SignatureParameters signingParameters, final DSSDocument document) throws Exception {
		
		final ASiCWithXAdESSignatureParameters params = new ASiCWithXAdESSignatureParameters();
		params.setSignatureLevel(SignatureLevel.XAdES_BASELINE_B);
		params.aSiC().setContainerType(ASiCContainerType.ASiC_S);
		params.bLevel().setSigningDate(new Date());
		params.setCertificateChain(signingParameters.getCertificateChain());
		params.setSigningCertificate(signingParameters.getCertificate());
		
		final CertificateVerifier certificateVerifier = new CommonCertificateVerifier();
		final DocumentSignatureService<ASiCWithXAdESSignatureParameters> signatureService = new ASiCWithXAdESService(certificateVerifier);
		
		ToBeSigned bytesToSign = signatureService.getDataToSign(document, params);
		
		// SignatureValue signatureValue = TestUtils.sign(params.getSignatureAlgorithm(), privateKeyEntry, dataToSign);
		final SignatureAlgorithm sigAlgorithm = params.getSignatureAlgorithm();
		final String jceSignatureAlgorithm = sigAlgorithm.getJCEId();
		final Signature signature = Signature.getInstance(jceSignatureAlgorithm);
		signature.initSign(signingParameters.getPrivateKey());
		signature.update(bytesToSign.getBytes());
		final byte[] signatureValue = signature.sign();
		final SignatureValue signedData = new SignatureValue(sigAlgorithm, signatureValue);
		
		final DSSDocument signedDocument = signatureService.signDocument(document, params, signedData); 
		
		return signedDocument;
	}

	/**
	 * signs a document with an PAdES_BES/ENVELOPED signature
	 *
	 * @param signingParameters the signing parameters (algorithms, certificates and private key)
	 * @param document          the to be signed document
	 * @return a new document based on the input and signed
	 * @throws java.security.NoSuchAlgorithmException from the underlying classes
	 */
	static DSSDocument signPAdES(final SignatureParameters signingParameters, final DSSDocument document) throws Exception {

		final CertificateVerifier certificateVerifier = new CommonCertificateVerifier(true);
		final DocumentSignatureService<PAdESSignatureParameters> signatureService = new PAdESService(certificateVerifier);

		final EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithm.forName(signingParameters.getSignatureAlgorithm());
		final DigestAlgorithm digestAlgorithm = DigestAlgorithm.forName(signingParameters.getDigestAlgorithm());

		final PAdESSignatureParameters params = new PAdESSignatureParameters();

		params.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
		params.setSignaturePackaging(SignaturePackaging.ENVELOPED);
		params.setEncryptionAlgorithm(encryptionAlgorithm);
		params.setDigestAlgorithm(digestAlgorithm);
		params.setCertificateChain(signingParameters.getCertificateChain());
		params.setSigningCertificate(signingParameters.getCertificate());
		
		ToBeSigned bytesToSign = signatureService.getDataToSign(document, params);
		
		final SignatureAlgorithm sigAlgorithm = params.getSignatureAlgorithm();
		final String jceSignatureAlgorithm = sigAlgorithm.getJCEId();

		// final SignatureValue signatureValue = DSSUtils.encrypt(jceSignatureAlgorithm, privateKey, bytesToSign);
		final Signature signature = Signature.getInstance(jceSignatureAlgorithm);
		signature.initSign(signingParameters.getPrivateKey());
		signature.update(bytesToSign.getBytes());
		final byte[] signatureValue = signature.sign();
		final SignatureValue signedData = new SignatureValue(sigAlgorithm, signatureValue);

		final DSSDocument signedDocument = signatureService.signDocument(document, params, signedData); 

		return signedDocument;
	}

	/**
	 * signs a document with an XAdES_BES signature with the packaging in parameter
	 *
	 * @param signingParameters  the signing parameters (algorithms, certificates and private key)
	 * @param document           the to be signed document
	 * @param signaturePackaging the packaging (ENVELOPED or DETACHED)
	 * @return a new document based on the input and signed
	 * @throws java.security.NoSuchAlgorithmException from the underlying classes
	 */
	static DSSDocument signXAdES(final SignatureParameters signingParameters, final DSSDocument document, final SignaturePackaging signaturePackaging) throws Exception {

		final CertificateVerifier certificateVerifier = new CommonCertificateVerifier();
		final XAdESService signatureService = new XAdESService(certificateVerifier);

		final String encryptionAlgorithm_ = signingParameters.getSignatureAlgorithm();
		final EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithm.forName(encryptionAlgorithm_);
		final DigestAlgorithm digestAlgorithm = DigestAlgorithm.forName(signingParameters.getDigestAlgorithm());

		final XAdESSignatureParameters params = new XAdESSignatureParameters();
		params.setSignatureLevel(SignatureLevel.XAdES_BASELINE_B);
		params.setSignaturePackaging(signaturePackaging); 
		params.setEncryptionAlgorithm(encryptionAlgorithm);
		params.setDigestAlgorithm(digestAlgorithm);
		params.setCertificateChain(signingParameters.getCertificateChain());
		params.setSigningCertificate(signingParameters.getCertificate());
		params.bLevel().setSigningDate(new Date());
		//params.setEmbedXML(true);
		
		ToBeSigned bytesToSign = signatureService.getDataToSign(document, params);
		
		final SignatureAlgorithm sigAlgorithm = params.getSignatureAlgorithm();
		final String jceSignatureAlgorithm = sigAlgorithm.getJCEId();

		// final SignatureValue signatureValue = DSSUtils.encrypt(jceSignatureAlgorithm, privateKey, bytesToSign);
		final Signature signature = Signature.getInstance(jceSignatureAlgorithm);
		signature.initSign(signingParameters.getPrivateKey());
		signature.update(bytesToSign.getBytes());
		final byte[] signatureValue = signature.sign();
		final SignatureValue signedData = new SignatureValue(sigAlgorithm, signatureValue);

		final DSSDocument signedDocument = signatureService.signDocument(document, params, signedData); 

		return signedDocument;
	}
}
