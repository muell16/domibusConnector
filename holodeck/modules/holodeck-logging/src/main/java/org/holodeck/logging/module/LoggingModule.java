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

/**
 * This Class represents the starting point of this module and is used to initialize the database connection
 * 
 * @author Stefan Mueller
 * @author Tim Nowosadtko
 * @date 07-13-2012
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

    
//    DbStore dbs = new DbStore("logging-mysql");
//    Constants.store = dbs;
    
    try
    {
      
    }
    catch(Exception e) { e.printStackTrace(); log.error(e.getMessage()); }
    Constants.store = loadDataStore(config, module);

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
    String pUnit = "logging-mysql";
    if (pUnitParam != null)
    {
      pUnit = (String)pUnitParam.getValue();
      log.debug("Using PersistenceUnit : " + pUnitParam.getValue());
    }
    DbStore store = new DbStore(pUnit);
    return store;
  }
}