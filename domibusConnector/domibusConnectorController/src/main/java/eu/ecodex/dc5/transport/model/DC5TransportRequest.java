package eu.ecodex.dc5.transport.model;

import eu.ecodex.dc5.message.model.DC5BusinessMessageState;
import eu.ecodex.dc5.message.model.DC5Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class DC5TransportRequest {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private DC5Message message;

    private String remoteMessageId;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private DC5TransportRequestState currentState;

    private String transportSystemId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DC5TransportRequestState> states = new ArrayList<>();

    public void changeCurrentState(DC5TransportRequestState currentState) {
        if (currentState.getId() == null) {
            this.currentState = currentState;
            if (states == null) {
                states = new ArrayList<>();
            }
            states.add(currentState);
        } else {
            throw new IllegalArgumentException("Not a new state!");
        }
    }

}
