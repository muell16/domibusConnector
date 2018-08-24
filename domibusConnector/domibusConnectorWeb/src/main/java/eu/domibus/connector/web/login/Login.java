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

	public Login(@Autowired WebUserService userService) {
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
		TextField password = new TextField();
		password.setLabel("Password");
		passwordDiv.add(password);
		passwordDiv.getStyle().set("text-align", "center");
		loginDialog.add(passwordDiv);
		
//		Div loginExceptionDiv = new Div();
//		Label loginException = new Label();
//		loginException.getStyle().set("font-weight", "bold");
//		loginException.getStyle().set("color", "grey");
//		loginExceptionDiv.add(loginException);
//		loginExceptionDiv.getStyle().set("text-align", "center");
//		loginExceptionDiv.setVisible(false);
		
		
		Div loginButtonContent = new Div();
		loginButtonContent.getStyle().set("text-align", "center");
		loginButtonContent.getStyle().set("padding", "10px");
		Button loginButton = new Button("Login");
		loginButton.addClickListener(e -> {
			try {
				userService.login(username.getValue(), password.getValue());
			} catch (UserLoginException e1) {
				Dialog errorDialog = new Dialog();
				
				Div loginExceptionDiv = new Div();
				Label loginException = new Label(e1.getMessage());
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
					username.clear();
					password.clear();
					errorDialog.close();
				});
				okContent.add(okButton);
				
				
				errorDialog.add(okContent);
				errorDialog.open();
				return;
			} catch (InitialPasswordException e1) {
				
				e1.printStackTrace();
			}
			this.getUI().ifPresent(ui -> ui.navigate(MainView.class));
			loginDialog.close();
		});
		loginButtonContent.add(loginButton);
		
		
		loginDialog.add(loginButtonContent);
		
		loginDialog.open();
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
