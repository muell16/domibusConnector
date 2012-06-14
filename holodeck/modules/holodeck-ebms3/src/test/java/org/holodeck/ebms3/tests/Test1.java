package org.holodeck.ebms3.tests;


import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.*;
import org.simpleframework.xml.graph.CycleStrategy;
import java.util.*;


//import org.simpleframework.xml.load.*;
//import org.apache.axis2.context.*;
//import org.apache.axis2.addressing.EndpointReference;
//import org.holodeck.common.client.*;
//import org.holodeck.ebms3.submit.*;
//import org.holodeck.ebms3.pmodes.Producer;

/**
 * @author Hamid Ben Malek
 */
public class Test1
{
//   public static void main(String args[])
//   {
//     org.junit.runner.JUnitCore.runClasses(org.holodeck.ebms3.tests.Test1.class);
     //Test1 test = new Test1();
     //test.serializeMsgCtx();
//   }
  /*
   public static junit.framework.Test suite()
   {
     return new junit.framework.JUnit4TestAdapter(org.holodeck.ebms3.tests.Test1.class);
   } */


   @Test
   public void readJob()
   { /*
     try
     {
       String filePath =
          Test1.class.getClassLoader().getResource("job.xml").getPath();
       File jobFile = new File(filePath);
       Strategy strategy = new CycleStrategy("name", "myRef");
       Serializer serializer = new Persister(strategy);
       Job job = serializer.read(Job.class, jobFile);
       List<Task> tasks = job.getTasks();
       for (Task t : tasks)
       {
         System.out.println("Task's engine=" + t.getEngine());
         System.out.println("Workspace path=" + t.getWorkspace().getPath());
         System.out.println("-----------------------------------");
         if ( t.getEngine().equals("example.FirstEngine") )
              assertEquals("c:\\workspace\\task",
                           t.getWorkspace().getPath().getAbsolutePath());
         if ( t.getEngine().equals("example.SecondEngine") )
              assertEquals("c:\\workspace2\\task2",
               t.getWorkspace().getPath().getAbsolutePath());
       }
     }
     catch (Exception e) { e.printStackTrace(); }
     */
   }

    /*
   @Test
   public void writeMsgInfoSet()
   {

     MsgInfoSet meta = new MsgInfoSet();
     meta.setAgreementRef("AGR-X25");
     meta.setPmode("pmode_abc");
     Producer p = new Producer();
     p.addParty("corp", "fujitsu.com");
     p.addParty("research", "fla.fujitsu.com");
     p.setRole("sender");
     meta.setProducer(p);
     Properties props = new Properties();
     props.addProperty("greeting", "Hello there");
     props.addProperty("price", "$20");
     meta.setProperties(props);
     meta.writeToFile("F:\\meta.xml");

   }

   @Test
   public void serializeProducer()
   {
     Producer p = new Producer();
     p.addParty("corp", "fujitsu.com");
     p.addParty("corp", "fla.fujitsu.com");
     p.setRole("seller");
     serialize(p, "F:\\producer.xml");
   }

   @Test
   public void serializeMsgInfo()
   {
     MyMsgInfoSet mis = new MyMsgInfoSet();
     mis.addFromParty("corp", "fujitsu.com");
     mis.addFromParty("corp", "fla.fujitsu.com");
     mis.setFromRole("seller");
     mis.addProperty("greeting", "Hello there");
     mis.addProperty("price", "$40");
     mis.setPmode("UploadMode");
     mis.setConversationId("123456");
     mis.setAgreementRef("AGR123");
     mis.setLegNumber(2);
     mis.setCallbackClass("com.mypackage.MyCallback");
     mis.setBodyPayload("contract.xml");
     mis.addPayload("myForm", "form.pdf");
     mis.addPayload("myPicture", "picture.jpg");
     writeXML(mis, "F:\\metadata.xml");
   }


   @Test
   public void serializeMsgCtx()
   {

     MsgInfoSet mis = new MsgInfoSet();
     mis.addFromParty("corp", "fujitsu.com");
     mis.addFromParty("corp", "fla.fujitsu.com");
     mis.setFromRole("seller");
     mis.addProperty("greeting", "Hello there");
     mis.addProperty("price", "$40");
     mis.setPmode("UploadMode");
     mis.setConversationId("123456");
     mis.setAgreementRef("AGR123");
     mis.setLegNumber(2);
     mis.setCallbackClass("com.mypackage.MyCallback");
     mis.setBodyPayload("contract.xml");
     mis.addPayload("myForm", "form.pdf");
     mis.addPayload("myPicture", "picture.jpg");

     Client msg = new Client();
     msg.addToBody("F:\\metadata.xml");
     msg.getMessageContext().setProperty("msgInfo", mis);
     msg.getMessageContext().setTo(new EndpointReference("http://www.sun.com"));
     serialize(msg.getMessageContext(), "F:\\msgCtx");

     MessageContext ctx = (MessageContext)deserialize("F:\\msgCtx");
     MsgInfoSet m = (MsgInfoSet)ctx.getProperty("msgInfo");
     if ( m != null ) System.out.println("m is not null");
     else System.out.println("m is null");
     if ( m != null ) System.out.println("m.getPmode()=" + m.getPmode());
     System.out.println("m.getAgreementRef()=" + m.getAgreementRef());
     System.out.println("ctx.getTo().getAddress()=" + ctx.getTo().getAddress());
     System.out.println("========= envelope is ===============");
     System.out.println(ctx.getEnvelope().toString());

   }
   
   private void writeXML(Object obj, String fileName)
   {
     try
     {
       Persister serializer = new Persister();
       File result = new File(fileName);
       serializer.write(obj, result);
     }
     catch(Exception ex) { ex.printStackTrace(); }
   }

   private void serialize(Object obj, String fileName)
   {
     if (obj == null) return;
     try
     {
       FileOutputStream bos = new FileOutputStream(fileName) ;
       ObjectOutput out = new ObjectOutputStream(bos) ;
       out.writeObject(obj);
       out.close();
     }
     catch(Exception ex) { ex.printStackTrace(); }
   }

   private Object deserialize(String fileName)
   {
     if ( fileName == null  ) return null;
     Object result = null;
     try
     {
       FileInputStream fis = new FileInputStream(fileName);
       ObjectInputStream is = new ObjectInputStream(fis);
       result = is.readObject();
     }
     catch(Exception ex) { ex.printStackTrace(); }
     return result;
   }
   */
}