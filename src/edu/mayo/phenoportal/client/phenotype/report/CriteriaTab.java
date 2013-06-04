package edu.mayo.phenoportal.client.phenotype.report;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

import edu.mayo.phenoportal.client.core.AlgorithmData;

/**
 * Tab for displaying the Criteria info for the selected phenotype.
 */
public class CriteriaTab extends Tab implements ReportTab {

    private AlgorithmData i_algorithmData;
    private final VLayout i_criteriaLayout;
    private CriteriaPanel i_criteriaPanel;
    private Label i_newWindowLink;

    public CriteriaTab(String title) {
        super(title);

        // overall layout that holds everything in the criteria tab.
        i_criteriaLayout = new VLayout();

        createStack();
    }

    private void createStack() {

        i_criteriaPanel = null;
        i_criteriaPanel = new CriteriaPanel();

        i_newWindowLink = getLink("Show in new window");
        i_criteriaLayout.addMember(i_newWindowLink);

        i_criteriaPanel.createStack();
        i_criteriaLayout.addMember(i_criteriaPanel);

        setPane(i_criteriaLayout);
    }

    /**
     * Retrieve the new algorithm criteria info from the DB and display it.
     * 
     * @param algorithmData
     */
    public void updateSelection(AlgorithmData algorithmData) {
        i_algorithmData = algorithmData;
        i_criteriaPanel.updateSelection(i_algorithmData);
    }

    @Override
    public void clearTab() {
        i_criteriaPanel.getHtmlPane().setContents("");
    }

    /**
     * Create the new window link.
     * 
     * @param message
     * @return
     */
    private Label getLink(String message) {
        Label link = new Label(message);
        link.setWidth(150);
        link.setHeight(20);

        link.addStyleName("htpClickable");
        link.setAlign(Alignment.CENTER);

        link.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Window window = getWindow();
                window.centerInPage();
                window.show();
            }
        });

        return link;
    }

    /**
     * Show criteria info in a new window.
     * 
     * @return
     */
    private Window getWindow() {

        Window window = new Window();

        window.setTitle("Criteria");
        window.setWidth("95%");
        window.setHeight("95%");

        window.setModalMaskOpacity(90);

        window.setCanDragReposition(false);
        window.setCanDragResize(true);
        window.setAnimateMinimize(true);

        CriteriaPanel criteriaPanel = new CriteriaPanel();
        criteriaPanel.updateSelection(i_algorithmData);

        window.addItem(criteriaPanel);

        return window;
    }
}
