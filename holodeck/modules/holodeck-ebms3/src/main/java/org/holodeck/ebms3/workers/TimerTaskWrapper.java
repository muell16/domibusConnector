package org.holodeck.ebms3.workers;

import java.util.*;

/**
 * @author Hamid Ben Malek
 */
public class TimerTaskWrapper extends TimerTask
{
  protected Runnable task;

  public TimerTaskWrapper(Runnable action)
  {
    this.task = action;
  }

    public void run()
  {
    if ( task != null ) task.run();
  }
}