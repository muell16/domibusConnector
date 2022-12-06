package eu.domibus.connector.firststartup;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorKeystore;
import eu.domibus.connector.tools.logging.LoggingMarker;
import eu.domibus.connector.ui.service.WebPModeService;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;

//@Configuration
//@EnableConfigurationProperties(UploadPModesOnFirstStartConfigurationProperties.class)
//@DependsOn(CreateDefaultBusinessDomainOnFirstStart.BEAN_NAME)
//@ConditionalOnProperty(prefix = UploadPModesOnFirstStartConfigurationProperties.PREFIX, name = "enabled", havingValue = "true")
public class UploadPModesOnFirstStart {

    private static final Logger LOGGER = LogManager.getLogger(UploadPModesOnFirstStart.class);

    private final WebPModeService webPModeService;
    private final DCBusinessDomainManager dcBusinessDomainManager;
    private final UploadPModesOnFirstStartConfigurationProperties config;
    private final ApplicationContext ctx;

    public UploadPModesOnFirstStart(WebPModeService webPModeService,
                                    DCBusinessDomainManager dcBusinessDomainManager, UploadPModesOnFirstStartConfigurationProperties config,
                                    ApplicationContext ctx) {
        this.webPModeService = webPModeService;
        this.dcBusinessDomainManager = dcBusinessDomainManager;
        this.config = config;
        this.ctx = ctx;
    }

    @PostConstruct
    @Transactional
    public void startup() {
        config.getUpload().forEach(this::processUpload);
    }

    private void processUpload(UploadPModesOnFirstStartConfigurationProperties.PModeUpload pModeUpload) {

        String businessDomainName = pModeUpload.getBusinessDomainName();


        try {
            if (!dcBusinessDomainManager.getValidBusinessDomainsAllData().contains(businessDomainName)) {
                final DomibusConnectorBusinessDomain businessDomain = new DomibusConnectorBusinessDomain();
                final DomibusConnectorBusinessDomain.BusinessDomainId id = new DomibusConnectorBusinessDomain.BusinessDomainId(pModeUpload.getBusinessDomainName());
                businessDomain.setId(id);
                dcBusinessDomainManager.createBusinessDomain(businessDomain);
                LOGGER.info(LoggingMarker.Log4jMarker.CONFIG, String.format("Can't find domain with name %s. Creating ...", businessDomainName));
            }

            String pw = pModeUpload.getTrustStore().getPassword();
            String type = pModeUpload.getTrustStore().getType();
            byte[] trustStoreBytes = StreamUtils.copyToByteArray(ctx.getResource(pModeUpload.getTrustStore().getPath()).getInputStream());

            DomibusConnectorKeystore domibusConnectorKeystore = webPModeService.importConnectorstore(trustStoreBytes, pw, DomibusConnectorKeystore.KeystoreType.ofDbName(type));

            byte[] pModeXml = StreamUtils.copyToByteArray(pModeUpload.getpModeXml().getInputStream());
            boolean success = webPModeService.importPModes(pModeXml, "Initially loaded by UploadPModesOnFirstStart", domibusConnectorKeystore, new DomibusConnectorBusinessDomain.BusinessDomainId(pModeUpload.getBusinessDomainName())); // TODO: NAMING !!! the domain name is the ID

            if (success) {
                LOGGER.info(LoggingMarker.Log4jMarker.CONFIG, "Successfully Uploaded configured p-Modes and trustStore: [{}]", pModeUpload);
            } else {
                LOGGER.warn(LoggingMarker.Log4jMarker.CONFIG, "Failed to upload configured p-Modes and trustStore: [{}]", pModeUpload);
            }


        } catch (IOException e) {
            throw new RuntimeException("Error while reading from provided resource", e);
        }
    }
}
