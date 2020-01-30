package eu.domibus.connector.web.viewAreas.configuration;

import org.springframework.beans.factory.annotation.Qualifier;

@Qualifier("ConfigurationTab")
public @interface ConfigurationTab {

    String getTabTitle();

}
