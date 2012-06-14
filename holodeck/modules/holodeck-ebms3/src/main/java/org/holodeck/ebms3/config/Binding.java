package org.holodeck.ebms3.config;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;

/**
 * @author Hamid Ben Malek
 */
@Root(name="Binding", strict=false)
public class Binding implements java.io.Serializable
{
  private static final long serialVersionUID = -5593316501928370737L;

  @Attribute
  protected String name;

  @Element(name="MEP")
  protected MEP mep;

  public Binding() {}
  public Binding(String name, MEP mep)
  {
    this.name = name;
    this.mep = mep;
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public MEP getMep() { return mep; }
  public void setMep(MEP mep) { this.mep = mep; }

  public void setPmode(PMode pmode)
  {
    if ( pmode == null ) return;
    if ( mep != null ) mep.setPmode(pmode);
  }
}