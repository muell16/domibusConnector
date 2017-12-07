package eu.domibus.webadmin.blogic.connector.statistics;

import org.primefaces.model.chart.PieChartModel;

public interface IConnectorSummaryService {

    public void generateMessageSummary();

	PieChartModel getPieModelMessageSummary();

	PieChartModel getPieModelServiceSummary();

}