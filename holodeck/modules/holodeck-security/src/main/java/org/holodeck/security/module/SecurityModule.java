package org.holodeck.security.module;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.modules.Module;
import org.apache.neethi.Policy;
import org.apache.neethi.Assertion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Hamid Ben Malek
 */
public class SecurityModule implements Module
{
  private Log log = LogFactory.getLog(SecurityModule.class);


  public void init(ConfigurationContext configContext, AxisModule module)
              throws AxisFault
  {
    if ( log.isDebugEnabled() ) log.debug("initialization..., ");
    SecurityUtil.configContext = configContext;
    SecurityUtil.module = module;
    Configuration.loadPolicies();
    log.debug("Holodeck-Security module started");
  }
    

  public void engageNotify(AxisDescription axisDescription) throws AxisFault
  {
  }
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
}