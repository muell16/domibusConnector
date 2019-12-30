package eu.domibus.connector.link.service;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkInfo;
import eu.domibus.connector.link.api.CloseCallback;
import org.springframework.context.ConfigurableApplicationContext;

public class ActiveLink {

    private DomibusConnectorLinkConfiguration linkConfig;
//    private SubmitToLink submitToLink;
    private ConfigurableApplicationContext linkModuleApplicationContext;
    private String linkName;
    private CloseCallback closeCallback;

    public DomibusConnectorLinkConfiguration getLinkConfig() {
        return linkConfig;
    }

    public void setLinkConfig(DomibusConnectorLinkConfiguration linkConfig) {
        this.linkConfig = linkConfig;
    }

//    public SubmitToLink getSubmitToLink() {
//        return submitToLink;
//    }
//
//    public void setSubmitToLink(SubmitToLink submitToLink) {
//        this.submitToLink = submitToLink;
//    }

    public ConfigurableApplicationContext getLinkModuleApplicationContext() {
        return linkModuleApplicationContext;
    }

    public void setLinkModuleApplicationContext(ConfigurableApplicationContext linkModuleApplicationContext) {
        this.linkModuleApplicationContext = linkModuleApplicationContext;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setCloseCallback(CloseCallback closeCallback) {
        this.closeCallback = closeCallback;
    }

    public CloseCallback getCloseCallback() {
        return this.closeCallback;
    }
}
