package org.holodeck.logging.module;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.modules.Module;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;
import org.holodeck.logging.persistent.LoggerMessage;

/**
 * @author Hamid Ben Malek
 */
public class LoggingModule  implements Module
{
  private Log log = LogFactory.getLog(LoggingModule.class);

  public void init(ConfigurationContext configContext, AxisModule module)
              throws AxisFault
  {
    if ( log.isDebugEnabled() ) log.debug("initialization..., ");
    AxisConfiguration config = configContext.getAxisConfiguration();

    //storing the Reliability module as a parameter.
    Parameter parameter =
      new Parameter(Constants.MODULE_CLASS_LOADER, module.getModuleClassLoader());
    config.addParameter(parameter);
    //storing the module as a static variable
    Constants.setAxisModule(module);

//    System.out.println("Es soll etwas in die Datenbank geschrieben werden");
    
    DbStore dbs = new DbStore("logging-mysql");
    //dbs.save(new LogEvent("Nachricht", "Absender", "Empfaenger", "Gateway"));
//    dbs.save(new LoggerMessage("msgid", "sender", "fromRole", "recipient", "toRole", "service", "action", "converationid", "pmode"));
    Constants.store = dbs;
    
    try
    {
      
    }
    catch(Exception e) { e.printStackTrace(); log.error(e.getMessage()); }
    Constants.store = loadDataStore(config, module);

    startResendTasks(configContext, module);


    log.debug("Logging Module Started.");
  }

  public void engageNotify(AxisDescription axisDescription) throws AxisFault
  {
  }

  // shutdown the module
  public void shutdown(ConfigurationContext configCtx)
              throws AxisFault
  {
  }

  public String[] getPolicyNamespaces() { return null; }

  public void applyPolicy(Policy policy, AxisDescription axisDescription)
              throws AxisFault
  {
  }

  public boolean canSupportAssertion(Assertion assertion) { return true; }

 /**
  * construct a database store manager (dbAdapter) object and store it
  * as a parameter in AxisConfiguration ... so that other handlers and
  * any module can access it:
  */
  private DbStore loadDataStore(AxisConfiguration config, AxisModule module)
  {
    Parameter pUnitParam = module.getParameter("PersistenceUnit");
    String pUnit = "wsrm-mysql";
    if (pUnitParam != null)
    {
      pUnit = (String)pUnitParam.getValue();
      log.debug("Using PersistenceUnit : " + pUnitParam.getValue());
    }
    DbStore store = new DbStore(pUnit);
    return store;
  }

 /**
  *  If the parameter "ResendFromDBAtStartup" is true, load
  *  GMessages that are not expired, not yet acknowledged and
  *  their resendCount &lt; maximumRetransmissionCount and add
  *  them to resender worker queue.
  */
  private void startResendTasks(ConfigurationContext configContext,
                                AxisModule module)
  {
    Parameter resendFromDbParam = module.getParameter("ResendFromDBAtStartup");
    if (resendFromDbParam != null)
    {
      String val = (String)resendFromDbParam.getValue();
      if ( val != null && val.trim().equalsIgnoreCase("true") )
      {
       
      }
    }
  }
}