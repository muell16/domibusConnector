package eu.ecodex.connector.fsc;

import java.io.File;

public class ECodexConnectorGatewayFilesystemClientImpl implements ECodexConnectorGatewayFilesystemClient
{
	private File messageFolder = null;
	
	public ECodexConnectorGatewayFilesystemClientImpl(String pathToMessageFolder) {
		messageFolder = new File(pathToMessageFolder);
				
	}	
	
	@Override
	public String[] listPendingMessages() {
		
		
		
		return null;
	}

	@Override
	public byte[] downloadMessage(String messageId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
