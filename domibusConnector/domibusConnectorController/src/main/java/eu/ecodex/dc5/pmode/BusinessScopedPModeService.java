package eu.ecodex.dc5.pmode;

import eu.domibus.configuration.Configuration;
import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.domibus.connector.controller.spring.ConnectorMessageProcessingProperties;
import eu.ecodex.dc5.domain.CurrentBusinessDomain;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@BusinessDomainScoped
@RequiredArgsConstructor
public class BusinessScopedPModeService {

    private final ConnectorMessageProcessingProperties processingProperties;
    private final ApplicationContext applicationContext;
    private Configuration config;
    private DC5PmodeService.DomibusConnectorPModeSet currentPModeSet;

    @PostConstruct
    public void init() {
        try {
            config = byteArrayToXmlObject(getPModes(), Configuration.class, Configuration.class);
        } catch (Exception e) {
            String error = String.format("Unable to parse pModes from [%s]", getPModeLocation());
            throw new RuntimeException(error, e);
        }
    }

    private String getPModeLocation() {
        Objects.requireNonNull(processingProperties.getPModeConfig(), DCPModeException.P_MODE_FILE_NOT_CONFIGURED + ": PMode config must be set if PModeService is being called!");
        Objects.requireNonNull(processingProperties.getPModeConfig().getPModeLocation(), DCPModeException.P_MODE_FILE_NOT_CONFIGURED + ": PMode file in pmodeConfig must be set if PModeService is being called!");
        return processingProperties.getPModeConfig().getPModeLocation();
    }

    private byte[] getPModes() {
        Resource resource = applicationContext.getResource(getPModeLocation());
        if (resource == null) {
            String error = String.format("Unable to load pModes from [%s]", getPModeLocation());
            throw new RuntimeException(error);
        }
        try (InputStream is = resource.getInputStream()) {
            return StreamUtils.copyToByteArray(is);
        } catch (IOException e) {
            String error = String.format("Unable to load pModes from [%s]", getPModeLocation());
            throw new RuntimeException(error, e);
        }
    }

    public synchronized DC5PmodeService.DomibusConnectorPModeSet getCurrentPModeSet() {
        if (this.currentPModeSet == null) {
            Map<String, DC5PmodeService.PModeService> services = getServices();
            Map<String, DC5PmodeService.PModeAction> actions = getActions();
            Map<String, DC5PmodeService.PModeLeg> legs = getLegs(services, actions);
            List<DC5PmodeService.PModeParty> pModeParties = getPModeParties();

            this.currentPModeSet = DC5PmodeService.DomibusConnectorPModeSet.builder()
                    .pModes(getPModes())
                    .businessDomainId(CurrentBusinessDomain.getCurrentBusinessDomain())
                    .parties(pModeParties)
                    .homeParty(getHomeParty(pModeParties))
                    .services(new ArrayList<>(services.values()))
                    .actions(new ArrayList<>(actions.values()))
                    .businessProcess(getBusinessProcess(pModeParties, legs))
                    .legs(new ArrayList<>(legs.values()))
//                .connectorstore(loadConnectorStore())
                    .build();
        }
        return this.currentPModeSet;
    }

    private Map<String, DC5PmodeService.PModeLeg> getLegs(Map<String, DC5PmodeService.PModeService> services, Map<String, DC5PmodeService.PModeAction> actions) {
        return config.getBusinessProcesses().getLegConfigurations().getLegConfiguration().stream()
                .map(leg -> {
                    DC5PmodeService.PModeLeg pModeLeg = new DC5PmodeService.PModeLeg();
                    pModeLeg.setName(leg.getName());
                    pModeLeg.setAction(actions.get(leg.getAction()));
                    pModeLeg.setService(services.get(leg.getService()));
                    return pModeLeg;
                })
                .collect(Collectors.toMap(DC5PmodeService.PModeLeg::getName, Function.identity()));
    }

