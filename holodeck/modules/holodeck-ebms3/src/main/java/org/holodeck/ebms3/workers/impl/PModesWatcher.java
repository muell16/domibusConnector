package org.holodeck.ebms3.workers.impl;

import org.holodeck.ebms3.module.Configuration;
import org.holodeck.ebms3.config.PModePool;
import org.holodeck.ebms3.workers.DirWatcher;
import org.holodeck.ebms3.workers.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Map;

/**
 * @author Hamid Ben Malek
 */
public class PModesWatcher extends DirWatcher implements Task
{
  private Log log = LogFactory.getLog(PModesWatcher.class);

  protected File pmodesDir = null;

  public PModesWatcher()
  {
    setPath( Configuration.getPModesDir() );
  }

  protected void onChange( File file, String action )
  {
    if ( file == null || action == null ) return;
    PModePool pool = PModePool.load(file);
    if ( pool != null ) Configuration.addPModePool(pool);
  }

  public boolean accept(File file)
  {
    return !file.isDirectory() && file.getName().endsWith(".xml");
  }

  public void setParameters(Map<String, String> parameters)
  {
    if ( parameters == null ) return;
    String dir = parameters.get("pmodesDir");
    pmodesDir = Configuration.getRealPath(dir);
    if ( pmodesDir != null ) setPath(pmodesDir.getAbsolutePath());
    else setPath( Configuration.getPModesDir() );
    
    log.debug("Loading PModes...");
    for (File file : filesArray)
    {
      onChange(file, "New");
    }
    log.debug("All PModes have been loaded.");
  }
}