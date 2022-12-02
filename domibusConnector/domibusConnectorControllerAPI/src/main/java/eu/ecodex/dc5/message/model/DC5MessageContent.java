package eu.ecodex.dc5.message.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.ecodex.dc5.message.validation.IncomingBusinessMesssageRules;
import lombok.*;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


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
public class DC5MessageContent {

	@GeneratedValue
	@Id
	public long id;

	@OneToOne(cascade = CascadeType.ALL)
	@NotNull(groups = IncomingBusinessMesssageRules.class, message = "A incoming business message must have a ecodex content")
	private DC5EcodexContent ecodexContent;

	@OneToOne(cascade = CascadeType.ALL)
	private DC5BackendContent businessContent;

	@OneToMany(cascade = CascadeType.ALL)
	private List<DC5BusinessMessageState> messageStates = new ArrayList<>();

//	@OneToMany(cascade = CascadeType.ALL)
//	@NonNull
//	@Builder.Default
//	private List<DC5Confirmation> relatedConfirmations = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL)
	private DC5BusinessMessageState currentState;

	@PrePersist
	public void prePersist() {
		if (currentState == null) {
			currentState = DC5BusinessMessageState.builder()
					.state(DC5BusinessMessageState.BusinessMessagesStates.CREATED)
					.event(DC5BusinessMessageState.BusinessMessageEvents.NEW_MSG)
					.build();
		}
	}

	@Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("BusinessContent", this.businessContent);
		builder.append("ECodexContent", this.ecodexContent);
        return builder.toString();        
    }

	public void changeCurrentState(DC5BusinessMessageState currentState) {
		if (currentState.getId() == null) {
			this.currentState = currentState;
			this.messageStates.add(currentState);
		} else {
			throw new IllegalArgumentException("Not a new state!");
		}
	}

	public List<DC5Confirmation> getRelatedConfirmations() {
		return this.messageStates.stream()
				.map(DC5BusinessMessageState::getConfirmation)
				.collect(Collectors.toList());
	}


}