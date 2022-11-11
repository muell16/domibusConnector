package eu.domibus.connector.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.springframework.core.style.ToStringCreator;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Null;


/**
 * The DomibusConnectorMessageContent holds the main content of a message. This is
 * the XML data of the main Form of the message and the printable document that
 * most of the {@link DomibusConnectorAction} require.
 *
 * A message is a business message only if a messageContent is
 * present
 *
 *
 * @author riederb
 * @version 1.0
 * updated 29-Dez-2017 10:12:49
 */
@Entity
@Data
public class DomibusConnectorMessageContent implements Serializable {

	@GeneratedValue
	@Id
	public long id;

	@OneToOne(cascade = CascadeType.ALL)
	private DCEcodexContent ecodexContent;

	@OneToOne(cascade = CascadeType.ALL)
	private DCBackendContent businessContent;

	@Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("BusinessContent", this.businessContent);
		builder.append("ECodexContent", this.ecodexContent);
        return builder.toString();        
    }
    
}