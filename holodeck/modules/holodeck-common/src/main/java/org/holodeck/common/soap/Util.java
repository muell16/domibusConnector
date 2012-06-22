package org.holodeck.common.soap;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.activation.DataHandler;
import javax.activation.MimetypesFileTypeMap;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.axiom.attachments.Attachments;
import org.apache.axiom.attachments.IncomingAttachmentInputStream;
import org.apache.axiom.attachments.IncomingAttachmentStreams;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMOutputFormat;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.util.UUIDGenerator;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.builder.BuilderUtil;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.async.Callback;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.engine.AxisEngine;
import org.apache.axis2.transport.TransportUtils;
import org.apache.axis2.transport.http.SOAPMessageFormatter;
import org.apache.axis2.util.MessageContextBuilder;
import org.apache.axis2.util.XMLPrettyPrinter;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.log4j.Logger;

/**
 * @author Hamid Ben Malek
 */
public class Util
{
  public static OMFactory factory= OMAbstractFactory.getOMFactory();

//  private static final Log log = LogFactory.getLog(Util.class);
  private static final Logger log = Logger.getLogger(Util.class);

  public static MimetypesFileTypeMap mimeTypes = null;

  public static OMElement newElement(String localName, String uri, String prefix)
  {
    OMNamespace ns= factory.createOMNamespace(uri, prefix);
    return factory.createOMElement(localName, ns);
  }

  public static String toString(OMElement element)
  {
    if ( element == null ) return null;
    StringWriter sw = new StringWriter();
    writeTo(element, sw);
    return sw.toString();
  }

