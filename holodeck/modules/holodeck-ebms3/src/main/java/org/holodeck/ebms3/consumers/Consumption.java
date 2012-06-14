package org.holodeck.ebms3.consumers;

import org.holodeck.ebms3.module.MsgInfo;
//import org.holodeck.ebms3.pmodes.Party;
import org.holodeck.ebms3.config.Party;
import java.util.*;
import org.simpleframework.xml.*;

/**
 * @author Hamid Ben Malek
 */
@Root
public class Consumption
{
  @Element(required=false)
  protected MsgInfo filter;

  @ElementList(inline=true)
  protected List<ConsumerInfo> consumers = new ArrayList<ConsumerInfo>();

  public MsgInfo getFilter() { return filter; }
  public void setFilter(MsgInfo filter) { this.filter = filter; }

  public List<ConsumerInfo> getConsumers() { return consumers; }
  public void setConsumers(List<ConsumerInfo> consumers)
  {
    this.consumers = consumers;
  }

  public void addConsumerInfo(ConsumerInfo consumerInfo)
  {
    if ( consumerInfo == null ) return;
    consumers.add(consumerInfo);
  }

  public boolean match(MsgInfo info)
  {
    if ( filter == null ) return true;
    if ( filter.getMpc() != null &&
         !filter.getMpc().equalsIgnoreCase(info.getMpc()) ) return false;
    if ( filter.getMessageId() != null &&
         !filter.getMessageId().equalsIgnoreCase(info.getMessageId()) )
         return false;
    if ( filter.getRefToMessageId() != null &&
         !filter.getRefToMessageId().equalsIgnoreCase(info.getRefToMessageId()) )
         return false;
    if ( filter.getFromRole() != null &&
         !filter.getFromRole().equalsIgnoreCase(info.getFromRole()) )
         return false;
    if ( filter.getToRole() != null &&
         !filter.getToRole().equalsIgnoreCase(info.getToRole()) ) return false;
    if ( filter.getAgreementRef() != null &&
         !filter.getAgreementRef().equalsIgnoreCase(info.getAgreementRef()) )
         return false;
    if ( filter.getPmode() != null &&
         !filter.getPmode().equalsIgnoreCase(info.getPmode()) ) return false;
    if ( filter.getService() != null &&
         !filter.getService().equalsIgnoreCase(info.getService()) ) return false;
    if ( filter.getAction() != null &&
         !filter.getAction().equalsIgnoreCase(info.getAction()) ) return false;
    if ( filter.getConversationId() != null &&
         !filter.getConversationId().equalsIgnoreCase(info.getConversationId()) )
         return false;
    if ( !subsetOf(filter.getFromParties(), info.getFromParties()) ) return false;
    if ( !subsetOf(filter.getToParties(), info.getToParties()) ) return false;

    // Need to check messageProperties .....

    return true;
  }

  private boolean subsetOf(List<Party> p1, List<Party> p2)
  {
    if ( (p1 == null || p1.size() == 0) &&
         (p2 == null || p2.size() == 0)
       )
         return true;
    if ( p2 != null && (p1 == null || p1.size() == 0) ) return true;
    if ( p1 != null && p1.size() > 0 &&
         (p2 == null || p2.size() == 0)  ) return false;

    boolean failed = true;
    for (Party p : p1)
    {
      failed = true;
      for (Party q : p2)
      {
        if ( p.equals(q) ) failed = false;
      }
    }
    return !failed;
  }
}