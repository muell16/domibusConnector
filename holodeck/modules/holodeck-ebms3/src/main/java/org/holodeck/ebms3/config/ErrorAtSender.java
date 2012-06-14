package org.holodeck.ebms3.config;

import org.simpleframework.xml.*;

/**
 * @author Hamid Ben Malek
 */
@Root(name="ErrorAtSender", strict=false)
public class ErrorAtSender implements java.io.Serializable
{
  private static final long serialVersionUID = -8309197123450417779L;
    
  @Attribute(required=false)
  protected boolean notifiyProducer;

  @Attribute(required=false)
  protected boolean notifyConsumer;

  @Attribute(required=false)
  protected String reportTo;

  public ErrorAtSender() {}

  public ErrorAtSender(boolean notifiyProducer, boolean notifyConsumer,
                       String reportTo)
  {
    this.notifiyProducer = notifiyProducer;
    this.notifyConsumer = notifyConsumer;
    this.reportTo = reportTo;
  }

  public boolean isNotifiyProducer() { return notifiyProducer; }
  public void setNotifiyProducer(boolean notifiyProducer)
  {
    this.notifiyProducer = notifiyProducer;
  }

  public boolean isNotifyConsumer() { return notifyConsumer; }
  public void setNotifyConsumer(boolean notifyConsumer)
  {
    this.notifyConsumer = notifyConsumer;
  }

  public String getReportTo() { return reportTo; }
  public void setReportTo(String reportTo) { this.reportTo = reportTo; }
}