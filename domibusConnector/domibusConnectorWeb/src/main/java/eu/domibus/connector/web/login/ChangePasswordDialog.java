package eu.domibus.connector.web.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.PasswordField;

import eu.domibus.connector.web.exception.UserLoginException;
import eu.domibus.connector.web.service.WebUserService;
import eu.domibus.connector.web.view.DashboardView;

public class ChangePasswordDialog extends Dialog {

	public ChangePasswordDialog(WebUserService userService, String username, String password) {
		Div changePasswordDiv = new Div();
		Label changePassword = new Label("Change Password for User "+username);
		changePassword.getStyle().set("font-weight", "bold");
		changePasswordDiv.add(changePassword);
		changePasswordDiv.getStyle().set("text-align", "center");
		changePasswordDiv.setVisible(true);
		add(changePasswordDiv);
		
		Div changePassword2Div = new Div();
		Label changePassword2 = new Label("Your password must be changed.");
		changePassword2Div.add(changePassword2);
		changePassword2Div.getStyle().set("text-align", "center");
		changePassword2Div.setVisible(true);
		add(changePassword2Div);
		
		Div currentPwDiv = new Div();
		PasswordField currentPw = new PasswordField();
		currentPw.setLabel("Current Password:");
		currentPw.setValue(password);
		currentPwDiv.add(currentPw);
		currentPwDiv.getStyle().set("text-align", "center");
		add(currentPwDiv);
		
		Div newPwDiv = new Div();
		PasswordField newPw = new PasswordField();
		newPw.setLabel("New Password:");
		newPwDiv.add(newPw);
		newPwDiv.getStyle().set("text-align", "center");
		add(newPwDiv);
		
		Div confirmNewPwDiv = new Div();
		PasswordField confirmNewPw = new PasswordField();
		confirmNewPw.setLabel("Confirm new Password:");
		confirmNewPwDiv.add(confirmNewPw);
		confirmNewPwDiv.getStyle().set("text-align", "center");
		add(confirmNewPwDiv);
		
		
		Div changePasswordButtonContent = new Div();
		changePasswordButtonContent.getStyle().set("text-align", "center");
		changePasswordButtonContent.getStyle().set("padding", "10px");
		Button changePasswordButton = new Button("Change Password");
		changePasswordButton.addClickListener(e -> {
			if(currentPw.isEmpty()) {
				Dialog errorDialog = new LoginErrorDialog("The Field 'Current Password' must not be empty!");
				errorDialog.open();
				return;
			}
			if(newPw.isEmpty()) {
				Dialog errorDialog = new LoginErrorDialog("The Field 'New Password' must not be empty!");
				errorDialog.open();
				return;
			}
			if(confirmNewPw.isEmpty()) {
				Dialog errorDialog = new LoginErrorDialog("The Field 'Confirm new Password' must not be empty!");
				errorDialog.open();
				return;
			}
			if(!newPw.getValue().equals(confirmNewPw.getValue())) {
				Dialog errorDialog = new LoginErrorDialog("The Fields 'New Password' and 'Confirm new Password' must have the same values!");
				newPw.clear();
				confirmNewPw.clear();
				errorDialog.open();
				return;
			}
			
			try {
				userService.changePasswordLogin(username, currentPw.getValue(), newPw.getValue());
			} catch (UserLoginException e1) {
				Dialog errorDialog = new LoginErrorDialog(e1.getMessage());
				currentPw.clear();
				newPw.clear();
				confirmNewPw.clear();
				errorDialog.open();
				return;
			}
			this.getUI().ifPresent(ui -> ui.navigate(DashboardView.class));
			close();
		});
		changePasswordButtonContent.add(changePasswordButton);
		
		
		add(changePasswordButtonContent);
	}

}
