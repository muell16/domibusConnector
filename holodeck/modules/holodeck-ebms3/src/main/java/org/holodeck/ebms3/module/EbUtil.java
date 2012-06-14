package org.holodeck.ebms3.module;

//import org.holodeck.ebms3.packaging.*;
//import org.holodeck.ebms3.pmodes.*;
import org.holodeck.ebms3.config.*;

//import org.holodeck.ebms3.submit.MsgInfoSet;

//import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeader;
//import org.apache.axis2.description.Parameter;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ConfigurationContext;
//import org.apache.axiom.attachments.Attachments;
import org.apache.axiom.om.OMElement;

import org.holodeck.common.soap.Util;

import java.util.*;
import java.net.*;

/**
 * @author Hamid Ben Malek
 */
public class EbUtil
{
/*
  public static synchronized Messaging createMessagingElement(MessageContext msgCtx)
  {
    if ( msgCtx == null ) return null;
    MsgInfoSet mis =
          (MsgInfoSet)msgCtx.getProperty(Constants.MESSAGE_INFO_SET);
    if ( mis == null ) return null;
    String pmodeName = mis.getPmode();
    int legNumber = mis.getLegNumber();
    PMode pmode = Configuration.getPMode(pmodeName);
//    PMode pmode = PModesLoader.getPModeByName(pmodeName);
    if ( pmode == null ) return null;

    Leg fLeg = Configuration.getLeg(mis);
//    Leg fLeg =  pmode.getLeg(legNumber, null);

    if ( fLeg == null ) return null;
    SOAPFactory factory = (SOAPFactory)msgCtx.getEnvelope().getOMFactory();
    UserService us = fLeg.getUserService();
    // fill the from parties of mis with the producer data of the leg
    // or the MsgInfoSet, whichever is not null:
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
//    Binding parentBinding =
//         PModesLoader.getBindingByName(fLeg.getParentBinding());
//    String mep = parentBinding.getMep();
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
*/

  /*
  public static List<Binding> getOneWayPullBindings(String mpc, String address)
  {
    Set<String> keys = PModesLoader.getBindingsMap().keySet();
    List<Binding> res = null;
    if ( keys != null && keys.size() > 0 )
    {
      Iterator<String> it = keys.iterator();
      while ( it != null && it.hasNext() )
      {
        String binding = it.next();
        Binding b = PModesLoader.getBindingByName(binding);
        if ( b.getMep().equalsIgnoreCase(Constants.ONE_WAY_PULL) )
        {
          Leg f = b.getFirstLeg();
          if ( f != null && f.getMpc() != null &&
               f.getMpc().equalsIgnoreCase(mpc) &&
               f.getAddress().equals(address) )
          {
            if ( res == null ) res = new ArrayList<Binding>();
            res.add(b);
          }
        }
      }
    }
    return res;
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

//      us = new UserService();
//      us.setService(new EbService(null, reqMsgInfo.getService()));
//      us.setAction(reqMsgInfo.getAction());
      List<Party> toP = reqMsgInfo.getFromParties();
      for ( Party p : toP ) us.addToParty(p);
      us.getToParty().setRole( reqMsgInfo.getFromRole() );
      //us.setRole(reqMsgInfo.getFromRole());
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
*/
  // Tries to figure out the MEP from a given message context while being
  // on the service side handling a request:
  public static String getMep(MessageContext requestMsgCtx)
  {
    if ( requestMsgCtx == null ) return null;
    SOAPHeader header = requestMsgCtx.getEnvelope().getHeader();
    OMElement pullReq =
      Util.getGrandChildNameNS(header,
                               Constants.PULL_REQUEST, Constants.NS);
    if ( pullReq != null )
    {
      String mpc = Util.getAttributeValue(pullReq, "mpc");
      String address = requestMsgCtx.getTo().getAddress();
      PMode pmode = Configuration.matchPMode(mpc, address);
      if ( pmode != null ) return Configuration.getMep(pmode.getName());
      else return Constants.ONE_WAY_PULL;
    }
    else
    {
      OMElement userMessage =
         Util.getGrandChildNameNS(header,
                                  Constants.USER_MESSAGE, Constants.NS);
      if ( userMessage == null ) return null;
      String pmode =
         Util.getGrandChildAttributeValue(userMessage,
                                          Constants.AGREEMENT_REF, "pmode");
      if ( pmode == null )
      {
        MsgInfo mi = (MsgInfo)requestMsgCtx.getProperty(Constants.IN_MSG_INFO);
        String address =
          (String)Util.getPropertyFromInMsgCtx(requestMsgCtx, Constants.TO_ADDRESS);
        PMode pm = Configuration.match(mi, address);
        if ( pm == null ) return Constants.ONE_WAY_PUSH;
        else return Configuration.getMep(pm.getName());
        //return Constants.ONE_WAY_PUSH;
      }
      else return Configuration.getMep(pmode);
//      {
//        PMode pm = PModesLoader.getPModeByName(pmode);
        // To Do: right now, we are supposing that we only treat basic MEPs.
        // This needs to be corrected when considering complex MEPs
        // ( instead of return the mep of the immediate binding, you need
        // to drill down in case of a complex mep):
//        return pm.getBinding().getMep();
//      }
    }
  }

