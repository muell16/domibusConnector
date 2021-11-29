package eu.domibus.connector.ui.view.areas.configuration.security.importoldconfig;

import eu.domibus.connector.domain.enums.AdvancedElectronicSystemType;
import eu.domibus.connector.dss.configuration.SignatureConfigurationProperties;
import eu.domibus.connector.dss.configuration.SignatureValidationConfigurationProperties;
import eu.domibus.connector.evidences.spring.EvidencesIssuerInfo;
import eu.domibus.connector.evidences.spring.EvidencesToolkitConfigurationProperties;
import eu.domibus.connector.evidences.spring.PostalAdressConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.KeyConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import eu.domibus.connector.security.configuration.DCBusinessDocumentValidationConfigurationProperties;
import eu.domibus.connector.security.configuration.DCEcodexContainerProperties;

import java.util.Map;

public class OldConfigMapper {

    private static final String TOKEN_ISSUER_COUNTRY = "token.issuer.country";
    private static final String TOKEN_ISSUER_SERVICE_PROVIDER = "token.issuer.service-provider";
    private static final String TOKEN_ISSUER_IDENTITY_PROVIDER = "token.issuer.identity-provider";
    private static final String TOKEN_ISSUER_AES_TYPE = "token.issuer.advanced-electronic-system-type";

    private static final String SECURITY_TRUSTSTORE_PATH = "connector.security.trust-store.path";
    private static final String SECURITY_TRUSTSTORE_PASSWORD = "connector.security.trust-store.password";


    private static final String SECURITY_KEYSTORE_PATH = "connector.security.keystore.path";
    private static final String SECURITY_KEYSTORE_PASSWORD = "connector.security.keystore.password";
    private static final String SECURITY_PRIVATE_KEY_ALIAS = "connector.security.private-key.alias";
    private static final String SECURITY_PRIVATE_KEY_PASSWORD = "connector.security.private-key.password";

    private static final String EVIDENCE_KEYSTORE_PATH = "connector.evidences.key-store.path";
    private static final String EVIDENCE_KEYSTORE_PASSWORD = "connector.evidences.key-store.password";
    private static final String EVIDENCE_PRIVATE_KEY_ALIAS = "connector.evidences.private-key.alias";
    private static final String EVIDENCE_PRIVATE_KEY_PASSWORD = "connector.evidences.private-key.password";

    private static final String POSTAL_ADDR_STREET = "postal.address.street";
    private static final String POSTAL_ADDR_LOCALITY = "postal.address.locality";
    private static final String POSTAL_ADDR_ZIPCODE = "postal.address.zip-code";
    private static final String POSTAL_ADDR_COUNTRY = "postal.address.country";

    private final Map<String, String> oldProperties;

    public OldConfigMapper(Map<String, String> oldProperties) {
        this.oldProperties = oldProperties;
    }

    public DCBusinessDocumentValidationConfigurationProperties migrateBusinessDocumentConfigurationProperties() {
        DCBusinessDocumentValidationConfigurationProperties c = new DCBusinessDocumentValidationConfigurationProperties();
        c.setServiceProvider(oldProperties.get(TOKEN_ISSUER_SERVICE_PROVIDER));
        c.setCountry(oldProperties.get(TOKEN_ISSUER_COUNTRY));
        c.setDefaultAdvancedSystemType(AdvancedElectronicSystemType.valueOf(oldProperties.get(TOKEN_ISSUER_AES_TYPE)));

        if (oldProperties.containsKey(TOKEN_ISSUER_IDENTITY_PROVIDER)) {
            c.setAuthenticationValidation(new DCBusinessDocumentValidationConfigurationProperties.AuthenticationValidationConfigurationProperties());
            c.getAuthenticationValidation().setIdentityProvider(oldProperties.get(TOKEN_ISSUER_IDENTITY_PROVIDER));
        }

        c.setSignatureValidation(new SignatureValidationConfigurationProperties());
        c.getSignatureValidation().setTrustStoreEnabled(true);
        StoreConfigurationProperties trustStore = new StoreConfigurationProperties();
        c.getSignatureValidation().setTrustStore(trustStore);
        trustStore.setPath(oldProperties.get(SECURITY_TRUSTSTORE_PATH));
        trustStore.setPassword(oldProperties.get(SECURITY_TRUSTSTORE_PASSWORD));
        trustStore.setType("JKS");

        return c;
    }

    public DCEcodexContainerProperties migrateEcodexContainerProperties() {
        DCEcodexContainerProperties c = new DCEcodexContainerProperties();

        SignatureConfigurationProperties signature = new SignatureConfigurationProperties();
        c.setSignature(signature);
        StoreConfigurationProperties keyStore = new StoreConfigurationProperties();
        keyStore.setPassword(oldProperties.get(SECURITY_KEYSTORE_PASSWORD));
        keyStore.setPath(oldProperties.get(SECURITY_KEYSTORE_PATH));
        keyStore.setType("JKS");
        signature.setKeyStore(keyStore);
        KeyConfigurationProperties privateKey = new KeyConfigurationProperties();
        privateKey.setAlias(oldProperties.get(SECURITY_PRIVATE_KEY_ALIAS));
        privateKey.setPassword(oldProperties.get(SECURITY_PRIVATE_KEY_PASSWORD));
        signature.setPrivateKey(privateKey);

        c.setSignatureValidation(new SignatureValidationConfigurationProperties());
        c.getSignatureValidation().setTrustStoreEnabled(true);
        StoreConfigurationProperties trustStore = new StoreConfigurationProperties();
        c.getSignatureValidation().setTrustStore(trustStore);
        trustStore.setPath(oldProperties.get(SECURITY_TRUSTSTORE_PATH));
        trustStore.setPassword(oldProperties.get(SECURITY_TRUSTSTORE_PASSWORD));
        trustStore.setType("JKS");

        return c;
    }

    public EvidencesToolkitConfigurationProperties migrateEvidencesToolkitConfig() {
        EvidencesToolkitConfigurationProperties c = new EvidencesToolkitConfigurationProperties();

        SignatureConfigurationProperties signature = new SignatureConfigurationProperties();
        c.setSignature(signature);
        StoreConfigurationProperties keyStore = new StoreConfigurationProperties();
        keyStore.setPassword(oldProperties.get(EVIDENCE_KEYSTORE_PASSWORD));
        keyStore.setPath(oldProperties.get(EVIDENCE_KEYSTORE_PATH));
        keyStore.setType("JKS");
        signature.setKeyStore(keyStore);
        KeyConfigurationProperties privateKey = new KeyConfigurationProperties();
        privateKey.setAlias(oldProperties.get(EVIDENCE_PRIVATE_KEY_ALIAS));
        privateKey.setPassword(oldProperties.get(EVIDENCE_PRIVATE_KEY_PASSWORD));
        signature.setPrivateKey(privateKey);

        EvidencesIssuerInfo evidencesIssuerInfo = new EvidencesIssuerInfo();
        c.setIssuerInfo(evidencesIssuerInfo);
        PostalAdressConfigurationProperties postInfo = new PostalAdressConfigurationProperties();
        evidencesIssuerInfo.setPostalAddress(postInfo);
        postInfo.setCountry(oldProperties.get(POSTAL_ADDR_COUNTRY));
        postInfo.setLocality(oldProperties.get(POSTAL_ADDR_LOCALITY));
        postInfo.setZipCode(oldProperties.get(POSTAL_ADDR_ZIPCODE));
        postInfo.setStreet(oldProperties.get(POSTAL_ADDR_STREET));

        return c;
    }
}
