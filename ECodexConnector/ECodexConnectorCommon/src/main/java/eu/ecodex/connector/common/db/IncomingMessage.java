package eu.ecodex.connector.common.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "INCOMING_ECODEX_MESSAGES")
@SequenceGenerator(sequenceName = "ECMSL_SEQ", name = "ECMSL_SEQ_GEN")
public class IncomingMessage {

    @Column(name = "SENT_TO_NAT_BACKEND_DT")
    private Date sentToNatoinalBackendDt;
}
