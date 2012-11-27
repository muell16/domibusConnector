package org.holodeck.ebms3.module;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.Parameter;
import org.apache.log4j.Logger;
import org.holodeck.common.soap.Util;
import org.holodeck.ebms3.config.Binding;
import org.holodeck.ebms3.config.CollaborationInfo;
import org.holodeck.ebms3.config.Leg;
import org.holodeck.ebms3.config.MEP;
import org.holodeck.ebms3.config.PMode;
import org.holodeck.ebms3.config.PModePool;
import org.holodeck.ebms3.config.Party;
import org.holodeck.ebms3.config.Producer;
import org.holodeck.ebms3.config.ToParty;
import org.holodeck.ebms3.submit.MsgInfoSet;

/**
 * @author Hamid Ben Malek
 */
public class Configuration extends Constants
{
//  private static Log log = LogFactory.getLog(Configuration.class);
  private static Logger log = Logger.getLogger(Configuration.class);

  public static void loadPModes() throws Exception
  {
    File pmDir = new File(getPModesDir());
    log.debug("Loading PModes from directory: " + pmDir.getAbsolutePath());
    File[] files = pmDir.listFiles();
    if ( files != null && files.length > 0 )
    {
      for (File file : files)
      {
        if ( !file.isDirectory() && file.getName().endsWith(".xml") )
        {
          PModePool pool = PModePool.load(file);
          if ( pmodes == null ) pmodes = new HashMap<String, PMode>();
          for (PMode pm : pool.getPmodes())
          {
            pmodes.put(pm.getName(), pm);
            log.debug("PMode " + pm.getName() + " has been loaded.");
          }
        }
      }
    }
    log.debug("Done with loading pmodes.");
  }

  public static void addPModePool(PModePool pool)
  {
    if ( pool == null || pool.getPmodes() == null ) return;
    if ( pmodes == null ) pmodes = new HashMap<String, PMode>();
    for ( PMode pm : pool.getPmodes() )
    {
      pmodes.put(pm.getName(), pm);
      log.debug("PMode " + pm.getName() + " has been loaded.");
    }
  }

  public static String getSecurity(String pmodeName, int legNumber)
  {
    Leg leg = getLeg(pmodeName, legNumber);
    if ( leg == null ) return null;
    return leg.getSecurity();
  }
  public static String getSecurity(MsgInfoSet metadata)
  {
    Leg leg = getLeg(metadata);
    if ( leg == null ) return null;
    return leg.getSecurity();
  }

  public static PMode getPMode(String pmodeName)
  {
    if ( pmodeName == null || pmodeName.trim().equals("") ) return null;
    if ( pmodes != null ) return pmodes.get(pmodeName);
    else return null;
  }
  public static PMode getPMode(MsgInfoSet metadata)
  {
    if ( metadata == null ) return null;
    return getPMode(metadata.getPmode());
  }

  public static Leg getLeg(String pmodeName, int legNumber)
  {
    PMode pmode = getPMode(pmodeName);
    if ( pmode == null ) return null;
    List<Leg> legs = pmode.getBinding().getMep().getLegs();
    if ( legs == null || legs.size() == 0 ) return null;
    for (Leg leg : legs)
    {
      if ( legNumber < 0 && leg.getNumber() == 1 ) return leg;
      if ( leg.getNumber() == legNumber ) return leg;
    }
    return null;
  }
  public static Leg getLeg(MsgInfoSet metadata)
  {
    if ( metadata == null ) return null;
    return getLeg(metadata.getPmode(), metadata.getLegNumber());
  }

  public static String getMep(String pmodeName)
  {
    PMode pmode = getPMode(pmodeName);
    if ( pmode == null ) return null;
    Binding b = pmode.getBinding();
    if ( b == null ) return null;
    MEP mep = b.getMep();
    if ( mep == null ) return null;
    return mep.getName();
  }
  public static String getMep(MsgInfoSet metadata)
  {
    if ( metadata == null ) return null;
    return getMep(metadata.getPmode());
  }

