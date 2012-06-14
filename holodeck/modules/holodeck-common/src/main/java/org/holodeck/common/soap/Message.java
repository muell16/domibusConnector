package org.holodeck.common.soap;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMOutputFormat;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.wsdl.WSDLConstants;
//import org.apache.axis2.transport.TransportUtils;
//import org.apache.axiom.attachments.Attachments;
import org.apache.axis2.transport.http.SOAPMessageFormatter;
import org.apache.axis2.Constants;
import org.apache.axiom.soap.*;

import javax.activation.DataHandler;
import java.util.*;
import java.io.*;

/**
 * @author Hamid Ben Malek
 */
public class Message implements java.io.Serializable
{
  private static final long serialVersionUID = -4289356544949842551L;  

  private double soapVersion = 1.1;
  private List<OMElement> headerElements = new ArrayList<OMElement>();
  private List<OMElement> bodyElements = new ArrayList<OMElement>();
  private MessageContext msgContext = new MessageContext();

  public Message() { msgContext.setDoingSwA(true); }
  public Message(OMElement[] headers, OMElement bodyElement)
  {
    this();
    if (headers != null && headers.length > 0)
    {
      for (OMElement header : headers) headerElements.add(header);
    }
    if (bodyElement != null) bodyElements.add(bodyElement);
  }
  public Message(OMElement[] headers, OMElement bodyElement,
                 DataHandler[] payloads)
  {
    this(headers, bodyElement);
    if (payloads != null && payloads.length > 0)
    {
      for (DataHandler attachment : payloads) msgContext.addAttachment(attachment);
    }
  }

  public String addAttachment(DataHandler attachment)
  {
    if (attachment == null) return null;
    return msgContext.addAttachment(attachment);
  }
  public DataHandler getAttachment(String cid)
  {
    return msgContext.getAttachment(cid);
  }

  public void addHeaderElement(OMElement header)
  {
    if (header == null) return;
    headerElements.add(header);
  }

  public SOAPHeaderBlock addHeaderElement(String localName, String uri, String prefix)
  {
    SOAPEnvelope env = getEnvelope();
    OMNamespace ns = env.getOMFactory().createOMNamespace(uri, prefix);
    SOAPHeader soapHeader = env.getHeader();
    if (soapHeader == null)
        soapHeader = ((SOAPFactory)env.getOMFactory()).createSOAPHeader(env);
    return soapHeader.addHeaderBlock(localName, ns);
  }

  public SOAPHeaderBlock createHeaderElement(String localName,
                                             String uri, String prefix)
  {
    SOAPFactory factory = getSOAPFactory();
    OMNamespace ns = factory.createOMNamespace(uri, prefix);
    return factory.createSOAPHeaderBlock(localName, ns);
  }

  public void addBodyElement(OMElement payload)
  {
    if (payload == null) return;
    bodyElements.add(payload);
  }

  public SOAPEnvelope getEnvelope()
  {
    SOAPEnvelope env = createEnvelope();
    try { msgContext.setEnvelope(env); }
    catch(Exception ex) { ex.printStackTrace(); }

    return env;
  }

  public MessageContext getMessageContext()
  {
    SOAPEnvelope env = createEnvelope();
    try { msgContext.setEnvelope(env); }
    catch(Exception ex) { ex.printStackTrace(); }
    return msgContext;
  }

  public void setSoapVersion(double version) { this.soapVersion = version; }
  public double getSoapVersion() { return soapVersion; }

  private SOAPFactory getSOAPFactory()
  {
    SOAPFactory omFactory = null;
    if ( soapVersion < 1.2 ) omFactory = OMAbstractFactory.getSOAP11Factory();
    else omFactory = OMAbstractFactory.getSOAP12Factory();
    return omFactory;
  }

  private SOAPEnvelope createEnvelope()
  {
    SOAPFactory omFactory = getSOAPFactory();
    SOAPEnvelope envelope = omFactory.getDefaultEnvelope();
    envelope.declareNamespace("http://www.w3.org/1999/XMLSchema-instance/", "xsi");
    envelope.declareNamespace("http://www.w3.org/1999/XMLSchema", "xsd");
    if (headerElements != null && headerElements.size() > 0)
    {
      for (OMElement header : headerElements)
           envelope.getHeader().addChild(header);
    }
    if (bodyElements != null && bodyElements.size() > 0)
    {
      for (OMElement paylod : bodyElements)
           envelope.getBody().addChild(paylod);
    }
    return envelope;
  }

  public MessageContext call(String soapAction, String endpointURI)
  {
    if ( endpointURI == null || endpointURI.trim().equals("") )
         return null;
    EndpointReference targetEPR = new EndpointReference(endpointURI);
    Options options = new Options();
    options.setAction(soapAction);
    options.setTo(targetEPR);
    options.setProperty(Constants.Configuration.ENABLE_SWA, Constants.VALUE_TRUE);
    if ( soapVersion < 1.2 )
        options.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
    else options.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
    options.setProperty(Constants.Configuration.CACHE_ATTACHMENTS,
                        Constants.VALUE_TRUE);
    File tmpdir = new File(System.getProperty("java.io.tmpdir"));
    if (!tmpdir.exists()) tmpdir = new File("C:\\temp");
    if (!tmpdir.exists()) tmpdir.mkdirs();
    options.setProperty(Constants.Configuration.ATTACHMENT_TEMP_DIR,
                        tmpdir.getAbsolutePath());
    options.setProperty(Constants.Configuration.FILE_SIZE_THRESHOLD, "4000");
    options.setTimeOutInMilliSeconds(10000);
    try
    {
      ServiceClient sender = new ServiceClient();
      sender.setOptions(options);
      OperationClient mepClient =
              sender.createClient(ServiceClient.ANON_OUT_IN_OP);

      mepClient.addMessageContext(getMessageContext());
      mepClient.execute(true);
      return mepClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return null;
  }

  public void writeTo(OutputStream out)
  {
    if (out == null) return;
    String cth = "MIME-Version: 1.0\n" +
            "Content-Type: Multipart/Related; boundary=MIME_boundary; type=text/xml;\n\n";
    SOAPMessageFormatter smf = new SOAPMessageFormatter();
    OMOutputFormat format = new OMOutputFormat();
    format.setDoingSWA(true);
    format.setCharSetEncoding("UTF-8");
    format.setMimeBoundary("MIME_boundary");
    try
    {
      BufferedOutputStream bout = new BufferedOutputStream(out);
      bout.write(cth.getBytes());
      smf.writeTo(getMessageContext(), format, bout, true);
      bout.flush();
      bout.close();
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }
}
