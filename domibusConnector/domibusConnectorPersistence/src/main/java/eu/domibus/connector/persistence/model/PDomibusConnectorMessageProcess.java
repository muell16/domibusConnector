package eu.domibus.connector.persistence.model;

import eu.domibus.connector.domain.model.DomibusConnectorMessageProcessId;

import javax.persistence.*;

@Entity
@Table(name = PDomibusConnectorMessageProcess.TABLE_NAME)
public class PDomibusConnectorMessageProcess {

    public static final String TABLE_NAME = "DC_MESSAGE_PROCESS";


    @Id
    @Column(name="ID")
    @TableGenerator(name = "seqMsgProcess",
            table = PDomibusConnectorPersistenceModel.SEQ_STORE_TABLE_NAME,
            pkColumnName = PDomibusConnectorPersistenceModel.SEQ_NAME_COLUMN_NAME,
            pkColumnValue = TABLE_NAME + ".ID",
            valueColumnName = PDomibusConnectorPersistenceModel.SEQ_VALUE_COLUMN_NAME,
            initialValue = 100,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqMsgProcess")
    private Long id;

    @Column(name = "PROCESS_ID")
    private DomibusConnectorMessageProcessId processId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DomibusConnectorMessageProcessId getProcessId() {
        return processId;
    }

    public void setProcessId(DomibusConnectorMessageProcessId processId) {
        this.processId = processId;
    }
}
