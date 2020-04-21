package eu.domibus.connector.web.areas.info;

import com.vaadin.flow.router.Route;
import eu.domibus.connector.web.view.MainLayout;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.info.BuildProperties;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.component.LumoLabel;

//@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@UIScope
@org.springframework.stereotype.Component
@Route(value = Info.ROUTE, layout = MainLayout.class)
public class Info extends VerticalLayout implements InitializingBean {

	public static final String ROUTE = "info";

	@Autowired
	BuildProperties buildProperties;

	@Autowired
	DataSourceProperties dataSourceProperties;

	LumoLabel connectorVersion = new LumoLabel("");
	TextField dbUrlTextField;
	TextField dbDriverClassTextField;
	TextField dbUsernameTextField;
	
	public Info() {
		
		Div areaConnectorVersion = createConnectorVersionArea();
		
		Div areaConnectedDatabase = createConnectedDatabaseArea();
		
		add(areaConnectorVersion);
		add(areaConnectedDatabase);
	}

	private Div createConnectedDatabaseArea() {
		Div areaConnectedDatabase = new Div();
		
		VerticalLayout connectedDatabaseLayout = new VerticalLayout();
		
		LumoLabel header = new LumoLabel("Connected database information:");
		header.getStyle().set("font-size", "20px");
		
		connectedDatabaseLayout.add(header);
		
		dbDriverClassTextField = new TextField("Database Driver Class Name:");
		dbDriverClassTextField.setWidth("500px");
		dbDriverClassTextField.setReadOnly(true);
		
		connectedDatabaseLayout.add(dbDriverClassTextField);
		
		dbUrlTextField = new TextField("Database JDBC URL:");
		dbUrlTextField.setWidth("500px");
		dbUrlTextField.setReadOnly(true);
		
		connectedDatabaseLayout.add(dbUrlTextField);
		
		dbUsernameTextField = new TextField("Database Username:");
		dbUsernameTextField.setWidth("500px");
		dbUsernameTextField.setReadOnly(true);
		
		connectedDatabaseLayout.add(dbUsernameTextField);
		
		areaConnectedDatabase.add(connectedDatabaseLayout);
		
		return areaConnectedDatabase;
	}

	private Div createConnectorVersionArea() {
		Div areaConnectorVersion = new Div();
		
		HorizontalLayout connectorVersionLayout = new HorizontalLayout();
		
		LumoLabel connectorVersionLabel = new LumoLabel("domibusConnector Version:");
		connectorVersionLabel.getStyle().set("font-size", "20px");
		
//		connectorVersion = new TextField("domibusConnector Version: ");
//		connectorVersion.setReadOnly(true);
		
		connectorVersion.getStyle().set("font-size", "20px");
		
		connectorVersionLayout.add(connectorVersionLabel);
		connectorVersionLayout.add(connectorVersion);
		connectorVersionLayout.setPadding(true);
		
		areaConnectorVersion.add(connectorVersionLayout);
		areaConnectorVersion.setVisible(true);
		return areaConnectorVersion;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		connectorVersion.setText("Version: " + buildProperties.getVersion() + " build time: " + buildProperties.getTime());
		dbUrlTextField.setValue(dataSourceProperties.getUrl());
		dbDriverClassTextField.setValue(dataSourceProperties.getDriverClassName());
		dbUsernameTextField.setValue(dataSourceProperties.getUsername());
	}

	
}
