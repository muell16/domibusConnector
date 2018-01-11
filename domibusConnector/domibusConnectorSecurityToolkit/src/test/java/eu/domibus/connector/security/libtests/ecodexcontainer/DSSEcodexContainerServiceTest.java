package eu.domibus.connector.security.libtests.ecodexcontainer;

import eu.domibus.connector.security.container.DomibusSecurityContainer;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.CertificateStoreInfo;
import eu.ecodex.dss.model.SignatureParameters;
import eu.ecodex.dss.model.token.TokenIssuer;
import eu.ecodex.dss.service.ECodexException;
import eu.ecodex.dss.service.ECodexLegalValidationService;
import eu.ecodex.dss.service.impl.dss.DSSECodexContainerService;
import eu.ecodex.dss.service.impl.dss.DSSECodexLegalValidationService;
import eu.ecodex.dss.service.impl.dss.DSSECodexTechnicalValidationService;
import eu.ecodex.dss.util.SignatureParametersFactory;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.EncryptionAlgorithm;
import eu.europa.esig.dss.InMemoryDocument;
import eu.europa.esig.dss.MimeType;
import eu.europa.esig.dss.validation.CertificateVerifier;
import java.io.IOException;
import java.io.InputStream;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.StreamUtils;

/**
 * Exploration tests for DSSEcodexContainerService
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DSSEcodexContainerServiceTest {
    
    
    private DSSECodexContainerService initContainerService() throws Exception {
        DSSECodexContainerService containerService = new DSSECodexContainerService();
        
        ECodexLegalValidationService ecodexLegalValidationService = new DSSECodexLegalValidationService();
        containerService.setLegalValidationService(ecodexLegalValidationService);
        
        DSSECodexTechnicalValidationService technicalValidationService = new DSSECodexTechnicalValidationService();
        containerService.setTechnicalValidationService(technicalValidationService);
        

        CertificateStoreInfo certStore = new CertificateStoreInfo();
        certStore.setLocation("file:src/test/resources/keys/connector-keystore.jks");
        certStore.setPassword("connector");
        String keyAlias = "domibusConnector";
        String keyPassword = "connector";
        EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithm.RSA;
        DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA1;    
        
        SignatureParameters signingParameters = SignatureParametersFactory.create(certStore, keyAlias, keyPassword, encryptionAlgorithm, digestAlgorithm);
        assertThat(signingParameters).isNotNull();
        containerService.setContainerSignatureParameters(signingParameters);
        
        
//        CertificateVerifier certificateVerifier = Mockito.mock(CertificateVerifier.class);        
//        containerService.setCertificateVerifier(certificateVerifier);
        
        
        return containerService;
    }
    
    
    @Test
    public void testCreateContainer() throws Exception {
        DSSECodexContainerService containerService = initContainerService(); 
        
        
        
        BusinessContent businessContent = new BusinessContent();
        
        DSSDocument mainDocument = new InMemoryDocument(
                            loadByteArrayFromClassPathRessource("/examples/ExmaplePdf.pdf"),
                            DomibusSecurityContainer.MAIN_DOCUMENT_NAME + ".pdf", 
                            MimeType.PDF);        
        businessContent.setDocument(mainDocument);
        
        
        DSSDocument attachmentOne = new InMemoryDocument(
                loadByteArrayFromClassPathRessource("/examples/supercool.pdf"),
                "supercool.pdf",
                MimeType.PDF);
        
        businessContent.addAttachment(attachmentOne);
        

        TokenIssuer tokenIssuer = new TokenIssuer();
        
        containerService.create(businessContent, tokenIssuer);
        
    }
    
    
    
    private byte[] loadByteArrayFromClassPathRessource(String ressource) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(ressource);
            return StreamUtils.copyToByteArray(inputStream);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } 
    }
    

}
