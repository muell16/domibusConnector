package org.holodeck.ebms3.workers;

import java.util.*;
import java.io.*;

/**
 * @author Hamid Ben Malek
 */
public abstract class DirWatcher extends TimerTask implements FileFilter
{
  protected String path;
  protected File[] filesArray;
  protected Map<File, Long> dir = new HashMap<File, Long>();

  public DirWatcher() {}
  public DirWatcher(String path)
  {
    setPath(path);
  }

  public void setPath(String path)
  {
    this.path = path;
    filesArray = new File(path).listFiles(this);

    // transfer to the hashmap be used a reference and keep the
    // lastModfied value
    for (File aFilesArray : filesArray)
    {
      dir.put(aFilesArray, aFilesArray.lastModified());
    }
  }

  public final void run()
  {
    filesArray = new File(path).listFiles(this);

    // scan the files and check for modification/addition
    for (File aFilesArray : filesArray)
    {
      Long current = dir.get(aFilesArray);
      if (current == null)
      {
        // new file
        dir.put(aFilesArray, aFilesArray.lastModified());
        onChange(aFilesArray, "add");
      }
      else if (current != aFilesArray.lastModified())
      {
        // modified file
        dir.put(aFilesArray, aFilesArray.lastModified());
        onChange(aFilesArray, "modify");
      }
    }
  }

  public void watch(long period)
  {
    Timer timer = new Timer();
    timer.schedule( this , period, period );
  }

  protected abstract void onChange( File file, String action );
  public abstract boolean accept(File file);
}