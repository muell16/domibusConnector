package eu.domibus.connector.dss.service;

import eu.domibus.connector.common.SpringProfiles;
import eu.domibus.connector.common.service.DCKeyStoreService;
import eu.domibus.connector.dss.configuration.BasicDssConfiguration;
import eu.ecodex.utils.spring.converter.ConverterAutoConfiguration;
import eu.europa.esig.dss.service.http.proxy.ProxyConfig;
import liquibase.pro.packaged.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {BasicDssConfiguration.class,
        DSSTrustedListsManager.class,
        ConverterAutoConfiguration.class,
        DCKeyStoreService.class
},
    properties = "connector.dss.tlCacheLocation=file:./target/tlcache/"

)
@ActiveProfiles({"seclib-test", SpringProfiles.TEST, "dss-tl-test" })
class DSSTrustedListsManagerTest {


    @Autowired
    DSSTrustedListsManager dssTrustedListsManager;

    @Test
    public void testStartup() {
        assertThat(dssTrustedListsManager.getAllSourceNames()).hasSize(1);
        assertThat(dssTrustedListsManager.getCertificateSource("eu")).isPresent();
    }


}