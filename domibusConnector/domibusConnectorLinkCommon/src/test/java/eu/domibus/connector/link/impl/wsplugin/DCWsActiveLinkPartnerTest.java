package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.enums.LinkMode;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DCWsActiveLinkPartnerTest {




    @Test
    public void testConfigBinding() {
        String pushAddress = "http://localhost:8080/services/submission";

        DomibusConnectorLinkPartner linkPartner = new DomibusConnectorLinkPartner();
        linkPartner.setLinkMode(LinkMode.PUSH);
        linkPartner.setLinkType(LinkType.GATEWAY);
        Properties p = new Properties();
        p.put("push-address", pushAddress);
        linkPartner.setProperties(p);

        DCWsSubmitTo submitTo = Mockito.mock(DCWsSubmitTo.class);
        DCWsActiveLink activeLink = Mockito.mock(DCWsActiveLink.class);


        DCWsActiveLinkPartner dcWsActiveLinkPartner = new DCWsActiveLinkPartner(linkPartner, submitTo, activeLink);

        assertThat(dcWsActiveLinkPartner.getPushAddress()).isEqualTo(pushAddress);

    }


}