package org.holodeck.logging.module;

import org.apache.axis2.description.AxisModule;

/**
 * @author Hamid Ben Malek
 */
public class Constants
{

  public static final String MODULE_CLASS_LOADER = "RELIABILITY_MODULE_CLASS_LOADER";

  public static AxisModule module;


  public static DbStore store;

  public static void setAxisModule(AxisModule m) { module = m; }
  public static AxisModule getAxisModule() { return module; }

}