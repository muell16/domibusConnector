package org.holodeck.ebms3.config;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;

/**
 * @author Hamid Ben Malek
 */
@Root(name="UserService", strict=false)
public class UserService implements java.io.Serializable
{
  private static final long serialVersionUID = -5593316570192830737L;

  @Attribute(required=false)
  protected String name;

  @Element(name="ToPartyInfo")
  protected ToParty toParty;

  @Element(name="CollaborationInfo")
  protected CollaborationInfo collaborationInfo;

  @Element(name="MessageProperties", required=false)
  protected MessageProperties messageProperties;

  @Element(name="PayloadInfo", required=false)
  protected PayloadInfo payloadInfo;

  public UserService() {}
  public UserService(ToParty toParty, CollaborationInfo ci, MessageProperties mp,
                     PayloadInfo pi)
  {
    this.toParty = toParty;
    this.collaborationInfo = ci;
    this.messageProperties = mp;
    this.payloadInfo = pi;
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public ToParty getToParty() { return toParty; }
  public void setToParty(ToParty toParty) { this.toParty = toParty; }

  public CollaborationInfo getCollaborationInfo() { return collaborationInfo; }
  public void setCollaborationInfo(CollaborationInfo collaborationInfo)
  {
    this.collaborationInfo = collaborationInfo;
  }

  public MessageProperties getMessageProperties()
  {
    return messageProperties;
  }
  public void setMessageProperties(MessageProperties messageProperties)
  {
    this.messageProperties = messageProperties;
  }

  public PayloadInfo getPayloadInfo() { return payloadInfo; }
  public void setPayloadInfo(PayloadInfo payloadInfo)
  {
    this.payloadInfo = payloadInfo;
  }

  public void addToParty(Party party)
  {
    if ( party == null ) return;
    if ( toParty == null ) toParty = new ToParty();
    toParty.addParty(party);
  }
}