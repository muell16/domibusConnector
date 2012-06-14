package org.holodeck.ebms3.persistent;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import org.holodeck.ebms3.packaging.SignalMessage;
import org.holodeck.ebms3.packaging.PackagingFactory;
import org.holodeck.ebms3.packaging.Receipt;
import org.holodeck.ebms3.module.Constants;

import org.holodeck.common.soap.Element;
import org.holodeck.common.soap.Util;
import org.apache.axiom.om.OMElement;

/**
 * This table contains receipts that need to be sent out as a callback
 * by a working background thread
 *
 * @author Hamid Ben Malek
 */
@Entity
@Table(name = "Receipts")
public class ReceiptData implements Serializable
{
  private static final long serialVersionUID = 7109238203948576152L;

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy="uuid")
  @Column(name = "Receipt_ID")
  protected String id = null;

  @Column(name = "timestamp")
  protected Date timestamp;

  @Column(name = "messageId")
  protected String messageId;

  @Column(name = "refToMessaeId")
  protected String refToMessageId;

  @Lob  
  @Column(name = "Non_Repudiation_Info", length = 1999999999)
  protected String nonRepudiationInformation;

  @Column(name = "To_URL")
  protected String toURL;

  @Column(name = "Sent")
  protected boolean sent = false;

  @Transient
  protected OMElement nri;

  public ReceiptData() {  timestamp = new Date(); }
  public ReceiptData(String refToMessageId, List<OMElement> references)
  {
    timestamp = new Date();
    this.refToMessageId = refToMessageId;
    if ( references == null || references.size() == 0 ) return;
      
    //nri = new Element(Constants.NON_REPUDIATION_INFORMATION,
    //                  Constants.ebbpNS, Constants.ebbp_PREFIX);
    nri = new Element(Constants.NON_REPUDIATION_INFORMATION,
                      Constants.ebbpNS, Constants.ebbp_PREFIX).getElement();
    for ( OMElement ref : references )
    {
      Element mpn =
         new Element(Constants.MESSAGE_PART_NR_INFORMATION,
                     Constants.ebbpNS, Constants.ebbp_PREFIX);
      mpn.addChild(ref);
      //nri.addChild(mpn);
      nri.addChild(mpn.getElement());
    }
    nonRepudiationInformation = Util.toString(nri);
  }

  public String getRefToMessageId() { return refToMessageId; }
  public void setRefToMessageId(String refToMessageId)
  {
    this.refToMessageId = refToMessageId;
  }

  public String getToURL() { return toURL; }
  public void setToURL(String toURL) { this.toURL = toURL; }

  public boolean isSent() { return sent; }
  public void setSent(boolean sent) { this.sent = sent; }

  public SignalMessage getSignalMessage()
  {
    return PackagingFactory.createReceipt(timestamp, refToMessageId,
                                          getNonRepudiationInformation());
  }

  public Receipt getReceipt() { return new Receipt(nri); }

  public OMElement getNonRepudiationInformation()
  {
    if ( nri != null ) return nri;
    if ( nonRepudiationInformation == null ||
         nonRepudiationInformation.trim().equals("") ) return null;
    nri = Util.toOMElement(nonRepudiationInformation);
    return nri;
  }
}