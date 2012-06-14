package org.holodeck.ebms3.workers;

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
    this.source = file;
    this.timeStamp = file.lastModified();
  }

  public File getSource() { return source; }
  public void setSource(File source) { this.source = source; }

  public void watch(long period)
  {
    Timer timer = new Timer();
    timer.schedule( this , new Date(), period );
  }

  public final void run()
  {
    long timeStamp = source.lastModified();

    if ( this.timeStamp != timeStamp )
    {
      this.timeStamp = timeStamp;
      onChange(source);
    }
  }

  protected abstract void onChange(File file);
}