  public static MsgInfo createMsgInfo(MessageContext msgCtx)
  {
    if ( msgCtx == null ) return null;
    SOAPHeader header = msgCtx.getEnvelope().getHeader();
    if (header == null) return null;
    OMElement mess =
        Util.getGrandChildNameNS(header, Constants.MESSAGING, Constants.NS);
    if ( mess == null ) return null;
    MsgInfo msgInfo = new MsgInfo();
    OMElement userMessage =
        Util.getGrandChildNameNS(header, Constants.USER_MESSAGE, Constants.NS);
    OMElement signalMessage =
        Util.getGrandChildNameNS(header, Constants.SIGNAL_MESSAGE, Constants.NS);
    if ( userMessage != null )
    {
      String mpc = Util.getAttributeValue(userMessage, "mpc");
      String messageId =
          Util.getGrandChildValue(userMessage, Constants.MESSAGE_ID);
      String refToMessageId =
          Util.getGrandChildValue(userMessage, Constants.REF_TO_MESSAGE_ID);
      String agreementRef =
          Util.getGrandChildValue(userMessage, Constants.AGREEMENT_REF);
      String pmode =
          Util.getGrandChildAttributeValue(userMessage, Constants.AGREEMENT_REF,
                                      "pmode");
      String service = Util.getGrandChildValue(userMessage, Constants.SERVICE);
      String action = Util.getGrandChildValue(userMessage, Constants.ACTION);
      String conversationId =
          Util.getGrandChildValue(userMessage, Constants.CONVERSATION_ID);

      OMElement from =
          Util.getGrandChildNameNS(userMessage, Constants.FROM, Constants.NS);
      Iterator it = from.getChildElements();
      while ( it != null && it.hasNext() )
      {
        OMElement e = (OMElement)it.next();
        if ( e.getLocalName().equals(Constants.PARTY_ID) )
        {
          msgInfo.addFromParty( Util.getAttributeValue(e, "type"), e.getText() );
        }
        else if ( e.getLocalName().equals(Constants.ROLE) )
                  msgInfo.setFromRole(e.getText());
      }

      OMElement to =
          Util.getGrandChildNameNS(userMessage, Constants.TO, Constants.NS);
      it = to.getChildElements();
      while ( it != null && it.hasNext() )
      {
        OMElement e = (OMElement)it.next();
        if ( e.getLocalName().equals(Constants.PARTY_ID) )
        {
          msgInfo.addToParty( Util.getAttributeValue(e, "type"), e.getText() );
        }
        else if ( e.getLocalName().equals(Constants.ROLE) )
                  msgInfo.setToRole(e.getText());
      }

      OMElement msgProps =
           Util.getGrandChildNameNS(userMessage, Constants.MESSAGE_PROPERTIES,
                                    Constants.NS);
      if ( msgProps != null )
      {
        it = msgProps.getChildElements();
        while ( it != null && it.hasNext() )
        {
          OMElement e = (OMElement)it.next();
          msgInfo.addMessageProperty( Util.getAttributeValue(e, "name"), e.getText() );
        }
      }

      OMElement payloadInfo =
          Util.getGrandChildNameNS(userMessage, Constants.PAYLOAD_INFO,
                                   Constants.NS);
      if ( payloadInfo != null )
      {
        it = payloadInfo.getChildElements();
        while ( it != null && it.hasNext() )
        {
          OMElement e = (OMElement)it.next();
          String href = Util.getAttributeValue(e, "href");
          String schemaLocation =
             Util.getGrandChildAttributeValue(e, Constants.SCHEMA, "location");
          String desc =
             Util.getGrandChildValue(e, Constants.DESCRIPTION);
          PartInfo pi = msgInfo.addPartInfo(href, schemaLocation, desc);

          List<OMElement> props = Util.getGrandChildrenName(e, "Property");
          if ( props != null && props.size() > 0 )
          {
            for (OMElement p : props)
            {
              if ( Util.getAttributeValue(p, "name") != null &&
                   Util.getAttributeValue(p, "name").equals("MimeType") )
                   pi.setMimeType(p.getText());
            }
          }
        }
      }

      msgInfo.setMpc(mpc);
      msgInfo.setMessageId(messageId);
      msgInfo.setRefToMessageId(refToMessageId);
      msgInfo.setAgreementRef(agreementRef);
      msgInfo.setPmode(pmode);
      msgInfo.setService(service);
      msgInfo.setAction(action);
      msgInfo.setConversationId(conversationId);
    }

    if ( signalMessage != null )
    {
      String mpc =
            Util.getGrandChildAttributeValue(signalMessage,
                                             Constants.PULL_REQUEST, "mpc");
      msgInfo.setMpc(mpc);
    }

    return msgInfo;
  }
  /*
  public static Leg getLeg(MsgInfoSet metadata, Map<String, PMode> pmodesMap)
  {
    if ( metadata == null ) return null;
    PMode pmode = pmodesMap.get(metadata.getPmode());
    if ( pmode == null ) return null;
    //return pmode.getLeg(metadata.getLegNumber(), metadata.getParentBinding());
    return pmode.getLeg(metadata.getLegNumber(), null);
  }

  public static Binding getBinding(MsgInfoSet metadata,
                                   Map<String, PMode> pmodesMap)
  {
    if ( metadata == null ) return null;
    PMode pmode = pmodesMap.get(metadata.getPmode());
    //if ( metadata.getParentBinding() == null ||
    //     metadata.getParentBinding().trim().equals("") )
         return pmode.getBinding();
    //return pmode.getBinding(metadata.getParentBinding());
  }
  */
  public static String[] parseModules(String modules)
  {
    String[] mod;
    if ( modules != null && !modules.trim().equals("") )
    {
      StringTokenizer st = new StringTokenizer(modules, ",");
      mod = new String[st.countTokens()];
      int i = 0;
      while ( st.hasMoreTokens() )
      {
        mod[i] = st.nextToken().trim();
        i++;
      }
    }
    else mod = new String[]{"holodeck-ebms3"};
    return mod;
  }

