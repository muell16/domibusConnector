/**
 * SendMessageFault.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package eu.ecodex.connector.generated;

public class SendMessageFault extends java.lang.Exception {

	private static final long serialVersionUID = 1342095331255L;

	private backend.ecodex.org.FaultDetail faultMessage;

	public SendMessageFault() {
		super("SendMessageFault");
	}

	public SendMessageFault(java.lang.String s) {
		super(s);
	}

	public SendMessageFault(java.lang.String s, java.lang.Throwable ex) {
		super(s, ex);
	}

	public SendMessageFault(java.lang.Throwable cause) {
		super(cause);
	}

	public void setFaultMessage(backend.ecodex.org.FaultDetail msg) {
		faultMessage = msg;
	}

	public backend.ecodex.org.FaultDetail getFaultMessage() {
		return faultMessage;
	}
}
