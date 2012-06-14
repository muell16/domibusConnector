package org.holodeck.ebms3.config;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.List;

/**
 * @author Hamid Ben Malek
 */
public class TestPModePool
{
  private PModePool pool;

  @Before
  public void setUp() throws Exception
  {
    String filePath =
       TestPModePool.class.getClassLoader()
                    .getResource("pmode-collection.xml").getPath();
    pool = PModePool.load(filePath);
  }

  @Test
  public void vefify()
  {
    if ( pool == null ) fail("PModePool not loaded");
    List<PMode> pmodes = pool.getPmodes();
    if ( pmodes == null || pmodes.size() == 0 )
    {
      fail("No PMode object inside Pool"); return;
    }
    for (PMode pm : pmodes)
    {
      verifyUserService(pm);
      verifyLegs(pm);
      verifySoapBodySchema(pm);
      vefifyProducer(pm);
    }
  }

  private void verifyUserService(PMode pm)
  {
    if ( pm.getName().equals("ImagePulling") )
    {
      System.out.println("======= UserService for PMode " + pm.getName());
      Leg secondLeg = pm.getBinding().getMep().getLegs().get(1);
      UserService us = secondLeg.getUserService();
      String toPartyId = us.getToParty().getParties().get(0).getPartyId();
      assertEquals("pullerParty", toPartyId);
      assertEquals("imageService",
                    us.getCollaborationInfo().getService().getValue());
      assertEquals("storeImage", us.getCollaborationInfo().getAction());
      System.out.println("toPartyId=" + toPartyId);
      System.out.println("service=" +
                          us.getCollaborationInfo().getService().getValue());
      System.out.println("action=" + us.getCollaborationInfo().getAction());
    }
  }

  private void verifyLegs(PMode pm)
  {
    if ( pm.getName().equals("ImagePulling") )
    {
      Leg firstleg = pm.getBinding().getMep().getLegs().get(0);
      assertEquals(1, firstleg.getNumber());
      assertEquals("mpc//:imageStore", firstleg.getMpc());
      assertEquals("CALLBACK", firstleg.getReliability());
      assertEquals("1.2", firstleg.getEndpoint().getSoapVersion());
      assertEquals(false, firstleg.getErrorAtSender().isNotifyConsumer());
      assertEquals("joe", firstleg.getAuthorization().getUsername());
      assertEquals("black", firstleg.getAuthorization().getPassword());
      System.out.println("======= First Leg for PMode " + pm.getName());
      System.out.println("PMode's name: " + pm.getName());
      System.out.println("First Leg number: " + firstleg.getNumber());
      System.out.println("First Leg mpc: " + firstleg.getMpc());
      System.out.println("First Leg reliability: " + firstleg.getReliability());
      System.out.println("First Leg soap version: " + firstleg.getEndpoint().getSoapVersion());
      System.out.println("ErrorAtSender: notifyConsumer=" +
                          firstleg.getErrorAtSender().isNotifyConsumer());
      System.out.println("Authorization: username=" +
                         firstleg.getAuthorization().getUsername() +
                         "   password=" +
                         firstleg.getAuthorization().getPassword());
    }
  }

  private void verifySoapBodySchema(PMode pm)
  {
    if ( pm.getName().equals("QuantityOnHandPulling") )
    {
      Leg secondLeg = pm.getBinding().getMep().getLegs().get(1);
      UserService us = secondLeg.getUserService();
      MessagePayload msg = us.getPayloadInfo().getMessagePayloads().get(0);
      assertEquals("quantityMsg", msg.getLabel());
      assertEquals("http://domain.com/someSchema.xsd", msg.getSoapBodySchema());
      System.out.println("============== PMode " + pm.getName());
      System.out.println("messageLabel for second leg: " + msg.getLabel());
      System.out.println("SoapBodySchema for second leg: " + msg.getSoapBodySchema());
    }
  }

  private void vefifyProducer(PMode pm)
  {
    if ( pm.getName().equals("SendShipment") )
    {
      Leg firstLeg = pm.getBinding().getMep().getLegs().get(0);
      Producer producer = firstLeg.getProducer();
      assertEquals("autotech-supplier",
                    producer.getParties().get(0).getPartyId());
      assertEquals("GM", producer.getRole());
      System.out.println("--------First Leg of PMode " + pm.getName());
      System.out.println("producer partyId: " +
                          producer.getParties().get(0).getPartyId());
      System.out.println("producer role: " + producer.getRole());
    }
  }
}
