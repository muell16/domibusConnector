package eu.domibus.connector.ui.dbtables;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.grid.editor.EditorSaveEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.ui.layout.DCMainLayout;
import eu.domibus.connector.ui.utils.RoleRequired;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@UIScope
@Route(value = DbTableView.ROUTE, layout = DCMainLayout.class)
@PageTitle("domibusConnector - Administrator")
@RoleRequired(role = "ADMIN")
public class DbTableView extends VerticalLayout implements AfterNavigationObserver {
    public static final String ROUTE = "dbtables";

    private final DbTableService dbTableService;

    private Div gridDiv = new Div();

    Binder<DbTableService.ColumnRow> binder = new Binder<>();

    public DbTableView(Optional<DbTableService> dbTableService) {
        this.dbTableService = dbTableService.orElse(null);

        if (dbTableService == null) {
            add(new Label("Hidden DB table view is not activated!"));
        } else {
            initUI();
        }

    }

    private void initUI() {
        List<String> tables = dbTableService.getTables();
        ComboBox<String> tableChooser = new ComboBox<>();
        tableChooser.setItems(tables);
        tableChooser.addValueChangeListener(this::selectedTableChanged);
        this.add(tableChooser);
        Button addButton = new Button("Add Row");
        addButton.addClickListener(this::addButtonClicked);
        this.add(addButton);
        this.add(gridDiv);
        gridDiv.setSizeFull();

    }

    private void addButtonClicked(ClickEvent<Button> buttonClickEvent) {
//        grid.
        //TODO: open dialog and add new row...
    }

    Grid<DbTableService.ColumnRow> grid;
    private void selectedTableChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxStringComponentValueChangeEvent) {
        String tableName = comboBoxStringComponentValueChangeEvent.getValue();
        grid = createGrid(tableName);

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        gridDiv.removeAll();
        gridDiv.add(grid);

    }

    private Grid<DbTableService.ColumnRow> createGrid(String tableName) {
        DbTableService.TableDefinition tableDefinition = dbTableService.getTableDefinition(tableName);
        Collection<DbTableService.ColumnDefinition> columns = tableDefinition.getColumnDefinitionMap().values();


        Grid<DbTableService.ColumnRow> grid = new Grid<>();


        final Grid.Column<DbTableService.ColumnRow> editColumn = grid.addComponentColumn(row -> this.createEditButton(grid, row));

        for (DbTableService.ColumnDefinition cd : columns) {
            TextField tf = new TextField(cd.getColumnName());

            Grid.Column<DbTableService.ColumnRow> columnRowColumn = grid.addColumn((ValueProvider<DbTableService.ColumnRow, Object>) m -> m.getCell(cd.getColumnName()));
            columnRowColumn.setHeader(cd.getColumnName())
                    .setResizable(true)
                    .setFooter(cd.getColumnName());
            columnRowColumn.setEditorComponent(tf);


//            horizontalLayout.add(tf);
            binder.bind(tf, new ValueProvider<DbTableService.ColumnRow, String>() {
                @Override
                public String apply(DbTableService.ColumnRow columnRow) {
                    return Objects.toString(columnRow.getCell(cd.getColumnName()));
                }
            }, new Setter<DbTableService.ColumnRow, String>() {
                @Override
                public void accept(DbTableService.ColumnRow columnRow, String s) {
                    columnRow.setCell(cd.getColumnName(), s);
                }
            });
        }


        grid.setDataProvider(dbTableService.getDataProvider(tableDefinition));
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addSelectionListener(this::rowSelected);

        Editor<DbTableService.ColumnRow> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);
        editor.addSaveListener(this::saveChangedRow);

        Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
            editor.save();
        });
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                e -> editor.cancel());
        Button deleteButton = new Button(VaadinIcon.TRASH.create(), e -> this.deleteRow(e, editor.getItem()));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton, deleteButton);
        editColumn.setEditorComponent(actions);

        return grid;

    }

    private Button createEditButton(Grid grid, DbTableService.ColumnRow columnRow) {
        final Button edit = new Button(new Icon(VaadinIcon.PENCIL));
        edit.addClickListener(event -> {
            final Editor<DbTableService.ColumnRow> editor = grid.getEditor();
            if (editor.isOpen()) {
                editor.cancel();
            }
            editor.editItem(columnRow);
        });
        return edit;
    }

    private void deleteRow(ClickEvent<Button> buttonClickEvent, DbTableService.ColumnRow columnRow) {
        try {
            dbTableService.deleteColumn(columnRow);
        } catch (Exception e) { //TODO: update exception handling
            Notification.show("Error deleting row!" + e.getLocalizedMessage());
        }
    }

    private void saveChangedRow(EditorSaveEvent<DbTableService.ColumnRow> columnRowEditorSaveEvent) {
        DbTableService.ColumnRow item = columnRowEditorSaveEvent.getItem();
        try {
            dbTableService.updateColumn(item);
        } catch (Exception e) { //TODO: update exception handling
//            columnRowEditorSaveEvent.getSource().
            Notification.show("Error updating changed row!" + e.getLocalizedMessage());
        }
    }



    private void rowSelected(SelectionEvent<Grid<DbTableService.ColumnRow>, DbTableService.ColumnRow> gridColumnRowSelectionEvent) {

    }


    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        //nothing yet
    }

}
