package org.holodeck.ebms3.config;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="PayloadInfo", strict=false)
public class PayloadInfo implements java.io.Serializable
{
  private static final long serialVersionUID = -5593310293832120737L;

  @ElementList(inline=true)
  protected List<MessagePayload> messagePayloads =
                                    new ArrayList<MessagePayload>();

  public PayloadInfo() {}
  public PayloadInfo(List<MessagePayload> messagePayloads)
  {
    this.messagePayloads = messagePayloads;
  }

  public void addMessagePayload(String label, String maxSize, List<Part> parts)
  {
    MessagePayload p = new MessagePayload(label, maxSize, parts);
    messagePayloads.add(p);
  }
  public void addMessagePayload(MessagePayload p) { messagePayloads.add(p); }

  public List<MessagePayload> getMessagePayloads() { return messagePayloads; }
  public void setMessagePayloads(List<MessagePayload> messages)
  {
    this.messagePayloads = messages;
  }

  /* To serialize objects to Flex UI */
  public MessagePayload[] getMessagePayloadsArray()
  {
    if ( messagePayloads == null ) return null;
    MessagePayload[] res = new MessagePayload[messagePayloads.size()];
    int i = 0;
    for (MessagePayload p : messagePayloads)
    {
      res[i] = p;
      i++;
    }
    return res;
  }
  public void setMessagePayloadsArray(MessagePayload[] list)
  {
    if ( list == null || list.length == 0 )
    {
      if ( messagePayloads != null && messagePayloads.size() > 0 )
           messagePayloads.clear();
      return;
    }
    if ( messagePayloads == null )
         messagePayloads = new ArrayList<MessagePayload>();
    if ( messagePayloads.size() > 0 ) messagePayloads.clear();
    for (MessagePayload p : list) addMessagePayload(p);
  }
}