package eu.domibus.connector.link.impl.gwjmsplugin;

import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.nio.file.Path;

@Profile(GwJmsPluginConfiguration.GW_JMS_PLUGIN_PROFILE)
@ConfigurationProperties(prefix = GwJmsPluginConfiguration.GW_JMS_PLUGIN_PROFILE)
@Validated
public class GwJmsPluginConfigurationProperties {

    @ConfigurationLabel("Put Attachments into Queue")
    @ConfigurationDescription("Should the attachments be put into the jms message as bytes. The setting here must be the same as on gateway side!")
    private boolean putAttachmentInQueue = false;

    @ConfigurationLabel("Attachment Storage Location")
    @ConfigurationDescription("If the messages are not put into the jms message. They have to be stored on disk. This value must be the same as on gateway side (property domibus.attachment.storage.location).")
    private Path attachmentStorageLocation;

    @ConfigurationLabel("P1InBody")
    @ConfigurationDescription("Must be the same os on gateway side!")
    private boolean firstPayloadInBody = false;

    @ConfigurationLabel("Gateway Plugin Username")
    @ConfigurationDescription("Required by the gateway in Multitenancy mode")
    @NotBlank
    private String username;

    @ConfigurationLabel("Gateway Plugin Password")
    @ConfigurationDescription("Required by the gateway in Multitenancy mode")
    @NotBlank
    private String password;

    @ConfigurationLabel("toDomibus")
    @ConfigurationDescription("Same as domibus.backend.jmsInQueue in gw config. Submit a message from connector to Domibus.")
    @NotBlank
    private String toDomibusGateway;

    @ConfigurationLabel("toConnector")
    @ConfigurationDescription("Same as domibus.backend.jms.replyQueue,domibus.backend.jms.outQueue,domibus.backend.jms.errorNotifyProducer,domibus.backend.jms.errorNotifyConsumer in gw config. All messages and notifications going to the connector.")
    @NotBlank
    private String toConnector;

//    @ConfigurationLabel("outQueue")
//    @ConfigurationDescription("Same as domibus.backend.jms.outQueue in gw config")
//    @NotBlank
//    private String outQueue;
//
//    @ConfigurationLabel("errorNotifyProducer")
//    @ConfigurationDescription("Same as domibus.backend.jms.errorNotifyProducer in gw config")
//    @NotBlank
//    private String errorNotifyProducerQueue;
//
//    @ConfigurationLabel("errorNotifyConsumer")
//    @ConfigurationDescription("Same as domibus.backend.jms.errorNotifyConsumer in gw config")
//    @NotBlank
//    private String errorNotifyConsumerQueue;


    public boolean isPutAttachmentInQueue() {
        return putAttachmentInQueue;
    }

    public void setPutAttachmentInQueue(boolean putAttachmentInQueue) {
        this.putAttachmentInQueue = putAttachmentInQueue;
    }

    public Path getAttachmentStorageLocation() {
        return attachmentStorageLocation;
    }

    public void setAttachmentStorageLocation(Path attachmentStorageLocation) {
        this.attachmentStorageLocation = attachmentStorageLocation;
    }

    public boolean isFirstPayloadInBody() {
        return firstPayloadInBody;
    }

    public void setFirstPayloadInBody(boolean firstPayloadInBody) {
        this.firstPayloadInBody = firstPayloadInBody;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToDomibusGateway() {
        return toDomibusGateway;
    }

    public void setToDomibusGateway(String toDomibusGateway) {
        this.toDomibusGateway = toDomibusGateway;
    }

    public String getToConnector() {
        return toConnector;
    }

    public void setToConnector(String toConnector) {
        this.toConnector = toConnector;
    }

//    public String getOutQueue() {
//        return outQueue;
//    }
//
//    public void setOutQueue(String outQueue) {
//        this.outQueue = outQueue;
//    }
//
//    public String getErrorNotifyProducerQueue() {
//        return errorNotifyProducerQueue;
//    }
//
//    public void setErrorNotifyProducerQueue(String errorNotifyProducerQueue) {
//        this.errorNotifyProducerQueue = errorNotifyProducerQueue;
//    }
//
//    public String getErrorNotifyConsumerQueue() {
//        return errorNotifyConsumerQueue;
//    }
//
//    public void setErrorNotifyConsumerQueue(String errorNotifyConsumerQueue) {
//        this.errorNotifyConsumerQueue = errorNotifyConsumerQueue;
//    }
}
