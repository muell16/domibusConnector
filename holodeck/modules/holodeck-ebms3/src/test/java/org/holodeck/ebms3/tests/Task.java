package org.holodeck.ebms3.tests;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.io.File;

/**
 * Testing object cyling and refrences in XML
 * serialization with Simple framework.
 */
@Root
public class Task
{
   @Element
   protected Workspace myWorkspace;

   @Attribute
   protected String engine;

    public Workspace getWorkspace()
    {
        return myWorkspace;
    }

    public void setWorkspace(Workspace workspace)
    {
        this.myWorkspace = workspace;
    }

    public String getEngine()
    {
        return engine;
    }

    public void setEngine(String engine)
    {
        this.engine = engine;
    }

    public File getPath() { return myWorkspace.getPath(); }
}
