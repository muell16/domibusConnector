package org.holodeck.ebms3.tests;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;

import java.io.File;

/**
 * Testing object cyling and refrences in XML
 * serialization with Simple framework.
 */
@Root(strict=false)
public class Workspace {

   @Attribute
   protected File path;

   @Attribute(required=false)
   protected String name;

   public File getPath() {
      return path;
   }

   public String getName() {
      return name;
   }

    public void setName(String name)
    {
        this.name = name;
    }
 
    public void setPath(File path)
    {
        this.path = path;
    }


}

