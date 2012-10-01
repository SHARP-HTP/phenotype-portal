package edu.mayo.phenoportal.client.upload;

import com.google.gwt.core.client.Callback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.phenoportal.client.core.AlgorithmData;
import edu.mayo.phenoportal.client.datasource.PhenotypeXmlDS;
import edu.mayo.phenoportal.client.navigation.NavigationTree;

public class CategorySelectionWindow extends Window {

    private static final String BACKGROUND_COLOR = "#b0abdb";
    private static final String TITLE = "Category Selection";
    private static final String MESSAGE = "Select a category to place the algorithm in";

    private NavigationTree i_navigationTree;
    private AlgorithmData i_algorithmData;

    private final Callback<AlgorithmData, String> i_onSelectCallback;

    protected Label i_label;
    protected Button i_okButton;
    protected Button i_launchButton;
    protected Button i_cancelButton;

    public CategorySelectionWindow(Callback<AlgorithmData, String> onSelectCallback) {
        super();

        i_onSelectCallback = onSelectCallback;

        VLayout layout = new VLayout(5);

        setWidth(600);
        setHeight(500);
        setMargin(20);

        setTitle(TITLE);
        setShowMinimizeButton(false);
        setIsModal(true);
        setShowModalMask(true);
        setCanDragResize(true);
        centerInPage();

        addCloseClickHandler(new CloseClickHandler() {

            @Override
            public void onCloseClick(CloseClickEvent event) {
                destroy();
            }
        });

        layout.addMember(createDisplayLabel());
        layout.addMember(createTree());
        layout.addMember(getButtons());

        addItem(layout);

        i_okButton.setDisabled(true);
        show();
    }

    private NavigationTree createTree() {

        // create the navigation tree
        PhenotypeXmlDS phenotypeDS = PhenotypeXmlDS.getUniqueSelectionTree();
        i_navigationTree = new NavigationTree(phenotypeDS);

        // Tree click handler. When user clicks on any node in the tree, this
        // will capture the event. If the user clicks on a node that is level 3,
        // then we need to enable the "Select" button
        i_navigationTree.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                ListGridRecord record = i_navigationTree.getSelectedRecord();

                if (record != null) {

                    int level = i_navigationTree.convertToInt(record.getAttribute("Level"));

                    i_okButton.setDisabled(!isValidLevel(level));

                    if (isValidLevel(level)) {

                        i_algorithmData = new AlgorithmData(record.getAttribute("CategoryId"),
                                record.getAttribute("ParentId"),
                                record.getAttribute("AlgoVersion"),
                                record.getAttribute("AlgoUser"), record.getAttribute("AlgoDesc"),
                                record.getAttribute("Name"));

                    } else {
                        i_algorithmData = null;
                    }

                    System.out.println("-----  " + record.getAttribute("Count") + " Count: "
                            + record.getAttribute("Level") + " Level: "
                            + record.getAttribute("CategoryId") + " CategoryId: "
                            + record.getAttribute("ParentId") + " ParentId: "
                            + record.getAttribute("AlgoVersion") + " AlgoVersion:"
                            + record.getAttribute("AlgoUser") + " AlgoUser:"
                            + record.getAttribute("AlgoDesc") + " AlgoDesc"
                            + record.getAttribute("Name") + " Name"
                            + record.getAttribute("isFolder") + "isFolder");
                }
            }
        });

        return i_navigationTree;
    }

    private VLayout createDisplayLabel() {
        i_label = new Label("<b>" + MESSAGE + "<b>");
        i_label.setWidth100();
        i_label.setHeight(30);
        i_label.setMargin(2);
        i_label.setValign(VerticalAlignment.CENTER);
        i_label.setBackgroundColor(BACKGROUND_COLOR);

        final VLayout vLayoutLayoutSpacers = new VLayout();
        vLayoutLayoutSpacers.setWidth100();
        vLayoutLayoutSpacers.setHeight(30);
        vLayoutLayoutSpacers.setBackgroundColor(BACKGROUND_COLOR);
        vLayoutLayoutSpacers.setLayoutMargin(6);
        vLayoutLayoutSpacers.setMembersMargin(6);
        vLayoutLayoutSpacers.addMember(i_label);

        return vLayoutLayoutSpacers;
    }

    private HLayout getButtons() {

        // Buttons on the bottom
        HLayout buttonLayout = new HLayout();
        buttonLayout.setWidth100();
        buttonLayout.setHeight(40);
        buttonLayout.setLayoutMargin(6);
        buttonLayout.setMembersMargin(10);
        buttonLayout.setAlign(Alignment.RIGHT);

        i_okButton = new Button("Select");
        i_okButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                i_onSelectCallback.onSuccess(i_algorithmData);

                // close the window
                destroy();
            }
        });

        i_cancelButton = new Button("Cancel");
        i_cancelButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // close the window
                destroy();
            }
        });

        buttonLayout.addMember(i_okButton);
        buttonLayout.addMember(i_cancelButton);

        return buttonLayout;
    }

    public Button getCloseButton() {
        return i_okButton;
    }

    public AlgorithmData getSelectedCategoryData() {
        return i_algorithmData;
    }

    private boolean isValidLevel(int selectedLevel) {
        return true;
    }
}
