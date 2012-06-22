package org.holodeck.ebms3.workers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * @author Hamid Ben Malek
 */
@Root(name="Workers")
public class WorkerPool extends FileWatcher implements java.io.Serializable
{
  private static final long serialVersionUID = -5593318201928374737L;

//  private static final Log log = LogFactory.getLog(WorkerPool.class.getName());
  private static final Logger log = Logger.getLogger(WorkerPool.class.getName());

  @ElementList(entry="Worker", inline=true, required=false)
  protected List<Worker> workers = new ArrayList<Worker>();

  public WorkerPool() {}

  public List<Worker> getWorkers() { return workers; }
  public void setWorkers(List<Worker> workers)
  {
    this.workers = workers;
  }

  public void addWorker(String name, String workerClass, boolean activate)
  {
    if ( name == null || workerClass == null ) return;
    Worker w = getWorker(name);
    if ( w == null )
    {
      w = new Worker();
      w.setName(name);
      w.setWorkerClass(workerClass);
      w.setActivate(activate);
      workers.add(w);
      if ( activate ) w.start();
    }
    else
    {
      w.setWorkerClass(workerClass);
      boolean act = w.isActivate();
      w.setActivate(activate);
      if ( !act && activate ) w.start();
    }
  }

  public Worker getWorker(String name)
  {
    if ( name == null || name.trim().equals("") ) return null;
    for ( Worker w : workers )
    {
      if ( w.getName().equals(name) ) return w;
    }
    return null;
  }

  public static WorkerPool load(File sourceFile)
  {
    if ( sourceFile == null || !sourceFile.exists() )
    {
      log.debug("Could not load file " + sourceFile +
                "  because it is either null or does not exists");
      return null;
    }
    WorkerPool pool = null;
    try
    {
      Serializer serializer = new Persister();
      pool = serializer.read(WorkerPool.class, sourceFile);
      pool.setSource(sourceFile);
      //log.debug("loaded workers from file " + sourceFile);
    }
    catch(Exception ex) { ex.printStackTrace(); }
    return pool;
  }

  public void start()
  {
    if ( workers != null && workers.size() > 0 )
    {
      for (Worker w : workers)
      {
        if ( w.isActivate() )
        {
          w.start();
          log.debug("started worker " + w.getName());
        }
      }
    }
  }

  public void reload(File source)
  {
    WorkerPool pool = WorkerPool.load(source);
    if ( pool == null ) return;
    for ( Worker w : pool.getWorkers() )
    {
      Worker worker = getWorker(w.getName());
      if ( worker != null ) worker.update(w);
      else
      {
        workers.add(w);
        if ( w.isActivate() ) w.start();
      }
    }
  }

  public void onChange(File s)
  {
    //log.debug("workers file " + s + "  has changed. Reloading it...");
    reload(s);
  }
}