
package eu.domibus.connector.domain.model.builder;

import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDocument;
import eu.domibus.connector.domain.model.helper.CopyHelper;

import java.util.Arrays;

/**
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public final class DomibusConnectorMessageContentBuilder {

    private byte xmlContent[];
    private DomibusConnectorMessageDocument document;

    private DomibusConnectorMessageContentBuilder() {
    }

    public static DomibusConnectorMessageContentBuilder createBuilder() {
        return new DomibusConnectorMessageContentBuilder();
    }

    public DomibusConnectorMessageContentBuilder setXmlContent(byte[] xmlContent) {
        this.xmlContent = xmlContent;
        return this;
    }

    public DomibusConnectorMessageContentBuilder setDocument(DomibusConnectorMessageDocument document) {
        this.document = document;
        return this;
    }

    public DomibusConnectorMessageContent build() {
        DomibusConnectorMessageContent content = new DomibusConnectorMessageContent();
        content.setDocument(document);
        content.setXmlContent(xmlContent);
        return content;
    }

    public DomibusConnectorMessageContentBuilder copyPropertiesForm(DomibusConnectorMessageContent content) {
        this.document = CopyHelper.copyDocument(content.getDocument());
        this.xmlContent = Arrays.copyOf(content.getXmlContent(), content.getXmlContent().length);
        return this;
    }


}
