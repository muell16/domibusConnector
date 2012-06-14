package org.holodeck.ebms3.tests;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.ElementList;

import java.util.*;

/**
 * Testing object cyling and refrences in XML
 * serialization with Simple framework.
 */
@Root
public class Job
{
  @ElementList(entry="workspace", inline=true)
  protected List<Workspace> workspaces = new ArrayList<Workspace>();

  @ElementList(entry="task", inline=true)
  protected List<Task> tasks = new ArrayList<Task>();

    public List<Workspace> getWorkspaces()
    {
        return workspaces;
    }

    public void setWorkspaces(List<Workspace> workspaces)
    {
        this.workspaces = workspaces;
    }

    public List<Task> getTasks()
    {
        return tasks;
    }

    public void setTasks(List<Task> tasks)
    {
        this.tasks = tasks;
    }
}
