package eu.ecodex.dc5.core.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Entity(name = DC5MsgProcess.TABLE_NAME)

@Getter
@Setter
@NoArgsConstructor
public class DC5MsgProcess {

    public static final String TABLE_NAME = "DC5_MSG_PROCESS";

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue
    private Long id;

//    @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval = true)
//    private DC5Msg message;

//    @OneToOne(optional = true)
//    private DC5Domain domain;

    @Column(name = "PROCESS_ID")
    private String processId;

    @Column(name = "CREATED")
    private LocalDateTime created;

    @Column(name = "FINISHED")
    private LocalDateTime finished;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DC5ProcessStep> procStepList = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable
    @MapKeyColumn(name = "PROPERTY_NAME", nullable = false)
    @Column(name = "PROPERTY_VALUE", length = 2048)
    private Map<String, String> properties;

    public void addProcessStep(DC5ProcessStep processStep) {
        this.procStepList.add(processStep);
    }

}
