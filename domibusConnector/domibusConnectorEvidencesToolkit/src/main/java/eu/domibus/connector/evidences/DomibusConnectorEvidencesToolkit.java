package eu.domibus.connector.evidences;


import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.ecodex.evidences.types.ECodexMessageDetails;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Interface to publish methods for creation of eCodex Evidence Messages.
 * 
 * @author riederb
 * 
 */
public interface DomibusConnectorEvidencesToolkit {

	Evidence createEvidence(DomibusConnectorEvidenceType type, MessageParameters params, DomibusConnectorRejectionReason rejectionReason, String details) throws DomibusConnectorEvidencesToolkitException;

	@Getter
	@Builder(toBuilder = true)
	class HashValue {
		String hash;
		String algorithm;
	}

	@Getter
	@Builder(toBuilder = true)
	class Evidence {
		@lombok.NonNull
		DomibusConnectorEvidenceType type;
		@lombok.NonNull
		byte[] evidence;
	}

	//TODO: merge this class with ECodexMessageDetails
	@Getter
	class MessageParameters {

		@NonNull private final String senderAddress;
		@NonNull private final String recipientAddress;
		@NonNull private final String nationalMessageId;
		@NonNull private final String ebmsMessageId;
		@NonNull private final List<Evidence> relatedEvidences;
		@NonNull
		private final DomibusConnectorEvidencesToolkit.HashValue businessDocumentHash;

		@Builder(toBuilder = true)
		MessageParameters(String senderAddress,
						  String recipientAddress,
						  String nationalMessageId,
						  String ebmsMessageId,
						  List<Evidence> relatedEvidences,
						  HashValue businessDocumentHash) {
			this.senderAddress = checkNotNullOrBlank("senderAddress", senderAddress);
			this.recipientAddress = checkNotNullOrBlank("recipienAddress", recipientAddress);
			this.nationalMessageId = checkNotNullOrBlank("nationalMessageId", nationalMessageId);
			this.ebmsMessageId = checkNotNullOrBlank("ebmsMessageId", ebmsMessageId);
			if (relatedEvidences == null) {
				throw new IllegalArgumentException("relatedEvidences is null!");
			}
			this.relatedEvidences = relatedEvidences;
			if (businessDocumentHash == null) {
				throw new IllegalArgumentException("businessDocumentHash is null!");
			}
			this.businessDocumentHash = businessDocumentHash;
		}

		static String checkNotNullOrBlank(String param, String instance) {

			if (instance == null) {
				throw new IllegalArgumentException(param + " is null!");
			}
			if (StringUtils.isBlank(instance)) {
				throw new IllegalArgumentException(param + " is blank");
			}
			return instance;
		}

		public ECodexMessageDetails getECodexMessageDetails() {
			ECodexMessageDetails d = new ECodexMessageDetails();
			d.setNationalMessageId(this.getNationalMessageId());
			d.setEbmsMessageId(this.getEbmsMessageId());
			d.setHashAlgorithm(this.getBusinessDocumentHash().getAlgorithm());
			d.setHashValue(this.getBusinessDocumentHash().getHash().getBytes());
			d.setSenderAddress(this.getSenderAddress());
			return d;
		}
	}


}
