package org.holodeck.reliability.module;

import org.apache.axis2.AxisFault;
//import org.apache.axis2.receivers.RawXMLINOutMessageReceiver;
//import org.apache.axis2.receivers.RawXMLINOnlyMessageReceiver;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;
//import org.apache.axis2.description.AxisService;
import org.apache.axis2.modules.Module;
import org.apache.neethi.Policy;
import org.apache.neethi.Assertion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Hamid Ben Malek
 */
public class ReliabilityModule  implements Module
{
  private Log log = LogFactory.getLog(ReliabilityModule.class);

  public void init(ConfigurationContext configContext, AxisModule module)
              throws AxisFault
  {
    if ( log.isDebugEnabled() ) log.debug("initialization..., ");
    //Thread.currentThread().setContextClassLoader(module.getModuleClassLoader());
    Constants.configContext = configContext;
    AxisConfiguration config = configContext.getAxisConfiguration();

    //storing the Reliability module as a parameter.
    Parameter parameter =
      new Parameter(Constants.MODULE_CLASS_LOADER, module.getModuleClassLoader());
    config.addParameter(parameter);
    //storing the module as a static variable
    Constants.setAxisModule(module);

    readStorageFolder(config, module);

    try
    {
      Configuration conf = new Configuration();
      conf.loadReliabilityConfig();
    }
    catch(Exception e) { e.printStackTrace(); log.error(e.getMessage()); }
/*
    Parameter qualityParam = module.getParameter("Quality");
    //log.debug("==== Got the Quality parameter ====");
    OMElement quality = qualityParam.getParameterElement();
    Iterator it = quality.getChildElements();
    Reliability reliability = null;
    while ( it != null && it.hasNext() )
    {
      OMElement relOMElement = (OMElement)it.next();
      if ( relOMElement.getLocalName().equals("Reliability") )
      {
        reliability = new Reliability(relOMElement);
        Parameter param = new Parameter(reliability.getName(), reliability);
        config.addParameter(param);
        log.debug("Added quality " + reliability.getName() + " to configContext");
      }
    }
*/
    Constants.store = loadDataStore(config, module);

    // Create a ScheduledExecutor and store it in the config:
    //ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
    //Parameter execParam = new Parameter(Constants.SCHEDULED_EXECUTOR, executor);
    //config.addParameter(execParam);

    startResendTasks(configContext, module);

    // start OrderedDeliveryWorker:
    OrderedDeliveryWorker orderedDeliveryWorker =
                                new OrderedDeliveryWorker(configContext);
    orderedDeliveryWorker.start();
    Constants.orderedDeliveryWorker = orderedDeliveryWorker;

    // Start CallbackAckWorker:
    CallbackAckWorker callbackAckWorker =
                      new CallbackAckWorker(configContext);
    callbackAckWorker.start();
    Constants.callbackAckWorker = callbackAckWorker;

    // start poll request sender worker:
    PollReqSenderWorker pollReqSenderWorker =
                      new PollReqSenderWorker(configContext);
    pollReqSenderWorker.start();
    Constants.pollReqSenderWorker = pollReqSenderWorker;

    // start CleanupWorker:
    CleanupWorker cleanupWorker = new CleanupWorker(configContext);
    cleanupWorker.start();
    Constants.cleanupWorker = cleanupWorker;

    // Create a dumb service to listen for Callbacks:
    //createDumbService(config, module);
    log.debug("Reliability Module Started.");
  }

  public void engageNotify(AxisDescription axisDescription) throws AxisFault
  {
  }

  // shutdown the module
  public void shutdown(ConfigurationContext configCtx)
              throws AxisFault
  {
    //ScheduledThreadPoolExecutor executor =
    //      (ScheduledThreadPoolExecutor)configCtx.getAxisConfiguration()
    //                              .getParameter(Constants.SCHEDULED_EXECUTOR)
    //                              .getValue();
    //if ( executor != null ) executor.shutdownNow();
  }

  public String[] getPolicyNamespaces() { return null; }

  public void applyPolicy(Policy policy, AxisDescription axisDescription)
              throws AxisFault
  {
  }

  public boolean canSupportAssertion(Assertion assertion) { return true; }

  private void readStorageFolder(AxisConfiguration config,
                                 AxisModule module)
  {
    try
    {
      Parameter storageFolderParam =
               module.getParameter("MessageStorageFolder");
      String folder = "Storage_Folder";
      if ( storageFolderParam != null )
           folder = (String)storageFolderParam.getValue();
      Parameter storageParam =
             new Parameter(Constants.MESSAGE_STORAGE_FOLDER, folder);
      config.addParameter(storageParam);
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

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
//    DbStore store = null;
//    try
//    {
//      Class db =
//       Class.forName("org.holodeck.reliability.module.DbStore",
//                     true, module.getModuleClassLoader());
//      store = (DbStore)db.newInstance();
//      store.setPersistenceUnit(pUnit);      
//    }
//    catch(Exception e) { e.printStackTrace(); }
    DbStore store = new DbStore(pUnit);
    try
    {
      Parameter storeParam = new Parameter(Constants.STORE, store);
      config.addParameter(storeParam);
    }
    catch(Exception ex) { ex.printStackTrace(); }
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
        SenderWorker senderWorker = new SenderWorker(configContext);
        Constants.senderWorker = senderWorker;
        senderWorker.start();
        /*  
        List gmessages = store.getMessagesToResend();
        if ( gmessages != null && gmessages.size() > 0 )
        {
          GMessage g = null;
          for (Object gm : gmessages)
          {
            g = (GMessage)gm;
            if ( !g.isExpired() )
                 new SenderWorker( g.getMessageContext(configContext) );
          }
        }
        */
      }
    }
  }

  /*
  private void createDumbService(AxisConfiguration config, AxisModule module)
  {
    try
    {
      AxisService service =
          AxisService.createService(EmptyService.class.getName(),
                                    config, RawXMLINOnlyMessageReceiver.class);
      service.addModuleref(module.getName());
      service.setName("wsrm");
      service.engageModule(module, config);
      config.addService(service);
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }
  */
}