package org.holodeck.reliability.packaging;

//import org.holodeck.common.soap.Element;
import org.holodeck.reliability.module.*;

import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;

import java.util.*;

/**
 * @author Hamid Ben Malek
 */
public class PollRequest
{
  protected SOAPHeaderBlock root;

  public PollRequest(SOAPFactory factory)
  {
    if (factory == null) return;
    OMNamespace ns = factory.createOMNamespace(Constants.NS, Constants.PREFIX);
    root = factory.createSOAPHeaderBlock(Constants.POLL_REQUEST, ns);
    root.setMustUnderstand(true);
  }

  public PollRequest(SOAPEnvelope env)
  {
    if (env == null) return;
    SOAPFactory factory = (SOAPFactory)env.getOMFactory();
    OMNamespace ns = factory.createOMNamespace(Constants.NS, Constants.PREFIX);
    root = factory.createSOAPHeaderBlock(Constants.POLL_REQUEST, ns);
    root.setMustUnderstand(true);
    SOAPHeader header = env.getHeader();
    if (header == null) header = factory.createSOAPHeader(env);
    header.addChild(root);
  }

  public SOAPHeaderBlock getRoot() { return root; }

  public void addRefToMessageIds(String groupId, int from, int to)
  {
    SequenceNumRange range = new SequenceNumRange(from, to);
    RefToMessageIds ref =
          new RefToMessageIds(groupId, new SequenceNumRange[]{range});
    //root.addChild(ref);
    root.addChild(ref.getElement());
  }

  public void addRefToMessageIds(String groupId)
  {
    SequenceNumRange[] ranges = null;
    RefToMessageIds ref = new RefToMessageIds(groupId, ranges);
    //root.addChild(ref);
    root.addChild(ref.getElement());
  }

  public SOAPHeaderBlock addToHeader(SOAPEnvelope env)
  {
    if ( env == null ) return getRoot();
    env.declareNamespace(root.getNamespace());
    env.getHeader().addChild(root);
    return root;
  }

  public static SOAPHeaderBlock createPollRequest(SOAPFactory factory,
                                                  ReplyTo replyTo,
                                                  RefToMessageIds[] refs)
  {
    if (factory == null) return null;
    OMNamespace ns =
          factory.createOMNamespace(Constants.NS, Constants.PREFIX);
    SOAPHeaderBlock pollReq =
          factory.createSOAPHeaderBlock(Constants.POLL_REQUEST, ns);
    pollReq.setMustUnderstand(true);
    if (replyTo != null) pollReq.addChild(replyTo.getElement());
                         //pollReq.addChild(replyTo);
    if (refs == null && refs == null) return pollReq;
    if (refs != null && refs.length > 0)
    {
      for (RefToMessageIds ref : refs) pollReq.addChild(ref.getElement());
                                       //pollReq.addChild(ref);
    }
    return pollReq;
  }

  public static SOAPHeaderBlock createPollRequest(SOAPFactory factory,
                                                  ReplyTo replyTo,
                                                  List<RefToMessageIds> refs)
  {
    if (factory == null) return null;
    OMNamespace ns =
          factory.createOMNamespace(Constants.NS, Constants.PREFIX);
    SOAPHeaderBlock pollReq =
          factory.createSOAPHeaderBlock(Constants.POLL_REQUEST, ns);
    pollReq.setMustUnderstand(true);
    if (replyTo != null) pollReq.addChild(replyTo.getElement());
                         //pollReq.addChild(replyTo);
    if (refs == null && refs == null) return pollReq;
    if (refs != null && refs.size() > 0)
    {
      for (RefToMessageIds ref : refs) pollReq.addChild(ref.getElement());
                                       //pollReq.addChild(ref);
    }
    return pollReq;
  }
}