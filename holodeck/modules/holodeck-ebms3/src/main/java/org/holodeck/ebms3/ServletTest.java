package org.holodeck.ebms3;

import javax.servlet.http.*;
//import javax.servlet.*;
//import java.util.*;
import java.io.*;

import org.holodeck.common.soap.Util;

/**
 * @author Hamid Ben Malek
 */
public class ServletTest extends HttpServlet
{
  protected String location =
     "F:\\Projects\\Holodeck\\test-app\\demo\\server\\holodeck\\WEB-INF\\modules\\holodeck-ebms3\\Received_Messages\\request";

  public void service(HttpServletRequest req,
                      HttpServletResponse resp)
  {
    File file = new File(location);
    try
    {
      System.out.println("======= About to write request to file");
      //FileOutputStream out = new FileOutputStream(file);
      long start = System.currentTimeMillis();
      Util.writeToFile(file, req.getInputStream());
      long end = System.currentTimeMillis();
      System.out.println("======= It took " + (end - start) +
                         " milliseconds to write request to file");
    }
    catch(Exception ex) { ex.printStackTrace(); }
  }
}
