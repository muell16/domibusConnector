package eu.domibus.connector.link.service;

import eu.domibus.connector.controller.service.PullFromLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class DCLinkPullJob implements Job {

    private static final Logger LOGGER = LogManager.getLogger(DCLinkPullJob.class);

//    public static final String PULL_FROM_LINK_PROPERTY_NAME = "pullFromLink";
    public static final String LINK_PARTNER_NAME_PROPERTY_NAME = "linkPartnerName";

//    @Autowired
//    PullFromLink pullFromLink;
//
//    @Autowired
//    DomibusConnectorLinkPartner linkPartner;

    @Autowired
    DCActiveLinkManagerService dcActiveLinkManagerService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try (MDC.MDCCloseable mdcCloseable = MDC.putCloseable(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_PROCESSOR_PROPERTY_NAME, DCLinkPullJob.class.getSimpleName())) {
            String linkPartnerName = context.getMergedJobDataMap().getString(LINK_PARTNER_NAME_PROPERTY_NAME);
            LOGGER.debug("Running pull messages job for linkPartner [{}]", linkPartnerName);

            Optional<PullFromLink> pullFromLinkPartner = dcActiveLinkManagerService.getPullFromLinkPartner(linkPartnerName);

            pullFromLinkPartner.ifPresent((p) -> p.pullMessagesFrom(new DomibusConnectorLinkPartner.LinkPartnerName(linkPartnerName)));

        }
    }

}
