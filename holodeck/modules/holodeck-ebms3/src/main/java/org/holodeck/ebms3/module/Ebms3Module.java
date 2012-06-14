package org.holodeck.ebms3.module;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.axis2.AxisFault;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.modules.Module;
import org.apache.neethi.Policy;
import org.apache.neethi.Assertion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import org.holodeck.ebms3.submit.SubmitWorker;
import org.holodeck.ebms3.workers.WorkerPool;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * @author Hamid Ben Malek
 */
public class Ebms3Module  implements Module
{
  private Log log = LogFactory.getLog(Ebms3Module.class);
  //protected SubmitWorker submitWorker;
  //protected SenderWorker sender;
  protected List<PullWorker> pullers = new ArrayList<PullWorker>();
  protected BrokerService broker;

  public void init(ConfigurationContext configContext, AxisModule module)
              throws AxisFault
  {
    if ( log.isDebugEnabled() ) log.debug("initialization..., ");
    Constants.configContext = configContext;
    Constants.getAttachmentDir();

    AxisConfiguration config = configContext.getAxisConfiguration();

    Parameter parameter =
      new Parameter(Constants.MODULE_CLASS_LOADER, module.getModuleClassLoader());
    config.addParameter(parameter);

    //storing the module as a static variable
    Constants.setAxisModule(module);
/*
    log.debug("Loading PModes...");
    try { Configuration.loadPModes(); }
    catch(Exception ex) { ex.printStackTrace(); }
    log.debug("All PModes have been loaded.");
*/
    readModules(config, module);
    readOtherParameters(config, module);
    Constants.store = loadDataStore(config, module);
    /*
    submitWorker = new SubmitWorker();
    submitWorker.start();
    log.debug("SubmitWorker started");
    sender = new SenderWorker();
    sender.start();
    log.debug("SenderWorker started");
    */
    startActiveMQ();
    log.debug("ActiveMQ Broker Started.");
    log.debug("Loading Workers...");
    loadWorkers();
    log.debug("Done loading Workers");
    log.debug("ebms3 Module Started.");
  }

  // shutdown the module
  public void shutdown(ConfigurationContext configCtx)
              throws AxisFault
  {
    //submitWorker.terminate();
    //sender.terminate();
    try
    {
      broker.stop();
      if ( pullers != null && pullers.size() > 0 )
      {
        for (PullWorker puller : pullers) puller.terminate();
      }
      Constants.store.close();
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  private void readModules(AxisConfiguration config, AxisModule module)
  {
    Parameter modulesParam = module.getParameter("Modules");
    if ( modulesParam == null ) return;
    String mod = (String)modulesParam.getValue();
    try
    {
      Parameter modParam = new Parameter(Constants.MODULES, mod);
      config.addParameter(modParam);
      Constants.engagedModules = EbUtil.parseModules(mod);
    }
    catch(Exception e) { e.printStackTrace(); }
  }

  private void readOtherParameters(AxisConfiguration config,
                                   AxisModule module)
  {
    Parameter gatewayConfParam = module.getParameter("GatewayConfigFile");
    Parameter submitFolderParam =
            module.getParameter("SubmittedMessagesFolder");
    Parameter receivedFolderParam =
            module.getParameter("ReceivedMessagesFolder");
    Parameter localMachineParam = module.getParameter("LocalMachine");
    Parameter storeAttachmentsInDBParam =
            module.getParameter("StoreAttachmentsInDB");
    try
    {
      if ( gatewayConfParam != null )
      {
        Parameter gatewayParam =
            new Parameter(Constants.GATEWAY_CONFIG_FILE,
                          gatewayConfParam.getValue());
        config.addParameter(gatewayParam);
      }
      if ( submitFolderParam != null )
      {
        Parameter submitParam =
             new Parameter(Constants.SUBMITTED_MESSAGES_FOLDER,
                           submitFolderParam.getValue());
        config.addParameter(submitParam);
      }
      if ( receivedFolderParam != null )
      {
        Parameter receivedParam =
             new Parameter(Constants.RECEIVED_MESSAGES_FOLDER,
                           receivedFolderParam.getValue());
        config.addParameter(receivedParam);
      }
      if ( localMachineParam != null )
      {
        Parameter localParam =
                     new Parameter(Constants.LOCAL_MACHINE,
                                   localMachineParam.getValue());
        config.addParameter(localParam);
      }
      if ( storeAttachmentsInDBParam != null )
      {
        String storeInDB = (String)storeAttachmentsInDBParam.getValue();
        Parameter saidbParam =
                     new Parameter(Constants.STORE_ATTACHMENTS_IN_DB,
                     storeInDB);
        config.addParameter(saidbParam);
        if ( storeInDB != null && storeInDB.equalsIgnoreCase("true") )
             Constants.attachmentsInDB = true;
        else Constants.attachmentsInDB = false;
      }
    }
    catch(Exception e) { e.printStackTrace(); }
  }

 /**
  * construct a database store manager (dbAdapter) object and store it
  * as a parameter in AxisConfiguration ... so that other handlers and
  * any module can access it:
  */
  private DbStore loadDataStore(AxisConfiguration config, AxisModule module)
  {
    Parameter pUnitParam = module.getParameter("PersistenceUnit");
    String pUnit = "ebms3-mysql";
    if (pUnitParam != null)
    {
      pUnit = (String)pUnitParam.getValue();
      log.debug("Using PersistenceUnit : " + pUnitParam.getValue());
    }
    DbStore store = new DbStore(pUnit);
    try
    {
      Parameter storeParam = new Parameter(Constants.STORE, store);
      config.addParameter(storeParam);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return store;
  }

  private void loadWorkers()
  {
    log.debug("workers file is: " + Constants.getWorkersFile());
    WorkerPool pool = WorkerPool.load(Constants.getWorkersFile());
    Constants.workerPool = pool;
    if ( pool == null )
         log.debug("Could not load workers from file " +
                    Constants.getWorkersFile());
    else
    { 
      pool.start();
      pool.watch(30000);
      log.debug("started the workers pool");
    }
  }

  private void startActiveMQ()
  {
    try
    {
      broker = BrokerFactory.createBroker("xbean:activemq.xml");
      broker.start();
      broker.waitUntilStarted();
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }

  public void engageNotify(AxisDescription axisDescription) throws AxisFault
  {
  }
  public String[] getPolicyNamespaces() { return null; }
  public void applyPolicy(Policy policy, AxisDescription axisDescription)
              throws AxisFault
  {
  }
  public boolean canSupportAssertion(Assertion assertion) { return true; }
}