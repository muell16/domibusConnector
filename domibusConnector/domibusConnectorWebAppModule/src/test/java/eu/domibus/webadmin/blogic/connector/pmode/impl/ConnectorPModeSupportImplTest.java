package eu.domibus.webadmin.blogic.connector.pmode.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.primefaces.model.UploadedFile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import eu.domibus.configuration.Configuration;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.model.builder.DomibusConnectorServiceBuilder;
import eu.domibus.connector.persistence.service.DomibusConnectorActionPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPartyPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorServicePersistenceService;
import static org.mockito.Matchers.any;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

/**
 *
 * @author spindlest
 */
public class ConnectorPModeSupportImplTest {
    
    @Mock
    DomibusConnectorServicePersistenceService servicePersistenceService;
    
    @Mock
    DomibusConnectorPartyPersistenceService partyPersistenceService;
    
    @Mock
    DomibusConnectorActionPersistenceService actionPersistenceService;
    
    @InjectMocks
    ConnectorPModeSupportImpl pmodeSupportImpl;
    
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
                
        this.pmodeSupportImpl = new ConnectorPModeSupportImpl();
        this.pmodeSupportImpl.setActionPersistenceService(actionPersistenceService);
        Mockito.when(actionPersistenceService.persistNewAction(any(DomibusConnectorAction.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgumentAt(0, DomibusConnectorAction.class));
        
        this.pmodeSupportImpl.setPartyPersistenceService(partyPersistenceService);
        Mockito.when(partyPersistenceService.persistNewParty(any(DomibusConnectorParty.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgumentAt(0, DomibusConnectorParty.class));
        
        this.pmodeSupportImpl.setServicePersistenceService(servicePersistenceService);
        Mockito.when(servicePersistenceService.persistNewService(any(DomibusConnectorService.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgumentAt(0, DomibusConnectorService.class));
    }
    
    @After
    public void tearDown() {
    }

    static  UploadedFile uploadedFileHelperFactory(final Resource res) throws IOException  {
        
        final byte[] copyToByteArray = StreamUtils.copyToByteArray(res.getInputStream());
        
        
          UploadedFile upload = new UploadedFile() {
            @Override
            public String getFileName() {
                return "file";
            }

            @Override
            public InputStream getInputstream() throws IOException {
                return new ByteArrayInputStream(copyToByteArray);
            }

            @Override
            public long getSize() {
                return copyToByteArray.length;
            }

            @Override
            public byte[] getContents() {
                return copyToByteArray;
            }

            @Override
            public String getContentType() {
                return "text/xml";
            }

            @Override
            public void write(String filePath) throws Exception {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
          
        return upload;
        
    }
    
    
    
    /**
     * Test of importFromPModeFile method, of class ConnectorPModeSupportImpl.
     * 
     * Tests with "emptyDB" so the mocked DAOs are returning empty lists of 
 PDomibusConnectorService, PDomibusConnectorAction and PDomibusConnectorParty
 
 so each party, service and actions should only be persisted once to the db
     */
    @Test
    public void testImportFromPModeFile_emptyDB() throws Exception {        
        ClassPathResource classPathResource = new ClassPathResource("pmode.data/domibus-gw-sample-pmode-blue.xml");        
        UploadedFile uploadedPmode = uploadedFileHelperFactory(classPathResource);
              
        this.pmodeSupportImpl.importFromPModeFile(uploadedPmode);

        //there are 2 parties in the pmode xml
        Mockito.verify(partyPersistenceService, Mockito.times(2)).persistNewParty(Mockito.any(DomibusConnectorParty.class));
        //there are 5 services in the pmode xml
        Mockito.verify(servicePersistenceService, Mockito.times(5)).persistNewService(Mockito.any(DomibusConnectorService.class));
        //there are 19 actions in the pmode xml one duplicate
        Mockito.verify(actionPersistenceService, Mockito.times(18)).persistNewAction(Mockito.any(DomibusConnectorAction.class));                        
    }
    
        /**
     * Test of importFromPModeFile method, of class ConnectorPModeSupportImpl.
     * 
     * Tests with "emptyDB" so the mocked DAOs are returning empty lists of 
 PDomibusConnectorService, PDomibusConnectorAction and PDomibusConnectorParty
 
 so each party, service and actions should only be persisted once to the db
     */
    @Test
    public void testImportFromPModeFile2_emptyDB() throws Exception {        
        ClassPathResource classPathResource = new ClassPathResource("pmode.data/domibus-configuration-service_at.xml");        
        UploadedFile uploadedPmode = uploadedFileHelperFactory(classPathResource);
              
        this.pmodeSupportImpl.importFromPModeFile(uploadedPmode);

        //there are 2 parties in the pmode xml
        Mockito.verify(partyPersistenceService, Mockito.times(12)).persistNewParty(Mockito.any(DomibusConnectorParty.class));
        //there are 5 services in the pmode xml
        Mockito.verify(servicePersistenceService, Mockito.times(4)).persistNewService(Mockito.any(DomibusConnectorService.class));
        //there are 17 actions in the pmode xml, one is a duplicate
        Mockito.verify(actionPersistenceService, Mockito.times(16)).persistNewAction(Mockito.any(DomibusConnectorAction.class));                        
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void testImportFromPModeFile_nullFile() throws Exception {
        this.pmodeSupportImpl.importFromPModeFile(null);
    }
    
    @Test
    public void testImportFromPModeFile_existingData() throws Exception {        
        ClassPathResource classPathResource = new ClassPathResource("pmode.data/domibus-gw-sample-pmode-blue.xml");        
        UploadedFile uploadedPmode = uploadedFileHelperFactory(classPathResource);
              
        //simulate existing Service
        DomibusConnectorService s1 = DomibusConnectorServiceBuilder.createBuilder()
                .setService("BR")
                .withServiceType("urn:e-codex:services:")
                .build();
        
        List<DomibusConnectorService> serviceList = new ArrayList<>();
        serviceList.add(s1);        
        Mockito.when(servicePersistenceService.getServiceList()).thenReturn(serviceList);
        
        //simulate existing Action        
        // 	<service name="BRService" value="BR" type="urn:e-codex:services:"/>
        
        
        this.pmodeSupportImpl.importFromPModeFile(uploadedPmode);

        //there are 2 parties in the pmode xml
        Mockito.verify(partyPersistenceService, Mockito.times(2)).persistNewParty(Mockito.any(DomibusConnectorParty.class));
        //there are 5 services in the pmode xml
        Mockito.verify(servicePersistenceService, Mockito.times(4)).persistNewService(Mockito.any(DomibusConnectorService.class));
        //there are 19 actions in the pmode xml one duplicate
        Mockito.verify(actionPersistenceService, Mockito.times(18)).persistNewAction(Mockito.any(DomibusConnectorAction.class));                        
    }
    
    

    /**
     * Test of byteArrayToXmlObject method, of class ConnectorPModeSupportImpl.
     */
    @Test
    public void testByteArrayToXmlObject() throws Exception {        
        ClassPathResource classPathResource = new ClassPathResource("pmode.data/domibus-gw-sample-pmode-blue.xml");        
        InputStream is = classPathResource.getInputStream();
        byte[] xmlAsBytes = StreamUtils.copyToByteArray(is);        
        Configuration configuration = (Configuration) ConnectorPModeSupportImpl.byteArrayToXmlObject(xmlAsBytes, Configuration.class, Configuration.class);
        
        assertThat(configuration).isNotNull();
    }
    
    /**
     * Test of byteArrayToXmlObject method, of class ConnectorPModeSupportImpl.
     */
    @Test(expected=Exception.class)
    public void testByteArrayToXmlObject_withCorruptedXML() throws Exception {        
        ClassPathResource classPathResource = new ClassPathResource("pmode.data/domibus-gw-sample-pmode-blue.xml");        
        InputStream is = classPathResource.getInputStream();
        byte[] xmlAsBytes = StreamUtils.copyToByteArray(is);    
        
        xmlAsBytes[20] = 'a'; //destroy xml
        
        Configuration configuration = (Configuration) ConnectorPModeSupportImpl.byteArrayToXmlObject(xmlAsBytes, Configuration.class, Configuration.class);
        
        assertThat(configuration).isNotNull();
    }
    
}
