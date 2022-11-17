package eu.ecodex.dc5.core.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Entity(name = DC5MsgProcess.TABLE_NAME)
@Data
public class DC5MsgProcess {

    public static final String TABLE_NAME = "DC5_MSG_PROCESS";

    @Id
    @Column(name = "ID", nullable = false)
//    @TableGenerator(name = "seq" + TABLE_NAME,
//            table = DC5PersistenceSettings.SEQ_STORE_TABLE_NAME,
//            pkColumnName = DC5PersistenceSettings.SEQ_NAME_COLUMN_NAME,
//            pkColumnValue = TABLE_NAME + ".ID",
//            valueColumnName = DC5PersistenceSettings.SEQ_VALUE_COLUMN_NAME,
//            initialValue = DC5PersistenceSettings.INITIAL_VALUE,
//            allocationSize = DC5PersistenceSettings.ALLOCATION_SIZE)
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
    @CollectionTable(name = TABLE_NAME + DC5PersistenceSettings.PROPERTY_SUFFIX,
            joinColumns = @JoinColumn(name = TABLE_NAME,
                    referencedColumnName = "ID"))
    @MapKeyColumn(name = "PROPERTY_NAME", nullable = false)
    @Column(name = "PROPERTY_VALUE", length = 2048)
    private Map<String, String> properties;

    public void addProcessStep(DC5ProcessStep processStep) {
        this.procStepList.add(processStep);
    }

}
