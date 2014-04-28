package eu.ecodex.webadmin.blogic;

import java.util.List;

import eu.ecodex.webadmin.model.MessageReportDO;

public interface IMessageFilter {

    public List<MessageReportDO> filterByFromParty(String fromParty, List<MessageReportDO> customResultList);

    public List<MessageReportDO> filterByToParty(String toParty, List<MessageReportDO> customResultList);

    public List<MessageReportDO> filterByDirection(String direction, List<MessageReportDO> customResultList);

    public List<MessageReportDO> filterByStatus(String status, List<MessageReportDO> customResultList);

    public List<MessageReportDO> filterByService(String service, List<MessageReportDO> customResultList);

    public List<MessageReportDO> filterByAction(String action, List<MessageReportDO> customResultList);

    public List<MessageReportDO> filterByLastEvidence(String evidence, List<MessageReportDO> customResultList);

}