  public static Binding getBinding(MsgInfoSet metadata)
  {
    if ( metadata == null ) return null;
    PMode pmode = getPMode(metadata.getPmode());
    if ( pmode != null ) return pmode.getBinding();
    else return null;
  }

	public static PMode getPModeO(String action, String service, String fromPartyid, String fromPartyidType,
			String toPartyid, String toPartyidType)
	{
		for (org.holodeck.ebms3.config.PMode pmode : org.holodeck.ebms3.module.Constants.pmodes.values()) {
			org.holodeck.ebms3.config.Binding binding = pmode.getBinding();
			if (binding == null || binding.getMep() == null) {
				continue;
			}
			boolean producerFounded = false;
			if (pmode.getProducers() != null) {
				for (org.holodeck.ebms3.config.Producer producer : pmode.getProducers()) {
					if (producer.getParties() != null && producer.getParties().size() > 0
							&& producer.getParties().get(0).getPartyId()!=null
							&& producer.getParties().get(0).getPartyId().equalsIgnoreCase(fromPartyid)
//							&& producer.getParties().get(0).getType()!=null
//							&& producer.getParties().get(0).getType().equalsIgnoreCase(fromPartyidType)
						) {
						producerFounded = true;
						break;
					}
				}
			}
			if (!producerFounded && binding.getMep().getLegs() != null) {
				for (org.holodeck.ebms3.config.Leg leg : binding.getMep().getLegs()) {
					if (leg.getProducer() != null && leg.getProducer().getParties() != null
							&& leg.getProducer().getParties().size() > 0
							&& leg.getProducer().getParties().get(0).getPartyId()!=null
							&& leg.getProducer().getParties().get(0).getPartyId().equalsIgnoreCase(fromPartyid)
//							&& leg.getProducer().getParties().get(0).getType()!=null
//							&& leg.getProducer().getParties().get(0).getType().equalsIgnoreCase(fromPartyidType)
						) {
						producerFounded = true;
						break;
					}
				}
			}
			if (!producerFounded) {
				continue;
			}
			boolean toFounded = false;
			if (pmode.getUserServices() != null) {
				for (org.holodeck.ebms3.config.UserService userService : pmode.getUserServices()) {
					if (userService.getToParty() != null && userService.getToParty().getParties() != null
							&& userService.getToParty().getParties().size() > 0
							&& userService.getToParty().getParties().get(0).getPartyId()!=null
							&& userService.getToParty().getParties().get(0).getPartyId().equalsIgnoreCase(toPartyid)
//							&& userService.getToParty().getParties().get(0).getType()!=null
//							&& userService.getToParty().getParties().get(0).getType().equalsIgnoreCase(toPartyidType)
						) {
						toFounded = true;
						break;
					}
				}
			}
			if (!toFounded && binding.getMep().getLegs() != null) {
				for (org.holodeck.ebms3.config.Leg leg : binding.getMep().getLegs()) {
					if (leg.getUserService().getToParty() != null
							&& leg.getUserService().getToParty().getParties() != null
							&& leg.getUserService().getToParty().getParties().size() > 0
							&& leg.getUserService().getToParty().getParties().get(0).getPartyId()!=null
							&& leg.getUserService().getToParty().getParties().get(0).getPartyId().equalsIgnoreCase(toPartyid)
//							&& leg.getUserService().getToParty().getParties().get(0).getType()!=null
//							&& leg.getUserService().getToParty().getParties().get(0).getType().equalsIgnoreCase(toPartyidType)
						) {
						toFounded = true;
						break;
					}
				}
			}
			if (!toFounded) {
				continue;
			}
			boolean serviceAndActionFounded = false;
			if (pmode.getUserServices() != null) {
				for (org.holodeck.ebms3.config.UserService userService : pmode.getUserServices()) {
					if (userService.getCollaborationInfo() != null
							&& userService.getCollaborationInfo().getAction()!=null 
							&& userService.getCollaborationInfo().getAction().equals(action)
							&& userService.getCollaborationInfo().getService().getValue()!=null
							&& userService.getCollaborationInfo().getService().getValue().equals(service)) {
						serviceAndActionFounded = true;
						break;
					}
				}
			}
			if (!serviceAndActionFounded && binding.getMep().getLegs() != null) {
				for (org.holodeck.ebms3.config.Leg leg : binding.getMep().getLegs()) {
					if (leg.getUserService() != null
							&& leg.getUserService().getCollaborationInfo() != null
							&& leg.getUserService().getCollaborationInfo().getAction()!=null
							&& leg.getUserService().getCollaborationInfo().getAction().equals(action)
							&& leg.getUserService().getCollaborationInfo().getService().getValue()!=null
							&& leg.getUserService().getCollaborationInfo().getService().getValue().equals(service)) {
						serviceAndActionFounded = true;
						break;
					}
				}
			}
			if (serviceAndActionFounded) {
				return pmode;
			}
		}
		return null;
	}
	
