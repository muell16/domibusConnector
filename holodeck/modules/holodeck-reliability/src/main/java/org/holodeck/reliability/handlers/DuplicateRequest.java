package org.holodeck.reliability.handlers;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.log4j.Logger;
import org.holodeck.common.soap.Util;
import org.holodeck.reliability.module.Constants;
import org.holodeck.reliability.module.DbStore;
import org.holodeck.reliability.persistent.BusinessResponse;

/**
 * This handler simply returns a cached business response to a duplicate
 * business request (the duplicate request is also blocked by this
 * handler when there is a corresponding cached business response)
 *
 * @author Hamid Ben Malek
 */
public class DuplicateRequest extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(DuplicateRequest.class.getName());
  private static final Logger log = Logger.getLogger(DuplicateRequest.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( !msgCtx.isServerSide() ) return InvocationResponse.CONTINUE;
    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    //log.debug(logPrefix + msgCtx.getEnvelope().getHeader());
    Util.debug(log, logPrefix, msgCtx.getEnvelope().getHeader());

    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return InvocationResponse.CONTINUE;
    OMElement wsrmReq =
       Util.getGrandChildNameNS(header, Constants.REQUEST, Constants.NS);
    if (wsrmReq == null) return InvocationResponse.CONTINUE;

    if ( msgCtx.getFLOW() == MessageContext.IN_FLOW )
    {
      String replyPattern = Util.getGrandChildValue(wsrmReq, Constants.VALUE);
      log.debug(logPrefix + "replyPattern from previous request is " +
                replyPattern);
      if ( replyPattern == null || !replyPattern.equals("Response") )
           msgCtx.setProperty(org.holodeck.common.Constants.EXPECT_ACK, true);
      else msgCtx.setProperty(org.holodeck.common.Constants.EXPECT_ACK, false);
    }

    String groupId =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.MESSAGE_ID, "groupId");
    String seq =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "number");
    DbStore store = (DbStore)msgCtx.getParameter(Constants.STORE).getValue();
    boolean duplicate = store.isDuplicate(groupId, seq);
    if ( duplicate )
        log.info(logPrefix + "Received message is a duplicate");
    if (!duplicate)
    {
      // put the RM-Request Element as a property in Msg context, so
      // that the outgoing Msg context may retrieve it.
      if (msgCtx.getFLOW() == MessageContext.IN_FLOW && msgCtx.isServerSide())
          msgCtx.setProperty(Constants.IN_REQUEST, wsrmReq);
      return InvocationResponse.CONTINUE;
    }
    BusinessResponse bresp = store.getBusinessResponse(groupId, seq);
    if ( bresp != null )
    {
      MessageContext resp = bresp.getMessageContext();
              //bresp.getMessageContext(msgCtx.getConfigurationContext());
      Util.sendResponse(resp, msgCtx);
      log.info(logPrefix + "cached business response sent back to client");
      return InvocationResponse.ABORT;
    }
    return InvocationResponse.ABORT;
  }
}