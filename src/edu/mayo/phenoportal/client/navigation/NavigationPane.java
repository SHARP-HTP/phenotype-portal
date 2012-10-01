package edu.mayo.phenoportal.client.navigation;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.phenoportal.client.Htp;
import edu.mayo.phenoportal.client.core.AlgorithmData;
import edu.mayo.phenoportal.client.datasource.PhenotypeXmlDS;
import edu.mayo.phenoportal.client.events.LoggedOutEvent;
import edu.mayo.phenoportal.client.events.LoggedOutEventHandler;
import edu.mayo.phenoportal.client.events.PhenotypeSelectionChangedEvent;

/**
 * VLayout to hold the NavigationSectionStack
 */
public class NavigationPane extends VLayout {

    static Logger lgr = Logger.getLogger(NavigationPane.class.getName());

    private static final int WIDTH = 400;

    private NavigationSectionStack i_navigationSectionStack;
    private NavigationTree i_navigationTree;

    public NavigationPane() {
        super();

        lgr.log(Level.INFO, "init Navigation Pane()...");

        init();
        createLoggeOutRequestEventHanlder();
    }

    private void init() {
        // initialize the layout container
        setWidth(WIDTH);
        setShowResizeBar(true);

        // create the navigation tree
        PhenotypeXmlDS phenotypeDS = PhenotypeXmlDS.getInstanceDefault();

        // Tree click handler. When user clicks on any node in the tree, this
        // will capture the event. If the user clicks on a leaf node
        // (algorithm),
        // then we need to pass that information to the data panel to show.
        i_navigationTree = new NavigationTree(phenotypeDS);

        i_navigationTree.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                ListGridRecord record = i_navigationTree.getSelectedRecord();

                if (record != null) {

                    int level = i_navigationTree.convertToInt(record.getAttribute("Level"));
                    if (level == 4) {

                        // onClick of the algorithm to show the dataPanel.
                        AlgorithmData algData = new AlgorithmData(
                                record.getAttribute("CategoryId"), record.getAttribute("ParentId"),
                                record.getAttribute("AlgoVersion"),
                                record.getAttribute("AlgoUser"), record.getAttribute("AlgoDesc"),
                                record.getAttribute("Name"));

                        Htp.EVENT_BUS.fireEvent(new PhenotypeSelectionChangedEvent(algData));
                    }

                    lgr.log(Level.INFO,
                            record.getAttribute("Count") + " Count: "
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

        i_navigationSectionStack = new NavigationSectionStack(i_navigationTree);
        setMembers(i_navigationSectionStack);
    }

    public void refreshTreeNavigation() {
        i_navigationTree.refreshTree();
    }

    /**
     * Create a handler to listen for a logged out request.
     */
    private void createLoggeOutRequestEventHanlder() {
        Htp.EVENT_BUS.addHandler(LoggedOutEvent.TYPE, new LoggedOutEventHandler() {

            @Override
            public void onLoggedOut(LoggedOutEvent loggedOutEvent) {
                refreshTreeNavigation();
            }
        });

    }

}
