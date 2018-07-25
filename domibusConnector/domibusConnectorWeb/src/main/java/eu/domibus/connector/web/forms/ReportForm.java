package eu.domibus.connector.web.forms;

import java.util.Date;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.formlayout.FormLayout;

@HtmlImport("styles/shared-styles.html")
public class ReportForm extends FormLayout {

	private DatePicker fromDate = new DatePicker();
    private DatePicker toDate = new DatePicker();
    private Checkbox includeEvidences = new Checkbox();
    private Button submit = new Button();
    
	public ReportForm() {
		addFormItem(fromDate, "From Date"); 
		addFormItem(toDate, "To Date");
		addFormItem(includeEvidences, "Include sent evidences as messages");
		addFormItem(submit, "Submit");
	}

}
