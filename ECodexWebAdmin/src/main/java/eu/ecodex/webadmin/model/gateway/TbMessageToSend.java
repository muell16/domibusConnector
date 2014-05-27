package eu.ecodex.webadmin.model.gateway;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TB_MESSAGE_TO_SEND")
public class TbMessageToSend {

    @Column(name = "AGREEMENT_REF")
    private String AGREEMENT_REF;

    @Column(name = "CALLBACK_CLASS")
    private String CALLBACK_CLASS;

    @Column(name = "CONVERSATION_ID")
    private String CONVERSATION_ID;

    @Column(name = "LEG_NUMBER")
    private Integer LEG_NUMBER;

    @Id
    @Column(name = "MESSAGE_ID")
    private String MESSAGE_ID;

    @OneToOne
    @JoinColumn(name = "MESSAGE_ID", nullable = false)
    private TbReceiptTracking tbReceiptTracking;

    @Column(name = "PMODE")
    private String PMODE;

    @Column(name = "NAME")
    private String NAME;

    @Column(name = "ROLE")
    private String ROLE;

    @Column(name = "REF_TO_MESSAGE_ID")
    private String REF_TO_MESSAGE_ID;

    @Column(name = "TIME_IN_MILLIS")
    private Long TIME_IN_MILLUS;

    @Column(name = "SENT")
    private Integer SENT;

    @Column(name = "ID")
    private String ID;

    @Column(name = "PAYLOADS_ID")
    private String PAYLOADS_ID;

    @Column(name = "PROPERTIES_ID")
    private String PROPERTIES_ID;

    public String getAGREEMENT_REF() {
        return AGREEMENT_REF;
    }

    public void setAGREEMENT_REF(String aGREEMENT_REF) {
        AGREEMENT_REF = aGREEMENT_REF;
    }

    public String getCALLBACK_CLASS() {
        return CALLBACK_CLASS;
    }

    public void setCALLBACK_CLASS(String cALLBACK_CLASS) {
        CALLBACK_CLASS = cALLBACK_CLASS;
    }

    public String getCONVERSATION_ID() {
        return CONVERSATION_ID;
    }

    public void setCONVERSATION_ID(String cONVERSATION_ID) {
        CONVERSATION_ID = cONVERSATION_ID;
    }

    public Integer getLEG_NUMBER() {
        return LEG_NUMBER;
    }

    public void setLEG_NUMBER(Integer lEG_NUMBER) {
        LEG_NUMBER = lEG_NUMBER;
    }

    public String getPMODE() {
        return PMODE;
    }

    public void setPMODE(String pMODE) {
        PMODE = pMODE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String nAME) {
        NAME = nAME;
    }

    public String getROLE() {
        return ROLE;
    }

    public void setROLE(String rOLE) {
        ROLE = rOLE;
    }

    public String getREF_TO_MESSAGE_ID() {
        return REF_TO_MESSAGE_ID;
    }

    public void setREF_TO_MESSAGE_ID(String rEF_TO_MESSAGE_ID) {
        REF_TO_MESSAGE_ID = rEF_TO_MESSAGE_ID;
    }

    public Long getTIME_IN_MILLUS() {
        return TIME_IN_MILLUS;
    }

    public void setTIME_IN_MILLUS(Long tIME_IN_MILLUS) {
        TIME_IN_MILLUS = tIME_IN_MILLUS;
    }

    public Integer getSENT() {
        return SENT;
    }

    public void setSENT(Integer sENT) {
        SENT = sENT;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getPAYLOADS_ID() {
        return PAYLOADS_ID;
    }

    public void setPAYLOADS_ID(String pAYLOADS_ID) {
        PAYLOADS_ID = pAYLOADS_ID;
    }

    public String getPROPERTIES_ID() {
        return PROPERTIES_ID;
    }

    public void setPROPERTIES_ID(String pROPERTIES_ID) {
        PROPERTIES_ID = pROPERTIES_ID;
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
