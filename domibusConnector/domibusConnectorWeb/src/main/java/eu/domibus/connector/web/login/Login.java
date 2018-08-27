package eu.domibus.connector.web.login;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import eu.domibus.connector.web.MainView;
import eu.domibus.connector.web.exception.InitialPasswordException;
import eu.domibus.connector.web.exception.UserLoginException;
import eu.domibus.connector.web.service.WebUserService;

@HtmlImport("styles/shared-styles.html")
@Route("domibusConnector/login/")
@PageTitle("domibusConnector - Login")
public class Login extends VerticalLayout{

	WebUserService userService;
	
	public Login(@Autowired WebUserService userService) {
		this.userService = userService;
		HorizontalLayout header = createHeader();
		add(header);
		
		Dialog loginDialog = new Dialog();
		
		
		Div usernameDiv = new Div();
		TextField username = new TextField();
		username.setLabel("Username");
		usernameDiv.add(username);
		usernameDiv.getStyle().set("text-align", "center");
		loginDialog.add(usernameDiv);
		
		Div passwordDiv = new Div();
		PasswordField password = new PasswordField();
		password.setLabel("Password");
		passwordDiv.add(password);
		passwordDiv.getStyle().set("text-align", "center");
		loginDialog.add(passwordDiv);
		
		
		Div loginButtonContent = new Div();
		loginButtonContent.getStyle().set("text-align", "center");
		loginButtonContent.getStyle().set("padding", "10px");
		
		Button loginButton = new Button("Login");
		loginButton.addClickListener(e -> {
			try {
				userService.login(username.getValue(), password.getValue());
			} catch (UserLoginException e1) {
				Dialog errorDialog = createLoginErrorDialog(e1.getMessage());
				username.clear();
				password.clear();
				errorDialog.open();
				return;
			} catch (InitialPasswordException e1) {
				Dialog changePasswordDialog = createChangePasswordDialog(username.getValue(), password.getValue());
				username.clear();
				password.clear();
				loginDialog.close();
				changePasswordDialog.open();
			}
			this.getUI().ifPresent(ui -> ui.navigate(MainView.class));
			loginDialog.close();
		});
		loginButtonContent.add(loginButton);
		
		Button changePasswordButton = new Button("Change Password");
		changePasswordButton.addClickListener(e -> {
			Dialog changePasswordDialog = createChangePasswordDialog(username.getValue(), password.getValue());
			username.clear();
			password.clear();
			loginDialog.close();
			changePasswordDialog.open();
		});
		loginButtonContent.add(changePasswordButton);
		
		
		loginDialog.add(loginButtonContent);
		
		loginDialog.open();
	}
	
