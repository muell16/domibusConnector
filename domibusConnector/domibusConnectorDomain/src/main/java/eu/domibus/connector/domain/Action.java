package eu.domibus.connector.domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class Action {

    private String action;
    
    private boolean pdfRequired;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isPdfRequired() {
        return pdfRequired;
    }

    public void setPdfRequired(boolean pdfRequired) {
        this.pdfRequired = pdfRequired;
    }
    
    
}
