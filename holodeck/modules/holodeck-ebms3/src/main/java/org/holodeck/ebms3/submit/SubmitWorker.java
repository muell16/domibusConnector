package org.holodeck.ebms3.submit;

//import java.util.concurrent.*;

import org.holodeck.ebms3.module.PeriodicWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Hamid Ben Malek
 */
public class SubmitWorker extends PeriodicWorker //implements Runnable
{
  private static final Log log =
                  LogFactory.getLog(SubmitWorker.class.getName());

  public void init()
  {
    //Constants.executor.scheduleWithFixedDelay(this, 10, 15, TimeUnit.SECONDS);
  }

  protected void task()
  {
    FileBasedSubmitter.submitMsgFromFolders();
  }
  /*
  public void run()
  {
    //System.out.println("=========== SubmitWorker is running now...");
    //log.debug("=========== SubmitWorker is running now...");
    FileBasedSubmitter.submitMsgFromFolders();
  }
  */
}
