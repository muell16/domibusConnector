package org.holodeck.reliability.module;

import java.util.*;
import java.io.*;

/**
 * @author Hamid Ben Malek
 */
public abstract class FileWatcher extends TimerTask
{
  protected long timeStamp;
  protected File source;

  public FileWatcher() {}
  public FileWatcher(File file)
  {
    source = file;
    timeStamp = file.lastModified();
  }

  public File getSource() { return source; }
  public void setSource(File s) { source = s; }

  public void watch(long period)
  {
    Timer timer = new Timer();
    timer.schedule( this , new Date(), period );
  }

  public final void run()
  {
    long ts = source.lastModified();

    if ( timeStamp != ts )
    {
      timeStamp = ts;
      onChange(source);
    }
  }

  protected abstract void onChange(File file);
}