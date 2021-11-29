package eu.domibus.connector.ui.view.areas.configuration.security.importoldconfig;

import eu.domibus.connector.domain.enums.AdvancedElectronicSystemType;
import eu.domibus.connector.dss.configuration.SignatureConfigurationProperties;
import eu.domibus.connector.dss.configuration.SignatureValidationConfigurationProperties;
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

}
