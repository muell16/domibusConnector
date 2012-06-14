package org.holodeck.common.soap;

import org.apache.axiom.om.*;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.namespace.QName;
import java.io.*;
import java.util.*;

/**
 * @author Hamid Ben Malek
 */
public class Element implements java.io.Serializable
{
   public static final long serialVersionUID = 7825985226389871436L;

   protected OMElement element = null;
   protected String localName = null;
   protected OMNamespace namespace = null;
   protected String text = null;

   public Element() {}
   public Element(String name)
   {
     OMFactory factory = OMAbstractFactory.getOMFactory();
     element = factory.createOMElement(name, null);
   }
   public Element(String localName, String uri, String prefix)
   {
     OMFactory factory = OMAbstractFactory.getOMFactory();
     OMNamespace ns = factory.createOMNamespace(uri, prefix);
     element = factory.createOMElement(localName, ns);
   }
   public Element(OMElement omElement)
   {
     fromOMElement(omElement);
   }

   public Element(File xmlFile)
   {
     if ( xmlFile == null ) return;
     try
     {
       XMLStreamReader parser =
          XMLInputFactory.newInstance().createXMLStreamReader(
                  new FileInputStream(xmlFile));
       StAXOMBuilder builder = new StAXOMBuilder(parser);
       element =  builder.getDocumentElement();
     }
     catch(Exception ex) { ex.printStackTrace(); }
   }

    public OMElement getElement()
   {
     if ( element != null ) return element;
     element = getOMFactory().createOMElement(localName, namespace);
     if ( text != null ) element.setText(text);
     return element;
   }

   public String getLocalName() { return localName; }
   public void setLocalName(String localName)
   {
     this.localName = localName;
     if ( element != null ) element.setLocalName(localName);
     else element = getOMFactory().createOMElement(localName, namespace);
   }

   public OMNamespace getNamespace() { return namespace; }
   public void setNamespace(OMNamespace namespace)
   {
     this.namespace = namespace;
     if ( element != null ) element.setNamespace(namespace);
     else element = getOMFactory().createOMElement(localName, namespace);
   }

   public String getText() { return text; }
   public void setText(String text)
   {
     this.text = text;
     if ( element != null ) element.setText(text);
     else
     {
       element = getOMFactory().createOMElement(localName, namespace);
       element.setText(text);
     }
   }

   private void fromOMElement(OMElement omElement)
   {
     if ( omElement == null ) return;
     element = omElement.cloneOMElement();
     text = element.getText();
     localName = element.getLocalName();
     namespace = element.getNamespace();
   }

   public String getAttributeValue(String name, String uri, String prefix)
   {
     QName qname = new QName(uri, name, prefix);
     return getElement().getAttributeValue(qname);
   }

   public void addAttribute(String name, String uri, String prefix, String value)
   {
     OMNamespace ns = null;
     if ( (uri != null && !uri.trim().equals("")) ||
          (prefix != null && !prefix.trim().equals("")) )
         ns = getOMFactory().createOMNamespace(uri, prefix);
     OMAttribute att = getOMFactory().createOMAttribute(name, ns, value);
     getElement().addAttribute(att);
   }

   public void addAttribute(String name, String value)
   {
     OMAttribute att = getOMFactory().createOMAttribute(name, null, value);
     getElement().addAttribute(att);
   }

   public void setAttribute(String attLocalName, String value)
   {
     if (attLocalName == null || attLocalName.trim().equals("")) return;
     OMAttribute att = getAttribute(attLocalName);
     if (att != null) att.setAttributeValue(value);
     else addAttribute(attLocalName, value);
   }

   public void setAttribute(String attLocalName, String prefix, String value)
   {
     if (attLocalName == null || attLocalName.trim().equals("")) return;
     if (prefix == null || prefix.trim().equals(""))
         setAttribute(attLocalName, value);
     else
     {
       OMNamespace ns = getElement().findNamespaceURI(prefix);
       OMAttribute att;
       if (ns != null)
       {
         att = getAttribute(ns.getNamespaceURI(), attLocalName, prefix);
         if (att != null)
         {
           att.setAttributeValue(value);
         }
         else
         {
           att = getOMFactory().createOMAttribute(attLocalName, ns, value);
           getElement().addAttribute(att);
         }
       }
       else getOMFactory().createOMAttribute(attLocalName, ns, value);
     }
   }

