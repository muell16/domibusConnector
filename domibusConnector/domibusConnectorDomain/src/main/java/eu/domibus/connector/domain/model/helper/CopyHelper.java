
package eu.domibus.connector.domain.model.helper;

import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDocument;
import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.model.builder.DomibusConnectorActionBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageAttachmentBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageContentBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDocumentBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorPartyBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorServiceBuilder;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class CopyHelper {

    public static DomibusConnectorAction copyAction(DomibusConnectorAction action) {
        return DomibusConnectorActionBuilder.createBuilder()
                .copyPropertiesFrom(action)
                .build();                
    }
    
    public static DomibusConnectorParty copyParty(DomibusConnectorParty party) {
        return DomibusConnectorPartyBuilder.createBuilder()
                .copyPropertiesFrom(party)
                .build();
    }
    
    public static DomibusConnectorService copyService(DomibusConnectorService service) {
        return DomibusConnectorServiceBuilder.createBuilder()
                .copyPropertiesFrom(service)
                .build();
    }
    
    public static DomibusConnectorMessageContent copyMessageContent(DomibusConnectorMessageContent content) {
        DomibusConnectorMessageContent copy = DomibusConnectorMessageContentBuilder
                .createBuilder()
                .copyPropertiesForm(content)
                .build();
        return copy;
    }

    public static DomibusConnectorMessageDocument copyDocument(DomibusConnectorMessageDocument document) {
        DomibusConnectorMessageDocument copy = DomibusConnectorMessageDocumentBuilder.createBuilder()
                .setContent(document.getDocument())
                .setName(document.getDocumentName())
                .withDetachedSignature(document.getDetachedSignature())
                .build();
        return copy;
    }
    
    public static DomibusConnectorMessageAttachment copyAttachment(DomibusConnectorMessageAttachment attachment) {
        DomibusConnectorMessageAttachment copy = DomibusConnectorMessageAttachmentBuilder.createBuilder()
                .copyPropertiesFrom(attachment)
                .build();
        return copy;
    }
    
    
}
