package eu.ecodex.dc5.message.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
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

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class DomibusConnectorMessageContent {

	@GeneratedValue
	@Id
	public long id;

	@OneToOne(cascade = CascadeType.ALL)
	private DC5EcodexContent ecodexContent;

	@OneToOne(cascade = CascadeType.ALL)
	private DC5BackendContent businessContent;

	@OneToMany(cascade = CascadeType.ALL)
	private List<DC5BusinessMessageState> messageStates = new ArrayList<>();

	@OneToOne(optional = false)
	private DC5BusinessMessageState currentState;

	@Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("BusinessContent", this.businessContent);
		builder.append("ECodexContent", this.ecodexContent);
        return builder.toString();        
    }
    
}