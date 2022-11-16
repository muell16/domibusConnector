package eu.ecodex.dc5.message.model;

import java.io.Serializable;

import lombok.Data;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;


/**
 * The DomibusConnectorMessageContent holds the main content of a message. This is
 * the XML data of the main Form of the message and the printable document that
 * most of the {@link DC5Action} require.
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
	private DC5EcodexContent ecodexContent;

	@OneToOne(cascade = CascadeType.ALL)
	private DC5BackendContent businessContent;

	@Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("BusinessContent", this.businessContent);
		builder.append("ECodexContent", this.ecodexContent);
        return builder.toString();        
    }
    
}