  public static boolean isLocal(String mshURL,
                                ConfigurationContext configCtx)
  {
    if ( mshURL == null ) return false;
    String host = null;
    System.out.println("mshURL=" + mshURL);
    if ( mshURL.startsWith("http") )
    {
       try
       {
         //host = mshURL.substring(8, mshURL.indexOf("/"));
         URL url = new URL(mshURL);
         host = url.getHost();
         System.out.println("========= host is: " + host);
       }
       catch(Exception ex) { ex.printStackTrace(); }
    }
    else if ( mshURL.indexOf("@") > 0 )
             host = mshURL.substring(mshURL.indexOf("@") + 1);
    if ( host != null && host.equals("localhost") ) return true;

    try
    {
      InetAddress addr = InetAddress.getLocalHost();
      String hostname = addr.getHostName();
      if ( host != null && host.equalsIgnoreCase(hostname) ) return true;
      byte[] ipAddr = addr.getAddress();
      String ipAddrStr = "";
      for (int i=0; i<ipAddr.length; i++)
      {
        if (i > 0) { ipAddrStr += "."; }
        ipAddrStr += ipAddr[i]&0xFF;
      }
      if ( host != null && host.equals(ipAddrStr) ) return true;

    }
    catch (UnknownHostException e) { e.printStackTrace(); }
    if ( configCtx == null ) return false;
    String localNames = null;
    if ( configCtx.getAxisConfiguration().getParameter("LOCAL_MACHINE") != null )
    {
      localNames =
       (String)configCtx.getAxisConfiguration().getParameter("LOCAL_MACHINE").getValue();
      if ( localNames != null )
      {
        StringTokenizer st = new StringTokenizer(localNames, ",");
        while ( st.hasMoreTokens() )
        {
          String token = st.nextToken().trim();
          if ( host != null && host.equalsIgnoreCase(token) ) return true;

        }
      }
    }
    return false;
  }
/*
  private static UserMessage createUserMessage(String mpc, MsgInfoSet mis,
                                               UserService us, Attachments att)
  {
    MessageInfo mi = new MessageInfo(mis.getRefToMessageId());
    PartyInfo pi = new PartyInfo();
    for (Party p : mis.getFromParties())
         pi.addFromParty(p.getPartyId(), p.getType(), null);
    pi.setFromRole(mis.getFromRole());

    for (Party p : us.getToParty().getParties())
//    for (Party p : us.getToParties())
         pi.addToParty(p.getPartyId(), p.getType(), null);
    pi.setToRole(us.getToParty().getRole());
//    pi.setToRole(us.getRole());

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
      String[] cids = att.getAllContentIDs();
      if ( cids != null && cids.length > 0 )
           payloadInfo = new org.holodeck.ebms3.packaging.PayloadInfo(cids);
    }

    return new UserMessage(mpc, mi, pi, ci, mp, payloadInfo);
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
           payloadInfo = new org.holodeck.ebms3.packaging.PayloadInfo(cids);
    }

    return new UserMessage(mpc, mi, pi, ci, mp, payloadInfo);
  }
*/
}