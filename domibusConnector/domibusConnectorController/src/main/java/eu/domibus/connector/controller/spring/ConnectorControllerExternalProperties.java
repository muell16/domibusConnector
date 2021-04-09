package eu.domibus.connector.controller.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="connector.controller")
@Component
public class ConnectorControllerExternalProperties {
	
	private boolean sendGeneratedEvidencesToBackend = true;

	public boolean isSendGeneratedEvidencesToBackend() {
		return sendGeneratedEvidencesToBackend;
	}

	public void setSendGeneratedEvidencesToBackend(boolean sendGeneratedEvidencesToBackend) {
		this.sendGeneratedEvidencesToBackend = sendGeneratedEvidencesToBackend;
	}



}
