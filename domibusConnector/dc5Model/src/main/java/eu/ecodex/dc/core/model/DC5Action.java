package eu.ecodex.dc.core.model;

import javax.persistence.Embeddable;

@Embeddable
public class DC5Action {

    public DC5Action() {}

    public DC5Action(String action) {
        this.action = action;
    }
    private String action;
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
}