	public static String getPMode(String action, String service, String fromPartyid, String fromPartyidType,
			String toPartyid, String toPartyidType)
	{
		org.holodeck.ebms3.config.PMode pmode = getPModeO(action, service, fromPartyid, fromPartyidType, toPartyid,
				toPartyidType);
		if (pmode != null) {
			return pmode.getName();
		} else {
			return null;
		}
	}

	public static String getMep(String action, String service, String fromPartyid, String fromPartyidType,
			String toPartyid, String toPartyidType)
	{
		org.holodeck.ebms3.config.PMode pmode = getPModeO(action, service, fromPartyid, fromPartyidType, toPartyid,
				toPartyidType);
		if (pmode != null) {
			return pmode.getBinding().getMep().getName();
		} else {
			return null;
		}
	}

  public static String getMpc(String pmodeName, int legNumber)
  {
    Leg leg = getLeg(pmodeName, legNumber);
    if ( leg == null ) return null;
    return leg.getMpc();
  }
  public static String getMpc(MsgInfoSet metadata)
  {
    if ( metadata == null ) return null;
    return getMpc(metadata.getPmode(), metadata.getLegNumber());
  }

  public static String getAddress(String pmodeName, int legNumber)
  {
    Leg leg = getLeg(pmodeName, legNumber);
    if ( leg == null ) return null;
    return ( leg.getEndpoint() != null ? leg.getEndpoint().getAddress() : null);
  }
  public static String getAddress(MsgInfoSet metadata)
  {
    if ( metadata == null ) return null;
    return getAddress(metadata.getPmode(), metadata.getLegNumber());
  }

  public static String getSoapAction(String pmodeName, int legNumber)
  {
    Leg leg = getLeg(pmodeName, legNumber);
    if ( leg == null ) return null;
    return leg.getSoapAction();
  }
  public static String getSoapAction(MsgInfoSet metadata)
  {
    if ( metadata == null ) return null;
    return getSoapAction(metadata.getPmode(), metadata.getLegNumber());
  }

  public static String getWsaAction(String pmodeName, int legNumber)
  {
    Leg leg = getLeg(pmodeName, legNumber);
    if ( leg == null ) return null;
    return leg.getWsaAction();
  }
  public static String getWsaAction(MsgInfoSet metadata)
  {
    if ( metadata == null ) return null;
    return getWsaAction(metadata.getPmode(), metadata.getLegNumber());
  }

  public static String getReliability(String pmodeName, int legNumber)
  {
    Leg leg = getLeg(pmodeName, legNumber);
    if ( leg == null ) return null;
    return leg.getReliability();
  }
  public static String getReliability(MsgInfoSet metadata)
  {
    if ( metadata == null ) return null;
    return getReliability(metadata.getPmode(), metadata.getLegNumber());
  }

  public static String getSoapVersion(String pmodeName, int legNumber)
  {
    Leg leg = getLeg(pmodeName, legNumber);
    if ( leg == null ) return null;
    return leg.getSoapVersion();
  }
  public static String getSoapVersion(MsgInfoSet metadata)
  {
    if ( metadata == null ) return null;
    return getSoapVersion(metadata.getPmode(), metadata.getLegNumber());
  }

