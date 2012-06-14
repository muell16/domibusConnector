package org.holodeck.ebms3.packaging;

import org.holodeck.common.soap.Element;
import org.holodeck.ebms3.module.*;
import org.apache.axiom.om.OMElement;

/**
 * @author Hamid Ben Malek
 */
public class Receipt extends Element
{
  private static final long serialVersionUID = -2001342201298347565L;  

  public Receipt()
  {
    super(Constants.RECEIPT, Constants.NS, Constants.PREFIX);
  }
  public Receipt(OMElement[] references) { setReferences(references); }

  public Receipt(OMElement nonRepudiationInformation)
  {
    this();
    if ( nonRepudiationInformation != null )
         addChild( nonRepudiationInformation );
  }

  public void setReferences(OMElement[] references)
  {
    Element nri =
      new Element(Constants.NON_REPUDIATION_INFORMATION,
                  Constants.ebbpNS, Constants.ebbp_PREFIX);
    for ( OMElement ref : references )
    {
      Element mpn =
         new Element(Constants.MESSAGE_PART_NR_INFORMATION,
                     Constants.ebbpNS, Constants.ebbp_PREFIX);
      mpn.addChild(ref);
      nri.addChild(mpn);
    }
    addChild(nri);
  }
}