	private Dialog createChangePasswordDialog(String username, String password) {
		Dialog changePasswordDialog = new Dialog();
		
		Div changePasswordDiv = new Div();
		Label changePassword = new Label("Change Password for User "+username);
		changePassword.getStyle().set("font-weight", "bold");
		changePasswordDiv.add(changePassword);
		changePasswordDiv.getStyle().set("text-align", "center");
		changePasswordDiv.setVisible(true);
		changePasswordDialog.add(changePasswordDiv);
		
		Div changePassword2Div = new Div();
		Label changePassword2 = new Label("Your password must be changed.");
		changePassword2Div.add(changePassword2);
		changePassword2Div.getStyle().set("text-align", "center");
		changePassword2Div.setVisible(true);
		changePasswordDialog.add(changePassword2Div);
		
		Div currentPwDiv = new Div();
		PasswordField currentPw = new PasswordField();
		currentPw.setLabel("Current Password:");
		currentPw.setValue(password);
		currentPwDiv.add(currentPw);
		currentPwDiv.getStyle().set("text-align", "center");
		changePasswordDialog.add(currentPwDiv);
		
		Div newPwDiv = new Div();
		PasswordField newPw = new PasswordField();
		newPw.setLabel("New Password:");
		newPwDiv.add(newPw);
		newPwDiv.getStyle().set("text-align", "center");
		changePasswordDialog.add(newPwDiv);
		
		Div confirmNewPwDiv = new Div();
		PasswordField confirmNewPw = new PasswordField();
		confirmNewPw.setLabel("Confirm new Password:");
		confirmNewPwDiv.add(confirmNewPw);
		confirmNewPwDiv.getStyle().set("text-align", "center");
		changePasswordDialog.add(confirmNewPwDiv);
		
		
		Div changePasswordButtonContent = new Div();
		changePasswordButtonContent.getStyle().set("text-align", "center");
		changePasswordButtonContent.getStyle().set("padding", "10px");
		Button changePasswordButton = new Button("Change Password");
		changePasswordButton.addClickListener(e -> {
			if(currentPw.isEmpty()) {
				Dialog errorDialog = createLoginErrorDialog("The Field 'Current Password' must not be empty!");
				errorDialog.open();
				return;
			}
			if(newPw.isEmpty()) {
				Dialog errorDialog = createLoginErrorDialog("The Field 'New Password' must not be empty!");
				errorDialog.open();
				return;
			}
			if(confirmNewPw.isEmpty()) {
				Dialog errorDialog = createLoginErrorDialog("The Field 'Confirm new Password' must not be empty!");
				errorDialog.open();
				return;
			}
			if(!newPw.getValue().equals(confirmNewPw.getValue())) {
				Dialog errorDialog = createLoginErrorDialog("The Fields 'New Password' and 'Confirm new Password' must have the same values!");
				newPw.clear();
				confirmNewPw.clear();
				errorDialog.open();
				return;
			}
			
			try {
				userService.changePasswordLogin(username, currentPw.getValue(), newPw.getValue());
			} catch (UserLoginException e1) {
				Dialog errorDialog = createLoginErrorDialog(e1.getMessage());
				currentPw.clear();
				newPw.clear();
				confirmNewPw.clear();
				errorDialog.open();
				return;
			}
			this.getUI().ifPresent(ui -> ui.navigate(MainView.class));
			changePasswordDialog.close();
		});
		changePasswordButtonContent.add(changePasswordButton);
		
		
		changePasswordDialog.add(changePasswordButtonContent);
		
		return changePasswordDialog;
	}

	private Dialog createLoginErrorDialog(String errorMessage) {
		Dialog errorDialog = new Dialog();
		
		Div loginExceptionDiv = new Div();
		Label loginException = new Label(errorMessage);
		loginException.getStyle().set("font-weight", "bold");
		loginException.getStyle().set("color", "red");
		loginExceptionDiv.add(loginException);
		loginExceptionDiv.getStyle().set("text-align", "center");
		loginExceptionDiv.setVisible(true);
		errorDialog.add(loginExceptionDiv);
		
		Div okContent = new Div();
		okContent.getStyle().set("text-align", "center");
		okContent.getStyle().set("padding", "10px");
		Button okButton = new Button("OK");
		okButton.addClickListener(e2 -> {
			
			errorDialog.close();
		});
		okContent.add(okButton);
		
		
		errorDialog.add(okContent);
		return errorDialog;
	}
	
	private HorizontalLayout createHeader() {
		
		Div ecodexLogo = new Div();
		Image ecodex = new Image("frontend/images/logo_ecodex_0.png", "eCodex");
		ecodex.setHeight("70px");
		ecodexLogo.add(ecodex);
		ecodexLogo.setHeight("70px");
		
		
		Div domibusConnector = new Div();
		Label dC = new Label("domibusConnector - Administration");
		dC.getStyle().set("font-size", "30px");
		dC.getStyle().set("font-style", "italic");
		dC.getStyle().set("color", "grey");
		domibusConnector.add(dC);
		domibusConnector.getStyle().set("text-align", "center");
		
		
		Div europaLogo = new Div();
		Image europa = new Image("frontend/images/europa-logo.jpg", "europe");
		europa.setHeight("50px");
		europaLogo.add(europa);
		europaLogo.setHeight("50px");
		europaLogo.getStyle().set("margin-right", "3em");
		
		
		HorizontalLayout headerLayout = new HorizontalLayout(ecodexLogo, domibusConnector, europaLogo);
		headerLayout.setAlignItems(Alignment.CENTER);
		headerLayout.expand(domibusConnector);
		headerLayout.setJustifyContentMode(com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.CENTER);
		headerLayout.setWidth("100%");
//		headerLayout.getStyle().set("border-bottom", "1px solid #9E9E9E");
		headerLayout.getStyle().set("padding-bottom", "16px");
		
		return headerLayout;
	}

}