 /**
  *  Tries to find any PMode that contains the arguments in one of its leg.
  *  This is used to figure out the PMode being used when receiving a
  *  PullRequest.
  *  @param mpc the mpc of a received PullRequest
  *  @param address this is the address URL of the receiving MSH (the MSH
  *        that is receiving the PullRequest)
  *  @return the first PMode that contains mpc and address in one its legs
  */
  public static PMode matchPMode(String mpc, String address)
  {
    if ( pmodes == null ) return null;
    Set<String> keys = pmodes.keySet();
    if ( keys == null || keys.size() == 0 ) return null;
    for (String n : keys)
    {
      PMode pmode = pmodes.get(n);
      if ( legExists(pmode, mpc, address) ) return pmode;
    }
    return null;
  }

  public static PMode getPMode(String mep, String mpc, String address)
  {
    if ( pmodes == null ) return null;
    Set<String> keys = pmodes.keySet();
    if ( keys == null || keys.size() == 0 ) return null;
    for (String n : keys)
    {
      PMode pmode = pmodes.get(n);
      if ( pmode.getLeg(mep, mpc, address) != null ) return pmode;
    }
    return null;
  }
  public static PMode getPMode(String mep, String mpc)
  {
    if ( pmodes == null ) return null;
    Set<String> keys = pmodes.keySet();
    if ( keys == null || keys.size() == 0 ) return null;
    for (String n : keys)
    {
      PMode pmode = pmodes.get(n);
      if ( pmode.getLeg(mep, mpc) != null ) return pmode;
    }
    return null;
  }
  public static PMode getPMode(int legNumber, String mep, String mpc)
  {
    if ( pmodes == null ) return null;
    Set<String> keys = pmodes.keySet();
    if ( keys == null || keys.size() == 0 ) return null;
    for (String n : keys)
    {
      PMode pmode = pmodes.get(n);
      if ( pmode.getLeg(legNumber, mep, mpc) != null ) return pmode;
    }
    return null;
  }
  public static Leg getLeg(String pmodeName, int legNumber, String mep,
                           String mpc)
  {
    if ( pmodeName != null && !pmodeName.trim().equals("") )
         return getLeg(pmodeName, legNumber);
    PMode pmode = getPMode(legNumber, mep, mpc);
    if ( pmode == null ) return null;
    else return getLeg(pmode.getName(), legNumber);
  }
  public static Leg getLeg(MsgInfo msgInfo, int legNumber, String mep)
  {
    if ( msgInfo == null ) return null;
    return getLeg(msgInfo.getPmode(), legNumber, mep, msgInfo.getMpc());
  }

  public static PMode match(MsgInfo mi, String address)
  {
    if ( mi == null ) return null;
    if ( mi.getPmode() != null && !mi.getPmode().trim().equals("") )
         return getPMode(mi.getPmode());
    if ( pmodes == null ) return null;
    Set<String> keys = pmodes.keySet();
    if ( keys == null || keys.size() == 0 ) return null;
    for (String n : keys)
    {
      PMode pmode = pmodes.get(n);
      List<Leg> legs = pmode.getBinding().getMep().getLegs();
      if ( legs == null || legs.size() == 0 ) return null;
      for (Leg leg : legs)
      {
        if ( match(leg, mi, address) ) return pmode;
      }
    }
    return null;
  }

