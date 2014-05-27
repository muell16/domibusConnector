package eu.ecodex.webadmin.model.gateway;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TB_RECEIPT_TRACKING")
public class TbReceiptTracking {

    @Column(name = "ID", unique = true)
    private String ID;

    @Column(name = "FIRST_RECEPTION")
    private Date FIRST_RECEPTION;

    @Column(name = "LAST_TRANSMISSION")
    private Date LAST_TRANSMISSION;

    @Id
    @Column(name = "MESSAGE_ID")
    private String MESSAGE_ID;

    /*
     * @OneToOne(mappedBy = "tbReceiptTracking", fetch = FetchType.EAGER)
     * private TbMessageToSend tbMessageToSend;
     * 
     * @OneToOne(mappedBy = "tbReceiptTracking", fetch = FetchType.EAGER)
     * private TbReceivedUserMsg tbReceivedUserMsg;
     */
    @Column(name = "PMODE")
    private String PMODE;

    @Column(name = "RECEIPT")
    private String RECEIPT;

    @Column(name = "RETRANSMISSIONS")
    private Integer RETRANSMISSIONS;

    @Column(name = "STATUS")
    private String STATUS;

    @Column(name = "TO_URL")
    private String TO_URL;

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public Date getFIRST_RECEPTION() {
        return FIRST_RECEPTION;
    }

    public void setFIRST_RECEPTION(Date fIRST_RECEPTION) {
        FIRST_RECEPTION = fIRST_RECEPTION;
    }

    public Date getLAST_TRANSMISSION() {
        return LAST_TRANSMISSION;
    }

    public void setLAST_TRANSMISSION(Date lAST_TRANSMISSION) {
        LAST_TRANSMISSION = lAST_TRANSMISSION;
    }

    public String getPMODE() {
        return PMODE;
    }

    public void setPMODE(String pMODE) {
        PMODE = pMODE;
    }

    public String getRECEIPT() {
        return RECEIPT;
    }

    public void setRECEIPT(String rECEIPT) {
        RECEIPT = rECEIPT;
    }

    public Integer getRETRANSMISSIONS() {
        return RETRANSMISSIONS;
    }

    public void setRETRANSMISSIONS(Integer rETRANSMISSIONS) {
        RETRANSMISSIONS = rETRANSMISSIONS;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String sTATUS) {
        STATUS = sTATUS;
    }

    public String getTO_URL() {
        return TO_URL;
    }

    public void setTO_URL(String tO_URL) {
        TO_URL = tO_URL;
    }

    public String getMESSAGE_ID() {
        return MESSAGE_ID;
    }

    public void setMESSAGE_ID(String mESSAGE_ID) {
        MESSAGE_ID = mESSAGE_ID;
    }

}
