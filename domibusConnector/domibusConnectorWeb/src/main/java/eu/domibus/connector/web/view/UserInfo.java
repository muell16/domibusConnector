package eu.domibus.connector.web.view;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.component.LumoLabel;
import eu.domibus.connector.web.login.LoginView;

//@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@UIScope
@org.springframework.stereotype.Component
public class UserInfo extends HorizontalLayout {
	
	LumoLabel username  = new LumoLabel("");

	public UserInfo() {
		HorizontalLayout userDiv = new HorizontalLayout();
		Icon userIcon = new Icon(VaadinIcon.USER);
		userIcon.getStyle().set("margin-right", "10px");
		userIcon.setSize("20px");
		userDiv.add(userIcon);
		username = new LumoLabel("");
		username.getStyle().set("font-size", "15px");
		userDiv.add(username);
		add(userDiv);
		
		Div logoutDiv = new Div();
		logoutDiv.getStyle().set("text-align", "center");
		logoutDiv.getStyle().set("padding", "10px");
		Button logoutButton = new Button("Logout");
		logoutButton.addClickListener(e -> {
			Dialog logoutDialog = new Dialog();
			
			Div logout2Div = new Div();
			Label logoutText = new Label("Logout call success!");
			logoutText.getStyle().set("font-weight", "bold");
			logoutText.getStyle().set("color", "red");
			logout2Div.add(logoutText);
			logout2Div.getStyle().set("text-align", "center");
			logout2Div.setVisible(true);
			logoutDialog.add(logout2Div);
			
			Div okContent = new Div();
			okContent.getStyle().set("text-align", "center");
			okContent.getStyle().set("padding", "10px");
			Button okButton = new Button("OK");
			okButton.addClickListener(e2 -> {
				SecurityContextHolder.getContext().setAuthentication(null);
				
				logoutDialog.close();
				
				this.getUI().ifPresent(ui -> ui.navigate(LoginView.class));
			});
			okContent.add(okButton);
			
			
			logoutDialog.add(okContent);
			
			logoutDialog.open();
			
			
		});
		logoutDiv.add(logoutButton);
		add(logoutDiv);
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.END);
		setWidth("95%");
//		userBar.getStyle().set("padding-bottom", "16px");
	}

	public void setUsernameValue(String username) {
		this.username.setText(username);
	}

}
