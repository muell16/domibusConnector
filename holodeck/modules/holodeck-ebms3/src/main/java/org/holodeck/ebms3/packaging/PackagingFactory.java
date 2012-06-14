package org.holodeck.ebms3.packaging;

import org.apache.axis2.context.MessageContext;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.attachments.Attachments;
import org.apache.axiom.om.OMElement;

import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.ebms3.module.Constants;
import org.holodeck.ebms3.module.Configuration;
import org.holodeck.ebms3.module.MsgInfo;
import org.holodeck.ebms3.config.PMode;
import org.holodeck.ebms3.config.Leg;
import org.holodeck.ebms3.config.UserService;
import org.holodeck.ebms3.config.Producer;
import org.holodeck.ebms3.config.Party;
import org.holodeck.ebms3.config.Service;
import org.holodeck.common.soap.Util;

import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.util.Date;

/**
 * @author Hamid Ben Malek
 */
public class PackagingFactory
{
  public static synchronized Messaging createMessagingElement(MessageContext msgCtx)
  {
    if ( msgCtx == null ) return null;
    MsgInfoSet mis =
          (MsgInfoSet)msgCtx.getProperty(Constants.MESSAGE_INFO_SET);
    if ( mis == null ) return null;
    String pmodeName = mis.getPmode();
    PMode pmode = Configuration.getPMode(pmodeName);
    if ( pmode == null ) return null;
    Leg fLeg = Configuration.getLeg(mis);
    if ( fLeg == null ) return null;

    // store the pmode and and leg in the message context so that when a response
    // is received on the back channel, we could determine the toURL of the
    // AS4 receipt that will be generated for that response.
    String m = pmode.getMep();
    if ( m != null && fLeg.getEndpoint() != null &&
         ( m.equalsIgnoreCase(Constants.ONE_WAY_PULL) ||
          m.equalsIgnoreCase(Constants.TWO_WAY_SYNC) ) )
    {
      String toURL = fLeg.getEndpoint().getAddress();
      msgCtx.setProperty(Constants.RECEIPT_TO, toURL);
    }

    SOAPFactory factory = (SOAPFactory)msgCtx.getEnvelope().getOMFactory();
    UserService us = fLeg.getUserService();
    Producer producer = fLeg.getProducer();
    if ( producer == null || producer.getParties() == null ||
         producer.getParties().size() == 0 ) producer = mis.getProducer();
    if ( producer != null && producer.getParties() != null &&
         producer.getParties().size() > 0 && mis.getProducer() == null )
    {
      for ( Party p : producer.getParties() ) mis.addFromParty(p);
      mis.setFromRole(producer.getRole());
    }
    if ( us != null )
    {
      UserMessage userMessage =
         createUserMessage(fLeg.getMpc(), mis, us, msgCtx.getAttachmentMap());
      return new Messaging(factory, null, userMessage);
    }
    String mep = Configuration.getMep(mis);
    if ( (fLeg.getNumber() == 1 && mep.equalsIgnoreCase(Constants.ONE_WAY_PULL))
         ||
         (fLeg.getNumber() == 2 && mep.equalsIgnoreCase(Constants.TWO_WAY_PUSH_AND_PULL))
         ||
         (fLeg.getNumber() == 1 && mep.equalsIgnoreCase(Constants.TWO_WAY_PULL_AND_PUSH))
         ||
         (fLeg.getNumber() == 3 && mep.equalsIgnoreCase(Constants.TWO_WAY_PULL_AND_Pull))
         ||
         (fLeg.getNumber() == 1 && mep.equalsIgnoreCase(Constants.TWO_WAY_PULL_AND_Pull))
       )
    {
      // construct a pull request signal and put it inside an eb:Messaging
      SignalMessage pullRequestSig = new SignalMessage(fLeg.getMpc());
      return new Messaging(factory, pullRequestSig, null);
    }
    // construct an anonymous UserMessage ...
    UserMessage u =
      createAnonymousUserMessage(fLeg.getMpc(), mis, msgCtx.getAttachmentMap());
    return new Messaging(factory, null, u);
  }

