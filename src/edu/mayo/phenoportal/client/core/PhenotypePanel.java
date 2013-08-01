package edu.mayo.phenoportal.client.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.phenoportal.client.Htp;
import edu.mayo.phenoportal.client.events.LoggedInEvent;
import edu.mayo.phenoportal.client.events.LoggedInEventHandler;
import edu.mayo.phenoportal.client.events.PhenotypeExecuteSetupEvent;
import edu.mayo.phenoportal.client.events.PhenotypeExecuteSetupEventHandler;
import edu.mayo.phenoportal.client.events.PhenotypeSelectionChangedEvent;
import edu.mayo.phenoportal.client.events.PhenotypeSelectionChangedEventHandler;
import edu.mayo.phenoportal.client.navigation.NavigationPane;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;
import edu.mayo.phenoportal.client.phenotype.report.DataCriteriaListGrid;
import edu.mayo.phenoportal.client.phenotype.report.ExecuteWindow;
import edu.mayo.phenoportal.client.phenotype.report.PhenotypeDateRange;
import edu.mayo.phenoportal.client.phenotype.report.PhenotypeReportTabSet;
import edu.mayo.phenoportal.client.utils.UiHelper;
import edu.mayo.phenoportal.shared.Execution;

/**
 * Panel for displaying all of the phenotype info on the left panel and its
 * respective data on the right side.
 */
public class PhenotypePanel extends HLayout {

    private PhenotypeDateRange i_inputRange;
    private PhenotypeReportTabSet i_reportTabs;

    private AlgorithmData i_algorithmData;
    private NavigationPane i_navigationPane;

    public PhenotypePanel() {
        super();

        init();
    }

    private void init() {
        setWidth100();
        setHeight100();

        i_navigationPane = new NavigationPane();

        // add rounded borders to the layout.
        UiHelper.createLayoutWithBorders(i_navigationPane);

        i_inputRange = new PhenotypeDateRange();
        i_reportTabs = new PhenotypeReportTabSet();

        addMember(i_navigationPane);

        VLayout dataPanel = new VLayout();

        // add rounded borders to the layout.
        UiHelper.createLayoutWithBorders(dataPanel);

        dataPanel.addMember(i_inputRange);
        dataPanel.addMember(i_reportTabs);

        addMember(dataPanel);

        createPhenotypeSelectionChangedEventHandler();
        createLoggedInEventHandler();
	    createExecutionSetupEventHandler();
    }

    public void refreshTreeNavigation() {
        i_navigationPane.refreshTreeNavigation();
    }

    public void updateSelection(AlgorithmData algorithmData) {
        i_algorithmData = algorithmData;

        i_inputRange.updateSelection(i_algorithmData);
        i_reportTabs.updateSelection(i_algorithmData);

        if (Htp.getLoggedInUser() != null) {
            PhenotypeServiceAsync phenotypeService = GWT.create(PhenotypeService.class);
            phenotypeService.getLatestExecution(i_algorithmData.getAlgorithmName(), i_algorithmData
                    .getAlgorithmVersion(), i_algorithmData.getParentId(), Htp.getLoggedInUser()
                    .getUserName(), new AsyncCallback<Execution>() {
                @Override
                public void onFailure(Throwable caught) {
                    i_inputRange.updateLatestExecutionDetails(null);
                    i_reportTabs.updateResults(null);
                    SC.warn("Unable to display the latest execution results.");
                }

                @Override
                public void onSuccess(Execution execution) {
                    i_inputRange.updateLatestExecutionDetails(execution);
                    i_reportTabs.updateResults(execution);
                }
            });

        }
        // i_reportTabs.disableSummaryAndDemographicsTabs(true);
    }

    public void createPhenotypeSelectionChangedEventHandler() {
        Htp.EVENT_BUS.addHandler(PhenotypeSelectionChangedEvent.TYPE,
                new PhenotypeSelectionChangedEventHandler() {

                    @Override
                    public void onPhenotypeSelectionChanged(
                            PhenotypeSelectionChangedEvent phenotypeSelectionChangedEvent) {
                        updateSelection(phenotypeSelectionChangedEvent.getAlgorithmData());

                    }
                });
    }

    /**
     * Create a handler to change Logged in/out button based on the log in state
     */
    private void createLoggedInEventHandler() {
        Htp.EVENT_BUS.addHandler(LoggedInEvent.TYPE, new LoggedInEventHandler() {

            @Override
            public void onLoggedIn(LoggedInEvent loggedInEvent) {

                // only update if the user has previously selected an algorithm
                if (i_algorithmData != null) {
                    updateSelection(i_algorithmData);
                }
            }
        });
    }

	private void createExecutionSetupEventHandler() {
		Htp.EVENT_BUS.addHandler(PhenotypeExecuteSetupEvent.TYPE, new PhenotypeExecuteSetupEventHandler() {
			@Override
			public void onExecuteSetup(PhenotypeExecuteSetupEvent event) {
				ExecuteWindow executeWindow = new ExecuteWindow(i_algorithmData, i_inputRange.getFromDate(), i_inputRange.getToDate());
				executeWindow.centerInPage();
				executeWindow.show();
				executeWindow.redraw();
			}
		});
	}

}
