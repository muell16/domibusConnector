package eu.ecodex.dc.core.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = DC5ProcessStep.TABLE_NAME)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DC5_PROC_STEP_TYPE",
        discriminatorType = DiscriminatorType.STRING,
        length = 100
)
@DiscriminatorValue("PROCESS_STEP")
public class DC5ProcessStep {


    public enum STEPS {
        RECEIVE_STEP
    }

    public static final String TABLE_NAME = "DC5_PROC_STEP";

    @Id
    @Column(name="PK_ID")
    private Long id;

    @Column(name="STEP_NAME", nullable = false)
    private String stepName;

    @Column(name="ERROR")
    @Lob
    private String error;

    @Column(name="ERROR_TXT")
    @Lob
    private String errorText;

    @ManyToOne
    private DC5Msg messageResult;

    private LocalDateTime created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepId) {
        this.stepName = stepId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public DC5Msg getMessageResult() {
        return messageResult;
    }

    public void setMessageResult(DC5Msg messageResult) {
        this.messageResult = messageResult;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
