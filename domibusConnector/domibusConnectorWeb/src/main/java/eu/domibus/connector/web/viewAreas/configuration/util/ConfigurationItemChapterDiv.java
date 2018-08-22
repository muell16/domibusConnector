package eu.domibus.connector.web.viewAreas.configuration.util;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;

public class ConfigurationItemChapterDiv extends Div {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfigurationItemChapterDiv(String chapterTitle) {
		Label chapterLabel = new Label(chapterTitle);
		chapterLabel.getStyle().set("font-size", "20px");
		
		chapterLabel.getStyle().set("font-style", "italic");
		
		super.add(chapterLabel);
	}

	

}
