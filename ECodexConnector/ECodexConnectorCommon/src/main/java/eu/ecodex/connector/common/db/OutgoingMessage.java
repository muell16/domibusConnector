package eu.ecodex.connector.common.db;

import java.util.Date;

//@Entity
//@Table(name = "OUTGOING_ECODEX_MESSAGES")
//@SequenceGenerator(sequenceName = "ECMSL_SEQ", name = "ECMSL_SEQ_GEN")
public class OutgoingMessage extends Message {

    // @Column(name = "SENT_TO_GW_DT")
    private Date sentToGatewayDt;
}
