package eu.ecodex.dc5.flow.steps;

import eu.ecodex.dc5.core.model.DC5Msg;
import eu.ecodex.dc5.core.model.DC5MsgProcess;
import eu.ecodex.dc5.flow.api.DC5TransformToDomain;
import eu.ecodex.dc5.flow.api.Step;

public class DC5LookupDomainStep {

    @Step(name = "LookupDomainStep")
    public DC5Msg lookupDomain(DC5Msg msg) {
//        msg.setDomain()
        return msg;
    }


}
