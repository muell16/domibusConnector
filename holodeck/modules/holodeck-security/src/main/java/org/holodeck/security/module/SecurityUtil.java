package org.holodeck.security.module;


import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;

import java.io.File;

/**
 * @author Hamid Ben Malek
 */
public class SecurityUtil
{
  public static final String SECURITY_CONFIG_FILE = "SecurityConfigFile";
  public static final String POLICIES_FOLDER = "PoliciesFolder";
  public static final String KEYS_FOLDER = "KeysFolder";  

  public static final String SECURITY = "SECURITY";

  public static ConfigurationContext configContext;
  public static AxisModule module;
    
  public static String getSecurityConfig()
  {
    if ( configContext == null ) return null;
    Parameter secConfParam = module.getParameter(SECURITY_CONFIG_FILE);
    String securityConfigFile = null;
    if ( secConfParam != null )
    {
      securityConfigFile = (String)secConfParam.getValue(); 
      if ( securityConfigFile != null && new File(securityConfigFile).exists() )
           return securityConfigFile;
    }
    if ( securityConfigFile == null || securityConfigFile.trim().equals("") )
         securityConfigFile = "security-config.xml";
    File baseFolder = configContext.getRealPath("/modules/" + module.getName());
    securityConfigFile = baseFolder.getAbsolutePath() +
                            File.separator + securityConfigFile;
    return securityConfigFile;
  }

  public static String getPoliciesFolder()
  {
    if ( configContext == null ) return null;
    File baseFolder = configContext.getRealPath("/modules/" + module.getName());
    String dir = baseFolder.getAbsolutePath() + File.separator + "policies";
    Parameter policiesFolderParam = module.getParameter(POLICIES_FOLDER);
    if ( policiesFolderParam == null ) return dir;
    String policiesFolder = (String)policiesFolderParam.getValue();
    if ( policiesFolder == null || policiesFolder.trim().equals("") )
         return dir;
    if ( new File(policiesFolder).exists() ) return policiesFolder;
    
    policiesFolder = baseFolder.getAbsolutePath() +
                            File.separator + policiesFolder;
    File sub = new File(policiesFolder);
    if ( !sub.exists() ) sub.mkdirs();
    return policiesFolder;
  }

  public static String getKeysFolder()
  {
    if ( configContext == null ) return null;
    File baseFolder = configContext.getRealPath("/modules/" + module.getName());
    String dir = baseFolder.getAbsolutePath() + File.separator + "keys";
    Parameter keysFolderParam = module.getParameter(KEYS_FOLDER);
    if ( keysFolderParam == null ) return dir;
    String keysFolder = (String)keysFolderParam.getValue();
    if ( keysFolder == null || keysFolder.trim().equals("") )
         return dir;
    if ( new File(keysFolder).exists() ) return keysFolder;

    keysFolder =
       baseFolder.getAbsolutePath() + File.separator + keysFolder;
    File sub = new File(keysFolder);
    if ( !sub.exists() ) sub.mkdirs();
    return keysFolder;
  }
}