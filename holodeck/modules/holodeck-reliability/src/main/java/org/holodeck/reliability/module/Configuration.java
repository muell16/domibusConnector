package org.holodeck.reliability.module;

import org.apache.axis2.description.Parameter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.holodeck.reliability.config.*;

import java.io.File;
import java.util.List;

/**
 * @author Hamid Ben Malek
 */
public class Configuration extends FileWatcher
{
  private static Log log = LogFactory.getLog(Configuration.class);

  public Configuration()
  {
    source = getReliabilityConfig();
    if ( source != null ) timeStamp = source.lastModified();
  }

  public void loadReliabilityConfig() throws Exception
  {
    if ( source == null )
    {
      log.error("Could not locate reliability-config.xml file");
      return;
    }
    load( source );
    watch(30000);
  }

  public void load(File conf) throws Exception
  {
    if ( conf == null ) return;
    log.debug("Loading reliability config file: " + conf.getAbsolutePath());
    ReliabilityConfig reliabilityConfig = ReliabilityConfig.load(conf);
    List<Reliability> reliabilities = reliabilityConfig.getReliabilities();
    if ( reliabilities != null && reliabilities.size() > 0 )
    {
      for (Reliability reliability : reliabilities)
      {
        Parameter param =
                Constants.configContext.getAxisConfiguration()
                         .getParameter(reliability.getName());
        if ( param == null )
        {
          param = new Parameter(reliability.getName(), reliability);
          Constants.configContext.getAxisConfiguration().addParameter(param);
        }
        else param.setValue(reliability);

        log.debug("Added quality " + reliability.getName() + " to configContext");
      }
    }
    log.debug("Done with loading reliability qualities.");
  }

  public File getReliabilityConfig()
  {
    return getFile(Constants.RELIABILITY_CONFIG_FILE, "reliability-config.xml");
  }

  public static File getFile(String parameterName, String defaultName)
  {
    if ( Constants.module == null ) return null;
    Parameter param = Constants.module.getParameter(parameterName);
    File base = Constants.configContext.getRealPath("/modules/" +
                Constants.module.getName());
    String file = base.getAbsolutePath() + File.separator + defaultName;
    if ( param == null ) return new File(file);
    String wFile = (String)param.getValue();
    File res = new File(wFile);
    if ( res.exists() ) return res;
    else res =
        new File( base.getAbsolutePath() + File.separator + wFile );

    return res;
  }

  public void onChange(File s)
  {
    try { load(s); }
    catch(Exception ex) { ex.printStackTrace(); }
  }
}