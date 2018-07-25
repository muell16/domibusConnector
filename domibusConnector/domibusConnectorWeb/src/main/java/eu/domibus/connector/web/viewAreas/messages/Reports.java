package eu.domibus.connector.web.viewAreas.messages;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.FooterRow.FooterCell;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.dto.WebReport;
import eu.domibus.connector.web.dto.WebReportEntry;
import eu.domibus.connector.web.service.WebReportsService;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@Component
@UIScope
public class Reports extends VerticalLayout {
	
	private WebReportsService reportsService;
	
	Date fromDateValue;
	Date toDateValue;
	boolean includeEvidencesValue;
	
	VerticalLayout reportDataArea = new VerticalLayout(); 

	public Reports(@Autowired WebReportsService reportsService) {
		
		this.reportsService = reportsService;
		
		VerticalLayout reportFormArea = new VerticalLayout(); 
		
		Div details = new Div();
		details.setWidth("300px");
		
		DatePicker fromDate = new DatePicker();
		fromDate.setLocale(Locale.ENGLISH);
		fromDate.setLabel("From Date");
		fromDate.setErrorMessage("From Date invalid!");
		fromDate.addValueChangeListener(e1 -> updateFromDate(fromDate));
		details.add(fromDate);
		
		DatePicker toDate = new DatePicker();
		toDate.setLocale(Locale.ENGLISH);
		toDate.setLabel("To Date");
		toDate.setErrorMessage("To Date invalid!");
		toDate.addValueChangeListener(e2 -> updateToDate(toDate));
		toDate.setEnabled(true);
		details.add(toDate);
		
		Checkbox includeEvidences = new Checkbox();
		includeEvidences.setLabel("Include sent evidences as messages");
		includeEvidences.addValueChangeListener(e -> includeEvidencesValue = includeEvidences.getValue().booleanValue());
		details.add(includeEvidences);
		
		Button submit = new Button();
		submit.addClickListener(e -> generateReport());
		submit.setText("Generate Report");
		details.add(submit);
		
		reportFormArea.add(details);
		//setAlignItems(Alignment.START);
		setSizeFull();
//		reportFormArea.setHeight("100vh");
		reportFormArea.setWidth("300px");
		add(reportFormArea);

		setHeight("100vh");
	}
	
	

	private void updateToDate(DatePicker value) {
		toDateValue = asDate(value.getValue());
	}



	private void updateFromDate(DatePicker value) {
		fromDateValue = asDate(value.getValue());
	}



	private void generateReport() {
		
//		System.out.println("Here! "+fromDateValue + toDateValue + includeEvidencesValue);
		
		List<WebReportEntry> generatedReport = reportsService.generateReport(fromDateValue, toDateValue, includeEvidencesValue);
		
		if(!CollectionUtils.isEmpty(generatedReport)) {
			reportDataArea.removeAll();
			
			List<WebReport> sortReport = sortReport(generatedReport);
			
			for(WebReport report:sortReport) {
				Div details = new Div();
				details.setWidth("100vw");
				
				Grid<WebReportEntry> grid = new Grid<>();
				
				grid.setItems(report.getEntries());
				
				Column<WebReportEntry> partyCol = grid.addColumn(WebReportEntry::getParty).setHeader("Party").setWidth("200px");
				Column<WebReportEntry> serviceCol = grid.addColumn(WebReportEntry::getService).setHeader("Service").setWidth("300px");
				Column<WebReportEntry> receivedCol = grid.addColumn(WebReportEntry::getReceived).setHeader("Messages received from").setWidth("300px");
				Column<WebReportEntry> sentCol = grid.addColumn(WebReportEntry::getSent).setHeader("Messages sent to").setWidth("300px");
				
				HeaderRow topRow = grid.prependHeaderRow();

				HeaderCell informationCell = topRow.join(partyCol, serviceCol, receivedCol, sentCol);
				informationCell.setText(report.getPeriod());
				
				FooterRow footerRow = grid.appendFooterRow();
				FooterCell totalsCell = footerRow.getCell(serviceCol);
				totalsCell.setText("Total:");
				FooterCell receivedCell = footerRow.getCell(receivedCol);
				receivedCell.setText(Long.toString(report.getSumReceived()));
				FooterCell sentCell = footerRow.getCell(sentCol);
				sentCell.setText(Long.toString(report.getSumSent()));
				
				grid.setWidth("1150px");
				grid.setHeight("150px");
				grid.setMultiSort(true);
				
				for(Column<WebReportEntry> col : grid.getColumns()) {
					col.setSortable(true);
					col.setResizable(true);
				}
				details.add(grid);
				
				reportDataArea.add(details);
				
			}
			
			reportDataArea.setHeight("100vh");
			reportDataArea.setWidth("100vw");
			add(reportDataArea);
		}
	}
	
	private List<WebReport> sortReport(List<WebReportEntry> result) {
		Map<String, WebReport> periodMap = new HashMap<String, WebReport>();

        for (WebReportEntry entry : result) {
            String period = entry.getMonth() + "/" + entry.getYear();
            if (!periodMap.containsKey(period)) {
            	WebReport p = new WebReport();
                p.setPeriod(period);
                p.setYear(entry.getYear());
                p.setMonth(entry.getMonth().length() > 1 ? entry.getMonth() : "0" + entry.getMonth());
                p.setEntries(new ArrayList<WebReportEntry>());
                periodMap.put(period, p);
            }

            periodMap.get(period).getEntries().add(entry);
            periodMap.get(period).setSumReceived(periodMap.get(period).getSumReceived() + entry.getReceived());
            periodMap.get(period).setSumSent(periodMap.get(period).getSumSent() + entry.getSent());
        }

        List<WebReport> periods = new ArrayList<WebReport>(periodMap.size());
        periods.addAll(periodMap.values());

        Collections.sort(periods, new Comparator<WebReport>() {
            @Override
            public int compare(WebReport c1, WebReport c2) {

                String p1 = c1.getYear() + c1.getMonth();
                String p2 = c2.getYear() + c2.getMonth();

                return p1.compareTo(p2);
            }
        });
        
        return periods;
	}
	
	public static Date asDate(LocalDate localDate) {
	    return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	  }

}
