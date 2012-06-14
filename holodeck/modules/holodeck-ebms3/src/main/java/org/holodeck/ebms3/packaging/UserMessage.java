package org.holodeck.ebms3.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.ebms3.module.*;

/**
 * @author Hamid Ben Malek
 */
public class UserMessage extends Element
{
  private static final long serialVersionUID = 4124457807040373968L;  

  public UserMessage(MessageInfo mi, PartyInfo partyInfo, CollaborationInfo ci,
                     MessageProperties mp, PayloadInfo pi)
  {
    super(Constants.USER_MESSAGE, Constants.NS, Constants.PREFIX);
    if ( mi != null ) addChild(mi);
    else addChild( new MessageInfo() );
    if ( partyInfo != null ) addChild(partyInfo);
    if ( ci != null ) addChild(ci);
    if ( mp != null ) addChild(mp);
    if ( pi != null ) addChild(pi);
  }

  public UserMessage(String mpc, MessageInfo mi, PartyInfo partInfo,
                     CollaborationInfo ci, MessageProperties mp, PayloadInfo pi)
  {
    this(mi, partInfo, ci, mp, pi);
    addAttribute("mpc", mpc);
  }

  public UserMessage(String mpc, String[] fromPartyIds, String[] toPartyIds,
                     String service, String action, String conversationId,
                     String[] cids)
  {
    super(Constants.USER_MESSAGE, Constants.NS, Constants.PREFIX);
    if ( mpc != null && !mpc.trim().equals("") ) addAttribute("mpc", mpc);
    addChild(new MessageInfo());
    PartyInfo partyInfo = new PartyInfo();
    partyInfo.addFromParties(fromPartyIds, null);
    partyInfo.addToParties(toPartyIds, null);
    addChild(partyInfo);
    CollaborationInfo ci =
      new CollaborationInfo(null, null, service, null, action, conversationId);
    addChild(ci);
    addChild( new PayloadInfo(cids) );
  }

  public UserMessage(String mpc, String fromPartyID, String toPartyID,
                      String service, String action, String conversationId,
                      String[] cids)
  {
    super(Constants.USER_MESSAGE, Constants.NS, Constants.PREFIX);
    if ( mpc != null && !mpc.trim().equals("") ) addAttribute("mpc", mpc);
    addChild(new MessageInfo());
    PartyInfo partyInfo = new PartyInfo();
    partyInfo.addFromParty(fromPartyID, null, null);
    partyInfo.addFromParty(toPartyID, null, null);
    addChild(partyInfo);
    CollaborationInfo ci =
      new CollaborationInfo(null, null, service, null, action, conversationId);
    addChild(ci);
    addChild( new PayloadInfo(cids) );
  }
}