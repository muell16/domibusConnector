package eu.dc5.domain.model;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Map;


@Entity(name = DC5MsgProcess.TABLE_NAME)
public class DC5MsgProcess {

    public static final String TABLE_NAME = "DC5_MSG_PROCESS";

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // use the message ids as process ids.
    @JoinColumn(name = "ID") // rename the colum from message_id to id
    private DC5Msg message;

    private String processId; // who needs this?
    private ZonedDateTime created; // TODO UTC or Zoned?
    private ZonedDateTime finished;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = TABLE_NAME + DC5PersistenceSettings.PROPERTY_SUFFIX,
            joinColumns=@JoinColumn(name= TABLE_NAME,
                    referencedColumnName = "ID"))
    @MapKeyColumn (name="PROPERTY_NAME", nullable = false)
    @Column(name="PROPERTY_VALUE", length = 2048)
    private Map<String, String> properties; // TODO: does DC5MsgProcessProperty really need to be a class?

//    private Map<DC5ProcStep, ZonedDateTime> currProcStep;
//    private DC5Domain optionalDomain;
}