   public OMAttribute getAttribute(String attLocalName)
   {
     if ( attLocalName == null || attLocalName.trim().equals("") ) return null;
     Iterator it = getElement().getAllAttributes();
     OMAttribute att;
     while (it != null && it.hasNext())
     {
       att = (OMAttribute)it.next();
       if ( att != null && att.getLocalName().equals(attLocalName) ) return att;
     }
     return null;
   }

   public OMAttribute getAttribute(String uri, String name, String prefix)
   {
     OMAttribute att = null;
     if (uri != null && prefix != null)
     {
       QName qname = new QName(uri, name, prefix);
       att = getElement().getAttribute(qname);
     }
     if (att != null) return att;
     else return getAttribute(name);
   }

   public String getAttributeValue(String attLocalName)
   {
      OMAttribute att = getAttribute(attLocalName);
      if ( att != null ) return att.getAttributeValue();
      else return null;
   }

   public Element addElement(String localName, String prefix)
   {
     if (localName == null || localName.trim().equals("")) return null;
     OMNamespace ns = null;
     if (prefix != null && !prefix.trim().equals(""))
         ns = getElement().findNamespaceURI(prefix);
     Element child = new Element();
     child.setLocalName(localName);
     child.setNamespace(ns);
     getElement().addChild(child.getElement());
     return child;
   }

   public OMElement getChild(String localName, String prefix)
   {
     if (localName == null || localName.trim().equals("")) return null;
     Iterator it = getElement().getChildElements();
     while (it != null && it.hasNext())
     {
       OMElement e = (OMElement)it.next();
       if (e != null && e.getLocalName().equals(localName) &&
           e.getNamespace() != null && e.getNamespace().getPrefix() != null &&
           e.getNamespace().getPrefix().equals(prefix)) return e;
     }
     return null;
   }

   public OMElement getFirstGrandChildWithName(String _localName)
   {
     return getFirstGrandChildWithName(element, _localName);
   }

   public String getGrandChildValue(String _localName)
   {
     OMElement gc = getFirstGrandChildWithName(_localName);
     if ( gc != null ) return gc.getText();
     else return null;
   }

   private OMElement getFirstGrandChildWithName(OMElement root, String _localName)
   {
     if (_localName == null || _localName.trim().equals("") || root == null)
         return null;
     if (root.getLocalName().equals(_localName)) return root;

     OMElement om = root.getFirstElement();
     if (om != null)
     {
       if ( om.getLocalName().equals(_localName) ) return om;
       OMElement temp = getFirstGrandChildWithName(om, _localName);
       if (temp != null) return temp;

       for (OMElement tmp = (OMElement)om.getNextOMSibling();
            tmp != null; )
       {
         temp = getFirstGrandChildWithName(tmp, _localName);
         if (temp != null) return temp;
         tmp = (OMElement)tmp.getNextOMSibling();
       }
     }
     return null;
   }

   public void writeTo(Writer writer)
   {
     try
     {
       XMLOutputFactory xof = XMLOutputFactory.newInstance();
       XMLStreamWriter w = xof.createXMLStreamWriter(writer);
       getElement().serialize(w);
       writer.flush();
     }
     catch(Exception ex) { ex.printStackTrace(); }
   }

   public String toXML()
   {
     StringWriter sw = new StringWriter();
     writeTo(sw);
     return sw.toString();
   }

   public void addChild(OMNode omNode)
   {
     if ( omNode == null ) return;
     getElement().addChild(omNode);
   }

   public void addChild(Element node)
   {
     if ( node == null ) return;
     getElement().addChild(node.getElement());
   }

   public Iterator getChildElements()
   {
     return getElement().getChildElements();
   }

   private OMFactory getOMFactory()
   {
     return OMAbstractFactory.getOMFactory();
   }
}