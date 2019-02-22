package eu.domibus.webadmin.blogic.connector.statistics;

import java.util.List;

import eu.domibus.webadmin.model.connector.MessageReportDO;

public interface IConnectorMessageFilter {

    public List<MessageReportDO> filterByFromParty(String fromParty, List<MessageReportDO> customResultList);

    public List<MessageReportDO> filterByToParty(String toParty, List<MessageReportDO> customResultList);

    public List<MessageReportDO> filterByDirection(String direction, List<MessageReportDO> customResultList);

    public List<MessageReportDO> filterByStatus(String status, List<MessageReportDO> customResultList);

    public List<MessageReportDO> filterByService(String service, List<MessageReportDO> customResultList);

    public List<MessageReportDO> filterByAction(String action, List<MessageReportDO> customResultList);

    public List<MessageReportDO> filterByLastEvidence(String evidence, List<MessageReportDO> customResultList);

}