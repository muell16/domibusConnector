package eu.domibus.connector.domain.model;

import java.io.Serializable;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.style.ToStringCreator;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;


/**
 * Internal part of the {@link DomibusConnectorMessage}. All message related
 * errors raised while processing a message and all message related errors
 * reported by the gateway are stored and added to the message.
 * @author riederb
 * @version 1.0
 */
@Data
@RequiredArgsConstructor
public class DomibusConnectorMessageError {

	@NotNull
	private final String text;
	@NotNull
	private final String details;
	@NotNull
	private final String source;
	@NotNull
	private final String step;
	@NotNull
	private final String processor;

    @Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("errorText", this.text);
        builder.append("source", this.source);
        builder.append("details", this.details);
        return builder.toString();        
    }

}