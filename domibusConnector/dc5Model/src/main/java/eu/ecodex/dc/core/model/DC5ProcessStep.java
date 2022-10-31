package eu.ecodex.dc.core.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = DC5ProcessStep.TABLE_NAME)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DC5_PROC_STEP_TYPE",
        discriminatorType = DiscriminatorType.STRING,
        length = 100
)
@DiscriminatorValue("PROCESS_STEP")
@Data
public class DC5ProcessStep {


    public enum STEPS {
        RECEIVE_STEP
    }

    public static final String TABLE_NAME = "DC5_PROC_STEP";

    @Id
    @Column(name="PK_ID")
    @GeneratedValue
    private Long id;

    @Column(name="STEP_NAME", nullable = false)
    private String stepName;

    @Column(name="ERROR")
    @Lob
    private String error;

    @Column(name="ERROR_TXT")
    @Lob
    private String longErrorText;

    @ManyToOne
    private DC5Msg messageResult;

    private LocalDateTime created;

}
