/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorActionDao;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorPartyDao;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorServiceDao;

/**
 *
 * @author spindlest
 */
public class ConnectorPModeSupportImplTest {
    
    @Mock
    IDomibusWebAdminConnectorServiceDao serviceDao;
    
    @Mock
    IDomibusWebAdminConnectorPartyDao partyDao;
    
    @Mock
    IDomibusWebAdminConnectorActionDao actionDao;
    
    @InjectMocks
    ConnectorPModeSupportImpl pmodeSupportImpl;
    
    
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        this.pmodeSupportImpl = new ConnectorPModeSupportImpl(actionDao, serviceDao, partyDao);
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
     * DomibusConnectorService, DomibusConnectorAction and DomibusConnectorParty
     * 
     * so each party, service and actions should only be persisted once to the db
     */
    @Test
    public void testImportFromPModeFile_emptyDB() throws Exception {        
        ClassPathResource classPathResource = new ClassPathResource("pmode.data/domibus-gw-sample-pmode-blue.xml");        
        UploadedFile uploadedPmode = uploadedFileHelperFactory(classPathResource);
              
        this.pmodeSupportImpl.importFromPModeFile(uploadedPmode);

        //there are 2 parties in the pmode xml
        Mockito.verify(partyDao, Mockito.times(2)).persistNewParty(Mockito.any(DomibusConnectorParty.class));
        //there are 5 services in the pmode xml
        Mockito.verify(serviceDao, Mockito.times(5)).persistNewService(Mockito.any(DomibusConnectorService.class));
        //there are 19 actions in the pmode xml one duplicate
        Mockito.verify(actionDao, Mockito.times(18)).persistNewAction(Mockito.any(DomibusConnectorAction.class));                        
    }
    
        /**
     * Test of importFromPModeFile method, of class ConnectorPModeSupportImpl.
     * 
     * Tests with "emptyDB" so the mocked DAOs are returning empty lists of 
     * DomibusConnectorService, DomibusConnectorAction and DomibusConnectorParty
     * 
     * so each party, service and actions should only be persisted once to the db
     */
    @Test
    public void testImportFromPModeFile2_emptyDB() throws Exception {        
        ClassPathResource classPathResource = new ClassPathResource("pmode.data/domibus-configuration-service_at.xml");        
        UploadedFile uploadedPmode = uploadedFileHelperFactory(classPathResource);
              
        this.pmodeSupportImpl.importFromPModeFile(uploadedPmode);

        //there are 2 parties in the pmode xml
        Mockito.verify(partyDao, Mockito.times(12)).persistNewParty(Mockito.any(DomibusConnectorParty.class));
        //there are 5 services in the pmode xml
        Mockito.verify(serviceDao, Mockito.times(4)).persistNewService(Mockito.any(DomibusConnectorService.class));
        //there are 17 actions in the pmode xml, one is a duplicate
        Mockito.verify(actionDao, Mockito.times(16)).persistNewAction(Mockito.any(DomibusConnectorAction.class));                        
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
        DomibusConnectorService s1 = new DomibusConnectorService();
        s1.setService("BR");
        s1.setServiceType("urn:e-codex:services:");
        List<DomibusConnectorService> serviceList = new ArrayList<>();
        serviceList.add(s1);        
        Mockito.when(serviceDao.getServiceList()).thenReturn(serviceList);
        
        //simulate existing Action        
        // 	<service name="BRService" value="BR" type="urn:e-codex:services:"/>
        
        
        this.pmodeSupportImpl.importFromPModeFile(uploadedPmode);

        //there are 2 parties in the pmode xml
        Mockito.verify(partyDao, Mockito.times(2)).persistNewParty(Mockito.any(DomibusConnectorParty.class));
        //there are 5 services in the pmode xml
        Mockito.verify(serviceDao, Mockito.times(4)).persistNewService(Mockito.any(DomibusConnectorService.class));
        //there are 19 actions in the pmode xml one duplicate
        Mockito.verify(actionDao, Mockito.times(18)).persistNewAction(Mockito.any(DomibusConnectorAction.class));                        
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
