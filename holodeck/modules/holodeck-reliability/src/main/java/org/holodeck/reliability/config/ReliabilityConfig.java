package org.holodeck.reliability.config;

import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

/**
 * @author Hamid Ben Malek
 */
@Root(name="Reliability-Config", strict=false)
public class ReliabilityConfig implements java.io.Serializable
{
  private static final long serialVersionUID = -8300908978670392549L;

  @ElementList(entry="Reliability", inline=true, required=false)
  protected List<Reliability> reliabilities = new ArrayList<Reliability>();

  public ReliabilityConfig() {}

  public List<Reliability> getReliabilities() { return reliabilities; }
  public void setReliabilities(List<Reliability> producers)
  {
    this.reliabilities = producers;
  }

  public void addReliability(Reliability rel)
  {
    if ( rel != null ) reliabilities.add(rel);
  }

  public static ReliabilityConfig load(File source)
  {
    if ( source == null || !source.exists() ) return null;
    ReliabilityConfig reliabilityConfig = null;
    try
    {
      Serializer serializer = new Persister();
      reliabilityConfig = serializer.read(ReliabilityConfig.class, source);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return reliabilityConfig;
  }
}