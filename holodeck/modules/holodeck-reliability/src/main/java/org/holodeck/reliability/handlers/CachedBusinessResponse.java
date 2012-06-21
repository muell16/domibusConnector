package org.holodeck.reliability.handlers;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.log4j.Logger;
import org.holodeck.common.soap.Util;
import org.holodeck.reliability.module.Constants;
import org.holodeck.reliability.module.DbStore;
import org.holodeck.reliability.persistent.BusinessResponse;

/**
 * This handler operates at the server side in the OUT_FLOW period and it
 * simply stores in the database the outgoing message (provided that the
 * corresponding request was an RM-Request and it has been delivered)
 *
 * @author Hamid Ben Malek
 */
public class CachedBusinessResponse extends AbstractHandler
{
//  private static final Log log = LogFactory.getLog(CachedBusinessResponse.class.getName());
  private static final Logger log = Logger.getLogger(CachedBusinessResponse.class.getName());
  private String logPrefix = "";

  public InvocationResponse invoke(MessageContext msgCtx) throws AxisFault
  {
    if ( !msgCtx.isServerSide() || msgCtx.getFLOW() != MessageContext.OUT_FLOW)
         return InvocationResponse.CONTINUE;
    if ( log.isDebugEnabled() ) logPrefix = Util.logPrefix(msgCtx);
    log.debug(logPrefix);

    OMElement wsrmReq =
       (OMElement)Util.getPropertyFromInMsgCtx(msgCtx, Constants.IN_REQUEST);

    if ( wsrmReq == null || !wsrmReq.getLocalName().equals(Constants.REQUEST) )
         return InvocationResponse.CONTINUE;

    String groupId =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.MESSAGE_ID, "groupId");
    String seq =
      Util.getGrandChildAttributeValue(wsrmReq, Constants.SEQUENCE_NUM, "number");
    DbStore store = (DbStore)msgCtx.getParameter(Constants.STORE).getValue();
    boolean delivered = store.isDelivered(groupId, seq);
    if ( !delivered ) return InvocationResponse.CONTINUE;;
    BusinessResponse bResp = new BusinessResponse(groupId, seq, msgCtx);
    store.save(bResp);
    log.info(logPrefix + "outgoing business response saved to DB");

    return InvocationResponse.CONTINUE;
  }
}