package eu.domibus.connector.persistence.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 *  This class stores message content like
 *   <ul>
 *      <li>message attachments</li>
 *      <li>message content xml</li>
 *      <li>message content document</li>
 *   </ul>
 * 
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Entity
@Table(name="DOMIBUS_CONNECTOR_MSG_CONT")
public class PDomibusConnectorMsgCont implements Serializable {

    @Id
    @Column(name="ID", length=512)
    @TableGenerator(name = "seqStoreMsgContent", table = "DOMIBUS_CONNECTOR_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "DOMIBUS_CONNECTOR_MSG_CONT.ID", valueColumnName = "SEQ_VALUE", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStoreMsgContent")
    private Long id;

    @Column(name="CONTENT")
    @Lob
    private byte[] content;
    
    @Column(name="CHECKSUM")
    private String checksum;
    
    @Column(name="CONTENT_TYPE")
    private String contentType;
    
    @ManyToOne
    @JoinColumn(name="MESSAGE_ID")
    private PDomibusConnectorMessage message;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public PDomibusConnectorMessage getMessage() {
        return message;
    }

    public void setMessage(PDomibusConnectorMessage message) {
        this.message = message;
    }

    @Override
    public String toString() {
        ToStringBuilder toString = new ToStringBuilder(this);
        toString.append("id", this.id);
        toString.append("messageId", this.getMessage().getId());
        return toString.build();
    }

}
