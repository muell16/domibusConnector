package eu.domibus.connector.web.view.areas.users;

import java.util.EnumSet;
import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.Route;
import eu.domibus.connector.web.view.areas.configuration.UserTab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.component.LumoLabel;
import eu.domibus.connector.web.dto.WebUser;
import eu.domibus.connector.web.enums.UserRole;
import eu.domibus.connector.web.service.WebUserService;

//@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@Component
@UIScope
@Route(value = NewUser.ROUTE, layout = UserLayout.class)
@UserTab(title = "Add new User")
public class NewUser extends VerticalLayout {

	public static final String ROUTE = "new";

	private WebUserService userService;

	TextField username = new TextField("Username");
	TextField initialPassword = new TextField("Initial password");
	ComboBox<UserRole> role = new ComboBox<UserRole>();
	
	
	public NewUser(@Autowired WebUserService service) {
		this.userService = service;
		initialPassword.setMinLength(4);
		
		VerticalLayout newUserArea = new VerticalLayout();
		
		
		newUserArea.add(username);
		
		role.setLabel("Role");
		role.setItems(EnumSet.allOf(UserRole.class));
		
		newUserArea.add(role);
		
		newUserArea.add(initialPassword);
		
		Div createUserResult = new Div();
		
		LumoLabel resultLabel = new LumoLabel("");
		resultLabel.getStyle().set("font-size", "20px");
		createUserResult.add(resultLabel);
//		createUserResult.setHeight("100vh");
		createUserResult.setWidth("100vw");
		
		Button createUser = new Button(
				new Icon(VaadinIcon.USER_CHECK));
		createUser.setText("Create User");
		createUser.addClickListener(e -> {
			Optional<String> username = createUser();
			if(username.isPresent()) {
				resultLabel.setText("The user was successfully created!");
				resultLabel.getStyle().set("color", "green");
				UI.getCurrent().navigate(UserDetails.class, username.get());
			}else {
				resultLabel.setText("The creation of the user failed!");
				resultLabel.getStyle().set("color", "red");
			}
			});
		newUserArea.add(createUser);
		
		
//		setHeight("100vh");
		setWidth("100vw");
		add(newUserArea);
		add(createUserResult);
	}
	
	private Optional<String> createUser() {

		// TODO: this just a little bit of validation - one could create users with empty string
		if (username.getValue().isEmpty() || initialPassword.isEmpty() || initialPassword.getValue().length() < initialPassword.getMinLength()) {
			return Optional.empty();
		}

		WebUser newUser = new WebUser();
		
		newUser.setUsername(username.getValue());
		newUser.setRole(role.getValue());
		newUser.setPassword(initialPassword.getValue());

		return userService.createNewUser(newUser) ?
				Optional.of(username.getValue()) :
				Optional.empty();
	}

}
