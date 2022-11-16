package eu.domibus.connector.persistence.service.impl;

import eu.ecodex.dc5.message.model.DC5Confirmation;
import eu.domibus.connector.persistence.model.PDomibusConnectorEvidence;
import eu.domibus.connector.persistence.service.impl.helper.EvidenceTypeMapper;
import eu.domibus.connector.persistence.service.impl.helper.MapperHelper;

public class MessageConfirmationMapper {


    public static DC5Confirmation mapFromDbToDomain(PDomibusConnectorEvidence e) {
        DC5Confirmation confirmation = new DC5Confirmation();
        if (e.getEvidence() != null) {
            confirmation.setEvidence(e.getEvidence().getBytes());
        }
        confirmation.setEvidenceType(EvidenceTypeMapper.mapEvidenceFromDbToDomain(e.getType()));
        return confirmation;
    }

    public static PDomibusConnectorEvidence mapFromDomainIntoDb(PDomibusConnectorEvidence evidence, DC5Confirmation confirmation) {
        evidence.setType(EvidenceTypeMapper.mapEvidenceTypeFromDomainToDb(confirmation.getEvidenceType()));
        if (confirmation.getEvidence() != null) {
            evidence.setEvidence(MapperHelper.convertByteArrayToString(confirmation.getEvidence()));
        }
        return evidence;
    }

    public static PDomibusConnectorEvidence mapFromDomainToDb(DC5Confirmation confirmation) {
        PDomibusConnectorEvidence evidence = new PDomibusConnectorEvidence();
        return mapFromDomainIntoDb(evidence, confirmation);
    }

}
