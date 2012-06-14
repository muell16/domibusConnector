package org.holodeck.ebms3.workers.impl;


import org.holodeck.ebms3.submit.FileBasedSubmitter;

/**
 * @author Hamid Ben Malek
 */
public class SubmitWorker implements Runnable
{
  public void run()
  {
    FileBasedSubmitter.submitMsgFromFolders();
  }
}