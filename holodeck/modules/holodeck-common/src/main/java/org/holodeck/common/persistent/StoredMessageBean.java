package org.holodeck.common.persistent;

//import javax.persistence.*;
//import org.hibernate.annotations.GenericGenerator;

/**
 * @author Hamid Ben Malek
 */
//@Entity
//@Table(name = "Stored_Msg_Bean")
public class StoredMessageBean implements java.io.Serializable
{
  private static final long serialVersionUID = -4691246535249341297L;  

  //@Id @GeneratedValue(generator="system-uuid")
  //@GenericGenerator(name="system-uuid", strategy="uuid")
  //@Column(name = "Stored_Msg_Bean_ID")
  private String id = null;

  //@Column(name = "Transport_Out")
  protected String transportOut;

  //@Column(name = "Axis_ServiceGroup")
  protected String axisServiceGroup;

  //@Column(name = "Axis_Service")
  protected String axisService;

  //@Column(name = "Axis_Operation_Name")
  protected String axisOperationLocalPart;

  //@Column(name = "Axis_Operation_Prefix")
  protected String axisOperationPrefix;

  //@Column(name = "Axis_Operation_URI")
  protected String axisOperationNamespaceURI;

  //@Column(name = "Operation_MEP")
  protected String axisOperationMEP;

  //@Column(name = "To_URL")
  protected String toURL;

  //@Column(name = "Transport_To")
  protected String transportTo;

  //@Column(name = "TransportContro")
  protected String transportControl;

  //@Column(name = "Flow")
  protected int flow;

  //@Column(name = "Execution_Chain")
  protected String executionChainString;

  //@Column(name = "Msg_Receiver")
  protected String messageReceiverString;

  //@Column(name = "ServiceSide")
  protected boolean serverSide;

  //@Column(name = "InMsgStore_Key")
  protected String inMessageStoreKey;

  //@Column(name = "Msg_ID")
  protected String messageID;

  //@Column(name = "Callback_Classname")
  protected String callbackClassName;

  //@Column(name = "Action")
  protected String action;

  //@Lob
  //@Column(name = "Properties", length = 1999999999)
  protected byte[] properties;

  //@OneToOne(mappedBy="bean")
  //protected SwA swa = null;

  public StoredMessageBean() {}

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  protected String getMessageID() { return messageID; }
  public void setMessageID(String messageID) { this.messageID = messageID; }

  public String getTransportOut() { return transportOut; }
  public void setTransportOut(String transportSender)
  {
    this.transportOut = transportSender;
  }

  public String getAxisOperationLocalPart() { return axisOperationLocalPart; }
  public void setAxisOperationLocalPart(String axisOperation)
  {
    this.axisOperationLocalPart = axisOperation;
  }

  public String getAxisOperationPrefix() { return axisOperationPrefix; }
  public void setAxisOperationPrefix(String axisOperationPrefix)
  {
    this.axisOperationPrefix = axisOperationPrefix;
  }

  public String getAxisOperationNamespaceURI() { return axisOperationNamespaceURI;}
  public void setAxisOperationNamespaceURI(String axisOperationNamespaceURI)
  {
    this.axisOperationNamespaceURI = axisOperationNamespaceURI;
  }

    public String getAxisService() { return axisService; }
  public void setAxisService(String axisService)
  {
    this.axisService = axisService;
  }

  public String getAxisServiceGroup() { return axisServiceGroup; }
  public void setAxisServiceGroup(String axisServiceGroup)
  {
    this.axisServiceGroup = axisServiceGroup;
  }

  public String getAxisOperationMEP() { return axisOperationMEP; }
  public void setAxisOperationMEP(String axisOperationAdd)
  {
    this.axisOperationMEP = axisOperationAdd;
  }

  public String getToURL() { return toURL; }
  public void setToURL(String toURL) { this.toURL = toURL; }

  public String getTransportTo() { return transportTo; }
  public void setTransportTo(String transportTo)
  {
    this.transportTo = transportTo;
  }

  public String getExecutionChainString() { return executionChainString; }
  public void setExecutionChainString(String executionChainString)
  {
    this.executionChainString = executionChainString;
  }

  public int getFlow() { return flow; }
  public void setFlow(int flow) { this.flow = flow; }

  public String getMessageReceiverString() { return messageReceiverString; }
  public void setMessageReceiverString(String messageReceiverString)
  {
    this.messageReceiverString = messageReceiverString;
  }

  public boolean isServerSide() { return serverSide; }
  public void setServerSide(boolean serverSide) { this.serverSide = serverSide;}

  public String getInMessageStoreKey() { return inMessageStoreKey; }
  public void setInMessageStoreKey(String requestMessageKey)
  {
    this.inMessageStoreKey = requestMessageKey;
  }

  public String getCallbackClassName() { return callbackClassName; }
  public void setCallbackClassName(String callbackClassName)
  {
    this.callbackClassName = callbackClassName;
  }

  public String getAction() { return action; }
  public void setAction(String action) { this.action = action; }

  public String getTransportControl() { return transportControl; }
  public void setTransportControl(String transportControl)
  {
    this.transportControl = transportControl;
  }

  public byte[] getProperties() { return properties; }
  public void setProperties(byte[] properties) { this.properties = properties; }

  //public SwA getSwa() { return swa; }
  //public void setSwa(SwA swa) { this.swa = swa; }
}