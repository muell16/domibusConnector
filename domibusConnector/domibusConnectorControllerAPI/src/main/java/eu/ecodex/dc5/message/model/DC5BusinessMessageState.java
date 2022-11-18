package eu.ecodex.dc5.message.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class DC5BusinessMessageState {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private DC5Confirmation confirmation;

    //TODO state
    private String state;




}
