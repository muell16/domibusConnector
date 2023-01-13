package eu.ecodex.dc5.message.model;


import eu.ecodex.dc5.message.validation.ConfirmationMessageRules;
import eu.ecodex.dc5.message.validation.IncomingMessageRules;
import eu.ecodex.dc5.message.validation.OutgoingMessageRules;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@Table(name = DC5Party.TABLE_NAME)
@Entity
public class DC5Party {
    public static final String TABLE_NAME = "DC5_PARTY";
    @Id
    @GeneratedValue
    private long id;

    @Builder(toBuilder = true)
    public DC5Party(String partyId, String partyIdType) {
        this.partyId = partyId;
        this.partyIdType = partyIdType;
    }

    @NotBlank(groups = OutgoingMessageRules.class, message = "A outgoing message needs at least a partyId")
    @NotBlank(groups = {IncomingMessageRules.class, ConfirmationMessageRules.class},
            message = "A valid party message must have a partyIdType")
    private String partyId;
    @NotBlank(groups = {IncomingMessageRules.class, ConfirmationMessageRules.class},
            message = "A valid party message must have a partyIdType")
    private String partyIdType;

    public String toString() {
        return new ToStringBuilder(this)
                .append("partyId", partyId)
                .append("partyIdType", partyIdType)
                .toString();
    }

}
