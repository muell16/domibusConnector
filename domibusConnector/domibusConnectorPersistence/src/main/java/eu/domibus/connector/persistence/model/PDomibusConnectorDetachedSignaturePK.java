package eu.domibus.connector.persistence.model;


import java.io.Serializable;


public class PDomibusConnectorDetachedSignaturePK implements Serializable {

    private PDomibusConnectorMsgCont content;

    public PDomibusConnectorMsgCont getContent() {
        return content;
    }

    public void setContent(PDomibusConnectorMsgCont content) {
        this.content = content;
    }
}
