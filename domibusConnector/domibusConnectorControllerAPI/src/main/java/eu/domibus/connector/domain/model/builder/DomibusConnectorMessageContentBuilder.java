
package eu.domibus.connector.domain.model.builder;

import eu.ecodex.dc5.message.model.DC5MessageContent;

/**
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public final class DomibusConnectorMessageContentBuilder {
    //TODO

//    private byte xmlContent[];
//    private DomibusConnectorMessageDocument document;

    private DomibusConnectorMessageContentBuilder() {
    }

    public static DomibusConnectorMessageContentBuilder createBuilder() {
        return new DomibusConnectorMessageContentBuilder();
    }

    public DomibusConnectorMessageContentBuilder setXmlContent(byte[] xmlContent) {
//        this.xmlContent = xmlContent;
        return this;
    }

//    public DomibusConnectorMessageContentBuilder setDocument(DomibusConnectorMessageDocument document) {
//        this.document = document;
//        return this;
//    }

    public DC5MessageContent build() {
        DC5MessageContent content = new DC5MessageContent();
//        content.setDocument(document);
//        content.setXmlContent(xmlContent);
        return content;
    }

//    public boolean canBuild() {
//        return xmlContent != null && document != null;
//    }

    public DomibusConnectorMessageContentBuilder copyPropertiesFrom(DC5MessageContent content) {
//        if (content.getDocument() != null) {
//            this.document = DomibusConnectorMessageDocumentBuilder.createBuilder()
//                    .copyPropertiesFrom(content.getDocument())
//                    .build();
//        }
//        this.xmlContent = Arrays.copyOf(content.getXmlContent(), content.getXmlContent().length);
        return this;
    }


}
