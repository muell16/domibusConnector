package eu.ecodex.webadmin.model.gateway;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TB_RECEIVED_USER_MSG")
public class TbReceivedUserMsg {

    @Column(name = "ACTION")
    private String ACTION;

    @Column(name = "AGREEMENT_REF")
    private String AGREEMENT_REF;

    @Column(name = "CONVERSATION_ID")
    private String CONVERSATION_ID;

    @Column(name = "FROM_ROLE")
    private String FROM_ROLE;

    @Id
    @Column(name = "MESSAGE_ID")
    private String MESSAGE_ID;

    @OneToOne
    @JoinColumn(name = "MESSAGE_ID", nullable = false)
    private TbReceiptTracking tbReceiptTracking;

    @Column(name = "MPC")
    private String MPC;

    @Column(name = "PMODE")
    private String PMODE;

    @Column(name = "REF_TO_MESSAGE_ID")
    private String REF_TO_MESSAGE_ID;

    @Column(name = "SERVICE")
    private String SERVICE;

    @Column(name = "TO_ROLE")
    private String TO_ROLE;

    @Column(name = "ID")
    private String ID;

    public String getACTION() {
        return ACTION;
    }

    public void setACTION(String aCTION) {
        ACTION = aCTION;
    }

    public String getAGREEMENT_REF() {
        return AGREEMENT_REF;
    }

    public void setAGREEMENT_REF(String aGREEMENT_REF) {
        AGREEMENT_REF = aGREEMENT_REF;
    }

    public String getCONVERSATION_ID() {
        return CONVERSATION_ID;
    }

    public void setCONVERSATION_ID(String cONVERSATION_ID) {
        CONVERSATION_ID = cONVERSATION_ID;
    }

    public String getFROM_ROLE() {
        return FROM_ROLE;
    }

    public void setFROM_ROLE(String fROM_ROLE) {
        FROM_ROLE = fROM_ROLE;
    }

    public String getMPC() {
        return MPC;
    }

    public void setMPC(String mPC) {
        MPC = mPC;
    }

    public String getPMODE() {
        return PMODE;
    }

    public void setPMODE(String pMODE) {
        PMODE = pMODE;
    }

    public String getREF_TO_MESSAGE_ID() {
        return REF_TO_MESSAGE_ID;
    }

    public void setREF_TO_MESSAGE_ID(String rEF_TO_MESSAGE_ID) {
        REF_TO_MESSAGE_ID = rEF_TO_MESSAGE_ID;
    }

    public String getSERVICE() {
        return SERVICE;
    }

    public void setSERVICE(String sERVICE) {
        SERVICE = sERVICE;
    }

    public String getTO_ROLE() {
        return TO_ROLE;
    }

    public void setTO_ROLE(String tO_ROLE) {
        TO_ROLE = tO_ROLE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public TbReceiptTracking getTbReceiptTracking() {
        return tbReceiptTracking;
    }

    public void setTbReceiptTracking(TbReceiptTracking tbReceiptTracking) {
        this.tbReceiptTracking = tbReceiptTracking;
    }

    public String getMESSAGE_ID() {
        return MESSAGE_ID;
    }

    public void setMESSAGE_ID(String mESSAGE_ID) {
        MESSAGE_ID = mESSAGE_ID;
    }

}
