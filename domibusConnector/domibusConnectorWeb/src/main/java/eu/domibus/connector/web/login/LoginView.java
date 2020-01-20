package eu.domibus.connector.web.login;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyPressEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import eu.domibus.connector.web.exception.InitialPasswordException;
import eu.domibus.connector.web.exception.UserLoginException;
import eu.domibus.connector.web.service.WebUserService;
import eu.domibus.connector.web.view.DomibusConnectorAdminHeader;
import eu.domibus.connector.web.view.MainView;

//@HtmlImport("styles/shared-styles.html")
@Route("login/")
@PageTitle("domibusConnector - Login")
public class LoginView extends VerticalLayout{
	
	private LoginOverlay login = new LoginOverlay();
	
	public LoginView(@Autowired WebUserService userService, @Autowired DomibusConnectorAdminHeader header) {
		
		login.setAction("login"); // 
        getElement().appendChild(login.getElement()); 
		
		add(header);
		
		HorizontalLayout login = new HorizontalLayout();
		VerticalLayout loginArea = new VerticalLayout();
		
		Button loginButton = new Button("Login");
		
		Div usernameDiv = new Div();
		TextField username = new TextField();
		username.setLabel("Username");
		username.setAutofocus(true);
		username.addKeyPressListener(Key.ENTER, new ComponentEventListener<KeyPressEvent>() {
			
			@Override
			public void onComponentEvent(KeyPressEvent event) {
				loginButton.click();
				
			}
		});
		usernameDiv.add(username);
		usernameDiv.getStyle().set("text-align", "center");
		loginArea.add(usernameDiv);
		
		
		Div passwordDiv = new Div();
		PasswordField password = new PasswordField();
		password.setLabel("Password");
		password.addKeyPressListener(Key.ENTER, new ComponentEventListener<KeyPressEvent>() {
			
			@Override
			public void onComponentEvent(KeyPressEvent event) {
				loginButton.click();
				
			}
		});
		passwordDiv.add(password);
		passwordDiv.getStyle().set("text-align", "center");
		loginArea.add(passwordDiv);
		
		
		Div loginButtonContent = new Div();
		loginButtonContent.getStyle().set("text-align", "center");
		loginButtonContent.getStyle().set("padding", "10px");
		
		loginButton.addClickListener(e -> {
			if(username.getValue().isEmpty()) {
				Dialog errorDialog = new LoginErrorDialog("The field \"Username\" must not be empty!");
				username.clear();
				password.clear();
				errorDialog.open();
				return;
			}
			if(password.getValue().isEmpty()) {
				Dialog errorDialog = new LoginErrorDialog("The field \"Password\" must not be empty!");
				password.clear();
				errorDialog.open();
				return;
			}
			try {
				userService.login(username.getValue(), password.getValue());
			} catch (UserLoginException e1) {
				Dialog errorDialog = new LoginErrorDialog(e1.getMessage());
				username.clear();
				password.clear();
				errorDialog.open();
				return;
			} catch (InitialPasswordException e1) {
				Dialog changePasswordDialog = new ChangePasswordDialog(userService,username.getValue(), password.getValue());
				username.clear();
				password.clear();
//				close();
				changePasswordDialog.open();
			}
			this.getUI().ifPresent(ui -> ui.navigate(MainView.class));
//			close();
		});
		loginButtonContent.add(loginButton);
		
		Button changePasswordButton = new Button("Change Password");
		changePasswordButton.addClickListener(e -> {
			if(username.getValue().isEmpty()) {
				Dialog errorDialog = new LoginErrorDialog("The field \"Username\" must not be empty!");
				username.clear();
				password.clear();
				errorDialog.open();
				return;
			}
			Dialog changePasswordDialog = new ChangePasswordDialog(userService,username.getValue(), password.getValue());
			username.clear();
			password.clear();
//			close();
			changePasswordDialog.open();
		});
		loginButtonContent.add(changePasswordButton);
		
		
		loginArea.add(loginButtonContent);
		
		loginArea.setSizeFull();
		loginArea.setAlignItems(Alignment.CENTER);
		loginArea.getStyle().set("align-items", "center");
		login.add(loginArea);
		login.setVerticalComponentAlignment(Alignment.CENTER, loginArea);
		
		add(loginArea);
		
//		Dialog loginDialog = new LoginDialog(userService);
//		
//		loginDialog.open();
	}
	
}
