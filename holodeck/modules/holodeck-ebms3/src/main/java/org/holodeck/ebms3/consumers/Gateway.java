package org.holodeck.ebms3.consumers;

import javax.servlet.http.HttpServlet;
import javax.servlet.*;
import java.util.*;
import java.io.*;

import org.apache.axis2.context.*;
import org.apache.axis2.transport.http.AxisServlet;
import org.holodeck.ebms3.module.MsgInfo;
import org.holodeck.ebms3.module.Constants;
import org.holodeck.ebms3.module.Configuration;

import org.simpleframework.xml.core.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Hamid Ben Malek
 */
public class Gateway extends HttpServlet implements EbConsumer
{
  private static final Log log =
                  LogFactory.getLog(Gateway.class.getName());

  public static GatewayConfig gatewayConfig;

  static ServletContext application = null;
  static ConfigurationContext configCtx = null;

  protected Map<String, String> parameters;

  public static ConfigurationContext getConfigurationContext()
  {
    if ( configCtx != null ) return configCtx;
    if ( application == null ) return null;
    configCtx =
      (ConfigurationContext)application.getAttribute(AxisServlet.CONFIGURATION_CONTEXT);
    return configCtx;
  }

  public void init(ServletConfig config) throws ServletException
  {
    application = config.getServletContext();
    try
    {
      Persister serializer = new Persister();
      String confFile = getGatewayConfigFile();
      if ( confFile == null || confFile.trim().equals("") ) return;
      File source = new File(confFile);
      if ( !source.exists() ) return;
      gatewayConfig = serializer.read(GatewayConfig.class, source);
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  public void push(MsgInfo msgInfo, MessageContext outMsgCtx)
  {
    if ( getGatewayConfig() == null )
    {
      log.info("Could not find the configuration file for the Gateway: gateway.xml");
      return;
    }
    Consumption c = getGatewayConfig().getMatchingConsumption(msgInfo);
    if ( c != null )
    {
      log.debug("Gateway found matching consumption");
      List<ConsumerInfo> consumers = c.getConsumers();
      if ( consumers != null && consumers.size() > 0 )
      {
        for (ConsumerInfo cons: consumers)
        {
          EbConsumer consumer = cons.createInstance();
          if ( consumer != null )
          {
            log.info("Gateway is about to call consumer.push()");
            consumer.push(msgInfo, outMsgCtx);
          }
        }
      }
    }
  }

  public void pull(MsgInfo msgInfo, MessageContext outMsgCtx)
  {
    if ( getGatewayConfig() == null ) return;
    Consumption c = getGatewayConfig().getMatchingConsumption(msgInfo);
    if ( c != null )
    {
      List<ConsumerInfo> consumers = c.getConsumers();
      if ( consumers != null && consumers.size() > 0 )
      {
        ConsumerInfo cons = consumers.get(0);
        EbConsumer consumer = cons.createInstance();
        if ( consumer != null )
        {
          log.info("Gateway is about to call consumer.pull()");
          consumer.pull(msgInfo, outMsgCtx);
        }
      }
    }
  }

  public void setParameters(Map<String, String> properties)
  {
    this.parameters = properties;
  }

  private static String getGatewayConfigFile()
  {
    File conf =
        Configuration.getFile(Constants.GATEWAY_CONFIG_FILE, "gateway.xml");
    if ( conf != null ) return conf.getAbsolutePath();
    else return null;
  }

  private static GatewayConfig getGatewayConfig()
  {
    if ( gatewayConfig != null ) return gatewayConfig;
    File source =
        Configuration.getFile(Constants.GATEWAY_CONFIG_FILE, "gateway.xml");
    //File source = new File(confFile);
    if ( !source.exists() ) return null;
    try
    {
      Persister serializer = new Persister();
      gatewayConfig = serializer.read(GatewayConfig.class, source);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return gatewayConfig;
  }
}