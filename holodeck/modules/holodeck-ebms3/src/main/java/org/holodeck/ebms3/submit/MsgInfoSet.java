package org.holodeck.ebms3.submit;

import org.holodeck.ebms3.config.Party;
import org.holodeck.ebms3.config.Producer;
//import org.holodeck.ebms3.pmodes.Party;
//import org.holodeck.ebms3.pmodes.Producer;
//import org.holodeck.ebms3.pmodes.Leg;
//import org.holodeck.ebms3.pmodes.PMode;
//import org.holodeck.ebms3.module.Constants;

import org.simpleframework.xml.*;
import org.simpleframework.xml.core.*;

import java.io.*;
import java.util.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="Metadata")
public class MsgInfoSet implements Serializable
{
  private static final long serialVersionUID = -5232023672278765474L;  

  @Element
  protected String pmode = null;

  @Element(name="AgreementRef", required=false)
  protected String agreementRef = null;

  @Element(name="ConversationId", required=false)
  protected String conversationId = null;

  @Element(name="RefToMessageId", required=false)
  protected String refToMessageId = null;

  @Element(name="From", required=false)
  protected Producer producer;

  @Element(name="Properties", required=false)
  protected Properties properties;

  @Element(required=false)
  protected int legNumber = -1;

  @Element(required=false)
  protected String callbackClass = null;

  @Element(name="Payloads", required=false)
  protected Payloads payloads;

  public MsgInfoSet() {}
  public MsgInfoSet(String agreementRef, String pmode, String conversationId,
                          String refToMessageId)
  {
    this.agreementRef = agreementRef;
    this.pmode = pmode;
    this.conversationId = conversationId;
    this.refToMessageId = refToMessageId;
  }

  public MsgInfoSet(String fromParty, String pmodeName)
  {
    if ( fromParty != null && !fromParty.trim().equals("") )
         addFromParty(null, fromParty);
    this.pmode = pmodeName;
  }

  public MsgInfoSet(String[] fromParty, String fromRole, String pmodeName)
  {
    if ( fromParty != null && fromParty.length > 0 )
    {
      for (String p : fromParty) addFromParty(null, p);
    }
    setFromRole(fromRole);
    this.pmode = pmodeName;
  }

  public String getPmode() { return pmode; }
  public void setPmode(String pmodeId) { this.pmode = pmodeId; }

  public String getRefToMessageId() { return refToMessageId; }
  public void setRefToMessageId(String refToMessageId) { this.refToMessageId = refToMessageId; }

  public String getFromRole()
  {
    if ( producer != null ) return producer.getRole();
    else return null;
  }
  public void setFromRole(String fromRole)
  {
    if ( producer == null ) producer = new Producer();
    producer.setRole(fromRole);
  }

  public String getAgreementRef() { return agreementRef; }
  public void setAgreementRef(String agreementRef) { this.agreementRef = agreementRef; }

  public String getConversationId() { return conversationId; }
  public void setConversationId(String conversationId)
  {
    this.conversationId = conversationId;
  }

  public Properties getProperties() { return properties; }
  public void setProperties(Properties properties)
  {
    this.properties = properties;
  }

  public Map<String, String> getPropertiesMap()
  {
    if ( properties != null ) return properties.getProperties();
    else return null;
  }
  public void setPropertiesMap(Map<String, String> props)
  {
    if ( properties == null ) properties = new Properties();
    properties.setProperties(props);
  }
  public void addProperty(String name, String value)
  {
    if ( properties == null ) properties = new Properties();
    properties.addProperty(name, value);
  }
  public String getProperty(String propertyName)
  {
    if ( properties == null ) properties = new Properties();
    return properties.getProperty(propertyName);
  }

  public int getLegNumber() { return legNumber; }
  public void setLegNumber(int legNumber) { this.legNumber = legNumber; }

  public String getCallbackClass() { return callbackClass; }
  public void setCallbackClass(String callbackClass)
  {
    this.callbackClass = callbackClass;
  }

  public List<Party> getFromParties()
  {
    if ( producer != null ) return producer.getParties();
    else return null;
  }
  public void setFromParties(List<Party> fromParties)
  {
    if ( producer == null ) producer = new Producer();
    producer.setParties(fromParties);
  }
  public void addFromParty(Party fromParty)
  {
    if ( producer == null ) producer = new Producer();
    producer.addParty(fromParty);
  }
  public void addFromParties(Party[] p)
  {
    if ( p == null || p.length == 0 ) return;
    for (Party aP : p) addFromParty(aP);
  }
  public void addFromParty(String type, String partyId)
  {
    if ( producer == null ) producer = new Producer();
    producer.addParty(type, partyId);
  }

  public void setProducer(Producer producer) { this.producer = producer; }
  public Producer getProducer() { return producer; }

  public String getBodyPayload()
  {
    if ( payloads != null ) return payloads.getBodyPayload();
    else return null;
  }
  public void setBodyPayload(String bodyPayload)
  {
    if ( payloads == null ) payloads = new Payloads();
    payloads.setBodyPayload(bodyPayload);
  }

  public Payloads getPayloads() { return payloads; }
  public void setPayloads(Payloads payloads) { this.payloads = payloads; }
  public void addPayload(String cid, String payloadURI)
  {
    if ( payloads == null ) payloads = new Payloads();
    payloads.addPayload(cid, payloadURI);
  }
  public String getPayload(String cid)
  {
    if ( payloads == null ) payloads = new Payloads();
    return payloads.getPayload(cid);
  }
  public String getCID(String payload)
  {
    if ( payloads == null ) payloads = new Payloads();
    return payloads.getCID(payload);
  }

  public static MsgInfoSet read(String file)
  {
    return read( new File(file) );
  }

  public static MsgInfoSet read(File file)
  {
    if ( file == null || !file.exists() ) return null;
    MsgInfoSet instance = null;
    try
    {
      Persister serializer = new Persister();
      instance = serializer.read(MsgInfoSet.class, file);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return instance;
  }
  public void writeToFile(String fileName)
  {
    try
    {
      Persister serializer = new Persister();
      File result = new File(fileName);
      serializer.write(this, result);
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  public boolean hasBodyPayload()
  {
    return ( payloads != null && payloads.getBodyPayload() != null &&
             !payloads.getBodyPayload().trim().equals("") );
  }

  public boolean isCompressed(String fileName)
  {
    if ( fileName == null || fileName.trim().equals("") ) return false;
    if ( payloads == null ) return false;
    return payloads.isCompressed(fileName);
  }

  public void setCID(String cid, String payloadFile)
  {
    if ( payloads == null ) payloads = new Payloads();
    payloads.setCID(cid, payloadFile);
  }

  public String getDescription(String payloadFile)
  {
    if ( payloads == null ) return null;
    return payloads.getDescription(payloadFile);
  }

  public String getSchemaLocation(String payloadFile)
  {
    if ( payloads == null ) return null;
    return payloads.getSchemaLocation(payloadFile);
  }

  public String[] getCids()
  {
    if ( payloads == null ) return null;
    List<String> cids = new ArrayList<String>();
    for ( Payload p : payloads.getPayloads() ) cids.add(p.getCid());
    String[] ids = new String[cids.size()];
    cids.toArray(ids);
    return ids;
  }

  // Just a convenience method:
  /*
  public Leg getLeg()
  {
    if ( Constants.pmodesMap == null ) return null;
    PMode pm = Constants.pmodesMap.get(pmode);
    if ( pm == null ) return null;
    return pm.getLeg(legNumber, null);
  }
  */
}