  public static void writeTo(OMElement element, Writer writer)
  {
    try
    {
      XMLOutputFactory xof = XMLOutputFactory.newInstance();
      XMLStreamWriter w = xof.createXMLStreamWriter(writer);
      element.serialize(w);
      writer.flush();
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  public static void prettyPrint(OMElement element, String fileName)
  {
    if ( element == null || fileName == null || fileName.trim().equals("") )
         return;
    try
    {
      FileOutputStream fos = new FileOutputStream(fileName);
      XMLPrettyPrinter.prettify(element, fos);
      fos.flush();
      fos.close();
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  public static String prettyToString(OMElement element)
  {
    if ( element == null ) return null;
    try
    {
      java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
      XMLPrettyPrinter.prettify(element, bos);
      return bos.toString();
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return element.toString();
  }

  public static void debug(Logger log, OMElement element)
  {
    if ( log == null || element == null ) return;
    log.debug( prettyToString(element) );
  }
  public static void debug(Logger log, String message, OMElement element)
  {
    if ( log == null || element == null ) return;
    log.debug( message + " " + prettyToString(element) );
  }
  public static void info(Logger log, OMElement element)
  {
    if ( log == null || element == null ) return;
    log.info( prettyToString(element) );
  }
  public static void info(Logger log, String message, OMElement element)
  {
    if ( log == null || element == null ) return;
    log.info( message + " " + prettyToString(element) );
  }
  public static void error(Logger log, OMElement element)
  {
    if ( log == null || element == null ) return;
    log.error( prettyToString(element) );
  }
  public static void error(Logger log, String message, OMElement element)
  {
    if ( log == null || element == null ) return;
    log.error( message + " " + prettyToString(element) );
  }

  public static OMElement toOMElement(String xml)
  {
    if ( xml == null || xml.trim().equals("") ) return null;
    OMElement element = null;
    try
    {
      StringReader sr = new StringReader(xml);
      XMLInputFactory xif= XMLInputFactory.newInstance();
      XMLStreamReader reader= xif.createXMLStreamReader(sr);
      StAXOMBuilder builder= new StAXOMBuilder(reader);
      return builder.getDocumentElement();
    }
    catch(Exception e) { e.printStackTrace(); }
    return element;
  }

  public static OMElement rootElement(File xmlFile)
  {
    if ( xmlFile == null || !xmlFile.exists() ) return null;
    try
    {
      FileInputStream fis= new FileInputStream(xmlFile);
      XMLInputFactory xif= XMLInputFactory.newInstance();
      XMLStreamReader reader= xif.createXMLStreamReader(fis);
      StAXOMBuilder builder= new StAXOMBuilder(reader);
      return builder.getDocumentElement();
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return null;
  }

  public static OMElement rootElement(InputStream in)
  {
    if ( in == null ) return null;
    try
    {
      XMLInputFactory xif= XMLInputFactory.newInstance();
      XMLStreamReader reader= xif.createXMLStreamReader(in);
      StAXOMBuilder builder= new StAXOMBuilder(reader);
      return builder.getDocumentElement();
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return null;
  }

  public static OMAttribute getAttribute(OMElement element, String attLocalName)
  {
    if ( element == null || attLocalName == null ||
         attLocalName.trim().equals("") )
         return null;
    Iterator it = element.getAllAttributes();
    OMAttribute att = null;
    while (it != null && it.hasNext())
    {
      att = (OMAttribute)it.next();
      if ( att != null && att.getLocalName().equals(attLocalName) ) return att;
    }
    return null;
  }

  public static String getAttributeValue(OMElement element, String attLocalName)
  {
    OMAttribute att = getAttribute(element, attLocalName);
    if ( att != null ) return att.getAttributeValue();
    else return null;
  }

  public static void addAttributeTo(OMElement element,
                                    String attName, String attValue)
  {
    if (element == null || (attName == null || attName.trim().equals("")) )
        return;
    OMAttribute att =
            element.getOMFactory().createOMAttribute(attName, null, attValue);
    element.addAttribute(att);
  }

  public static void addAttributeTo(OMElement element, String attName,
                                    String uri, String prefix, String attValue)
  {
    if (element == null || (attName == null || attName.trim().equals("")) )
        return;
    OMNamespace ns = null;
    if ( uri != null || prefix != null )
    {
      if (uri == null) ns = element.findNamespaceURI(prefix);
      if (ns == null) ns = element.getOMFactory().createOMNamespace(uri, prefix);
    }
    OMAttribute att =
            element.getOMFactory().createOMAttribute(attName, ns, attValue);
    element.addAttribute(att);
  }

  public static OMElement getFirstChildWithName(OMElement parent,
                                                String childName)
  {
    if ( parent == null || childName == null || childName.trim().equals("") )
         return null;
    Iterator it = parent.getChildElements();
    if ( it == null || !it.hasNext() ) return null;
    Object obj = null;
    OMElement child = null;
    while ( it.hasNext() )
    {
      obj = it.next();
      if ( obj instanceof OMElement )
      {
        child = (OMElement)obj;
        if ( child.getLocalName().equals(childName) ) return child;
      }
    }
    return null;
  }

  public static OMElement getFirstGrandChildWithName(OMElement root,
                                                     String localName)
  {
    if ( root == null || localName == null || localName.trim().equals("") )
         return null;
    Iterator it = root.getChildElements();
    if ( it == null || !it.hasNext() ) return null;
    OMElement result = null;
    result = getFirstChildWithName(root, localName);
    if ( result != null ) return result;

    Object obj = null;
    OMElement child = null;
    while ( it.hasNext() )
    {
      obj = it.next();
      if ( obj instanceof OMElement )
      {
        child = (OMElement)obj;
        result = getFirstGrandChildWithName(child, localName);
        if ( result != null ) return result;
      }
    }

    return result;
  }

  // will be used by modules
  public static String getGrandChildAttributeValue(OMElement root,
                                                   String localName,
                                                   String attributeName)
  {
    OMElement gc = getFirstGrandChildWithName(root, localName);
    if (gc == null) return null;
    return getAttributeValue(gc, attributeName);
  }
  // will be used by modules
  public static String getGrandChildValue(OMElement root,
                                          String localName)
  {
    OMElement gc = getFirstGrandChildWithName(root, localName);
    if (gc == null) return null;
    return gc.getText();
  }
  // will be used by modules
  public static OMElement getGrandChildNameNS(OMElement root,
                                              String _localName,
                                              String namespaceURI)
  {
    if (_localName == null || _localName.trim().equals("") || root == null)
         return null;
    Iterator it = root.getChildElements();
    OMElement e = null;
    while ( it != null && it.hasNext() )
    {
      e = (OMElement)it.next();
      if ( e.getLocalName().equals(_localName) &&
           hasNamespaceURI(e, namespaceURI) ) return e;
      OMElement temp = getGrandChildNameNS(e, _localName, namespaceURI);
      if (temp != null) return temp;
    }
    return null;
  }

  public static List<OMElement> getGrandChildrenNameNS(OMElement root,
                                                       String _localName,
                                                       String namespaceURI)
  {
    if (_localName == null || _localName.trim().equals("") || root == null)
         return null;
    List<OMElement> result = new ArrayList<OMElement>();
    Iterator it = root.getChildElements();
    OMElement e = null;
    while ( it != null && it.hasNext() )
    {
      e = (OMElement)it.next();
      if ( e.getLocalName().equals(_localName) &&
           hasNamespaceURI(e, namespaceURI) ) result.add(e);
      List<OMElement> tmp = getGrandChildrenNameNS(e, _localName, namespaceURI);
      if (tmp != null && tmp.size() > 0) result.addAll(tmp);
    }
    return result;
  }

  public static List<OMElement> getGrandChildrenName(OMElement root,
                                                     String _localName)
  {
    if (_localName == null || _localName.trim().equals("") || root == null)
         return null;
    List<OMElement> result = new ArrayList<OMElement>();
    Iterator it = root.getChildElements();
    OMElement e = null;
    while ( it != null && it.hasNext() )
    {
      e = (OMElement)it.next();
      if ( e.getLocalName().equals(_localName) ) result.add(e);
      List<OMElement> tmp = getGrandChildrenName(e, _localName);
      if (tmp != null && tmp.size() > 0) result.addAll(tmp);
    }
    return result;
  }

  private static boolean hasNamespaceURI(OMElement e, String uri)
  {
    if (e == null) return false;
    if (e.getNamespace() != null &&
        e.getNamespace().getNamespaceURI() != null &&
        uri != null && e.getNamespace().getNamespaceURI().equals(uri))
        return true;
    if ( e.getNamespace() == null && (uri == null || uri.trim().equals("")) )
         return true;
    return false;
  }

  public static org.jdom.Element toJdomElement(OMElement omElement)
  {
    if ( omElement == null ) return null;
    omElement.build();
    StringWriter sw = new StringWriter();
    writeTo(omElement, sw);
    StringReader sr = new StringReader(sw.toString());
    try
    {
      org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder();
      return builder.build(sr).getRootElement();
    }
    catch(Exception ex) { ex.printStackTrace(); return null; }
  }

  public static OMElement toOMElement(org.jdom.Element element)
  {
    if ( element == null ) return null;
    String xml = toXML(element);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintWriter pw = new PrintWriter(out);
    pw.write(xml);
    pw.close();
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    return rootElement(in);
  }

  public static String toXML(org.jdom.Element element)
  {
    String xml = null;
    try
    {
      org.jdom.output.XMLOutputter xo = new org.jdom.output.XMLOutputter();
      xo.setFormat(org.jdom.output.Format.getPrettyFormat());
      StringWriter st = new StringWriter();
      xo.output(element, st);
      xml = st.toString();
    }
    catch(Exception e) { e.printStackTrace(); }
    return xml;
  }

  public static SOAPEnvelope createEnvelope(double soapVersion)
  {
    SOAPFactory omFactory = null;
    if ( soapVersion < 1.2 ) omFactory = OMAbstractFactory.getSOAP11Factory();
    else omFactory = OMAbstractFactory.getSOAP12Factory();

    SOAPEnvelope envelope = omFactory.getDefaultEnvelope();
    envelope.declareNamespace("http://www.w3.org/1999/XMLSchema-instance/", "xsi");
    envelope.declareNamespace("http://www.w3.org/1999/XMLSchema", "xsd");
    return envelope;
  }

  public static OMElement performXLST(InputStream styleSheet, OMElement source)
  {
    if ( styleSheet == null || source == null) return source;
    try
    {
      Transformer transformer =
       TransformerFactory.newInstance().newTransformer(new StreamSource(styleSheet));
      ByteArrayOutputStream baosForSource = new ByteArrayOutputStream();
      XMLStreamWriter xsWriterForSource = XMLOutputFactory.newInstance().createXMLStreamWriter(baosForSource);
      source.serialize(xsWriterForSource);
      Source transformSrc = new StreamSource(new ByteArrayInputStream(baosForSource.toByteArray()));
      ByteArrayOutputStream baosForTarget = new ByteArrayOutputStream();
      StreamResult transformTgt = new StreamResult(baosForTarget);
      transformer.transform(transformSrc, transformTgt);
      StAXOMBuilder builder = new StAXOMBuilder(new ByteArrayInputStream(baosForTarget.toByteArray()));
      return builder.getDocumentElement();
    }
    catch(Exception e) { e.printStackTrace(); }
    return source;
  }

  public static org.jdom.Element performXLST(InputStream stylesheetFile, org.jdom.Element sourceDoc)
  {
    try
    {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Templates stylesheet = transformerFactory.newTemplates(new StreamSource(stylesheetFile));
      Transformer processor = stylesheet.newTransformer();
      // Use I/O streams for source files
      PipedInputStream sourceIn = new PipedInputStream();
      PipedOutputStream sourceOut = new PipedOutputStream(sourceIn);
      StreamSource source = new StreamSource(sourceIn);
      // Use I/O streams for output files
      PipedInputStream resultIn = new PipedInputStream();
      PipedOutputStream resultOut = new PipedOutputStream(resultIn);
      // Convert the output target for use in Xalan-J 2
      StreamResult result = new StreamResult(resultOut);
      // Get a means for output of the JDOM Document
      org.jdom.output.XMLOutputter xmlOutputter = new org.jdom.output.XMLOutputter();
      // Output to the I/O stream
      xmlOutputter.output(sourceDoc, sourceOut);
      sourceOut.close();
      // Feed the resultant I/O stream into the XSLT processor
      processor.transform(source, result);
      resultOut.close();
      // Convert the resultant transformed document back to JDOM
      org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder();
      org.jdom.Document resultDoc = builder.build(resultIn);
      return resultDoc.getRootElement();
    }
    catch(Exception ex) { ex.printStackTrace(); return null;}
  }

  public static OMElement performXLST(String xsltFile, OMElement source)
  {
    if ( xsltFile == null || xsltFile.trim().equals("") || source == null ) return source;
    File xsl = new File(xsltFile);
    if ( !xsl.exists() )
    {
      log.error("xslt file: " + xsltFile + " does not exist");
      return source;
    }
    try { return performXLST(new FileInputStream(xsltFile), source); }
    catch(Exception e) { e.printStackTrace(); }
    return source;
  }

  public static DataHandler getAttachment(MessageContext msgContext, String cid)
  {
    try
    {
//      Attachments atts = (Attachments)msgContext.getProperty(MTOMConstants.ATTACHMENTS);
//      return atts.getDataHandler(cid);
      return msgContext.getAttachment(cid);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return null;
  }

  public static String[] getAllContentIDs(MessageContext msgContext)
  {
    Attachments atts = msgContext.getAttachmentMap();
    //Attachments atts = (Attachments)msgContext.getProperty(MTOMConstants.ATTACHMENTS);
    return atts.getAllContentIDs();
  }

  public static OMNode evaluate(String xpathExpression, MessageContext context)
  {
    try
    {
      AXIOMXPath sourceXPath = new AXIOMXPath(xpathExpression);
      sourceXPath.addNamespace("SOAP-ENV", context.isSOAP11() ?
         SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI : SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
      log.debug("Transformation against source element evaluated by : " + sourceXPath);
      Object o = sourceXPath.evaluate(context.getEnvelope());
      if (o instanceof OMNode) return (OMNode) o;
      else if (o instanceof List && !((List) o).isEmpty())
      {
        return (OMNode) ((List) o).get(0);  // Always fetches *only* the first
      }
    }
    catch(Exception e) { e.printStackTrace(); }
    return null;
  }

  public static Object evaluate(String xpathExpression, OMNode source)
  {
    if ( source == null ) return null;
    if ( xpathExpression == null || xpathExpression.trim().equals("") ) return null;
    try
    {
      AXIOMXPath sourceXPath = new AXIOMXPath(xpathExpression);
      /*
      Object o = sourceXPath.evaluate(source);
      if (o instanceof OMNode) return (OMNode) o;
      if (o instanceof List && !((List) o).isEmpty())
         return (OMNode) ((List) o).get(0);
      if ( o == null ) return null;
      */
      log.debug(">>>>====== evaluating xpath " + xpathExpression);
      List result = sourceXPath.selectNodes(source);
      if ( result == null || result.size() == 0 ) return null;

      // Debugging:
      if ( result.get(0) == null ) log.debug("node is null");
      else log.debug("node is not null");
      log.debug("node's type is " + result.get(0).getClass().getName());
      if ( result.get(0) instanceof OMAttribute )
          log.debug("node is an instance of OMAttribute");
      else log.debug("node is not an instance of OMAttribute");

      return result.get(0);
    }
    catch(Exception e) { e.printStackTrace(); }
    return null;
  }

  public static List<OMNode> evaluateAll(String xpathExpression, OMNode source)
  {
    if ( source == null ) return null;
    if ( xpathExpression == null || xpathExpression.trim().equals("") ) return null;
    List<OMNode> result = new ArrayList<OMNode>();
    try
    {
      AXIOMXPath sourceXPath = new AXIOMXPath(xpathExpression);
      Object o = sourceXPath.evaluate(source);
      if (o instanceof OMNode)
      {
        result.add((OMNode)o);
        return result;
      }
      if (o instanceof List && !((List) o).isEmpty()) return ((List<OMNode>)o);

      if ( o == null ) return null;
    }
    catch(Exception e) { e.printStackTrace(); }
    return null;
  }

  public static OMElement performXLST(URL xsltUrl, String xpathExpression, MessageContext context)
  {
    try
    {
      OMNode sourceNode = evaluate(xpathExpression, context);
      if ( sourceNode == null ) return null;
      // create a transformer
      Transformer transformer = TransformerFactory.newInstance().newTransformer(
              new StreamSource(xsltUrl.openStream()));

      // create a byte array output stream and serialize the source node into it
      ByteArrayOutputStream baosForSource = new ByteArrayOutputStream();
      XMLStreamWriter xsWriterForSource = XMLOutputFactory.newInstance().createXMLStreamWriter(baosForSource);

      log.debug("Transformation source : " + sourceNode);
      sourceNode.serialize(xsWriterForSource);
      Source transformSrc = new StreamSource(new ByteArrayInputStream(baosForSource.toByteArray()));

      // create a new Stream result over a new BAOS..
      ByteArrayOutputStream baosForTarget = new ByteArrayOutputStream();
      StreamResult transformTgt = new StreamResult(baosForTarget);

      // perform transformation
      transformer.transform(transformSrc, transformTgt);
      StAXOMBuilder builder = new StAXOMBuilder(new ByteArrayInputStream(baosForTarget.toByteArray()));
      return builder.getDocumentElement();
    }
    catch (Exception ex) { ex.printStackTrace(); }
    return null;
  }

  public SOAPEnvelope createEnvelope(double soapVersion, OMElement[] headers,
                                     OMElement bodyPayload)
  {
    SOAPEnvelope env = createEnvelope(soapVersion);
    if (headers != null && headers.length > 0)
    {
      for (OMElement header : headers) env.getHeader().addChild(header);
    }
    if (bodyPayload != null) env.getBody().addChild(bodyPayload);
    return env;
  }

  public MessageContext createRequestMessageContext(SOAPEnvelope env)
  {
    MessageContext mc = new MessageContext();
    try { mc.setEnvelope(env); }
    catch(Exception ex) { ex.printStackTrace(); return null; }
    return mc;
  }

  public MessageContext invoke(String soapAction, SOAPEnvelope env,
                               DataHandler[] attachments, String endpointURI)
  {
    if ( env == null || endpointURI == null || endpointURI.trim().equals("") )
         return null;
    EndpointReference targetEPR = new EndpointReference(endpointURI);
    Options options = new Options();
    options.setAction(soapAction);
    options.setTo(targetEPR);
    options.setProperty(Constants.Configuration.ENABLE_SWA,
                        Constants.VALUE_TRUE);
    if ( env.getNamespace().getNamespaceURI()
                           .equals(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI) )
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

      MessageContext mc = new MessageContext();
      mc.setDoingSwA(true);
      mc.setEnvelope(env);
      if (attachments != null && attachments.length > 0)
      {
        for (DataHandler att : attachments) mc.addAttachment(att);
      }

      mepClient.addMessageContext(mc);
      mepClient.execute(true);
      return mepClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return null;
  }

  public MessageContext invoke(OMElement bodyPayload, DataHandler[] attachments,
                               String endpointURI)
  {
    SOAPEnvelope env = createEnvelope(1.1, null, bodyPayload);
    return invoke("", env, attachments, endpointURI);
  }

  public static OMElement getHeaderElementByName(MessageContext context,
                                                 String localName)
  {
    if (context == null || localName == null) return null;
    if (context.getEnvelope() == null) return null;
    OMElement soapHeader = context.getEnvelope().getHeader();
    if (soapHeader == null) return null;
    Iterator it = soapHeader.getChildElements();
    OMElement temp = null;
    while (it != null && it.hasNext())
    {
      temp = getFirstGrandChildWithName( (OMElement)it.next(), localName );
      if ( temp != null ) return temp;
    }
    return null;
  }

  public static String getHeaderElementAttributeValue(MessageContext context,
                                                      String localName,
                                                      String attributeName)
  {
    OMElement temp = getHeaderElementByName(context, localName);
    if (temp == null) return null;
    return getAttributeValue(temp, attributeName);
  }

  public static String getHeaderElementValue(MessageContext context,
                                             String localName)
  {
    OMElement temp = getHeaderElementByName(context, localName);
    if (temp == null) return null;
    return temp.getText();
  }

  public static Object getPropertyFromInMsgCtx(MessageContext outMsgCtx,
                                               String key)
  {
    if ( outMsgCtx == null || key == null ) return null;
    try
    {
      OperationContext opContext = outMsgCtx.getOperationContext();
      MessageContext inMsgContext =
         opContext.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
      return inMsgContext.getProperty(key);
    }
    catch(Exception ex) { ex.printStackTrace(); return null; }
  }

  public static MessageContext getOutgoingMsgCtxFromResponse(MessageContext respMsgCtx)
  {
    try
    {
      return respMsgCtx.getServiceContext()
                     .getLastOperationContext()
                     .getMessageContext(WSDLConstants.MESSAGE_LABEL_OUT_VALUE);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return null;
  }

  public static Object getPropertyFromOutMsgCtx(MessageContext inMsgCtx,
                                               String key)
  {
    if ( inMsgCtx == null || key == null ) return null;
    MessageContext outMsgCtx = getOutgoingMsgCtxFromResponse(inMsgCtx);
    if ( outMsgCtx == null ) return null;
    return outMsgCtx.getProperty(key);
  }

  public static SOAPEnvelope getEnvelopeFromInMsg(MessageContext outMsgContext)
  {
    if ( outMsgContext == null ) return null;
    try
    {
      OperationContext opContext = outMsgContext.getOperationContext();
      MessageContext inMsgContext =
         opContext.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
      return inMsgContext.getEnvelope();
    }
    catch(Exception ex) { ex.printStackTrace(); return null; }
  }

  public static Date utcToDate(String value)
  {
    Date date = null;
    if ( value != null && !value.trim().equals("") )
    {
      value = value.trim();
      SimpleDateFormat formatter =
                 new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      try { date = formatter.parse(value); }
      catch(Exception ex) { ex.printStackTrace(); }
    }
    return date;
  }

  public static String dateToUtc(Date date)
  {
    if ( date == null ) return null;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    return formatter.format(date);
  }

  public static void send(MessageContext mc, String toURL,
                          String soapAction, String[] modules)
  {
    try
    {
      EndpointReference targetEPR = new EndpointReference(toURL);
      Options options = new Options();
      options.setMessageId(UUIDGenerator.getUUID());
      if (soapAction != null) options.setAction(soapAction);
      if ( mc.getSoapAction() != null &&
          (soapAction == null || soapAction.trim().equals("")) )
          options.setAction(mc.getSoapAction());
      options.setTo(targetEPR);
      options.setTimeOutInMilliSeconds(10000);
      ServiceClient sender = new ServiceClient();
      sender.setOptions(options);
      OperationClient mepClient =
              sender.createClient(ServiceClient.ANON_OUT_ONLY_OP);
      if ( modules != null && modules.length > 0 )
      {
        for (String module : modules)
        {
          if (!mc.isEngaged(module) )
              sender.engageModule(new QName(module));
        }
      }

      mepClient.addMessageContext(mc);
      mepClient.execute(false);
    }
    catch(Exception ex) { ex.printStackTrace();  }
  }

  public static void send(MessageContext mc, String toURL, String soapAction)
  {
    try
    {
      EndpointReference targetEPR = new EndpointReference(toURL);
      Options options = new Options();
      options.setAction(soapAction);
      options.setTo(targetEPR);
      options.setTimeOutInMilliSeconds(10000);

      ServiceClient sender = new ServiceClient();
      sender.setOptions(options);
      OperationClient mepClient =
              sender.createClient(ServiceClient.ANON_OUT_ONLY_OP);

      mepClient.addMessageContext(mc);
      mepClient.execute(false);
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  public static MessageContext invoke(MessageContext mc, String toURL,
                                      String soapAction, String[] modules)
  {
    try
    {
      EndpointReference targetEPR = new EndpointReference(toURL);
      Options options = new Options();
      options.setMessageId(UUIDGenerator.getUUID());
      if (soapAction != null) options.setAction(soapAction);
      if ( mc.getSoapAction() != null &&
          (soapAction == null || soapAction.trim().equals("")) )
          options.setAction(mc.getSoapAction());
      options.setTo(targetEPR);
      options.setTimeOutInMilliSeconds(10000);
      ServiceClient sender = new ServiceClient();
      sender.setOptions(options);
      OperationClient mepClient =
              sender.createClient(ServiceClient.ANON_OUT_IN_OP);
      if ( modules != null && modules.length > 0 )
      {
        for (String module : modules)
        {
          if (!mc.isEngaged(module) )
              sender.engageModule(new QName(module));
        }
      }

      mepClient.addMessageContext(mc);
      mepClient.execute(true);
      return mepClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
    }
    catch(Exception ex) { ex.printStackTrace(); return null; }
  }

  public static MessageContext invoke(MessageContext mc, String toURL,
                                      String soapAction, String[] modules,
                                      Callback cb)
  {
    try
    {
      EndpointReference targetEPR = new EndpointReference(toURL);
      Options options = new Options();
      options.setMessageId(UUIDGenerator.getUUID());
      if (soapAction != null) options.setAction(soapAction);
      if ( mc.getSoapAction() != null &&
          (soapAction == null || soapAction.trim().equals("")) )
          options.setAction(mc.getSoapAction());
      options.setTo(targetEPR);
      options.setTimeOutInMilliSeconds(10000);
      ServiceClient sender = new ServiceClient();
      sender.setOptions(options);
      OperationClient mepClient =
              sender.createClient(ServiceClient.ANON_OUT_IN_OP);
      if ( modules != null && modules.length > 0 )
      {
        for (String module : modules)
        {
          if (!mc.isEngaged(module) )
              sender.engageModule(new QName(module));
        }
      }

      mepClient.addMessageContext(mc);
      if ( cb != null )
      {
        mepClient.setCallback(cb);
        mepClient.execute(false);
      }
      else mepClient.execute(true);
      return mepClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
    }
    catch(Exception ex) { ex.printStackTrace(); return null; }
  }

  public static void sendResponse(SOAPEnvelope response,
                                  MessageContext requestMessageContext)
  {
    try
    {
      MessageContext outMessage = null;
      OperationContext opContext = requestMessageContext.getOperationContext();
      if ( opContext != null )
        outMessage =
            opContext.getMessageContext(WSDLConstants.MESSAGE_LABEL_OUT_VALUE);
      if (outMessage == null)
      {
        //outMessage = Utils.createOutMessageContext(requestMessageContext);
        outMessage =
           MessageContextBuilder.createOutMessageContext(requestMessageContext);
        outMessage.getOperationContext().addMessageContext(outMessage);
      }
      outMessage.setEnvelope(response);
      outMessage.setResponseWritten(true);
      ConfigurationContext context =
              requestMessageContext.getConfigurationContext();
      AxisEngine engine = new AxisEngine(context);
      engine.send(outMessage);
      requestMessageContext.pause();
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  public static void sendResponse(MessageContext respMessage,
                                  MessageContext requestMessageContext)
  {
    try
    {
      MessageContext outMessage = null;
      OperationContext opContext = requestMessageContext.getOperationContext();
      if ( opContext != null )
        outMessage =
            opContext.getMessageContext(WSDLConstants.MESSAGE_LABEL_OUT_VALUE);
      if (outMessage == null)
      {
        outMessage =
           MessageContextBuilder.createOutMessageContext(requestMessageContext);
        outMessage.getOperationContext().addMessageContext(outMessage);
      }
      outMessage.setEnvelope(respMessage.getEnvelope());
      if ( respMessage.getAttachmentMap() != null )
      {
        outMessage.setAttachmentMap(respMessage.getAttachmentMap());
        /*
        String[] cids = respMessage.getAttachmentMap().getAllContentIDs();
        if ( cids != null && cids.length > 0 )
        {
          String soapCid =
                 respMessage.getAttachmentMap().getSOAPPartContentID();
          for (String cid : cids)
          {
            if ( !cid.equals(soapCid) )
                outMessage.addAttachment(cid, respMessage.getAttachment(cid));
          }
        }
        */
      }
      outMessage.setResponseWritten(true);
      ConfigurationContext context =
              requestMessageContext.getConfigurationContext();
      AxisEngine engine = new AxisEngine(context);
      engine.send(outMessage);
      //AxisEngine.send(outMessage);
      requestMessageContext.pause();
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  public static String logPrefix(MessageContext msgCtx)
  {
    if (msgCtx == null) return "";
    StringBuffer sb = new StringBuffer();
    sb.append( msgCtx.isServerSide() ? "ServerSide" : "ClientSide" );
    sb.append(", ");
    sb.append( msgCtx.getFLOW() == MessageContext.OUT_FLOW ? "OutFlow: " : "InFlow: " );
    return sb.toString();
  }

  public static String mimeType(String fileName)
  {
    if ( fileName == null || fileName.trim().equals("") ) return null;
    int dot = fileName.lastIndexOf(".");
    if ( dot < 0 ) return null;
    String extension = fileName.substring(dot + 1);

    if ( extension.equalsIgnoreCase("wmv") ) return "video/x-ms-wmv";
    if ( extension.equalsIgnoreCase("mp4") ) return "video/mp4";
    if ( extension.equalsIgnoreCase("mov") )
         return "video/quicktime";
    if ( extension.equalsIgnoreCase("hqx") )
         return "application/mac-binhex40";
    if ( extension.equalsIgnoreCase("cpt") )
         return "application/mac-compactpro";
    if ( extension.equalsIgnoreCase("doc") )
         return "application/msword";
    if ( extension.equalsIgnoreCase("pdf") )
         return "application/pdf";
    if ( extension.equalsIgnoreCase("ai") ||
         extension.equalsIgnoreCase("eps") ||
         extension.equalsIgnoreCase("ps")
       )
         return "application/postscript";
    if ( extension.equalsIgnoreCase("rtf") ) return "application/rtf";
    if ( extension.equalsIgnoreCase("bcpio") ) return "application/x-bcpio";
    if ( extension.equalsIgnoreCase("bz2") ) return "application/x-bzip2";
    if ( extension.equalsIgnoreCase("csh") ) return "application/x-csh";
    if ( extension.equalsIgnoreCase("gtar") ) return "application/x-gtar";
    if ( extension.equalsIgnoreCase("tgz") )
        return "application/x-gzip";
    if ( extension.equalsIgnoreCase("gz") ||
         extension.equalsIgnoreCase("gzip") )
         return "application/gzip";
    if ( extension.equalsIgnoreCase("kwd") ||
         extension.equalsIgnoreCase("kwt")
       )
        return "application/x-kword";
    if ( extension.equalsIgnoreCase("ksp") ) return "application/x-kspread";
    if ( extension.equalsIgnoreCase("kpr") ||
         extension.equalsIgnoreCase("kpt")
       )
        return "application/x-kpresenter";
    if ( extension.equalsIgnoreCase("chrt") ) return "application/x-kchart";
    if ( extension.equalsIgnoreCase("latex") ) return "application/x-latex";
    if ( extension.equalsIgnoreCase("sh") ) return "application/x-sh";
    if ( extension.equalsIgnoreCase("shar") ) return "application/x-shar";
    if ( extension.equalsIgnoreCase("swf") )
         return "application/x-shockwave-flash";
    if ( extension.equalsIgnoreCase("tar") ) return "application/x-tar";
    if ( extension.equalsIgnoreCase("tcl") ) return "application/x-tcl";
    if ( extension.equalsIgnoreCase("tex") ) return "application/x-tex";
    if ( extension.equalsIgnoreCase("texinfo") ||
         extension.equalsIgnoreCase("texi")
       )
         return "application/x-texinfo";
    if ( extension.equalsIgnoreCase("t") ||
         extension.equalsIgnoreCase("tr") ||
         extension.equalsIgnoreCase("roff")
       )
         return "application/x-troff";
    if ( extension.equalsIgnoreCase("man") ) return "application/x-troff-man";
    if ( extension.equalsIgnoreCase("zip") ) return "application/zip";
    if ( extension.equalsIgnoreCase("mpga") ||
         extension.equalsIgnoreCase("mp2") ||
         extension.equalsIgnoreCase("mp3")
       )
        return "audio/mpeg";
    if ( extension.equalsIgnoreCase("aif") ||
         extension.equalsIgnoreCase("aiff") ||
         extension.equalsIgnoreCase("aifc")
       )
        return "audio/x-aiff";
    if ( extension.equalsIgnoreCase("wav") ) return "audio/x-wav";
    if ( extension.equalsIgnoreCase("gif") ) return "image/gif";
    if ( extension.equalsIgnoreCase("ief") ) return "image/ief";
    if ( extension.equalsIgnoreCase("jpeg") ||
         extension.equalsIgnoreCase("jpg") ||
         extension.equalsIgnoreCase("jpe")
       )
         return "image/jpeg";
    if ( extension.equalsIgnoreCase("png") ) return "image/png";
    if ( extension.equalsIgnoreCase("tif") ||
         extension.equalsIgnoreCase("tiff")
       )
        return "image/tiff";
    if ( extension.equalsIgnoreCase("txt") ||
         extension.equalsIgnoreCase("asc")
       )
        return "text/plain";
    if ( extension.equalsIgnoreCase("rtf") ) return "text/rtf";
    if ( extension.equalsIgnoreCase("sgml") ||
         extension.equalsIgnoreCase("sgm")
       )
        return "text/sgml";
    if ( extension.equalsIgnoreCase("xml") ) return "text/xml";
    if ( extension.equalsIgnoreCase("mpeg") ||
         extension.equalsIgnoreCase("mpg") ||
         extension.equalsIgnoreCase("mpe")
       )
         return "video/mpeg";
    if ( extension.equalsIgnoreCase("avi") ) return "video/x-msvideo";
    if ( extension.equalsIgnoreCase("html") ||
         extension.equalsIgnoreCase("htm")
       )
         return "text/html";
    if ( extension.equalsIgnoreCase("rtx") ) return "text/richtext";
    if ( extension.equalsIgnoreCase("sct") ) return "text/scriptlet";
    if ( extension.equalsIgnoreCase("tsv") ) return "text/tab-separated-values";
    if ( extension.equalsIgnoreCase("css") ) return "text/css";
    if ( extension.equalsIgnoreCase("pfx") ) return "application/x-pkcs12";
    if ( extension.equalsIgnoreCase("dll") ) return "application/x-msdownload";
    if ( extension.equalsIgnoreCase("js") ) return "application/x-javascript";
    if ( extension.equalsIgnoreCase("iii") ) return "application/x-iphone";
    if ( extension.equalsIgnoreCase("dvi") ) return "application/x-dvi";
    if ( extension.equalsIgnoreCase("xla") ) return "application/vnd.ms-excel";
    if ( extension.equalsIgnoreCase("bin") ) return "application/octet-stream";
    if ( extension.equalsIgnoreCase("fif") ) return "application/fractals";
    if ( extension.equalsIgnoreCase("ppt") ) return "application/vnd.ms-powerpoint";
    if ( extension.equalsIgnoreCase("wps") ) return "application/vnd.ms-works";
    if ( extension.equalsIgnoreCase("mdb") ) return "application/x-msaccess";
    if ( extension.equalsIgnoreCase("pub") ) return "application/x-mspublisher";
    if ( extension.equalsIgnoreCase("der") ) return "application/x-x509-ca-cert";
    if ( extension.equalsIgnoreCase("ra") ) return "audio/x-pn-realaudio";
    if ( extension.equalsIgnoreCase("svg") ) return "image/svg+xml";
    if ( extension.equalsIgnoreCase("ico") ) return "image/x-icon";

    return null;
  }

  public static MimetypesFileTypeMap getMimeTypes()
  {
    if ( mimeTypes != null ) return mimeTypes;
    mimeTypes = new MimetypesFileTypeMap();

    mimeTypes.addMimeTypes("image/png png");
    mimeTypes.addMimeTypes("video/mp4 mp4");
    mimeTypes.addMimeTypes("application/mac-binhex40 hqx");
    mimeTypes.addMimeTypes("application/mac-compactpro cpt");
    mimeTypes.addMimeTypes("application/msword doc");
    mimeTypes.addMimeTypes("application/pdf pdf");
    mimeTypes.addMimeTypes("application/postscript ai eps ps");
    mimeTypes.addMimeTypes("application/rtf rtf");
    mimeTypes.addMimeTypes("application/x-bcpio bcpio");
    mimeTypes.addMimeTypes("application/x-bzip2 bz2");
    mimeTypes.addMimeTypes("application/x-csh csh");
    mimeTypes.addMimeTypes("application/x-gtar gtar");
    mimeTypes.addMimeTypes("application/x-gzip tgz");
    mimeTypes.addMimeTypes("application/gzip gz");
    mimeTypes.addMimeTypes("application/x-kword kwd kwt");
    mimeTypes.addMimeTypes("application/x-kspread ksp");
    mimeTypes.addMimeTypes("application/x-kpresenter kpr kpt");
    mimeTypes.addMimeTypes("application/x-kchart chrt");
    mimeTypes.addMimeTypes("application/x-latex latex");
    mimeTypes.addMimeTypes("application/x-sh sh");
    mimeTypes.addMimeTypes("application/x-shar shar");
    mimeTypes.addMimeTypes("application/x-shockwave-flash swf");
    mimeTypes.addMimeTypes("application/x-tar tar");
    mimeTypes.addMimeTypes("application/x-tcl tcl");
    mimeTypes.addMimeTypes("video/quicktime mov");

    mimeTypes.addMimeTypes("text/richtext rtx");
    mimeTypes.addMimeTypes("text/scriptlet sct");
    mimeTypes.addMimeTypes("text/tab-separated-values tsv");
    mimeTypes.addMimeTypes("text/css css");
    mimeTypes.addMimeTypes("application/x-pkcs12 pfx");
    mimeTypes.addMimeTypes("application/x-msdownload dll");
    mimeTypes.addMimeTypes("application/x-javascript js");
    mimeTypes.addMimeTypes("application/x-iphone iii");
    mimeTypes.addMimeTypes("application/x-dvi dvi");
    mimeTypes.addMimeTypes("application/vnd.ms-excel xla");
    mimeTypes.addMimeTypes("application/octet-stream bin");
    mimeTypes.addMimeTypes("application/fractals fif");
    mimeTypes.addMimeTypes("application/vnd.ms-powerpoint ppt");
    mimeTypes.addMimeTypes("application/vnd.ms-works wps");
    mimeTypes.addMimeTypes("application/x-msaccess mdb");
    mimeTypes.addMimeTypes("application/x-mspublisher pub");
    mimeTypes.addMimeTypes("application/x-x509-ca-cert der");
    mimeTypes.addMimeTypes("audio/x-pn-realaudio ra");
    mimeTypes.addMimeTypes("image/svg+xml svg");
    mimeTypes.addMimeTypes("image/x-icon ico");
    mimeTypes.addMimeTypes("video/x-ms-wmv wmv");
    mimeTypes.addMimeTypes("video/x-ms-asf asf");
    mimeTypes.addMimeTypes("audio/mp3 mp3");

    return mimeTypes;
  }

  public static String getFileExtension(String mimeType)
  {
    if ( mimeType == null || mimeType.trim().equals("") ) return null;

    if ( mimeType.equalsIgnoreCase("video/mp4") ) return "mp4";
    if ( mimeType.equalsIgnoreCase("audio/mp3") ) return "mp3";
    if ( mimeType.equalsIgnoreCase("audio/x-mp3") ) return "mp3";
    if ( mimeType.equalsIgnoreCase("audio/mpeg") ) return "mp3";
    if ( mimeType.equalsIgnoreCase("video/x-ms-asf") ) return "asf";
    if ( mimeType.equalsIgnoreCase("video/x-ms-wmv") )
         return "wmv";
    if ( mimeType.equalsIgnoreCase("video/quicktime") )
         return "mov";
    if ( mimeType.equalsIgnoreCase("application/mac-binhex40") )
         return "hqx";
    if ( mimeType.equalsIgnoreCase("application/mac-compactpro") )
         return "cpt";
    if ( mimeType.equalsIgnoreCase("application/msword") )
         return "doc";
    if ( mimeType.equalsIgnoreCase("application/pdf") )
         return "pdf";
    if ( mimeType.equalsIgnoreCase("application/postscript") )
         return "ps";
    if ( mimeType.equalsIgnoreCase("application/rtf") ) return "rtf";
    if ( mimeType.equalsIgnoreCase("application/x-bcpio") ) return "bcpio";
    if ( mimeType.equalsIgnoreCase("application/x-bzip2") ) return "bz2";
    if ( mimeType.equalsIgnoreCase("application/x-csh") ) return "csh";
    if ( mimeType.equalsIgnoreCase("application/x-gtar") ) return "gtar";
    if ( mimeType.equalsIgnoreCase("application/x-gzip") ||
         mimeType.equalsIgnoreCase("application/gzip")  )
        return "gz";
    if ( mimeType.equalsIgnoreCase("application/x-kword") )
        return "kwd";
    if ( mimeType.equalsIgnoreCase("application/x-kspread") ) return "ksp";
    if ( mimeType.equalsIgnoreCase("application/x-kpresenter") )
        return "kpr";
    if ( mimeType.equalsIgnoreCase("application/x-kchart") ) return "chrt";
    if ( mimeType.equalsIgnoreCase("application/x-latex") ) return "latex";
    if ( mimeType.equalsIgnoreCase("application/x-sh") ) return "sh";
    if ( mimeType.equalsIgnoreCase("application/x-shar") ) return "shar";
    if ( mimeType.equalsIgnoreCase("application/x-shockwave-flash") )
         return "swf";
    if ( mimeType.equalsIgnoreCase("application/x-tar") ) return "tar";
    if ( mimeType.equalsIgnoreCase("application/x-tcl") ) return "tcl";
    if ( mimeType.equalsIgnoreCase("application/x-tex") ) return "tex";
    if ( mimeType.equalsIgnoreCase("application/x-texinfo") )
         return "texi";
    if ( mimeType.equalsIgnoreCase("application/x-troff") )
         return "roff";
    if ( mimeType.equalsIgnoreCase("application/x-troff-man") ) return "man";
    if ( mimeType.equalsIgnoreCase("application/zip") ) return "zip";
    if ( mimeType.equalsIgnoreCase("audio/x-aiff") )
        return "aif";
    if ( mimeType.equalsIgnoreCase("audio/x-wav") ) return "wav";
    if ( mimeType.equalsIgnoreCase("image/gif") ) return "gif";
    if ( mimeType.equalsIgnoreCase("image/ief") ) return "ief";
    if ( mimeType.equalsIgnoreCase("image/jpeg") )
         return "jpg";
    if ( mimeType.equalsIgnoreCase("image/png") ) return "png";
    if ( mimeType.equalsIgnoreCase("image/tiff") )
        return "tif";
    if ( mimeType.equalsIgnoreCase("text/plain") )
        return "txt";
    if ( mimeType.equalsIgnoreCase("text/rtf") ) return "rtf";
    if ( mimeType.equalsIgnoreCase("text/sgml") )
        return "sgml";
    if ( mimeType.equalsIgnoreCase("text/xml") ) return "xml";
    if ( mimeType.equalsIgnoreCase("video/mpeg") )
         return "mpeg";
    if ( mimeType.equalsIgnoreCase("video/x-msvideo") ) return "avi";
    if ( mimeType.equalsIgnoreCase("text/html") )
         return "html";
    if ( mimeType.equalsIgnoreCase("text/richtext") ) return "rtx";
    if ( mimeType.equalsIgnoreCase("text/scriptlet") ) return "sct";
    if ( mimeType.equalsIgnoreCase("text/tab-separated-values") ) return "tsv";
    if ( mimeType.equalsIgnoreCase("text/css") ) return "css";
    if ( mimeType.equalsIgnoreCase("application/x-pkcs12") ) return "pfx";
    if ( mimeType.equalsIgnoreCase("application/x-msdownload") ) return "dll";
    if ( mimeType.equalsIgnoreCase("application/x-javascript") ) return "js";
    if ( mimeType.equalsIgnoreCase("application/x-iphone") ) return "iii";
    if ( mimeType.equalsIgnoreCase("application/x-dvi") ) return "dvi";
    if ( mimeType.equalsIgnoreCase("application/vnd.ms-excel") ) return "xla";
    if ( mimeType.equalsIgnoreCase("application/octet-stream") ) return "bin";
    if ( mimeType.equalsIgnoreCase("application/fractals") ) return "fif";
    if ( mimeType.equalsIgnoreCase("application/vnd.ms-powerpoint") ) return "ppt";
    if ( mimeType.equalsIgnoreCase("application/vnd.ms-works") ) return "wps";
    if ( mimeType.equalsIgnoreCase("application/x-msaccess") ) return "mdb";
    if ( mimeType.equalsIgnoreCase("application/x-mspublisher") ) return "pub";
    if ( mimeType.equalsIgnoreCase("application/x-x509-ca-cert") ) return "der";
    if ( mimeType.equalsIgnoreCase("audio/x-pn-realaudio") ) return "ra";
    if ( mimeType.equalsIgnoreCase("image/svg+xml") ) return "svg";
    if ( mimeType.equalsIgnoreCase("image/x-icon") ) return "ico";

    return null;
  }

  public static void writeDataHandlerToFile(DataHandler dataHandler, File file)
  {
    if ( dataHandler == null || file == null ) return;
    try
    {
      file.getParentFile().mkdirs();
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      dataHandler.writeTo(fileOutputStream);
      fileOutputStream.flush();
      fileOutputStream.close();
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

 /**
  * Writes the envelope and attachments to a file in the form of a
  * SwA message on the wire, and returns the content type. To read
  * back the message context, use the
  * readMessage(msgCtx, file, contentType) method.
  */
  public static String writeMessage(MessageContext ctx, File file)
  {
    if ( ctx == null || file == null ) return null;
    long start = System.currentTimeMillis();
    String name = UUIDGenerator.getUUID();
    name = name.substring(name.lastIndexOf(":") + 1);
    String boundary = "boundary_" + name;
    OMOutputFormat format = new OMOutputFormat();
    format.setDoingSWA(true);
    format.setDoOptimize(false);
    format.setSOAP11(true);
    format.setCharSetEncoding("UTF-8");
    format.setMimeBoundary(boundary);
    format.setAutoCloseWriter(true);

    SOAPMessageFormatter formatter = new SOAPMessageFormatter();
    String ct = formatter.getContentType(ctx, format, null);

    if ( ctx.getAttachmentMap() != null )
    {
      String cid = ctx.getAttachmentMap().getSOAPPartContentID();
      ctx.getAttachmentMap().removeDataHandler(cid);
      //System.out.println("--- removed attachment with content-id: " + cid);
    }
    //else System.out.println("--- attachmentMap is null");

    try
    {
      FileWriter fw = new FileWriter(file);
      fw.write("Content-Type: " + ct + "\n\n");
      fw.close();
      FileOutputStream fos = new FileOutputStream(file, true);
      formatter.writeTo(ctx, format, fos, true);
      fos.flush();
      fos.close();
    }
    catch(Exception ex) { ex.printStackTrace(); }
    long end = System.currentTimeMillis();
    System.out.println("--- It took " + (end - start) + " milliseconds to write Message to file");
    return ct;
  }

  /**
  *  For this method to work, the message context needs to have
  *  a configuration context object. This method populates the
  *  provided message context with the data from the file
  *  (envelope + attachments)
  * @param mc: the MessageContext to populate with the message in the file
  * @param in: the InputStream from which to read the message
  * @param ct: the content type of the message. This is returned
  * by the writeMessage(msgCtx, file) method.
  */
  public static MessageContext readMessage(MessageContext mc,
                                           InputStream in, String ct)
  {
    if ( in == null ) return null;
    if ( mc == null ) mc = new MessageContext();
    try
    {
      SOAPEnvelope envelope =
              TransportUtils.createSOAPMessage(mc, in, ct);
      String startCID = getStartCID(ct);
      Attachments atts = mc.getAttachmentMap();
      atts.removeDataHandler(startCID);
      mc.setEnvelope(envelope);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return mc;
  }

 /**
  *  For this method to work, the message context needs to have
  *  a configuration context object. This method populates the
  *  provided message context with the data from the file
  *  (envelope + attachments)
  * @param mc: the MessageContext to populate with the message in the file
  * @param file: the file from which to read the message
  * @param ct: the content type of the message. This is returned
  * by the writeMessage(msgCtx, file) method.
  */
  public static MessageContext readMessage(MessageContext mc,
                                           File file, String ct)
  {
    if ( file == null || !file.exists() ) return null;
    if ( mc == null ) mc = new MessageContext();
    try
    {
      FileInputStream in = new FileInputStream(file);

      SOAPEnvelope envelope =
              TransportUtils.createSOAPMessage(mc, in, ct);
      String startCID = getStartCID(ct);
      Attachments atts = mc.getAttachmentMap();
      atts.removeDataHandler(startCID);
      mc.setEnvelope(envelope);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return mc;
  }

  public static MessageContext readMessage(MessageContext mc,
                                           byte[] contents, String ct)
  {
    if ( contents == null || contents.length <= 0 ) return null;
    if ( mc == null ) mc = new MessageContext();
    try
    {
      ByteArrayInputStream in = new ByteArrayInputStream(contents);
      SOAPEnvelope envelope =
              TransportUtils.createSOAPMessage(mc, in, ct);
      String startCID = getStartCID(ct);
      Attachments atts = mc.getAttachmentMap();
      atts.removeDataHandler(startCID);
      mc.setEnvelope(envelope);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return mc;
  }

  public static byte[] getMessageBytes(MessageContext ctx)
  {
    if ( ctx == null ) return null;
    long start = System.currentTimeMillis();
    String name = UUIDGenerator.getUUID();
    name = name.substring(name.lastIndexOf(":") + 1);
    String boundary = "boundary_" + name;
    OMOutputFormat format = new OMOutputFormat();
    format.setDoingSWA(true);
    format.setDoOptimize(false);
    format.setSOAP11(true);
    format.setCharSetEncoding("UTF-8");
    format.setMimeBoundary(boundary);
    format.setAutoCloseWriter(true);

    SOAPMessageFormatter formatter = new SOAPMessageFormatter();
    String ct = formatter.getContentType(ctx, format, null);
    //format.setContentType("multipart/related");
    format.setContentType(ct);
    if ( ctx.getAttachmentMap() != null )
    {
      String cid = ctx.getAttachmentMap().getSOAPPartContentID();
      ctx.getAttachmentMap().removeDataHandler(cid);
    }

    try
    {
      return formatter.getBytes(ctx, format);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return null;
  }

  private static String getStartCID(String ct)
  {
    if ( ct == null ) return null;
    int i = ct.indexOf("start=\"");
    String temp = null;
    if ( i >= 0 ) temp = ct.substring(i + 7);
    int j = temp.indexOf("\"");
    temp = temp.substring(0, j);
    if ( temp.startsWith("<") )
         temp = temp.substring(1, temp.length() - 1);
    return temp;
  }

  // not tested yet...
  public static void writeToFile(File file, InputStream fileIs)
  {
    if ( file == null ||  fileIs == null ) return;
    file.getParentFile().mkdirs();
    try
    {
      WritableByteChannel channel = new FileOutputStream(file).getChannel();
      int size=1024*1024;
      ByteBuffer buf = ByteBuffer.allocateDirect(size);
      byte[] bytes = new byte[size];
      int count = 0;
      int index = 0;
      while (count >= 0)
      {
        if (index == count)
        {
          count = fileIs.read(bytes);
          index = 0;
        }
        while (index < count && buf.hasRemaining())
        {
          buf.put(bytes[index++]);
        }
        buf.flip();
        //int numWritten =
                channel.write(buf);
        if ( buf.hasRemaining() )
        {
          buf.compact();
        }
        else
        {
          buf.clear();
        }
      }
      channel.close();
    }
    catch (Exception e) { throw new RuntimeException(e); }
  }

  public static void writeAttachments(MessageContext msgCtx,
                                      List<String> cids, File location)
  {
    if ( msgCtx == null || cids == null || cids.size() <= 0 ) return;
    Attachments atts = msgCtx.getAttachmentMap();
    writeAttachments(atts, cids, location);
  }
  public static void writeAttachments(Attachments atts, List<String> cids,
                                      File location)
  {
    if ( cids == null || cids.size() <= 0 || atts == null ) return;
    String soapPartCid = atts.getSOAPPartContentID();
    for (String cid : cids)
    {
      if ( cid != null && !cid.equals(soapPartCid) )
      {
        DataHandler dh = atts.getDataHandler(cid);
        if ( dh == null )
        {
          //System.out.println("-------- dh is null");
          continue;
        }
        String name = cid;
        if ( cid.indexOf(":") >= 0 ) name = name.replaceAll(":", "-");
        if ( cid.indexOf("@") >= 0 ) name = name.replaceAll("@", "_");
        String dhct = dh.getContentType();
        //System.out.println("========= dh.getContentType()=" + dhct);
        String extension = Util.getFileExtension(dhct);
        //String extension = Util.getFileExtension(dh.getContentType());
        //System.out.println("===== extension is " + extension);
        String path = location.getAbsolutePath() + File.separator +
                      name + "." + extension;
        File att = new File(path);
        //System.out.println("     about to write attachment...");
        Util.writeDataHandlerToFile(dh, att); 
      }
    }
    System.out.println("Attachments were written to " + location.toString());
  }
  public static void writeAttachments(MessageContext msgCtx, List<String> cids,
                                      List<String> contentTypes, File location)
  {
    if ( msgCtx == null || cids == null || cids.size() <= 0 ) return;
    Attachments atts = msgCtx.getAttachmentMap();
    if ( atts == null ) return;
    String soapPartCid = atts.getSOAPPartContentID();
    for (int i = 0; i < cids.size(); i++)
    {
      if ( cids.get(i) != null && !cids.get(i).equals(soapPartCid) )
      {
        String extension1 = null;
        if ( contentTypes.get(i) != null )
             extension1 = Util.getFileExtension(contentTypes.get(i));
        DataHandler dh = atts.getDataHandler(cids.get(i));
        String name = cids.get(i);
        if ( cids.get(i).indexOf(":") >= 0 ) name = name.replaceAll(":", "-");
        if ( cids.get(i).indexOf("@") >= 0 ) name = name.replaceAll("@", "_");
        String extension2 = Util.getFileExtension(dh.getContentType());
        String path = location.getAbsolutePath() + File.separator +
                      name + "." + (extension1 == null? "" : extension1 + ".")
                      + extension2;
        File att = new File(path);
        //System.out.println("     about to write attachment...");
        Util.writeDataHandlerToFile(dh, att);
      }
    }
  }

  public static Object createInstance(String className)
  {
    if ( className == null || className.trim().equals("") ) return null;

    Object instance = null;
    try
    {
      int dollarPos = className.indexOf("$");
      if ( dollarPos < 0 )
      {
        Class instanceClass = Class.forName(className);
        instance = instanceClass.newInstance();
      }
      else
      {
        String containerClassName = className.substring(0, dollarPos);
        Class containerClass = Class.forName(containerClassName);
        Class innerClass = Class.forName(className);
        Object containerInstance = containerClass.newInstance();
        Constructor innerConstructor =
        innerClass.getDeclaredConstructor(new Class[] {containerClass});
        instance = innerConstructor.newInstance(containerInstance);
      }
    }
    catch (Exception e) { e.printStackTrace(); }
    return instance;
  }

  // not working...
  public static void saveAttachments(MessageContext context, File location)
  {
    if ( context == null || location == null ) return;
    Attachments atts = context.getAttachmentMap();
    if ( atts == null ) return;
    saveAttachments(atts, location);
  }
  // not working...
  public static void saveAttachments(Attachments atts, File location)
  {
    if ( atts == null ) return;
    IncomingAttachmentStreams streams =
                       atts.getIncomingAttachmentStreams();
    if ( streams == null ) return;
    System.out.println("====== got the IncomingAttachmentStreams");
    location.mkdirs();
    IncomingAttachmentInputStream stream = streams.getNextStream();
    System.out.println("====== got the IncomingAttachmentInputStream");
    int i = 1;
    while ( stream != null )
    {
      System.out.println("====== Inside the loop...");
      String ct = stream.getContentType();
      String extension = getFileExtension(ct);
/*
      String id = stream.getContentId();
      if ( id.indexOf(":") >= 0 ) id = id.replaceAll(":", "-");
      if ( id.startsWith("<") ) id = id.substring(1, id.length() - 1);
*/
      String path = location.getAbsolutePath() + File.separator +
                     i + "." + extension;
                    //id + "." + extension;
      System.out.println("====== about to write to " + path);
      writeToFile(new File(path), stream);
      while ( !streams.isReadyToGetNextStream() ) {}
      if ( streams.isReadyToGetNextStream() )
           stream = streams.getNextStream();
      i++;
    }
  }

  public static void doCompressFile(String inFileName)
  {
    if ( inFileName == null || inFileName.trim().equals("") ) return;
    try
    {
      File file = new File(inFileName);
      //System.out.println(" you are going to gzip the  : " + file + "file");
      FileOutputStream fos = new FileOutputStream(file + ".gz");
      //System.out.println(" Now the name of this gzip file is  : " + file + ".gz" );
      GZIPOutputStream gzos = new GZIPOutputStream(fos);
      //System.out.println(" opening the input stream");
      FileInputStream fin = new FileInputStream(file);
      BufferedInputStream in = new BufferedInputStream(fin);
      //System.out.println("Transferring file from" + inFileName + " to " + file + ".gz");
      byte[] buffer = new byte[1024];
      int i;
      while ((i = in.read(buffer)) >= 0)
      {
        gzos.write(buffer,0,i);
      }
      //System.out.println(" file is in now gzip format");
      in.close();
      gzos.close();
    }
    catch(IOException e)
    {
      System.out.println("Exception is" + e);
    }
  }

  public static InputStream getInputStream(javax.jms.Message message) throws Exception
  {
    try
    {
      // get the incoming msg content into a byte array
      if (message instanceof  BytesMessage)
      {
        byte[] buffer = new byte[8 * 1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BytesMessage byteMsg = (BytesMessage) message;
        for (int bytesRead = byteMsg.readBytes(buffer); bytesRead != -1; bytesRead = byteMsg
                                .readBytes(buffer))
        {
          out.write(buffer, 0, bytesRead);
        }
        return new ByteArrayInputStream(out.toByteArray());
      }
      else if (message instanceof  TextMessage)
      {
        TextMessage txtMsg = (TextMessage) message;
        String contentType = message.getStringProperty("contentType");
        if (contentType != null)
        {
           return new ByteArrayInputStream(txtMsg.getText().getBytes(
                                          BuilderUtil.getCharSetEncoding(contentType)));
        }
        else
        {
          return new ByteArrayInputStream(txtMsg.getText().getBytes());
        }
      }
      else
      {
        System.out.println("Unsupported JMS message type : "
                                + message.getClass().getName());
      }
    }
    catch (JMSException e)
    {
      System.out.println("JMS Exception getting InputStream into message");
    }
    catch (UnsupportedEncodingException e)
    {
      System.out.println("Encoding exception getting InputStream into message");
    }
    return null;
  }
}