  public static UserMessage createRespUserMessage(MsgInfo reqMsgInfo,
                                                  Producer producer,
                                                  UserService us,
                                                  Attachments att)
  {
    if ( reqMsgInfo == null ) return null;
    MsgInfoSet mis =
       new MsgInfoSet(reqMsgInfo.getAgreementRef(), reqMsgInfo.getPmode(),
               reqMsgInfo.getConversationId(), reqMsgInfo.getMessageId());
    if ( producer != null && producer.getParties() != null &&
         producer.getParties().size() > 0 )
    {
      for ( Party p : producer.getParties() ) mis.addFromParty(p);
      mis.setFromRole(producer.getRole());
    }
    else
    {
      List<Party> fromParties = reqMsgInfo.getToParties();
      for ( Party p : fromParties ) mis.addFromParty(p);
      mis.setFromRole(reqMsgInfo.getToRole());
    }
    if ( us == null )
    {
      org.holodeck.ebms3.config.CollaborationInfo ci =
         new org.holodeck.ebms3.config.CollaborationInfo();
      ci.setService( new Service(null, reqMsgInfo.getService()) );
      ci.setAction( reqMsgInfo.getAction() );
      us = new UserService();
      us.setCollaborationInfo(ci);
      List<Party> toP = reqMsgInfo.getFromParties();
      for ( Party p : toP ) us.addToParty(p);
      us.getToParty().setRole( reqMsgInfo.getFromRole() );
    }
    return createUserMessage(reqMsgInfo.getMpc(), mis, us, att);
  }

  public static UserMessage createUserMessage(String mpc, String refToMsgId,
                                              String pmode, UserService us,
                                              Attachments att)
  {
    MsgInfoSet mis = new MsgInfoSet(null, pmode, null, refToMsgId);
    mis.addFromParty(null, "Anonymous");
    return createUserMessage(mpc, mis, us, att);
  }

  public static UserMessage createUserMessage(String mpc, String refToMsgId,
                                              Producer producer, String pmode,
                                              UserService us, Attachments att)
  {
    MsgInfoSet mis = new MsgInfoSet(null, pmode, null, refToMsgId);
    if ( producer != null )
    {
      for ( Party p : producer.getParties() )
            mis.addFromParty(p.getType(), p.getPartyId());
      mis.setFromRole(producer.getRole());
    }
    return createUserMessage(mpc, mis, us, att);
  }

  public static SignalMessage createReceipt(String refToMessageId,
                                            OMElement[] references)
  {
    MessageInfo mi = new MessageInfo(refToMessageId);
    Receipt receipt = new Receipt(references);
    //return new SignalMessage(mi, null, receipt, null);
    return new SignalMessage(mi, null, receipt.getElement(), null);
  }

  public static SignalMessage createReceipt(String refToMessageId,
                                            OMElement nonRepudiationInfo)
  {
    MessageInfo mi = new MessageInfo(refToMessageId);
    Receipt receipt = new Receipt(nonRepudiationInfo);
    //return new SignalMessage(mi, null, receipt, null);
    return new SignalMessage(mi, null, receipt.getElement(), null);
  }

  public static SignalMessage createReceipt(Date timestamp,
                                            String refToMessageId,
                                            OMElement nonRepudiationInfo)
  {
    MessageInfo mi = new MessageInfo(timestamp, null, refToMessageId);
    Receipt receipt = new Receipt(nonRepudiationInfo);
    //return new SignalMessage(mi, null, receipt, null);
    return new SignalMessage(mi, null, receipt.getElement(), null);
  }

  private static UserMessage createUserMessage(String mpc, MsgInfoSet mis,
                                               UserService us, Attachments att)
  {
    MessageInfo mi = new MessageInfo(mis.getRefToMessageId());
    PartyInfo pi = new PartyInfo();
    for (Party p : mis.getFromParties())
         pi.addFromParty(p.getPartyId(), p.getType(), null);
    pi.setFromRole(mis.getFromRole());
    for (Party p : us.getToParty().getParties())
         pi.addToParty(p.getPartyId(), p.getType(), null);
    pi.setToRole(us.getToParty().getRole());
    org.holodeck.ebms3.packaging.CollaborationInfo ci =
       new org.holodeck.ebms3.packaging.CollaborationInfo(mis.getAgreementRef(), mis.getPmode(),
                 us.getCollaborationInfo().getService().getValue(),
                             us.getCollaborationInfo().getService().getType(),
                             us.getCollaborationInfo().getAction(),
                             mis.getConversationId());
    org.holodeck.ebms3.packaging.MessageProperties mp = null;
    if ( mis.getPropertiesMap() != null )
    {
      Set<String> keys = mis.getPropertiesMap().keySet();
      if ( keys != null && keys.size() > 0 )
      {
        Iterator<String> it = keys.iterator();
        mp = new org.holodeck.ebms3.packaging.MessageProperties();
        while ( it != null && it.hasNext() )
        {
          String key = it.next();
          mp.addProperty(key, mis.getProperty(key));
        }
      }
    }
    org.holodeck.ebms3.packaging.PayloadInfo payloadInfo = null;
    if ( att != null )
    {
      //String spId = att.getSOAPPartContentID();
      String[] cids = mis.getCids();
              //att.getAllContentIDs();
      if ( cids != null && cids.length > 0 )
      {
        //payloadInfo = createPayloadInfo(cids, spId, mis);
        payloadInfo = createPayloadInfo(cids, mis);
         //new org.holodeck.ebms3.packaging.PayloadInfo(cids,
         //                                             att.getSOAPPartContentID(),
         //                                             mis.hasBodyPayload());
      }
    }

    return new UserMessage(mpc, mi, pi, ci, mp, payloadInfo);
  }

