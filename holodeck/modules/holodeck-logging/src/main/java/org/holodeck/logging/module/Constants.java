package org.holodeck.logging.module;

import org.apache.axis2.description.AxisModule;

/**
 * Class for storing some objects you need in the whole module
 * 
 * @author Stefan Mueller
 * @author Tim Nowosadtko
 * @date 07-13-2012
 */
public class Constants
{

  public static final String MODULE_CLASS_LOADER = "RELIABILITY_MODULE_CLASS_LOADER";

  public static AxisModule module;


  public static DbStore store;

  public static void setAxisModule(AxisModule m) { module = m; }
  public static AxisModule getAxisModule() { return module; }

}