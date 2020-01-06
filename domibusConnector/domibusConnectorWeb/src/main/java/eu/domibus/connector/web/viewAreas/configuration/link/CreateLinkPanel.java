package eu.domibus.connector.web.viewAreas.configuration.link;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;

import java.util.ArrayList;
import java.util.List;

public class CreateLinkPanel extends VerticalLayout {

    private ProgressBar progressBar = new ProgressBar(10, 100, 10);
    private Button forwardButton = new Button("Next");
    private Button backButton = new Button("Back");
    private Text header = new Text ("Create Link");
    private Div content = new Div();


    List<Component> steps = new ArrayList<>();

    private DomibusConnectorLinkConfiguration domibusConnectorLinkConfiguration;


    public CreateLinkPanel() {
        initUI();
        initSteps();
    }

    private void initUI() {

        add(header);
        add(progressBar);
        add(content);
        add(new HorizontalLayout(backButton, forwardButton));
    }

    private void initSteps() {

    }



}
