package eu.domibus.connector.link.impl.gwjmsplugin;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkInfo;
import eu.domibus.connector.link.api.LinkPluginFactory;
import eu.domibus.connector.link.service.ActiveLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GwJmsPluginFactory implements LinkPluginFactory {

    public static final String LINK_IMPL_NAME = "gwjmsplugin";

    @Autowired
    ConfigurableApplicationContext applicationContext;

    /**
     *   Tell the world if we are responsible for the implementation of
     *   @param implementation
    **/
    @Override
    public boolean canHandle(String implementation) {
        return LINK_IMPL_NAME.equals(implementation);
    }

//    SubmitToLink createSubmitToLink(Properties properties) {
//        //TODO: create a submit to link for the given properties
//        return null;
//    }

    public List<Class> getSources() {
        return Stream
                .of(ReceiveFromJmsQueueConfiguration.class)
                .collect(Collectors.toList());
    }

    public List<String> getProfiles() {
        return Stream
                .of(ReceiveFromJmsQueueConfiguration.GW_JMS_PLUGIN_PROFILE)
                .collect(Collectors.toList());
    }

    @Override
    public ActiveLink createLink(DomibusConnectorLinkInfo linkInfo) {
        ActiveLink activeLink = new ActiveLink();
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder.parent(applicationContext);
        builder.sources(getSources().toArray(new Class[]{}));
        Properties p = linkInfo.getLinkConfiguration().getProperties();
        builder.properties(p);
        builder.profiles(getProfiles().toArray(new String[]{}));
        builder.bannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext linkModuleApplicationContext = builder.run();

        SubmitToLink submitToLink = linkModuleApplicationContext.getBean(SubmitToLink.class);

        activeLink.setLinkModuleApplicationContext(linkModuleApplicationContext);
//        activeLink.setSubmitToLink(submitToLink);
//        activeLink.setLinkConfig(linkInfo);
        activeLink.setLinkName(linkInfo.getLinkName());
        activeLink.setCloseCallback(() -> {
            applicationContext.close();
        });

        return activeLink;
    }


//    /**
//     * Link module will be created in child context of currently
//     * active spring context
//     * @param linkInfo - The properties of LinkInfo will become part of the environment!
//     * @return
//     */
//    ActiveLink registerLinkModule(DomibusConnectorLinkInfo linkInfo) {
//        ActiveLink activeLink = new ActiveLink();
//
//        SpringApplicationBuilder builder = new SpringApplicationBuilder();
//        builder.parent(applicationContext);
//        builder.sources(ReceiveFromJmsQueueConfiguration.class);
//        builder.properties(linkInfo.getProperties());
//        builder.profiles(ReceiveFromJmsQueueConfiguration.GW_JMS_PLUGIN_PROFILE);
//        ConfigurableApplicationContext linkModuleApplicationContext = builder.run();
//
//        SubmitToLink submitToLink = linkModuleApplicationContext.getBean(SubmitToLink.class);
//
//        activeLink.setLinkModuleApplicationContext(linkModuleApplicationContext);
//        activeLink.setSubmitToLink(submitToLink);
//        activeLink.setLinkInfo(linkInfo);
//
//        //from link
//        return activeLink;
//    }



}
