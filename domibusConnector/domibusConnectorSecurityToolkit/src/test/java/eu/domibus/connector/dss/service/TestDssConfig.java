package eu.domibus.connector.dss.service;

import eu.domibus.connector.common.SpringProfiles;
import eu.domibus.connector.common.service.DCKeyStoreService;
import eu.domibus.connector.dss.configuration.BasicDssConfiguration;
import eu.ecodex.utils.spring.converter.ConverterAutoConfiguration;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.TimestampBinary;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.x509.tsp.TSPSource;
import org.bouncycastle.jcajce.provider.digest.SHA512;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(classes = {BasicDssConfiguration.class,
        DSSTrustedListsManager.class,
        ConverterAutoConfiguration.class,
        DCKeyStoreService.class
},
        properties = "connector.dss.tlCacheLocation=file:./target/tlcache/"

)
@ActiveProfiles({"seclib-test", SpringProfiles.TEST, "dss-tl-test" })
@Disabled("Test is failing in local build")
public class TestDssConfig {

    @Autowired
    TSPSource tspSource;

    @Test
    public void testTspSource() throws UnsupportedEncodingException {
        final DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA256;
        final byte[] toDigest = "Hello world".getBytes("UTF-8");
        final byte[] digestValue = DSSUtils.digest(digestAlgorithm, toDigest);

        TimestampBinary timeStampResponse = tspSource.getTimeStampResponse(digestAlgorithm, digestValue);
        assertThat(timeStampResponse.getBytes()).isNotNull();
    }

}
