package org.holodeck.ebms3.module;

/**
 * @author Hamid Ben Malek
 */
public abstract class PeriodicWorker implements Runnable
{
  protected static final int ONE_SECOND = 1000;
  protected static final int DEFAULT_INTERVAL = 40;
  protected int timeInterval = 40;
  protected Thread currentThread = null;
  protected boolean running = true;

  public PeriodicWorker() {}
  public PeriodicWorker(String timeInSeconds) { setTimeInterval(timeInSeconds); }
  public PeriodicWorker(int time) { timeInterval = time; }

  protected void setTimeInterval(String timeInMinutes)
  {
    try { timeInterval = Integer.parseInt(timeInMinutes); }
    catch (Exception e) { timeInterval = DEFAULT_INTERVAL; }
  }

  public void start()
  {
    try
    {
      if ( currentThread == null ) currentThread = new Thread(this);
      running = true;
      currentThread.start();
    }
    catch (Exception e) { e.printStackTrace(); }
  }

  public void stop() { running = false; }
  public void terminate()
  {
    stop();
    currentThread = null;
  }

  public void run()
  {
    Thread thisThread = Thread.currentThread();
    try
    {
      while (thisThread == currentThread && currentThread != null && running )
      {
        task();
        Thread.sleep(ONE_SECOND * (timeInterval));
      }
    }
    catch (Exception e) { e.printStackTrace(); run(); }
  }

  protected abstract void task();
}