    private List<DC5PmodeService.PModeProcess> getBusinessProcess(List<DC5PmodeService.PModeParty> pModeParties,
                                                                  Map<String, DC5PmodeService.PModeLeg> legs) {
        Map<String, String> roles = config.getBusinessProcesses().getRoles().getRole().stream()
                .collect(Collectors.toMap(Configuration.BusinessProcesses.Roles.Role::getName, Configuration.BusinessProcesses.Roles.Role::getValue));
        return config.getBusinessProcesses().getProcess().stream()
                .map(b -> {
                    HashSet<String> initiatorParties = b.getInitiatorParties().getInitiatorParty().stream()
                            .map(Configuration.BusinessProcesses.Process.InitiatorParties.InitiatorParty::getName)
                            .collect(Collectors.toCollection(HashSet::new));
                    HashSet<String> responderParties = b.getInitiatorParties().getInitiatorParty().stream()
                            .map(Configuration.BusinessProcesses.Process.InitiatorParties.InitiatorParty::getName)
                            .collect(Collectors.toCollection(HashSet::new));
                    HashSet<String> legNames = b.getLegs().getLeg().stream().map(Configuration.BusinessProcesses.Process.Legs.Leg::getName).collect(Collectors.toCollection(HashSet::new));

                    DC5PmodeService.PModeProcess pModeProcess = new DC5PmodeService.PModeProcess();
                    pModeProcess.setName(b.getName());
                    pModeProcess.setInitiatorRole(roles.get(b.getInitiatorRole()));
                    pModeProcess.setResponderRole(roles.get(b.getResponderRole()));
                    pModeProcess.setInitiatorParties(pModeParties.stream().filter(party -> initiatorParties.contains(party.getName())).collect(Collectors.toList()));
                    pModeProcess.setResponderParties(pModeParties.stream().filter(party -> responderParties.contains(party.getName())).collect(Collectors.toList()));
                    pModeProcess.setLegs(legs.values().stream().filter(l -> legNames.contains(l.getName()))
                            .map(leg ->
                            {
                                leg.setBusinessProcess(pModeProcess);
                                return leg;
                            })
                            .collect(Collectors.toList()));
                    return pModeProcess;
                })
                .collect(Collectors.toList());
    }

//    private DomibusConnectorKeystore loadConnectorStore() {
//        return null;
//    }

    private Map<String, DC5PmodeService.PModeAction> getActions() {
        return config.getBusinessProcesses().getActions().getAction().stream()
                .collect(Collectors.toMap(Configuration.BusinessProcesses.Actions.Action::getName, a -> {
                    DC5PmodeService.PModeAction action = new DC5PmodeService.PModeAction();
                    action.setAction(a.getValue());
                    return action;
                }));
    }

    private Map<String, DC5PmodeService.PModeService> getServices() {
        return config.getBusinessProcesses().getServices().getService().stream()
                .collect(Collectors.toMap(Configuration.BusinessProcesses.Services.Service::getName, s -> {
                    DC5PmodeService.PModeService service = new DC5PmodeService.PModeService();
                    service.setService(s.getValue());
                    service.setServiceType(s.getType());
                    return service;
                }));
    }

    private DC5PmodeService.PModeParty getHomeParty(List<DC5PmodeService.PModeParty> pModeParties) {
        String homePartyName = config.getParty();
        return pModeParties.stream()
                .filter(p -> StringUtils.equals(homePartyName, p.getName()))
                .findAny()
                .orElseThrow(() -> new RuntimeException(String.format("No homeparty with name [%s] found in pModes", homePartyName)));
    }

    private List<DC5PmodeService.PModeParty> getPModeParties() {
        Map<String, String> partyIdTypes
                = config.getBusinessProcesses().getParties().getPartyIdTypes().getPartyIdType()
                .stream()
                .collect(Collectors.toMap(Configuration.BusinessProcesses.Parties.PartyIdTypes.PartyIdType::getName,
                        Configuration.BusinessProcesses.Parties.PartyIdTypes.PartyIdType::getValue));

        return config.getBusinessProcesses().getParties().getParty().stream()
                .flatMap(p -> p.getIdentifier().stream().map(identifier -> {
                    DC5PmodeService.PModeParty party = new DC5PmodeService.PModeParty();
                    party.setPartyId(identifier.getPartyId());
                    party.setPartyIdType(partyIdTypes.get(identifier.getPartyIdType()));
                    party.setName(p.getName());
                    return party;
                }))
                .collect(Collectors.toList());
    }

    static <T> T byteArrayToXmlObject(final byte[] xmlAsBytes, final Class<T> instantiationClazz,
                                      final Class<?>... initializationClasses) throws Exception {

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(xmlAsBytes);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            factory.setNamespaceAware(true);
            Document document = factory.newDocumentBuilder().parse(bis);

            JAXBContext ctx = JAXBContext.newInstance(initializationClasses);

            Unmarshaller unmarshaller = ctx.createUnmarshaller();

            JAXBElement<T> jaxbElement = unmarshaller.unmarshal(document, instantiationClazz);
            return jaxbElement.getValue();
        } catch (JAXBException | SAXException | IOException | ParserConfigurationException e) {
            throw new Exception("Exception parsing byte[] to " + instantiationClazz.getName(), e);
        }

    }

}
