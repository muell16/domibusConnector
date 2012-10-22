package eu.ecodex.signature;

import org.etsi.uri._02640.v2.REMEvidenceType;

public interface EvidenceUtils {
	
	public byte[] signByteArray(byte[] xmlData);
	public REMEvidenceType convertIntoEvidenceType(byte[] xmlData);

}
