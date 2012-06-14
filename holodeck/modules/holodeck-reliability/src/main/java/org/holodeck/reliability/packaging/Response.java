package org.holodeck.reliability.packaging;

//import org.holodeck.common.soap.Element;
import org.holodeck.reliability.module.*;

import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;

/**
 * @author Hamid Ben Malek
 */
public class Response
{
  protected SOAPHeaderBlock root;

  public Response(SOAPFactory factory)
  {
    if (factory == null) return;
    OMNamespace ns = factory.createOMNamespace(Constants.NS, Constants.PREFIX);
    root = factory.createSOAPHeaderBlock(Constants.RESPONSE, ns);
    root.setMustUnderstand(true);
  }

  public Response(SOAPEnvelope env)
  {
    if (env == null) return;
    SOAPFactory factory = (SOAPFactory)env.getOMFactory();
    OMNamespace ns = factory.createOMNamespace(Constants.NS, Constants.PREFIX);
    env.declareNamespace(ns);
    root = factory.createSOAPHeaderBlock(Constants.RESPONSE, ns);
    root.setMustUnderstand(true);
    SOAPHeader header = env.getHeader();
    if (header == null) header = factory.createSOAPHeader(env);
    header.addChild(root);
  }

  public SOAPHeaderBlock getRoot() { return root; }

  public void addNonSequenceReply(String groupId, String fault)
  {
    NonSequenceReply nsr = new NonSequenceReply(groupId, fault);
    //root.addChild(nsr);
    root.addChild(nsr.getElement());
  }

  public void addSequenceReplies(String groupId, int from, int to,
                                 String fault)
  {
    ReplyRange range = new ReplyRange(from, to, fault);
    SequenceReplies sr =
         new SequenceReplies(groupId, new ReplyRange[]{range});
    //root.addChild(sr);
    root.addChild(sr.getElement());
  }

  public static SOAPHeaderBlock createResponse(SOAPFactory factory,
                                               NonSequenceReply[] nsr,
                                               SequenceReplies[] sr)
  {
    if (factory == null) return null;
    OMNamespace ns =
          factory.createOMNamespace(Constants.NS, Constants.PREFIX);
    SOAPHeaderBlock resp =
          factory.createSOAPHeaderBlock(Constants.RESPONSE, ns);
    resp.setMustUnderstand(true);
    if (nsr == null && sr == null) return resp;
    if (nsr != null && nsr.length > 0)
    {
      for (NonSequenceReply r : nsr) resp.addChild(r.getElement());
                                     //resp.addChild(r);
    }
    if (sr != null && sr.length > 0)
    {
      for (SequenceReplies r : sr) resp.addChild(r.getElement());
                                   //resp.addChild(r);
    }
    return resp;
  }

  public static SOAPHeaderBlock createResponse(SOAPFactory factory, String groupId,
                                               String seq)
  {
    if (factory == null) return null;
    OMNamespace ns =
          factory.createOMNamespace(Constants.NS, Constants.PREFIX);
    SOAPHeaderBlock resp =
          factory.createSOAPHeaderBlock(Constants.RESPONSE, ns);
    resp.setMustUnderstand(true);
    if ( groupId != null && !groupId.trim().equals("") &&
        seq != null && !seq.trim().equals("") )
    {
      int seqNumber = Integer.parseInt(seq);
      ReplyRange[] ranges =
             new ReplyRange[] { new ReplyRange(seqNumber, seqNumber, null)};
      SequenceReplies sr = new SequenceReplies(groupId, ranges);
      //resp.addChild(sr);
      resp.addChild(sr.getElement());
    }
    else if ( groupId != null && !groupId.trim().equals("") &&
              (seq == null || seq.trim().equals("")) )
    {
      NonSequenceReply nsr = new NonSequenceReply(groupId, null);
      //resp.addChild(nsr);
      resp.addChild(nsr.getElement());
    }
    return resp;
  }

  public static SOAPHeaderBlock createResponse(SOAPEnvelope env, String groupId,
                                               String seq)
  {
    if (env == null) return null;
    Response resp = new Response(env);

    if ( groupId != null && !groupId.trim().equals("") &&
        seq != null && !seq.trim().equals("") )
    {
      int seqNumber = Integer.parseInt(seq);
      resp.addSequenceReplies(groupId, seqNumber, seqNumber, null);
    }
    else if ( groupId != null && !groupId.trim().equals("") &&
              (seq == null || seq.trim().equals("")) )
    {
      resp.addNonSequenceReply(groupId, null);
    }
    return resp.getRoot();
  }
}