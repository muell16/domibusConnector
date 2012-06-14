package org.holodeck.ebms3.consumers.impl;

import org.holodeck.ebms3.consumers.EbConsumer;
import org.holodeck.ebms3.module.MsgInfo;

import org.apache.axis2.context.MessageContext;
import org.holodeck.ebms3.module.PartInfo;

import java.util.*;

/**
 * @author Hamid Ben Malek
 */
public class SaveToTopic implements EbConsumer
{
  protected Map<String, String> parameters;

  public void setParameters(Map<String, String> properties)
  {
    this.parameters = properties;
  }

  public void push(MsgInfo msgInfo, MessageContext outMsgCtx)
  {
    System.out.println("==================== SaveToTopic Consumer ===================");
    MessageContext msgCtx = MessageContext.getCurrentMessageContext();

    String jndiConnectionFactory = parameters.get("jndi.ConnectionFactory");
    String jndiDestination = parameters.get("jndi.destination");
    JmsUtil jms = new JmsUtil(jndiConnectionFactory, jndiDestination);

    try { jms.publish(msgCtx, getProperties(msgInfo)); }
    catch(Exception ex) { ex.printStackTrace(); }
    System.out.println("=============================================================");
  }

  public void pull(MsgInfo msgInfo, MessageContext outMsgCtx)
  {
  }

  private Map<String, String> getProperties(MsgInfo msgInfo)
  {
    Map<String, String> jmsProperties = new HashMap<String, String>();
    jmsProperties.put("eb:mpc", msgInfo.getMpc());
    jmsProperties.put("eb:Action", msgInfo.getAction());
    jmsProperties.put("eb:AgreementRef", msgInfo.getAgreementRef());
    jmsProperties.put("eb:ConversationId", msgInfo.getConversationId());
    jmsProperties.put("eb:MessageId", msgInfo.getMessageId());
    jmsProperties.put("eb:pmode", msgInfo.getPmode());
    jmsProperties.put("eb:RefToMessageId", msgInfo.getRefToMessageId());
    jmsProperties.put("eb:Service", msgInfo.getService());
    jmsProperties.put("eb:From/eb:Role", msgInfo.getFromRole());
    jmsProperties.put("eb:To/eb:Role", msgInfo.getToRole());

    List<PartInfo> parts = msgInfo.getParts();
    List<String> cids = new ArrayList<String>();
    for (PartInfo part : parts)
    {
      cids.add(part.getCid());
    }
    StringBuffer sb = new StringBuffer();
    for (String cid : cids)
    {
      sb.append(cid);
      sb.append("|");
    }
    jmsProperties.put("eb:cids", sb.toString() );
      
    return jmsProperties;
  }
}