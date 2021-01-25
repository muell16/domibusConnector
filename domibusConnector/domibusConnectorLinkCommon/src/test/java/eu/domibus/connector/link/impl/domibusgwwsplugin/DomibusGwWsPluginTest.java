package eu.domibus.connector.link.impl.domibusgwwsplugin;

import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.eu.domibus.connector.link.LinkTestContext;

import java.util.Collection;
import java.util.List;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = {LinkTestContext.class },
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {

        }
)
@ActiveProfiles({"domibusgwwsplugin-test", "test", LINK_PLUGIN_PROFILE_NAME, "plugin-domibusgwwsplugin"})
@Disabled
class DomibusGwWsPluginTest {


    @Autowired
    DCActiveLinkManagerService linkManagerService;

    @Test
    public void testPluginIsLoaded() {
        List<LinkPlugin> availableLinkPlugins = linkManagerService.getAvailableLinkPlugins();
        assertThat(availableLinkPlugins).extracting(LinkPlugin::getPluginName).contains("domibusgwwsplugin");
    }

    @Test
    public void testPluginConfigs() {
        Collection<ActiveLinkPartner> activeLinkPartners = linkManagerService.getActiveLinkPartners();
        assertThat(activeLinkPartners).hasSize(1); //1 backend is configured...
    }

}