package eu.domibus.webadmin.blogic.connector.statistics.impl;

import java.util.ArrayList;
import java.util.List;

import eu.domibus.webadmin.blogic.connector.statistics.IConnectorMessageFilter;
import eu.domibus.webadmin.commons.BLConstants;
import eu.domibus.webadmin.model.connector.MessageReportDO;

public class ConnectorMessageFilter implements IConnectorMessageFilter {

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.domibus.webadmin.blogic.impl.IMessageFilter#filterByFromParty(java.
     * lang.String, java.util.List)
     */
    @Override
    public List<MessageReportDO> filterByFromParty(String fromParty, List<MessageReportDO> customResultList) {

        List<MessageReportDO> resultList = new ArrayList<MessageReportDO>();

        for (MessageReportDO eCodexMessageInfo : customResultList) {
            if (eCodexMessageInfo.getFrom() != null && fromParty.equals(eCodexMessageInfo.getFrom().getPartyId())) {
                resultList.add(eCodexMessageInfo);
            }
        }
        return resultList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.domibus.webadmin.blogic.impl.IMessageFilter#filterByToParty(java.lang
     * .String, java.util.List)
     */
    @Override
    public List<MessageReportDO> filterByToParty(String toParty, List<MessageReportDO> customResultList) {

        List<MessageReportDO> resultList = new ArrayList<MessageReportDO>();

        for (MessageReportDO eCodexMessageInfo : customResultList) {

            if (eCodexMessageInfo.getTo() != null && toParty.equals(eCodexMessageInfo.getTo().getPartyId())) {
                resultList.add(eCodexMessageInfo);
            }
        }
        return resultList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.domibus.webadmin.blogic.impl.IMessageFilter#filterByDirection(java.
     * lang.String, java.util.List)
     */
    @Override
    public List<MessageReportDO> filterByDirection(String direction, List<MessageReportDO> customResultList) {

        List<MessageReportDO> resultList = new ArrayList<MessageReportDO>();

        for (MessageReportDO eCodexMessageInfo : customResultList) {
            if (eCodexMessageInfo.getMessage().getDirection() != null
                    && direction.equals(eCodexMessageInfo.getMessage().getDirection().toString())) {
                resultList.add(eCodexMessageInfo);
            }
        }
        return resultList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.domibus.webadmin.blogic.impl.IMessageFilter#filterByStatus(java.lang
     * .String, java.util.List)
     */
    @Override
    public List<MessageReportDO> filterByStatus(String status, List<MessageReportDO> customResultList) {

        List<MessageReportDO> resultList = new ArrayList<MessageReportDO>();

        for (MessageReportDO eCodexMessageInfo : customResultList) {
            if (BLConstants.selectorStatusConfirmed.equals(status)) {
                if (eCodexMessageInfo.getMessage().getConfirmed() != null) {
                    resultList.add(eCodexMessageInfo);
                }

            } else if (BLConstants.selectorStatusRejected.equals(status)) {
                if (eCodexMessageInfo.getMessage().getRejected() != null) {
                    resultList.add(eCodexMessageInfo);
                }
            }
        }
        return resultList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.domibus.webadmin.blogic.impl.IMessageFilter#filterByService(java.lang
     * .String, java.util.List)
     */
    @Override
    public List<MessageReportDO> filterByService(String service, List<MessageReportDO> customResultList) {

        List<MessageReportDO> resultList = new ArrayList<MessageReportDO>();

        for (MessageReportDO eCodexMessageInfo : customResultList) {
            if (eCodexMessageInfo.getService() != null && service.equals(eCodexMessageInfo.getService().getService())) {
                resultList.add(eCodexMessageInfo);
            }
        }
        return resultList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.domibus.webadmin.blogic.impl.IMessageFilter#filterByAction(java.lang
     * .String, java.util.List)
     */
    @Override
    public List<MessageReportDO> filterByAction(String action, List<MessageReportDO> customResultList) {

        List<MessageReportDO> resultList = new ArrayList<MessageReportDO>();

        for (MessageReportDO eCodexMessageInfo : customResultList) {
            if (eCodexMessageInfo.getAction() != null && action.equals(eCodexMessageInfo.getAction().getAction())) {
                resultList.add(eCodexMessageInfo);
            }
        }
        return resultList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.domibus.webadmin.blogic.impl.IMessageFilter#filterByLastEvidence(java
     * .lang.String, java.util.List)
     */
    @Override
    public List<MessageReportDO> filterByLastEvidence(String evidence, List<MessageReportDO> customResultList) {

        List<MessageReportDO> resultList = new ArrayList<MessageReportDO>();

        for (MessageReportDO eCodexMessageInfo : customResultList) {
            if (eCodexMessageInfo.getLastEvidenceType() != null
                    && evidence.equals(eCodexMessageInfo.getLastEvidenceType())) {
                resultList.add(eCodexMessageInfo);
            }
        }
        return resultList;
    }

}
