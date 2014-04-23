package eu.ecodex.webadmin.model;

import java.io.Serializable;
import java.util.Date;

public class MessageReportDO implements Serializable {

    private static final long serialVersionUID = -5053455211577710753L;

    private String ebmsMessageId;
    private String fromParty;
    private String toParty;
    private String direction;
    private Date confirmed;
    private Date rejected;

    public String getEbmsMessageId() {
        return ebmsMessageId;
    }

    public void setEbmsMessageId(String ebmsMessageId) {
        this.ebmsMessageId = ebmsMessageId;
    }

    public String getFromParty() {
        return fromParty;
    }

    public void setFromParty(String fromParty) {
        this.fromParty = fromParty;
    }

    public String getToParty() {
        return toParty;
    }

    public void setToParty(String toParty) {
        this.toParty = toParty;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Date getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Date confirmed) {
        this.confirmed = confirmed;
    }

    public Date getRejected() {
        return rejected;
    }

    public void setRejected(Date rejected) {
        this.rejected = rejected;
    }

}
