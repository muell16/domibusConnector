package org.holodeck.ebms3.submit;

import org.holodeck.ebms3.module.Constants;
import org.holodeck.ebms3.persistent.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * @author Hamid Ben Malek
 */
public class FileBasedSubmitter
{
  private static Log log =
                  LogFactory.getLog(FileBasedSubmitter.class.getName());

  public static synchronized void submitMsgFromFolders()
  {
    String submitFolder = Constants.getSubmitFolder();
    //log.debug("Submit_Messages folder is: " + submitFolder);
    if ( submitFolder == null ) return;
    File submitF = new File(submitFolder);
    File[] directories = submitF.listFiles();
    if ( directories == null || directories.length == 0 )
    {
      log.debug("did not find any in message folders ...");
      return;
    }
    //log.debug("Found " + directories.length + " message folders");
    for (File dir : directories)
    {
      if ( dir.isDirectory() ) submitFromFolder(dir);
    }
  }

  public static synchronized void submitFromFolder(File folder)
  {
    if ( folder == null || !folder.exists() ) return;
    MsgInfoSet mis = null;
    try { mis = readMeta(folder); }
    catch(Exception ex) { log.debug(ex.getMessage()); return; }
    if ( mis == null )
    {
      //log.debug("No metadata.xml found in message folder " + folder.getName());
      return;
    }
    String bodyPayload = mis.getBodyPayload();

    log.debug("[ SubmitWorker ] is scanning message folder " + folder.getName());
    log.debug("body payload is " + bodyPayload);
    File[] files = folder.listFiles();
    if ( files == null || files.length == 0 )
    {
      log.debug("returning because no files found");
      return;
    }
    int type = SubmitUtil.msgCategory(mis);
    if ( type == SubmitUtil.TO_BE_PUSHED )
    {
      UserMsgToPush msg = new UserMsgToPush(folder, mis);
      Constants.store.save(msg);
      log.debug("UserMsgToPush was submitted to database");
    }
    else if ( type == SubmitUtil.TO_BE_PULLED )
    {
      UserMsgToPull msg = new UserMsgToPull(folder, mis);
      Constants.store.save(msg);
      log.debug("A UserMsgToPull message was submitted (i.e: saved to DB)");
    }
    else if ( type == SubmitUtil.TO_BE_SYNC_RESPONSE )
    {
      SyncResponse msg = new SyncResponse(folder, mis);
      Constants.store.save(msg);
      log.debug("A SyncResponse message was submitted (i.e: saved to DB)");
    }
  }

  private static boolean renameMetadata(File folder)
  {
    if ( folder == null ) return false;
    File meta =
      new File(folder.getAbsolutePath() + File.separator + "metadata.xml");
    File metaRenamed =
      new File(folder.getAbsolutePath() + File.separator +
               "metadata.xml.processed");
    return meta.renameTo(metaRenamed);
  }

  private static synchronized MsgInfoSet readMeta(File folder)
  {
    if ( folder == null || !folder.exists() ) return null;
    File meta =
      new File(folder.getAbsolutePath() + File.separator + "metadata.xml");
    if ( !meta.exists() ) return null;
    renameMetadata(folder);
    meta =
      new File(folder.getAbsolutePath() + File.separator + "metadata.xml.processed");

    MsgInfoSet mis = MsgInfoSet.read(meta);
    mis.setLegNumber( SubmitUtil.getLegNumber(mis) );  
    return mis;
  }
}