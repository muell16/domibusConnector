package eu.domibus.connector.web.viewAreas.configuration;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@Component
@UIScope
public class BackendConfiguration  extends VerticalLayout {

	/**
	 * This class should handle the following:
	 * adding, deleting,... backends (enter the corresponding DB entries).
	 * 
	 * Loading and editing the properties for backends (see properties below).
	 * 
	 * If backends are added or edited, also add or edit the keys in truststore.
	 * 
	 * 
	 * #############################  Properties for backend  #############################
		#defines the location of the backend keyStore:
		connector.backend.ws.key.store.path=file:C:/Entwicklung/EXEC-Installation-Workshop/EXECUser1/configuration/keystores/connector-backend-keystore.jks
		#defines the key store password:
		connector.backend.ws.key.store.password=12345
		#defines the key alias for the key which is used to sign the messages:
		connector.backend.ws.key.key.alias=connector-backend
		#defines the key password:
		connector.backend.ws.key.key.password=12345
		
		connector.backend.ws.trust.store.path=file:C:/Entwicklung/EXEC-Installation-Workshop/EXECUser1/configuration/keystores/connector-backend-keystore.jks
		connector.backend.ws.trust.store.password=12345

	 * 
	 */
	public BackendConfiguration() {
		// TODO Auto-generated constructor stub
	}

}
