package eu.ecodex.connector.common.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "OUTGOING_ECODEX_MESSAGES")
@SequenceGenerator(sequenceName = "ECMSL_SEQ", name = "ECMSL_SEQ_GEN")
public class OutgoingMessage extends Message {

    @Column(name = "SENT_TO_GW_DT")
    private Date sentToGatewayDt;
}
