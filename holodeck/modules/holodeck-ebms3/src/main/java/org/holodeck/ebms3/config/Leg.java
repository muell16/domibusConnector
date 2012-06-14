package org.holodeck.ebms3.config;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;

/**
 * @author Hamid Ben Malek
 */
@Root(name="Leg")
public class Leg implements java.io.Serializable
{
  private static final long serialVersionUID = -5592938470192830737L;

  @Attribute
  protected int number;

  @Attribute(name="userService", required=false)
  protected String userServiceName;

  @Element(name="UserService", required=false)
  protected UserService userService;

  @Attribute(required=false)
  protected String messageLabel;

  @Attribute(required=false)
  protected String mpc;

  @Attribute(name="producer", required=false)
  protected String producerName;

  @Element(name="Producer", required=false)
  protected Producer producer;

  @Attribute(required=false)
  protected String soapAction;

  @Attribute(required=false)
  protected String wsaAction;

  @Attribute(required=false)
  protected String binding;

  @Attribute(required=false)
  protected String reliability;

  @Attribute(required=false)
  protected String security;

  @Element(name="Endpoint", required=false)
  protected Endpoint endpoint;

  @Element(name="ErrorAtSender", required=false)
  protected ErrorAtSender errorAtSender;

  @Element(name="ErrorAtReceiver", required=false)
  protected ErrorAtReceiver errorAtReceiver;

  @Element(name="Authorization", required=false)
  protected Authorization authorization;

  @Element(name="As4Receipt", required=false)
  protected As4Receipt as4Receipt;

  /* This is not to be serialized */
  protected PMode pmode;

  public Leg() {}
  public Leg(int number, String us, String mpc, String producer,
             Endpoint endpoint)
  {
    this.number = number;
    this.userServiceName = us;
    this.mpc = mpc;
    this.producerName = producer;
    this.endpoint = endpoint;
  }

  public int getNumber() { return number; }
  public void setNumber(int number) { this.number = number; }

  public String getUserServiceName() { return userServiceName; }
  public void setUserServiceName(String userService)
  {
    this.userServiceName = userService;
  }

  public UserService getUserService()
  {
    if ( userService != null ) return userService;
    if ( userServiceName == null || userServiceName.trim().equals("") )
         return null;
    if ( pmode == null ) return null;
    return pmode.getUserService(userServiceName); 
  }
  public void setUserService(UserService userService)
  {
    this.userService = userService;
  }

  public String getMessageLabel() { return messageLabel; }
  public void setMessageLabel(String messageLabel)
  {
    this.messageLabel = messageLabel;
  }

  public String getMpc() { return mpc; }
  public void setMpc(String mpc) { this.mpc = mpc; }

  public String getProducerName() { return producerName; }
  public void setProducerName(String producer) { this.producerName = producer; }

  public Producer getProducer()
  {
    if ( producer != null ) return producer;
    if ( producerName == null || producerName.trim().equals("") ) return null;
    if ( pmode == null ) return null;
    return pmode.getProducer(producerName);
  }
  public void setProducer(Producer producer) { this.producer = producer; }

  public String getSoapAction() { return soapAction; }
  public void setSoapAction(String soapAtion) { this.soapAction = soapAtion; }

  public String getWsaAction() { return wsaAction; }
  public void setWsaAction(String wsaAction) { this.wsaAction = wsaAction; }

  public String getBinding() { return binding; }
  public void setBinding(String binding) { this.binding = binding; }

  public Endpoint getEndpoint() { return endpoint; }
  public void setEndpoint(Endpoint endpoint) { this.endpoint = endpoint; }

  public ErrorAtSender getErrorAtSender() { return errorAtSender; }
  public void setErrorAtSender(ErrorAtSender errorAtSender)
  {
    this.errorAtSender = errorAtSender;
  }

  public ErrorAtReceiver getErrorAtReceiver() { return errorAtReceiver; }
  public void setErrorAtReceiver(ErrorAtReceiver errorAtReceiver)
  {
    this.errorAtReceiver = errorAtReceiver;
  }

  public Authorization getAuthorization() { return authorization; }
  public void setAuthorization(Authorization authorization)
  {
    this.authorization = authorization;
  }

  public As4Receipt getAs4Receipt() { return as4Receipt; }
  public void setAs4Receipt(As4Receipt as4Receipt)
  {
    this.as4Receipt = as4Receipt;
  }

  public String getReliability() { return reliability; }
  public void setReliability(String reliability) { this.reliability = reliability; }

  public String getSecurity() { return security; }
  public void setSecurity(String security) { this.security = security; }

  public PMode getPmode() { return pmode; }
  public void setPmode(PMode pmode) { this.pmode = pmode; }

  public String getSoapVersion()
  {
    if ( endpoint == null ) return "1.1";
    return endpoint.getSoapVersion();
  }

  // convenient methods...
  public String getReceiptTo()
  {
    if ( as4Receipt == null ) return null;
    return as4Receipt.getReceiptTo();
  }
  public String getReceiptReply()
  {
    if ( as4Receipt == null ) return null;
    return as4Receipt.getValue();
  }
}