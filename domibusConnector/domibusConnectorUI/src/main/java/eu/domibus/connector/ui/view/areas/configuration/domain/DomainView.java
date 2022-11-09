package eu.domibus.connector.ui.view.areas.configuration.domain;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.ui.layout.DCVerticalLayoutWithTitleAndHelpButton;
import eu.domibus.connector.ui.utils.RoleRequired;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;
import eu.ecodex.dc5.core.model.DC5Domain;
import eu.ecodex.dc5.core.repository.DC5DomainRepo;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = DomainView.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
@TabMetadata(title = "Domain Configuration", tabGroup = ConfigurationLayout.TAB_GROUP_NAME)
@Order(99) // TODO
public class DomainView extends DCVerticalLayoutWithTitleAndHelpButton implements AfterNavigationObserver {

    public static final String ROUTE = "domain";

    public static final String TITLE = "Domain Configuration";
    public static final String HELP_ID = "ui/configuration/TODO_NOT_VALID.html"; // TODO

    private final DC5DomainRepo domainRepo;

    public DomainView(DC5DomainRepo domainRepo) {
        super(HELP_ID, TITLE);
        this.domainRepo = domainRepo;

        domainGrid = initGrid();
        addCourtDialog = initAddCourtDialog();
        add(createAddMatrixEntryButtonDiv(), domainGrid);
    }

    private Grid<DC5Domain> domainGrid;
    private final Dialog addCourtDialog;

    private Grid<DC5Domain> initGrid() {
        // Grid Settings
        final Grid<DC5Domain> grid = new Grid<>();
        grid.setAllRowsVisible(true);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);

        // Grid Layout
        final Grid.Column<DC5Domain> editColumn =
                grid.addComponentColumn(this::createEditButton)
                        .setHeader("edit")
                        .setWidth("160px")
                        .setFlexGrow(0)
                        .setTextAlign(ColumnTextAlign.CENTER);

        final Grid.Column<DC5Domain> nameColumn =
                grid.addColumn(DC5Domain::getName)
                        .setKey("getName")
                        .setHeader("Name")
                        .setSortable(true);

        final Grid.Column<DC5Domain> domainMsgLaneIdColumn =
                grid.addColumn(DC5Domain::getBusinessDomainId)
                        .setKey("getBusinessDomainId")
                        .setHeader("Business / Message Lane ID")
                        .setSortable(true);


        // Row editing configuration
        Binder<DC5Domain> binder = new Binder<>(DC5Domain.class);
        final Editor<DC5Domain> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        editor.addSaveListener(e -> {
            domainRepo.save(e.getItem());
        });

        Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
            editor.save();
        });
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                e -> editor.cancel());
        Button deleteButton = new Button(VaadinIcon.TRASH.create(),
                e -> {
                    domainRepo.delete(editor.getItem());
                    domainGrid.setItems(domainRepo.findAll());
                    editor.cancel();
                });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton, deleteButton);
        actions.setPadding(false);
        editColumn.setEditorComponent(actions);

        TextField nameField = new TextField();

        nameColumn.setEditorComponent(nameField);

        binder.forField(nameField)
                .asRequired("A domain name is required!")
                .bind(DC5Domain::getName, DC5Domain::setName);

        TextField domainMsgLaneIdTextField = new TextField();

        domainMsgLaneIdColumn.setEditorComponent(domainMsgLaneIdTextField);

        binder.forField(domainMsgLaneIdTextField)
                .asRequired("A domain ID is required!")
                // TODO: probably check uniqueness here
                .bind(DC5Domain::getBusinessDomainId, DC5Domain::setBusinessDomainId);

        return grid;
    }

    private Dialog initAddCourtDialog() {
        final Dialog addCourtDialog;
        addCourtDialog = new Dialog();
        addCourtDialog.getElement().setAttribute("aria-label", "Additional Court");
        addCourtDialog.add(createAddCourtDialog(addCourtDialog));
        addCourtDialog.setModal(false);
        addCourtDialog.setDraggable(true);
        return addCourtDialog;
    }

    private Div createAddMatrixEntryButtonDiv() {
        final Button addEntryButton = new Button("Add Matrix Entry");
        addEntryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addEntryButton.addClickListener(event -> addCourtDialog.open());
        final Div div = new Div();
        div.add(addEntryButton);
        return div;
    }

    private Button createEditButton(DC5Domain competence) {
        final Button edit = new Button(new Icon(VaadinIcon.PENCIL));
        edit.addClickListener(event -> {
            final Editor<DC5Domain> editor = domainGrid.getEditor();
            if (editor.isOpen()) {
                editor.cancel();
            }
            editor.editItem(competence);
        });
        return edit;
    }

    private VerticalLayout createAddCourtDialog(Dialog dialog) {
        H2 headline = new H2("Competence Matrix Entry");
        headline.getStyle().set("margin", "0").set("font-size", "1.5em")
                .set("font-weight", "bold");
        HorizontalLayout header = new HorizontalLayout(headline);
        header.getElement().getClassList().add("draggable");
        header.setSpacing(false);
        header.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("cursor", "move");
        // Use negative margins to make draggable header stretch over full width,
        // covering the padding of the dialog
        header.getStyle()
                .set("padding", "var(--lumo-space-m) var(--lumo-space-l)")
                .set("margin",
                        "calc(var(--lumo-space-s) * -1) calc(var(--lumo-space-l) * -1) 0");

        TextField nameField = new TextField("Name");
        TextField domainMsgLaneIdTextField = new TextField("Business / Message Lane ID");
        VerticalLayout fieldLayout = new VerticalLayout(nameField);

        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(Alignment.STRETCH);

        Button cancelButton = new Button("Cancel", e -> {
            dialog.close();
        });

        Binder<DC5Domain> binder = new Binder<>();
        binder.setBean(new DC5Domain()); // this db entity exists until save, it can cause subtle bugs, if the binder is not bound to another entity after save, as the entity persists in the dialog and subsequent saves store the old object again and again.

        binder.forField(nameField)
                .asRequired("A domain name is required!")
                .bind(DC5Domain::getName, DC5Domain::setName);

        binder.forField(domainMsgLaneIdTextField)
                .asRequired("A domain ID is required!")
                // TODO: probably check uniqueness here
                .bind(DC5Domain::getBusinessDomainId, DC5Domain::setBusinessDomainId);

        Button saveButton = new Button("Save", e -> {
            if (binder.validate().isOk()) {
                domainRepo.save(binder.getBean());
                this.domainGrid.setItems(domainRepo.findAll());
                dialog.close();
                binder.setBean(new DC5Domain()); // as mentioned above, this is very important when using the db entities directly.
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);

        VerticalLayout dialogLayout = new VerticalLayout(header, fieldLayout, buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return dialogLayout;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        domainGrid.setItems(domainRepo.findAll());
    }
}