  private static PayloadInfo createPayloadInfo(String[] cids, MsgInfoSet mis)
  {
    if ( cids == null || cids.length == 0 || mis == null ) return null;
    PayloadInfo pi = new PayloadInfo();
    if ( mis.hasBodyPayload() ) pi.addPartInfo(null, null, null, null, null);
    for (String cid : cids)
    {
      String payloadFile = mis.getPayload(cid);
      boolean compressed = mis.isCompressed(payloadFile);
      if ( compressed )
      {
        String ct = Util.mimeType(payloadFile);
        String[] propertyNames =
              new String[]{"MimeType", "Compressed"};
        String[] propertyValues = new String[]{ct, null};
        pi.addPartInfo(cid, mis.getSchemaLocation(payloadFile),
                       mis.getDescription(payloadFile),
                       propertyNames, propertyValues);
      }
      else pi.addPartInfo(cid, mis.getSchemaLocation(payloadFile),
                          mis.getDescription(payloadFile), null, null);
    }
    return pi;
  }

  private static PayloadInfo createPayloadInfo(String[] cids, String soapPartCid,
                                               MsgInfoSet mis)
  {
    if ( cids == null || cids.length == 0 || mis == null ) return null;
    PayloadInfo pi = new PayloadInfo();
    if ( mis.hasBodyPayload() ) pi.addPartInfo(null, null, null, null, null);
    for (String cid : cids)
    {
      if ( cid.equals(soapPartCid) ) continue;
      String payloadFile = mis.getPayload(cid);
      boolean compressed = mis.isCompressed(payloadFile);
      if ( compressed )
      {
        String ct = Util.mimeType(payloadFile);
        String[] propertyNames =
              new String[]{"MimeType", "Compressed"};
        String[] propertyValues = new String[]{ct, null};
        pi.addPartInfo(cid, mis.getSchemaLocation(payloadFile),
                       mis.getDescription(payloadFile),
                       propertyNames, propertyValues);
      }
      else pi.addPartInfo(cid, mis.getSchemaLocation(payloadFile),
                          mis.getDescription(payloadFile), null, null);
    }
    return pi;
  }

  private static UserMessage createAnonymousUserMessage(String mpc,
                                                        MsgInfoSet mis,
                                                        Attachments att)
  {
    MessageInfo mi = new MessageInfo(mis.getRefToMessageId());
    PartyInfo pi = new PartyInfo();
    for (Party p : mis.getFromParties())
         pi.addFromParty(p.getPartyId(), p.getType(), null);
    pi.setFromRole(mis.getFromRole());

    pi.addToParty("Anonymous", null, null);

    org.holodeck.ebms3.packaging.CollaborationInfo ci =
       new org.holodeck.ebms3.packaging.CollaborationInfo(mis.getAgreementRef(), mis.getPmode(),
                             "Anonymous", null, "Anonymous",
                             mis.getConversationId());
    org.holodeck.ebms3.packaging.MessageProperties mp = null;
    if ( mis.getPropertiesMap() != null )
    {
      Set<String> keys = mis.getPropertiesMap().keySet();
      if ( keys != null && keys.size() > 0 )
      {
        Iterator<String> it = keys.iterator();
        mp = new org.holodeck.ebms3.packaging.MessageProperties();
        while ( it != null && it.hasNext() )
        {
          String key = it.next();
          mp.addProperty(key, mis.getProperty(key));
        }
      }
    }
    org.holodeck.ebms3.packaging.PayloadInfo payloadInfo = null;
    if ( att != null )
    {
      String[] cids = att.getAllContentIDs();
      if ( cids != null && cids.length > 0 )
           payloadInfo =
             new org.holodeck.ebms3.packaging.PayloadInfo(cids,
                                                   att.getSOAPPartContentID(),
                                                   mis.hasBodyPayload());
    }

    return new UserMessage(mpc, mi, pi, ci, mp, payloadInfo);
  }
}