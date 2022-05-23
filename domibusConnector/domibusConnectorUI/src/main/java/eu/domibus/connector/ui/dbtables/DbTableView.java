package eu.domibus.connector.ui.dbtables;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.ui.layout.DCMainLayout;

import java.util.List;

@UIScope
@Route(value = DbTableView.ROUTE, layout = DCMainLayout.class)
@PageTitle("domibusConnector - Administrator")
public class DbTableView extends VerticalLayout implements AfterNavigationObserver {
    public static final String ROUTE = "dbtables";

    private final DbTableService dbTableService;

    public DbTableView(DbTableService dbTableService) {
        this.dbTableService = dbTableService;

        initUI();
    }

    private void initUI() {
        List<String> tables = dbTableService.getTables();
        ComboBox<String> tableChooser = new ComboBox<>();
        tableChooser.setItems(tables);
        tableChooser.addValueChangeListener(this::selectedTableChanged);
        this.add(tableChooser);

    }

    private void selectedTableChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxStringComponentValueChangeEvent) {
        String value = comboBoxStringComponentValueChangeEvent.getValue();
        List<DbTableService.ColumnDefinition> columns = dbTableService.getColumns(value);

        initTable(columns);
    }

    private void initTable(List<DbTableService.ColumnDefinition> columns) {


        Grid g = new Grid();
        for (DbTableService.ColumnDefinition cd : columns) {
//            g.addCol
        }
    }


    @Override
    public void afterNavigation(AfterNavigationEvent event) {

    }
}
