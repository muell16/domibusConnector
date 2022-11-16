package eu.domibus.connector.controller.processor.steps;

import eu.ecodex.dc5.message.model.DC5Message;

public interface MessageProcessStep {

    public boolean executeStep(DC5Message DC5Message);

}
