package org.holodeck.ebms3.consumers;

import org.simpleframework.xml.*;
import org.holodeck.ebms3.module.MsgInfo;

import java.util.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="gateway")
public class GatewayConfig
{
  @ElementList(inline=true)
  protected List<Consumption> consumptions = new ArrayList<Consumption>();

  public List<Consumption> getConsumptions() { return consumptions; }
  public void setConsumptions(List<Consumption> consumptions)
  {
    this.consumptions = consumptions;
  }

  public void addConsumption(Consumption consumption)
  {
    if ( consumption == null ) return;
    consumptions.add(consumption);
  }

  public Consumption getMatchingConsumption(MsgInfo info)
  {
    if ( consumptions == null || consumptions.size() == 0 ) return null;
    for (int i = 0; i < consumptions.size(); i++)
    {
      Consumption c = consumptions.get(i);
      if ( c.match(info) ) return c;
    }
    return null;
  }
}