  public static Leg getLegFromServerSideReq(MessageContext requestMsgCtx)
  {
    if ( requestMsgCtx == null ) return null;
    SOAPHeader header = requestMsgCtx.getEnvelope().getHeader();
    if ( header == null ) return null;
    String address =
//      (String)requestMsgCtx.getProperty(org.apache.axis2.Constants.HTTP_FRONTEND_HOST_URL);
      requestMsgCtx.getTo().getAddress();
    boolean isUserMessage = false;
    OMElement pullReq =
      Util.getGrandChildNameNS(header,
                               Constants.PULL_REQUEST, Constants.NS);
    if ( pullReq != null )
    {
      String mpc = Util.getAttributeValue(pullReq, "mpc");
      PMode pmode = matchPMode(mpc, address);
      if ( pmode == null ) return null;
      return getLegFromServerSideReq(pmode, isUserMessage, address);
    }
    else
    {
      OMElement userMessage =
         Util.getGrandChildNameNS(header,
                                  Constants.USER_MESSAGE, Constants.NS);
      if ( userMessage != null ) isUserMessage = true;
      String pm =
         Util.getGrandChildAttributeValue(userMessage,
                                          Constants.AGREEMENT_REF, "pmode");
      if ( pm != null )
      {
        PMode pmode = getPMode(pm);
        return getLegFromServerSideReq(pmode, isUserMessage, address);
      }

      MsgInfo mi = EbUtil.createMsgInfo(requestMsgCtx);
      PMode pmode = Configuration.match(mi, address);
      if ( pmode == null ) return null;
      else return getLegFromServerSideReq(pmode, isUserMessage, address);
    }
  }

  private static Leg getLegFromServerSideReq(PMode pmode, boolean userMessage,
                                             String address)
  {
    if ( pmode == null || pmode.getMep() == null ) return null;
    String mep = pmode.getMep();
    if ( !userMessage )
    {
      if ( mep.equalsIgnoreCase(Constants.ONE_WAY_PULL) ||
           mep.equalsIgnoreCase(Constants.TWO_WAY_PULL_AND_PUSH) )
           return getLeg(pmode.getName(), 1);
      if ( mep.equalsIgnoreCase(Constants.TWO_WAY_PUSH_AND_PULL) )
           return getLeg(pmode.getName(), 2);
      if ( mep.equalsIgnoreCase(Constants.TWO_WAY_PULL_AND_Pull) )
      {
        String adr = getAddress(pmode.getName(), 1);
        if ( adr != null && adr.equals(address) )
             return getLeg(pmode.getName(), 1);
        else return getLeg(pmode.getName(), 3);
      }
    }
    else
    {
      if ( mep.equalsIgnoreCase(Constants.ONE_WAY_PUSH) ||
           mep.equalsIgnoreCase(Constants.TWO_WAY_PUSH_AND_PULL) ||
           mep.equalsIgnoreCase(Constants.TWO_WAY_SYNC) )
           return getLeg(pmode.getName(), 1);
      if ( mep.equalsIgnoreCase(Constants.TWO_WAY_PULL_AND_PUSH) )
           return getLeg(pmode.getName(), 3);
      if ( mep.equalsIgnoreCase(Constants.TWO_WAY_PUSH_AND_PUSH) )
      {
        String adr = getAddress(pmode.getName(), 1);
        if ( adr != null && adr.equals(address) )
             return getLeg(pmode.getName(), 1);
        else return getLeg(pmode.getName(), 2);
      }
    }
    return null;
  }
  private static boolean match(Leg leg, MsgInfo mi, String address)
  {
    if ( leg == null || mi == null ) return false;
    if ( !same(mi.getMpc(), leg.getMpc()) ) return false;
    if ( leg.getUserService() == null ||
         leg.getUserService().getCollaborationInfo() == null )
    {
      if ( (mi.getService() != null && !mi.getService().trim().equals("")) ||
           (mi.getAction() != null && !mi.getAction().trim().equals("")) )
            return false;
    }
    else
    {
      CollaborationInfo ci = leg.getUserService().getCollaborationInfo();
      if ( !same( ci.getService().getValue(), mi.getService() ) )
           return false;
      if ( !same( ci.getAction(), mi.getAction() ) ) return false;
      if ( leg.getUserService().getToParty() == null )
      {
        if ( mi.getToParties() != null && mi.getToParties().size() > 0 )
             return false;
        if ( mi.getToRole() != null && !mi.getToRole().trim().equals("") )
             return false;
      }
      // compare leg.getUserService().getToParty() with mi.getToParties()
      if ( !match( leg.getUserService().getToParty(), mi.getToParties() ) )
             return false;
    }
    if ( leg.getEndpoint() != null )
    {
      if ( !same( leg.getEndpoint().getAddress(), address ) ) return false;
    }
    else if ( address != null && !address.trim().equals("") ) return false;
    if ( leg.getProducer() != null )
    {
      if ( mi.getFromParties() == null || mi.getFromParties().size() == 0 )
           return false;
      else
      {
        // compare mi.getFromParties() with the PartyIds in leg.getProducer()
        if ( !match(leg.getProducer(), mi.getFromParties()) ) return false;
      }
      if ( !same( leg.getProducer().getRole(), mi.getFromRole() ) )
           return false;
    }
    return true;
  }
  // compare leg.getUserService().getToParty() with mi.getToParties()
  private static boolean match(ToParty to, List<Party> parties)
  {
    if ( to == null && (parties == null || parties.size() == 0) )
         return true;
    if ( to == null ) return false;
    List<Party> fp = to.getParties();
    if ( fp == null || fp.size() == 0 || fp.size() != parties.size() )
         return false;
    for (int i = 0; i < fp.size(); i++)
    {
      if ( !fp.get(i).getPartyId().equals(parties.get(i).getPartyId()) )
           return false;
    }
    return true;
  }
  // compare mi.getFromParties() with the PartyIds in leg.getProducer()
  private static boolean match(Producer producer, List<Party> parties)
  {
    if ( producer == null && (parties == null || parties.size() == 0) )
         return true;
    if ( producer == null ) return false;
    List<Party> fp = producer.getParties();
    if ( fp == null || fp.size() == 0 || fp.size() != parties.size() )
         return false;
    for (int i = 0; i < fp.size(); i++)
    {
      if ( !fp.get(i).getPartyId().equals(parties.get(i).getPartyId()) )
           return false;
    }
    return true;
  }
  public static String getPModesDir()
  {
    String dir = "pmodes";
    Parameter pmodesDirParam = module.getParameter("PModesDir");
    if ( pmodesDirParam != null )
         dir = (String)pmodesDirParam.getValue();
    if ( new File(dir).exists() ) return dir;
    else
    {
      File baseFolder = configContext
                            .getRealPath("/modules/" + module.getName());
      return baseFolder.getAbsolutePath() + File.separator + dir;
    }
  }

