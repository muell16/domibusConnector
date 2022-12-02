package eu.ecodex.dc5.transport.model;

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

    @OneToOne(optional = false)
    private DC5TransportRequestState currentState;

    @OneToMany
    private List<DC5TransportRequestState> states = new ArrayList<>();



}
