package eu.dc5.domain.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = DC5ProcStep.TABLE_NAME)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DC5_PROC_STEP_TYPE_ID",
        discriminatorType = DiscriminatorType.INTEGER,
        length = 1
)
@DiscriminatorValue("1")
public class DC5ProcStep {

    public static final String TABLE_NAME = "DC5_PROC_STEP";

    @Id
    @Column(name="STEP_ID", nullable = false, unique = true)
    private String stepId;

    private LocalDateTime created;
}
