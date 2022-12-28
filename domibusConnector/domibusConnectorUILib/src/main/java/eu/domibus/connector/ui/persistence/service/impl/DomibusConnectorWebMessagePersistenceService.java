package eu.domibus.connector.ui.persistence.service.impl;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.ui.dto.WebMessage;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.util.*;

@org.springframework.stereotype.Service("webMessagePersistenceService")
public class DomibusConnectorWebMessagePersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorWebMessagePersistenceService.class);
    private final DC5MessageRepo messageRepo;

    public DomibusConnectorWebMessagePersistenceService(DC5MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public List<WebMessage> findAll() {
        return mapDbMessagesToWebMessages(messageRepo.findAll());
    }

    public Optional<WebMessage> getMessageByConnectorId(String connectorMessageId) {
        return mapDbMessageToWebMessage(messageRepo.findOneByConnectorMessageId(DC5MessageId.ofString(connectorMessageId)));
    }

    public Optional<WebMessage> findMessageByEbmsId(String ebmsId, DomibusConnectorMessageDirection gatewayToBackend) {
        return mapDbMessageToWebMessage(messageRepo.findOneByEbmsMessageIdAndDirectionTarget(EbmsMessageId.ofString(ebmsId), gatewayToBackend.getTarget()));
    }

    public Optional<WebMessage> findMessageByNationalId(String backendMessageId, DomibusConnectorMessageDirection backendToGateway) {
        return mapDbMessageToWebMessage(messageRepo.findOneByBackendMessageIdAndDirectionTarget(BackendMessageId.ofString(backendMessageId), backendToGateway.getTarget()));
    }

    public List<WebMessage> findByConversationId(String conversationId) {
        return mapDbMessagesToWebMessages(messageRepo.findAllByEbmsData_ConversationId(conversationId));
    }

    private static class DBMessageToWebMessageConverter implements Converter<DC5Message, WebMessage> {

        @Nullable
        @Override
        public WebMessage convert(DC5Message pMessage) {
            WebMessage message = new WebMessage();

            message.setConnectorMessageId(
                    Optional.ofNullable(pMessage.getConnectorMessageId())
                            .map(DC5MessageId::getConnectorMessageId)
                            .orElseThrow(() -> new RuntimeException("Connector Message Id was null.")));

            message.setEbmsId(
                    Optional.ofNullable(pMessage.getEbmsData())
                            .map(DC5Ebms::getEbmsMessageId)
                            .map(EbmsMessageId::getEbmsMesssageId)
                            .orElse(null)); // this should never happen

            message.setBackendMessageId(
                    Optional.ofNullable(pMessage.getBackendData())
                            .map(DC5BackendData::getBackendMessageId)
                            .map(BackendMessageId::getBackendMessageId)
                            .orElse(null));

            message.setConversationId(Optional.ofNullable(pMessage.getEbmsData())
                    .map(DC5Ebms::getConversationId)
                    .orElse(null));

            // TODO: not null
//			pMessage.getBackendLinkName().getLinkName()
            message.setBackendName(Optional.ofNullable(pMessage.getBackendLinkName())
                    .map(DomibusConnectorLinkPartner.LinkPartnerName::getLinkName)
                    .orElse(null));

            message.setMessageDirection(pMessage.getDirection().toString());

            message.setCreated(pMessage.getCreated());


            final List<DC5BusinessMessageState> prvStates = Optional.ofNullable(pMessage.getMessageContent())
                    .map(DC5MessageContent::getMessageStates)
                    .orElseGet(Collections::emptyList);

            StringBuilder prvStatesPresentation = new StringBuilder();
            for (int i = 0; i < prvStates.size(); i++) {
                prvStatesPresentation.append(prvStates.get(i).getState());
                if (i != prvStates.size()-1) {
                    prvStatesPresentation.append("->");
                }
            }
            message.setPrvStates(prvStatesPresentation.toString());
//            message.setMsgContentState(
//                    Optional.ofNullable(pMessage.getMessageContent())
//                            .map(DC5MessageContent::getMessageStates)
//                            .orElseGet(Collections::emptyList)
//                            .stream().map(DC5BusinessMessageState::toString)
//                            .collect(Collectors.joining("->")));

            message.setMessageContentState(pMessage.getMessageContent() != null
                    ? pMessage.getMessageContent().getCurrentState().getState().toString()
                    : null);
            // TODO: calculate the rest of the data and decide what additional data should be transported to UI

//			message.setDeliveredToNationalSystem(pMessage.getDeliveredToNationalSystem()!=null?ZonedDateTime.ofInstant(pMessage.getDeliveredToNationalSystem().toInstant(), ZoneId.systemDefault()):null);
//			message.setDeliveredToGateway(pMessage.getDeliveredToGateway()!=null?ZonedDateTime.ofInstant(pMessage.getDeliveredToGateway().toInstant(), ZoneId.systemDefault()):null);
//			message.setCreated(pMessage.getCreated()!=null?ZonedDateTime.ofInstant(pMessage.getCreated().toInstant(), ZoneId.systemDefault()):null);
//			message.setConfirmed(pMessage.getConfirmed()!=null?ZonedDateTime.ofInstant(pMessage.getConfirmed().toInstant(), ZoneId.systemDefault()):null);
//			message.setRejected(pMessage.getRejected()!=null?ZonedDateTime.ofInstant(pMessage.getRejected().toInstant(), ZoneId.systemDefault()):null);
//
//			DC5MessageInfo pMessageInfo = pMessage.getMessageInfo();
//			if(pMessageInfo!=null) {
//
//				message.setMessageInfo(mapDbMessageInfoToWebMessageDetail(pMessageInfo));
//				
//			}
//			
//			if(!CollectionUtils.isEmpty(pMessage.getRelatedEvidences())) {
//				for(PDomibusConnectorEvidence dbEvidence:pMessage.getRelatedEvidences()) {
//					WebMessageEvidence evidence = new WebMessageEvidence();
//					evidence.setEvidenceType(dbEvidence.getType().name());
//					evidence.setDeliveredToGateway(dbEvidence.getDeliveredToGateway());
//					evidence.setDeliveredToBackend(dbEvidence.getDeliveredToBackend());
//					message.getEvidences().add(evidence);
//				}
//			}
            return message;
        }
    }

    private List<WebMessage> mapDbMessagesToWebMessages(Iterable<DC5Message> messages) {
        LinkedList<WebMessage> webMessages = new LinkedList<WebMessage>();
        Iterator<DC5Message> msgIt = messages.iterator();
        while (msgIt.hasNext()) {
            DC5Message pMessage = msgIt.next();

            WebMessage message = new DBMessageToWebMessageConverter().convert(pMessage);
            webMessages.addLast(message);
        }

        return webMessages;
    }

    private Optional<WebMessage> mapDbMessageToWebMessage(Optional<DC5Message> pMessage) {
        return pMessage.map(m -> new DBMessageToWebMessageConverter().convert(m));
    }


}