  public static File getFile(String parameterName, String defaultName)
  {
    if ( module == null ) return null;
    Parameter param = module.getParameter(parameterName);
    File base = configContext.getRealPath("/modules/" + module.getName());
    String file = base.getAbsolutePath() + File.separator + defaultName;
    if ( param == null ) return new File(file);
    String wFile = (String)param.getValue();
    File res = new File(wFile);
    if ( res.exists() ) return res;
    else res =
        new File( base.getAbsolutePath() + File.separator + wFile );

    return res;
  }

 /**
  *
  * @param relativePath this is the path to a file relative to the
  *        holodeck-ebms3 module
  * @return The absolute path to the file (or directory) referenced by the
  *         relative path in the argument
  */
  public static File getRealPath(String relativePath)
  {
    if ( relativePath == null || relativePath.trim().equals("") )
         return null;
    File file = new File(relativePath);
    if ( file.exists() ) return file;
    File baseFolder =
         configContext.getRealPath("/modules/" + module.getName());
    file = new File( baseFolder.getAbsolutePath() +
                     File.separator + relativePath );
    return file;
  }

  private static boolean legExists(PMode pmode, String mpc, String address)
  {
    if ( pmode.getBinding() == null ) return false;
    MEP m = pmode.getBinding().getMep();
    if ( m == null ) return false;
    List<Leg> legs = m.getLegs();
    if ( legs == null || legs.size() == 0 ) return false;
    for ( Leg leg : legs )
    {
      if ( leg.getEndpoint() == null )
      {
        if ( same(leg.getMpc(), mpc) &&
             (address == null || address.trim().equals("")) ) return true;
      }
      else if ( same(leg.getMpc(), mpc) &&
                same(address, leg.getEndpoint().getAddress()) ) return true;
    }
    return false;
  }
  private static boolean same(String v1, String v2)
  {
    if ( v1 != null ) return v1.equals(v2);
    return v2 == null || v2.equals(v1);
  }
}