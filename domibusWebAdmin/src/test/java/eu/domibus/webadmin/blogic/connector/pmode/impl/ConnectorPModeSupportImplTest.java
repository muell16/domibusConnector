/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.domibus.webadmin.blogic.connector.pmode.impl;

import eu.domibus.configuration.Configuration;
import static org.assertj.core.api.Assertions.*;
import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorPartyPK;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.webadmin.blogic.connector.pmode.IConnectorPModeSupport;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorActionDao;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorPartyDao;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorServiceDao;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.primefaces.model.UploadedFile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

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
        System.out.println("importFromPModeFile");
        
        ClassPathResource classPathResource = new ClassPathResource("pmode.data/domibus-gw-sample-pmode-blue.xml");        
//        InputStream is = classPathResource.getInputStream();
//        byte[] xmlAsBytes = StreamUtils.copyToByteArray(is);        
//        Configuration configuration = (Configuration) ConnectorPModeSupportImpl.byteArrayToXmlObject(xmlAsBytes, Configuration.class, Configuration.class);
        UploadedFile uploadedPmode = uploadedFileHelperFactory(classPathResource);
              
        this.pmodeSupportImpl.importFromPModeFile(uploadedPmode);

        //there are 2 parties in the pmode xml
        Mockito.verify(partyDao, Mockito.times(2)).persistNewParty(Mockito.any(DomibusConnectorParty.class));
        //there are 5 services in the pmode xml
        Mockito.verify(serviceDao, Mockito.times(5)).persistNewService(Mockito.any(DomibusConnectorService.class));
        //there are 19 actions in the pmode xml
        Mockito.verify(actionDao, Mockito.times(19)).persistNewAction(Mockito.any(DomibusConnectorAction.class));                        
    }
    
    

    /**
     * Test of byteArrayToXmlObject method, of class ConnectorPModeSupportImpl.
     */
    @Test
    public void testByteArrayToXmlObject() throws Exception {
        System.out.println("byteArrayToXmlObject");
        
        ClassPathResource classPathResource = new ClassPathResource("pmode.data/domibus-gw-sample-pmode-blue.xml");        
        InputStream is = classPathResource.getInputStream();
        byte[] xmlAsBytes = StreamUtils.copyToByteArray(is);        
        Configuration configuration = (Configuration) ConnectorPModeSupportImpl.byteArrayToXmlObject(xmlAsBytes, Configuration.class, Configuration.class);
        
        assertThat(configuration).isNotNull();
    }
    
}
