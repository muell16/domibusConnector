package org.holodeck.ebms3.web;

/**
 * @author Hamid Ben Malek
 */
public class MsgMetadata implements java.io.Serializable
{
  protected String pmode;
  protected String agreementRef;
  protected String conversationId;
  protected String refToMessageId;
  protected Party[] fromParties;
  protected MsgProperty[] messageProperties;
  protected String bodyPayload;

  public String getPmode() { return pmode; }
  public void setPmode(String pmode) { this.pmode = pmode; }

  public String getAgreementRef() { return agreementRef; }
  public void setAgreementRef(String agreementRef)
  {
    this.agreementRef = agreementRef;
  }

  public String getConversationId() { return conversationId; }
  public void setConversationId(String conversationId)
  {
    this.conversationId = conversationId;
  }

  public String getRefToMessageId() { return refToMessageId; }
  public void setRefToMessageId(String refToMessageId)
  {
    this.refToMessageId = refToMessageId;
  }

  public Party[] getFromParties() { return fromParties; }
  public void setFromParties(Party[] fromParties)
  {
    this.fromParties = fromParties;
  }

  public MsgProperty[] getMessageProperties()
  {
    return messageProperties;
  }
  public void setMessageProperties(MsgProperty[] messageProperties)
  {
    this.messageProperties = messageProperties;
  }

  public String getBodyPayload() { return bodyPayload; }
  public void setBodyPayload(String bodyPayload)
  {
    this.bodyPayload = bodyPayload;
  }
}