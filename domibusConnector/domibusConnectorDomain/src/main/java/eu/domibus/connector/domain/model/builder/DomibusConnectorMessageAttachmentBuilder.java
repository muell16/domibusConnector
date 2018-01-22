/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.domain.model.builder;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorMessageAttachmentBuilder {

    private String identifier;
	private DomibusConnectorBigDataReference attachment;
	private String name;
	private String mimeType;
	private String description;
    
    public static DomibusConnectorMessageAttachmentBuilder createBuilder() {
        return new DomibusConnectorMessageAttachmentBuilder();
    }
    
    private DomibusConnectorMessageAttachmentBuilder() {}

    public DomibusConnectorMessageAttachmentBuilder setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public DomibusConnectorMessageAttachmentBuilder setAttachment(DomibusConnectorBigDataReference attachment) {
        this.attachment = attachment;
        return this;
    }

    public DomibusConnectorMessageAttachmentBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DomibusConnectorMessageAttachmentBuilder withMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public DomibusConnectorMessageAttachmentBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DomibusConnectorMessageAttachment build() {
        if (this.attachment == null) {
            throw new IllegalArgumentException("Attachment must be provided!");
        }
        if (this.identifier == null) {
            throw new IllegalArgumentException("identifier must be provided!");
        }
        DomibusConnectorMessageAttachment domibusConnectorMessageAttachment = new DomibusConnectorMessageAttachment(attachment, identifier);
        domibusConnectorMessageAttachment.setDescription(description);
        domibusConnectorMessageAttachment.setMimeType(mimeType);
        domibusConnectorMessageAttachment.setName(name);
        return domibusConnectorMessageAttachment;
    }

}