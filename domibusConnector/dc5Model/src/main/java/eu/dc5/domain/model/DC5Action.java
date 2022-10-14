package eu.dc5.domain.model;

import javax.persistence.Embeddable;

@Embeddable
public class DC5Action {
    private String action;
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
