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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.common.service.DCBusinessDomainManager;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.ui.layout.DCVerticalLayoutWithTitleAndHelpButton;
import eu.domibus.connector.ui.utils.RoleRequired;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final DCBusinessDomainManager domainRepo; // TODO: read only

    public DomainView(DCBusinessDomainManager domainRepo) {
        super(HELP_ID, TITLE);
        this.domainRepo = domainRepo;

        domainGrid = initGrid();
        addCourtDialog = initAddCourtDialog();
        add(createAddMatrixEntryButtonDiv(), domainGrid);
    }

    private Grid<DomibusConnectorBusinessDomain> domainGrid;
    private final Dialog addCourtDialog;

    private Grid<DomibusConnectorBusinessDomain> initGrid() {
        // Grid Settings
        final Grid<DomibusConnectorBusinessDomain> grid = new Grid<>();
        grid.setAllRowsVisible(true);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);

        // Grid Layout
        final Grid.Column<DomibusConnectorBusinessDomain> editColumn =
                grid.addComponentColumn(this::createEditButton)
                        .setHeader("edit")
                        .setWidth("160px")
                        .setFlexGrow(0)
                        .setTextAlign(ColumnTextAlign.CENTER);

        final Grid.Column<DomibusConnectorBusinessDomain> nameColumn =
                grid.addColumn(DomibusConnectorBusinessDomain::getId)
                        .setKey("getId")
                        .setHeader("Domain Name / ID")
                        .setSortable(true);

        final Grid.Column<DomibusConnectorBusinessDomain> domainMsgLaneIdColumn =
                grid.addColumn(DomibusConnectorBusinessDomain::getDescription)
                        .setKey("getDescription")
                        .setHeader("Description")
                        .setSortable(true);

        final Grid.Column<DomibusConnectorBusinessDomain> domainConfigSource =
                grid.addColumn(d -> d.getConfigurationSource().toString())
                        .setKey("getConfigurationSource")
                        .setHeader("Configuration Source")
                        .setSortable(true);


        // Row editing configuration
        Binder<DomibusConnectorBusinessDomain> binder = new Binder<>(DomibusConnectorBusinessDomain.class);
        final Editor<DomibusConnectorBusinessDomain> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        editor.addSaveListener(e -> {
            domainRepo.createBusinessDomain(e.getItem());
        });

        Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
            editor.save();
        });
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                e -> editor.cancel());
        Button deleteButton = new Button(VaadinIcon.TRASH.create(),
                e -> {
                    if (editor.getItem().getConfigurationSource().equals(ConfigurationSource.DB)) {
//                        domainRepo.delete(editor.getItem()); // TODO what to do here???
                        domainGrid.setItems(domainRepo.getValidBusinessDomains().stream().map(id -> domainRepo.getBusinessDomain(id)).map(Optional::get).collect(Collectors.toList()));
                    } else {
                        new Notification("Can't delete / edit properties that are not stored in the database!").open();
                    }
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
                .withValidator(s -> Charset.forName("US-ASCII").newEncoder().canEncode(s), "Only ASCII allowed!")
                .withValidator(s -> s.length() < 255, "Must use less than 255 characters!")
                .withConverter(DomibusConnectorBusinessDomain.BusinessDomainId::new, DomibusConnectorBusinessDomain.BusinessDomainId::getBusinessDomainId)
                .bind(DomibusConnectorBusinessDomain::getId, DomibusConnectorBusinessDomain::setId);

        return grid;
    }

    private Dialog initAddCourtDialog() {
        final Dialog addCourtDialog;
        addCourtDialog = new Dialog();
        addCourtDialog.getElement().setAttribute("aria-label", "Additional Court");
        addCourtDialog.add(createAddDomainDialog(addCourtDialog));
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

    private Button createEditButton(DomibusConnectorBusinessDomain competence) {
        final Button edit = new Button(new Icon(VaadinIcon.PENCIL));
        edit.addClickListener(event -> {
            final Editor<DomibusConnectorBusinessDomain> editor = domainGrid.getEditor();
            if (editor.isOpen()) {
                editor.cancel();
            }
            editor.editItem(competence);
        });
        return edit;
    }

    private VerticalLayout createAddDomainDialog(Dialog dialog) {
        H2 headline = new H2("New Domain");
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

        TextField nameField = new TextField("Domain Name / ID");
        TextField descriptionField = new TextField("Description");
        TextField configurationSourceField = new TextField("Configuration Source");
        configurationSourceField.setReadOnly(true);

        VerticalLayout fieldLayout = new VerticalLayout(nameField, descriptionField,configurationSourceField);

        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(Alignment.STRETCH);

        Button cancelButton = new Button("Cancel", e -> {
            dialog.close();
        });

        Binder<DomibusConnectorBusinessDomain> binder = new Binder<>();
        final DomibusConnectorBusinessDomain bean = new DomibusConnectorBusinessDomain();
        bean.setConfigurationSource(ConfigurationSource.DB);
        binder.setBean(bean); // this db entity exists until save, it can cause subtle bugs, if the binder is not bound to another entity after save, as the entity persists in the dialog and subsequent saves store the old object again and again.

        binder.forField(nameField)
                .asRequired("A domain name is required!")
                .withValidator(s -> Charset.forName("US-ASCII").newEncoder().canEncode(s), "Only ASCII allowed!")
                .withValidator(s -> s.length() < 255, "Must use less than 255 characters!")
                .withValidator(s -> !domainRepo.getBusinessDomain(new DomibusConnectorBusinessDomain.BusinessDomainId(s)).isPresent(), "Domain already exists!")
                .withConverter(DomibusConnectorBusinessDomain.BusinessDomainId::new, id -> id == null ? "" : id.getBusinessDomainId())
                .bind(DomibusConnectorBusinessDomain::getId, DomibusConnectorBusinessDomain::setId);

        binder.forField(descriptionField)
                .bind(DomibusConnectorBusinessDomain::getDescription, DomibusConnectorBusinessDomain::setDescription);

        binder.forField(configurationSourceField)
                .bind(d -> d.getConfigurationSource().toString(), null);

        Button saveButton = new Button("Save", e -> {
            if (binder.validate().isOk()) {
                domainRepo.createBusinessDomain(binder.getBean());
                domainGrid.setItems(domainRepo.getValidBusinessDomains().stream().map(id -> domainRepo.getBusinessDomain(id)).map(Optional::get).collect(Collectors.toList()));
                dialog.close();
                binder.setBean(new DomibusConnectorBusinessDomain()); // as mentioned above, this is very important when using the db entities directly.
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
        domainGrid.setItems(domainRepo.getValidBusinessDomains().stream().map(id -> domainRepo.getBusinessDomain(id)).map(Optional::get).collect(Collectors.toList()));
    }
}