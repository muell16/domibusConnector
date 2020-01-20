package eu.domibus.connector.web.viewAreas.users;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.dto.WebUser;

//@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@UIScope
@org.springframework.stereotype.Component
public class Users extends VerticalLayout {
	
	Div areaUserList = null;
	Div areaUserDetails = null;
	Div areaNewUser = null;
	
	Tab userListTab = new Tab("All Users");
	Tab userDetailsTab = new Tab("User Details");
	Tab newUserTab = new Tab("Add new User");
	
	Tabs userMenu = new Tabs();
	
	UserDetails userDetails;

	public Users(@Autowired UserList userList, @Autowired UserDetails userDetails, @Autowired NewUser newUser) {
		this.userDetails = userDetails;
		
		userList.setUsersView(this);
		
		areaUserList = new Div();
		areaUserList.add(userList);
		areaUserList.setVisible(true);
		
		areaUserDetails = new Div();
		areaUserDetails.add(userDetails);
		areaUserDetails.setVisible(false);
		
		areaNewUser = new Div();
		areaNewUser.add(newUser);
		areaNewUser.setVisible(false);
		
		Map<Tab, Component> tabsToPages = new HashMap<>();
		tabsToPages.put(userListTab, areaUserList);
		tabsToPages.put(userDetailsTab, areaUserDetails);
		tabsToPages.put(newUserTab, areaNewUser);
		
		
		userMenu.add(userListTab, userDetailsTab, newUserTab);
		
		
		Div pages = new Div(areaUserList, areaUserDetails, areaNewUser);
		
		Set<Component> pagesShown = Stream.of(areaUserList)
		        .collect(Collectors.toSet());
		
	
		userMenu.addSelectedChangeListener(event -> {
		    pagesShown.forEach(page -> page.setVisible(false));
		    pagesShown.clear();
		    Component selectedPage = tabsToPages.get(userMenu.getSelectedTab());
		    selectedPage.setVisible(true);
		    pagesShown.add(selectedPage);
		});

		add(userMenu,pages);
	}

	public void showUserDetails(WebUser user) {
//		userDetails.loadUserDetails(user);
		userDetails.setUser(user);
		userDetails.setAlignItems(Alignment.START);
		userMenu.setSelectedTab(userDetailsTab);
	}
}
