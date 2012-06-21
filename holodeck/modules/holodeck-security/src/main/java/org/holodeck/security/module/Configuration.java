package org.holodeck.security.module;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.log4j.Logger;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.holodeck.security.model.Security;
import org.holodeck.security.model.SecurityConfig;

/**
 * @author Hamid Ben Malek
 */
public class Configuration extends SecurityUtil
{
//  private static Log log = LogFactory.getLog(Configuration.class);
  private static Logger log = Logger.getLogger(Configuration.class);

  protected static SecurityConfig securityConfig;
  protected static Map<String, Policy> initiatorPolicies;
  protected static Map<String, Policy> recipientPolicies;

  public static void loadPolicies()
  {
    String configFile = getSecurityConfig();
    securityConfig = SecurityConfig.load(new File(configFile));
    log.debug("Security Config file " + configFile + " has been loaded");
    File pDir = new File(getPoliciesFolder());
    log.debug("Loading policies from directory: " + pDir.getAbsolutePath());
    File[] files = pDir.listFiles();
    initiatorPolicies = new HashMap<String, Policy>();
    recipientPolicies = new HashMap<String, Policy>();
    if ( files != null && files.length > 0 )
    {
      for (File file : files)
      {
        if ( !file.isDirectory() && file.getName().endsWith(".xml") )
        {
          Policy iPolicy = loadPolicy(file.getAbsolutePath());
          Policy rPolicy = loadPolicy(file.getAbsolutePath());
          initiatorPolicies.put(file.getName(), iPolicy);
          recipientPolicies.put(file.getName(), rPolicy);
          log.debug("loaded policy file " + file.getName());
        }
      }
    }
    init();
    log.debug("Done with loading policies.");
  }

  public static Security getSecurity(String name)
  {
    if ( name == null || name.trim().equals("") ) return null;
    if ( securityConfig == null ) return null;
    return securityConfig.getSecurity(name);
  }

  public static Policy getInitiatorPolicy(String securityName)
  {
    Security sec = getSecurity(securityName);
    if ( sec == null ) return null;
    return sec.getInitiatorPolicy();
  }

  public static Policy getRecipientPolicy(String securityName)
  {
    Security sec = getSecurity(securityName);
    if ( sec == null ) return null;
    return sec.getRecipientPolicy();
  }

  private static Policy loadPolicy(String policyFile)
  {
    if ( policyFile == null || policyFile.trim().equals("") ) return null;
    Policy policy = null;
    try
    {
      StAXOMBuilder builder = new StAXOMBuilder(policyFile);
      policy = PolicyEngine.getPolicy(builder.getDocumentElement());
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return policy;
  }

  private static void init()
  {
    if ( securityConfig == null ) return;
    for ( Security sec : securityConfig.getSecurities() )
    {
      Policy policy = initiatorPolicies.get( sec.getPolicyFile() );
      sec.setInitiatorPolicy(policy);

      Policy rPolicy = recipientPolicies.get( sec.getPolicyFile() );
      sec.setRecipientPolicy(rPolicy);
    }
  }
}