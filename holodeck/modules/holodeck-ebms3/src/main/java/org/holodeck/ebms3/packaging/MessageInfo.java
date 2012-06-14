package org.holodeck.ebms3.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.common.soap.Util;
import org.holodeck.ebms3.module.*;
import org.apache.axiom.om.util.UUIDGenerator;

import java.util.*;

/**
 * @author Hamid Ben Malek
 */
public class MessageInfo extends Element
{
  private static final long serialVersionUID = -2001342282242828965L;  

  public MessageInfo()
  {
    super(Constants.MESSAGE_INFO, Constants.NS, Constants.PREFIX);
    Element timestamp = addElement(Constants.TIMESTAMP, Constants.PREFIX);
    timestamp.setText(Util.dateToUtc(new Date()));

    Element msgId = addElement(Constants.MESSAGE_ID, Constants.PREFIX);
    msgId.setText(UUIDGenerator.getUUID());
  }

  public MessageInfo(String refToMessageId)
  {
    this();
    if ( refToMessageId != null && !refToMessageId.trim().equals("") )
    {
      Element refMsgId = addElement(Constants.REF_TO_MESSAGE_ID, Constants.PREFIX);
      refMsgId.setText(refToMessageId);
    }
  }
    
  public MessageInfo(Date time, String messageId, String refToMessageId)
  {
    super(Constants.MESSAGE_INFO, Constants.NS, Constants.PREFIX);
    Element timestamp = addElement(Constants.TIMESTAMP, Constants.PREFIX);
    if ( time != null ) timestamp.setText(Util.dateToUtc(time));
    else timestamp.setText(Util.dateToUtc(new Date()));

    Element msgId = addElement(Constants.MESSAGE_ID, Constants.PREFIX);
    if ( messageId != null ) msgId.setText(messageId);
    else msgId.setText(UUIDGenerator.getUUID());

    if ( refToMessageId != null && !refToMessageId.trim().equals("") )
    {
      Element refMsgId =
          addElement(Constants.REF_TO_MESSAGE_ID, Constants.PREFIX);
      refMsgId.setText(refToMessageId);
    }
  }

  //  =========================== Getter methods ===============================

  public String getMessageId() { return getGrandChildValue(Constants.MESSAGE_ID); }
  public String getRefToMessageId()
  {
    return getGrandChildValue(Constants.REF_TO_MESSAGE_ID);
  }
  public String getTimestamp() { return getGrandChildValue(Constants.TIMESTAMP); }
}