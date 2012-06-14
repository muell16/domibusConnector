package org.holodeck.ebms3.module;

import java.util.*;
//import org.holodeck.ebms3.pmodes.Party;
import org.holodeck.ebms3.config.Party;

import org.simpleframework.xml.*;

/**
 * @author Hamid Ben Malek
 */
@Root
public class MsgInfo implements java.io.Serializable
{
   private static final long serialVersionUID = 1200796957717630663L; 

   @Element(required=false)
   protected String mpc;

   @Element(required=false)
   protected String messageId;

   @Element(required=false)
   protected String refToMessageId;

   @ElementList(required=false)
   protected List<Party> fromParties = new ArrayList<Party>();

   @Element(required=false)
   protected String fromRole;

   @ElementList(required=false)
   protected List<Party> toParties = new ArrayList<Party>();

   @Element(required=false)
   protected String toRole;

   @Element(required=false)
   protected String agreementRef;

   @Element(required=false)
   protected String pmode;

   @Element(required=false)
   protected String service;

   @Element(required=false)
   protected String action;

   @Element(required=false)
   protected String conversationId;
    
   @ElementList(required=false)
   protected List<Property> messageProperties = new ArrayList<Property>();
    
   protected List<PartInfo> parts = new ArrayList<PartInfo>();

   public MsgInfo() {}
   public MsgInfo(String mpc, String messageId, String refToMessageId,
                  String agreementRef, String pmode, String service,
                  String action, String conversationId)
   {
     this.mpc = mpc;
     this.messageId = messageId;
     this.refToMessageId = refToMessageId;
     this.agreementRef = agreementRef;
     this.pmode = pmode;
     this.service = service;
     this.action = action;
     this.conversationId = conversationId;
   }

   public void addFromParty(String type, String partyId)
   {
     Party p = new Party(type, partyId);
     fromParties.add(p);
   }

   public void addToParty(String type, String partyId)
   {
     Party p = new Party(type, partyId);
     toParties.add(p);
   }

   public void addMessageProperty(String name, String value)
   {
     Property p = new Property(name, value);
     messageProperties.add(p);
   }

   public PartInfo addPartInfo(String href, String schemaLocation, String desc)
   {
     PartInfo part = new PartInfo(href, schemaLocation, desc);
     parts.add(part);
     return part;
   }

    public String getMpc() { return mpc; }
    public void setMpc(String mpc) { this.mpc = mpc; }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getRefToMessageId() { return refToMessageId; }
    public void setRefToMessageId(String refToMessageId)
    {
      this.refToMessageId = refToMessageId;
    }

    public List<Party> getFromParties() { return fromParties; }
    public void setFromParties(List<Party> fromParties)
    {
      this.fromParties = fromParties;
    }

    public String getFromRole() { return fromRole; }
    public void setFromRole(String fromRole) { this.fromRole = fromRole; }

    public List<Party> getToParties() { return toParties; }
    public void setToParties(List<Party> toParties)
    {
      this.toParties = toParties;
    }

    public String getToRole() { return toRole; }
    public void setToRole(String toRole) { this.toRole = toRole; }

    public String getAgreementRef() { return agreementRef; }
    public void setAgreementRef(String agreementRef)
    {
      this.agreementRef = agreementRef;
    }

    public String getPmode() { return pmode; }
    public void setPmode(String pmode) { this.pmode = pmode; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId)
    {
      this.conversationId = conversationId;
    }

    public List<Property> getMessageProperties()
    {
      return messageProperties;
    }

    public void setMessageProperties(List<Property> messageProperties)
    {
      this.messageProperties = messageProperties;
    }

    public List<PartInfo> getParts() { return parts; }
    public void setParts(List<PartInfo> parts) { this.parts = parts; }
}