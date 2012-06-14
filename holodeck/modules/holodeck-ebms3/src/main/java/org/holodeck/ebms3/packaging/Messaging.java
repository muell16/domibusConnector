package org.holodeck.ebms3.packaging;

//import org.holodeck.ebms3.config.PMode;
//import org.holodeck.ebms3.config.PModeConfig;
//import org.holodeck.ebms3.config.PModeConfigRegistry;

import org.holodeck.ebms3.module.*;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
//import org.apache.axiom.attachments.*;
//import org.apache.axis2.context.MessageContext;

/**
 * @author Hamid Ben Malek
 */
public class Messaging
{
  protected SOAPHeaderBlock root;

  public Messaging(SOAPFactory factory)
  {
    if (factory == null) return;
    OMNamespace ns = factory.createOMNamespace(Constants.NS, Constants.PREFIX);
    root = factory.createSOAPHeaderBlock(Constants.MESSAGING, ns);
    root.setMustUnderstand(true);
  }

  public Messaging(SOAPEnvelope env)
  {
    if (env == null) return;
    SOAPFactory factory = (SOAPFactory)env.getOMFactory();
    OMNamespace ns = factory.createOMNamespace(Constants.NS, Constants.PREFIX);
    env.declareNamespace(ns);
    root = factory.createSOAPHeaderBlock(Constants.MESSAGING, ns);
    root.setMustUnderstand(true);
    SOAPHeader header = env.getHeader();
    if (header == null) header = factory.createSOAPHeader(env);
    header.addChild(root);
  }

  public Messaging(SOAPFactory factory, SignalMessage s, UserMessage u)
  {
    this(factory);
    //if ( s != null ) root.addChild(s);
    //if ( u != null ) root.addChild(u);

    if ( s != null ) root.addChild(s.getElement());
    if ( u != null ) root.addChild(u.getElement());
  }

  public Messaging(SOAPEnvelope env, SignalMessage s, UserMessage u)
  {
    this(env);
    //if ( s != null ) root.addChild(s);
    //if ( u != null ) root.addChild(u);

    if ( s != null ) root.addChild(s.getElement());
    if ( u != null ) root.addChild(u.getElement());
  }

  public Messaging(SOAPFactory factory, SignalMessage[] s, UserMessage[] u)
  {
    this(factory);
    if ( s != null && s.length > 0 )
    {
      for (SignalMessage sm : s) root.addChild(sm.getElement());
                                  //root.addChild(sm);
    }
    if ( u != null && u.length > 0 )
    {
      for (UserMessage um : u) root.addChild(um.getElement());
           //root.addChild(um);
    }
  }

  public Messaging(SOAPEnvelope env, SignalMessage[] s, UserMessage[] u)
  {
    this(env);
    if ( s != null && s.length > 0 )
    {
      for (SignalMessage sm : s) root.addChild(sm.getElement());
                                 //root.addChild(sm);
    }
    if ( u != null && u.length > 0 )
    {
      for (UserMessage um : u) root.addChild(um.getElement());
                               //root.addChild(um);
    }
  }

  public SOAPHeaderBlock getRoot() { return root; }

  public void addToHeader(SOAPEnvelope env)
  {
    if ( env == null ) return;
    SOAPFactory factory = (SOAPFactory)env.getOMFactory();
    OMNamespace ns = factory.createOMNamespace(Constants.NS, Constants.PREFIX);
    env.declareNamespace(ns);

    SOAPHeader header = env.getHeader();
    if (header == null)
        header = ((SOAPFactory)env.getOMFactory()).createSOAPHeader(env);
    header.addChild(getRoot());
    //env.getHeader().addChild(getRoot());
  }
  /*
  public static Messaging createUserMessageHeader(String mpc, PMode pMode,
                                                  String refToMessageId,
                                                  String conversationId)
  {
    if ( pMode == null ) return null;
    PModeConfig pmodes = PModeConfigRegistry.getPModeConfig(pMode.getID());

    MessageInfo mi = new MessageInfo(refToMessageId);

    PartyInfo pi = new PartyInfo();
    String[] fromPartyIds = null;
    String[] fromTypes = null;
    String fromRole = null;
    String[] toPartyIds = pmodes.getPartyIdValues(pMode.getID());
    String[] toTypes = pmodes.getPartyIdTypes(pMode.getID());
    String toRole = pmodes.getRole(pMode.getID());
    pi.addFromParties(fromPartyIds, fromTypes, fromRole);
    pi.addToParties(toPartyIds, toTypes, toRole);

    String service = pmodes.getService(pMode.getID());
    String action = pmodes.getFirstAction(pMode.getID());
    CollaborationInfo ci =
       new CollaborationInfo(null, null, service, null, action, conversationId);

    MessageProperties mprops = null;
    PayloadInfo pli = null;

    UserMessage userMsg = new UserMessage(mpc, mi, pi, ci, mprops, pli);
    return new Messaging(userMsg);
  }

  public void fill(SOAPMessage message)
  {
    if ( message == null ) return;
    if ( getUserMessage().getPayloadInfo() != null )
    {
      super.fill(message);
      return;
    }

    Iterator it = message.getAttachments();
    String[] partIds = null;
    List partIdList = new ArrayList();
    while ( it != null && it.hasNext() )
    {
      AttachmentPart att = (AttachmentPart)it.next();
      partIdList.add(att.getContentId());
    }
    if ( partIdList.size() > 0 )
    {
      partIds = new String[partIdList.size()];
      for (int i = 0; i < partIdList.size(); i++) partIds[i] = (String)partIdList.get(i);
    }
    if ( partIds == null )
    {
      PayloadInfo p = new PayloadInfo();
      p.addPartInfo(null, null, null);
      getUserMessage().setPayloadInfo(p);
      return;
    }
    getUserMessage().setPayloadInfo(new PayloadInfo(partIds));
    super.fill(message);
  }
  */
}