/*
 * Project: e-CODEX Connector - Container Services/DSS
 * Contractor: ARHS-Developments
 *
 * $HeadURL: http://forge.aris-lux.lan/svn/dgmarktdss/ecodex/src/main/java/eu/ecodex/dss/util/SignatureParametersFactory.java $
 * $Revision: 1879 $
 * $Date: 2013-04-18 09:39:53 +0200 (jeu., 18 avr. 2013) $
 * $Author: meyerfr $
 */

package eu.ecodex.dss.util;

import java.io.InputStream;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import eu.ecodex.dss.model.CertificateStoreInfo;
import eu.ecodex.dss.model.SignatureParameters;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.EncryptionAlgorithm;
//import eu.europa.ec.markt.dss.DSSUtils;
//import eu.europa.ec.markt.dss.DigestAlgorithm;
//import eu.europa.ec.markt.dss.EncryptionAlgorithm;
//import eu.europa.ec.markt.dss.parameter.ChainCertificate;
//import eu.europa.ec.markt.dss.validation102853.CertificateToken;
import eu.europa.esig.dss.x509.CertificateToken;

/**
 * Provides convenience-methods for creating a {@link SignatureParameters} instance.
 * <p/>
 * <p>
 * DISCLAIMER: Project owner e-CODEX
 * </p>
 *
 * @author <a href="mailto:eCodex.Project-DSS@arhs-developments.com">ARHS Developments</a>
 * @version $Revision: 1879 $ - $Date: 2013-04-18 09:39:53 +0200 (jeu., 18 avr. 2013) $
 */
public class SignatureParametersFactory {

	private static final LogDelegate LOG = new LogDelegate(SignatureParametersFactory.class);

	/**
	 * utility constructor
	 */
	private SignatureParametersFactory() {
	}

	/**
	 * convenience method to be used if the algorithms are set externally.
	 *
	 * @param certStoreInfo access information for the keystore
	 * @param certAlias     the alias of the certificate that should be used
	 * @param certPassword  the password to get the (required) private key
	 * @return a {@link SignatureParameters} instance (or null if provided data insufficient or cert not found)
	 * @throws Exception as of the underlying classes
	 */
	public static SignatureParameters create(final CertificateStoreInfo certStoreInfo, final String certAlias, final String certPassword) throws Exception {
		return create(certStoreInfo, certAlias, certPassword, null, null);
	}

	/**
	 * loads a default keystore and extracts - via the alias - the certificate upon which the SignatureParameters is created.
	 *
	 * @param certStoreInfo       access information for the keystore
	 * @param certAlias           the alias of the certificate that should be used
	 * @param certPassword        the password to get the (required) private key
	 * @param encryptionAlgorithm the algorithm used to sign
	 * @param digestAlgorithm     the algorithm used to create the digest
	 * @return a {@link SignatureParameters} instance (or null if provided data insufficient or cert not found)
	 * @throws Exception as of the underlying classes
	 */
	public static SignatureParameters create(final CertificateStoreInfo certStoreInfo, final String certAlias, final String certPassword,
	                                         final EncryptionAlgorithm encryptionAlgorithm, final DigestAlgorithm digestAlgorithm) throws Exception {
		LOG.lDetail("parameters: {} for cert-alias '{}' algorithms: signature {} signing {}", certStoreInfo, certAlias, encryptionAlgorithm, digestAlgorithm);

		// check if configuration is feasible
		if (certStoreInfo == null) {
			LOG.lWarn("no information about the keystore provided");
			return null;
		}
		if (!certStoreInfo.isValid()) {
			LOG.lWarn("the information about the keystore is invalid");
			return null;
		}
		if (StringUtils.isEmpty(certAlias)) {
			LOG.lWarn("the alias for getting the certificate is empty/null");
			return null;
		}
		if (certPassword == null) {
			LOG.lWarn("the password for getting the private key is null");
			return null;
		}

		final URL ksLocation = new URL(certStoreInfo.getLocation());
		LOG.lDetail("loading keystore from url: {}", ksLocation);

		final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

		final InputStream ksStream = ksLocation.openStream();
		try {
			ks.load(ksStream, (certStoreInfo.getPassword() == null) ? null : certStoreInfo.getPassword().toCharArray());
		} finally {
			IOUtils.closeQuietly(ksStream);
		}

		LOG.lDetail("acquiring certificate for alias: {}", certAlias);
		final Certificate cert = ks.getCertificate(certAlias);
		if (cert == null) {
			LOG.lWarn("the certificate for alias {} could not be found in the keystore");
			return null;
		}
		if (!(cert instanceof X509Certificate)) {
			LOG.lWarn("the certificate for alias {} does not represent an X509Certificate");
			return null;
		}

		LOG.lDetail("acquiring private key for alias: {}", certAlias);
		final Key key = ks.getKey(certAlias, certPassword.toCharArray());
		if (key == null) {
			LOG.lWarn("the key for alias {} could not be found in the keystore");
			return null;
		}
		if (!(key instanceof PrivateKey)) {
			LOG.lWarn("the key for alias {} does not represent a PrivateKey");
			return null;
		}

		final SignatureParameters params = new SignatureParameters();
		CertificateToken tkn = new CertificateToken((X509Certificate) cert);
		params.setCertificate(tkn);
		params.setPrivateKey((PrivateKey) key);

		final List<CertificateToken> x509Certs = new ArrayList<CertificateToken>();
		final Certificate[] certs = ks.getCertificateChain(certAlias);
		for (final Certificate certificate : certs) {
			if (certificate instanceof X509Certificate) {
				CertificateToken chainCert = new CertificateToken((X509Certificate) certificate);
				x509Certs.add(chainCert);
			} else {
				LOG.lWarn("the alias {} has a certificate chain item that does not represent an X509Certificate; it is ignored");
			}
		}
		params.setCertificateChain(x509Certs);

		params.setSignatureAlgorithm((encryptionAlgorithm == null) ? ((PrivateKey) key).getAlgorithm() : encryptionAlgorithm.getName());
		params.setDigestAlgorithm((digestAlgorithm == null) ? null : digestAlgorithm.getName());

		return params;
	}

}
