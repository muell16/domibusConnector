package org.holodeck.ebms3.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.ebms3.module.*;
import org.apache.axiom.om.util.UUIDGenerator;

/**
 * @author Hamid Ben Malek
 */
public class CollaborationInfo extends Element
{
  private static final long serialVersionUID = -6434646562915911782L;  

  public CollaborationInfo(String agreementRef, String pModeId,
                           String service, String serviceType,
                           String action, String conversationId)
  {
    super(Constants.COLLABORATION_INFO, Constants.NS, Constants.PREFIX);
    if ( agreementRef != null && !agreementRef.trim().equals("") )
    {
      Element agr = addElement(Constants.AGREEMENT_REF, Constants.PREFIX);
      agr.setText(agreementRef);
      if ( pModeId != null && !pModeId.trim().equals("") )
           agr.addAttribute("pmode", pModeId);
    }
    if ( service != null && !service.trim().equals("") )
    {
      Element srv = addElement(Constants.SERVICE, Constants.PREFIX);
      srv.setText(service);
      if ( serviceType != null && !serviceType.trim().equals("") )
           srv.addAttribute("type", serviceType);
    }
    if ( action != null && !action.trim().equals("") )
    {
      Element act = addElement(Constants.ACTION, Constants.PREFIX);
      act.setText(action);
    }
    Element conv = addElement(Constants.CONVERSATION_ID, Constants.PREFIX);
    if ( conversationId != null && !conversationId.trim().equals("") )
         conv.setText(conversationId);
    else conv.setText( UUIDGenerator.getUUID() );
  }

  public String getAgreementRef()
  {
    return getGrandChildValue(Constants.AGREEMENT_REF);
  }
  public String getAction() { return getGrandChildValue(Constants.ACTION); }
  public String getConversationID()
  {
    return getGrandChildValue(Constants.CONVERSATION_ID);
  }
  public String getService() { return getGrandChildValue(Constants.SERVICE); }
}