package eu.ecodex.dc5.message.model;

import eu.ecodex.dc5.message.validation.IncomingBusinessMesssageRules;
import lombok.*;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * The DomibusConnectorMessageContent holds the main content of a message. This is
 * the XML data of the main Form of the message and the printable document that
 * most of the {@link DC5Action} require.
 * <p>
 * A message is a business message only if a messageContent is
 * present
 *
 * @author riederb
 * @version 1.0
 * updated 29-Dez-2017 10:12:49
 */
@NamedEntityGraph(
		name = "content-entity-graph",
		attributeNodes = {
				@NamedAttributeNode(value = "messageStates", subgraph = "states"),
				@NamedAttributeNode("currentState")
		},
		subgraphs = @NamedSubgraph(name = "states", attributeNodes = {
				@NamedAttributeNode("id"),
				@NamedAttributeNode("confirmation"),
				@NamedAttributeNode("state"),
				@NamedAttributeNode("event"),
				@NamedAttributeNode("principal"),
				@NamedAttributeNode("created"),
		})
)
@Entity
@Table(name = "DC5_BUSINESS_MSG")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class DC5MessageContent {

	public static DC5MessageContentBuilder builder() {
		return new CustomDC5MessageContentBuilder();
	}

	private static class CustomDC5MessageContentBuilder extends DC5MessageContentBuilder {

		public DC5MessageContent build() {
			DC5MessageContent content = super.build();
			if (content.getCurrentState() == null && CollectionUtils.isEmpty(content.getMessageStates())) { //set initial state
				content.changeCurrentState(DC5BusinessMessageState.builder()
					.state(DC5BusinessMessageState.BusinessMessagesStates.CREATED)
					.event(DC5BusinessMessageState.BusinessMessageEvents.NEW_MSG)
					.build());
			} else if (content.getCurrentState() == null) {
				content.setCurrentState(content.getMessageStates().get(content.getMessageStates().size() - 1)); //set to last state in list
			}
			return content;
		}
	}

	@GeneratedValue
	@Id
	public long id;

	@OneToOne(cascade = CascadeType.ALL)
	@NotNull(groups = IncomingBusinessMesssageRules.class, message = "A incoming business message must have a ecodex content")
	private DC5EcodexContent ecodexContent;

	@OneToOne(cascade = CascadeType.ALL)
	private DC5BackendContent businessContent;

//	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "DC5_BUSIMSG_2_MSGSTATE")
//	@Fetch(FetchMode.SUBSELECT)
	private List<DC5BusinessMessageState> messageStates = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, optional = false)
	private DC5BusinessMessageState currentState;

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
			if (messageStates == null) {
				messageStates = new ArrayList<>();
			}
			messageStates.add(currentState);
		} else {
			throw new IllegalArgumentException("Not a new state!");
		}
	}

	public List<DC5Confirmation> getRelatedConfirmations() {
		return this.messageStates.stream()
				.map(DC5BusinessMessageState::getConfirmation)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	void setCurrentState(DC5BusinessMessageState currentState) {
		this.currentState = currentState;
	}


}