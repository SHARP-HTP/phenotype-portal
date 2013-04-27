package edu.mayo.phenoportal.client.phenotype.report;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.phenoportal.client.Htp;
import edu.mayo.phenoportal.client.core.AlgorithmData;
import edu.mayo.phenoportal.client.events.LoggedInEvent;
import edu.mayo.phenoportal.client.events.LoggedInEventHandler;
import edu.mayo.phenoportal.client.events.LoggedOutEvent;
import edu.mayo.phenoportal.client.events.LoggedOutEventHandler;
import edu.mayo.phenoportal.client.events.PhenotypeExecuteCompletedEvent;
import edu.mayo.phenoportal.client.events.PhenotypeExecuteCompletedEventHandler;
import edu.mayo.phenoportal.client.events.PhenotypeExecuteStartedEvent;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;
import edu.mayo.phenoportal.client.utils.MessageWindow;
import edu.mayo.phenoportal.client.utils.UiHelper;
import edu.mayo.phenoportal.shared.Demographic;
import edu.mayo.phenoportal.shared.DemographicStat;
import edu.mayo.phenoportal.shared.DemographicsCategory;
import edu.mayo.phenoportal.shared.Execution;
import edu.mayo.phenoportal.shared.User;

/**
 * Panel to select the from and to dates for executing a specific
 */
public class PhenotypeDateRange extends VLayout {

    private static final String BACKGROUND_COLOR_LABEL = "#d7d5ec";

    DynamicForm i_fromForm;
    DynamicForm i_toForm;
    DynamicForm i_executeForm;
    IButton i_executeButton;

    private DateItem i_fromDate = new DateItem();
    private DateItem i_toDate = new DateItem();
    private Label i_selectedPhenotype;
    private AlgorithmData i_algorithmData;
    private DynamicForm execPanel;
    private StaticTextItem executionDate;
    private StaticTextItem executionTime;
    private StaticTextItem rangeFrom;
    private StaticTextItem rangeTo;
    private final Logger logger = Logger.getLogger(PhenotypeDateRange.class.getName());

    public PhenotypeDateRange() {
        super();
        init();
    }

    private void init() {

        i_selectedPhenotype = UiHelper.createLabel("", Alignment.CENTER);
        addMember(i_selectedPhenotype);

        HLayout hLayout = new HLayout();
        hLayout.addMember(createDateLayout());
        hLayout.addMember(createLatestExecutionLayout());

        addMember(hLayout);

        createLoggedInEventHandler();
        createLoggedOutEventHandler();
        createPhenotypeExecuteCompletedEventHandler();
        // createPhenotypeSelectionChangedEventHandler();

        // initially set the form to false until a user logs in.
        setDateFormDisabled(true);
    }

    private HLayout createLatestExecutionLayout() {
        HLayout hLayout = new HLayout();
        hLayout.setWidth100();
        hLayout.setHeight100();
        hLayout.setAlign(Alignment.RIGHT);

        execPanel = new DynamicForm();
        execPanel.setGroupTitle("Latest Execution");
        execPanel.setIsGroup(true);
        execPanel.setWidth(350);
        execPanel.setHeight100();
        execPanel.setAlign(Alignment.RIGHT);

        executionDate = createTextItem("Executed_on", "Executed on");
        executionTime = createTextItem("Elapsed_time", "Elapsed time");
        rangeFrom = createTextItem("Range_from", "Range from");
        rangeTo = createTextItem("Range_to", "Range to");

        execPanel.setFields(executionDate, executionTime, rangeFrom, rangeTo);

        clearLastExecutionDetails();

        hLayout.addMember(execPanel);
        return hLayout;
    }

    /**
     * Create a StaticTextItem with a common style.
     * 
     * @param name
     * @param title
     * @return
     */
    private StaticTextItem createTextItem(String name, String title) {
        StaticTextItem item = new StaticTextItem(name);
        item.setTitle(title);
        item.setWrap(false);
        item.setWidth(100);

        return item;
    }

    public void updateSelection(AlgorithmData algorithmData) {

        i_algorithmData = algorithmData;
        String selected = UiHelper.getFormattedLabelText(i_algorithmData.getAlgorithmName());

        i_selectedPhenotype.setContents(selected);
        setEnablementBasedOnUser(Htp.getLoggedInUser());
    }

    private void updateExecutionDetails() {
        if (Htp.getLoggedInUser() != null) {
            PhenotypeServiceAsync phenotypeService = GWT.create(PhenotypeService.class);
            phenotypeService.getLatestExecution(i_algorithmData.getAlgorithmName(), i_algorithmData
                    .getAlgorithmVersion(), i_algorithmData.getParentId(), Htp.getLoggedInUser()
                    .getUserName(), new AsyncCallback<Execution>() {
                @Override
                public void onFailure(Throwable caught) {
                    clearLastExecutionDetails();
                    logger.log(Level.INFO, "Failed to get the user's latest execution.");
                }

                @Override
                public void onSuccess(Execution execution) {
                    updateLatestExecutionDetails(execution);
                }
            });
        } else {
            updateLatestExecutionDetails(null);
        }
    }

    public void updateLatestExecutionDetails(Execution execution) {
        if (execution != null) {
            String date = execution.getStartDate();
            String time = execution.getElapsedTime();
            String dateRangeFrom = execution.getDateRangeFrom();
            String dateRangeTo = execution.getDateRangeTo();
            if (date != null) {
                if (!date.trim().equals("")) {
                    executionDate.setValue(date);
                    executionDate.show();
                }

                if (time != null && !time.trim().equals("")) {
                    executionTime.setValue(formatTime(time));
                    executionTime.show();
                }

                if (dateRangeFrom != null && !dateRangeFrom.trim().equals("")) {
                    rangeFrom.setValue(dateRangeFrom);
                    rangeFrom.show();
                }

                if (dateRangeTo != null && !dateRangeTo.trim().equals("")) {
                    rangeTo.setValue(dateRangeTo);
                    rangeTo.show();
                }

                execPanel.show();
            } else {
                clearLastExecutionDetails();
            }
        } else {
            clearLastExecutionDetails();
        }
    }

    private void clearLastExecutionDetails() {
        execPanel.hide();
        executionDate.hide();
        executionTime.hide();
        rangeFrom.hide();
        rangeTo.hide();
    }

    private Layout createDateLayout() {

        VLayout dateLayout = new VLayout();
        dateLayout.setWidth(350);
        dateLayout.setHeight(50);
        dateLayout.setMembersMargin(3);
        dateLayout.setMargin(5);

        Label label = new Label();
        label.setHeight(25);
        label.setAlign(Alignment.CENTER);
        label.setContents("<b>Select an execution date range</b>");
        dateLayout.addMember(label);

        i_fromForm = new DynamicForm();
        i_fromDate = new DateItem();
        i_fromDate.setName("From");
        i_fromForm.setFields(i_fromDate);
        dateLayout.addMember(i_fromForm);

        i_toForm = new DynamicForm();
        i_toDate = new DateItem();
        i_toDate.setName("To");
        i_toForm.setFields(i_toDate);
        dateLayout.addMember(i_toForm);

        HLayout buttonLayout = new HLayout();
        buttonLayout.setWidth100();
        buttonLayout.setAlign(Alignment.CENTER);
        i_executeButton = new IButton("Execute");

        i_executeButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                // Validate the from and to dates.
                Date fromDate = i_fromDate.getValueAsDate();
                Date toDate = i_toDate.getValueAsDate();

                if (toDate.before(fromDate)) {
                    String title = "Invalid Dates";
                    String message = "The From date must be before the To date.";
                    MessageWindow messageWindow = new MessageWindow(title, message);
                    messageWindow.show();
                } else {
                    executePhenotype(i_algorithmData,
                            fromDate, toDate);
                }
            }
        });

        buttonLayout.addMember(i_executeButton);
        dateLayout.addMember(buttonLayout);

        return dateLayout;
    }

    /**
     * Execute the phenotype with the selected dates On Success, It will return
     * the List<Demographic> object
     */
    private void executePhenotype(AlgorithmData algorithmData, Date fromDate, Date toDate) {

        Htp.EVENT_BUS.fireEvent(new PhenotypeExecuteStartedEvent());

        PhenotypeServiceAsync phenotypeService = GWT.create(PhenotypeService.class);
        phenotypeService.executePhenotype(algorithmData, fromDate, toDate, Htp
                .getLoggedInUser().getUserName(), new AsyncCallback<Execution>() {

            @Override
            public void onSuccess(Execution result) {

                boolean printDebug = false;
                if (printDebug) {
                    printExecutionResults(result);
                }

                String title = "Phenotype Execution Complete";
                String message = "The Phenotype execution is complete.  You can view the results in the Summary, Demographics and WorkFlow tabs.";
                MessageWindow messageWindow = new MessageWindow(title, message);
                messageWindow.show();

                Htp.EVENT_BUS.fireEvent(new PhenotypeExecuteCompletedEvent(true, result));
            }

            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.WARNING, "Phenotype execution has failed.", caught);

                String title = "Phenotype Execution Failed";
                String message = "The Phenotype execution has failed. If this continues, please contact support.";
                MessageWindow messageWindow = new MessageWindow(title, message);
                messageWindow.show();

                Htp.EVENT_BUS.fireEvent(new PhenotypeExecuteCompletedEvent(false));
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
                    User user = loggedInEvent.getUser();
                    setEnablementBasedOnUser(user);
                }
            }
        });
    }

    /**
     * Create a handler to change Logged in/out button based on the log in state
     */
    private void createLoggedOutEventHandler() {
        Htp.EVENT_BUS.addHandler(LoggedOutEvent.TYPE, new LoggedOutEventHandler() {

            @Override
            public void onLoggedOut(LoggedOutEvent loggedOutEvent) {
                i_algorithmData = null;
                setDateFormDisabled(true);
            }
        });
    }

    /**
     * Listen for when a phenotype execution completes
     */
    private void createPhenotypeExecuteCompletedEventHandler() {
        Htp.EVENT_BUS.addHandler(PhenotypeExecuteCompletedEvent.TYPE,
                new PhenotypeExecuteCompletedEventHandler() {
                    @Override
                    public void onPhenotypeExecuteCompleted(
                            PhenotypeExecuteCompletedEvent phenotypeExecuteCompletedEvent) {
                        if (phenotypeExecuteCompletedEvent.getExecuteSuccess()) {
                            updateExecutionDetails();
                        }
                    }

                });
    }

    // private void createPhenotypeSelectionChangedEventHandler() {
    // Htp.EVENT_BUS.addHandler(PhenotypeSelectionChangedEvent.TYPE,
    // new PhenotypeSelectionChangedEventHandler() {
    //
    // @Override
    // public void onPhenotypeSelectionChanged(
    // PhenotypeSelectionChangedEvent phenotypeSelectionChangedEvent) {
    // updateSelection(phenotypeSelectionChangedEvent.getAlgorithmData());
    //
    // }
    // });
    // }

    private void setEnablementBasedOnUser(User user) {
        // Admin = 1, Execute = 2
        if (user != null && user.getRole() <= 2) {
            setDateFormDisabled(false);
        } else {
            setDateFormDisabled(true);
        }
    }

    private void setDateFormDisabled(boolean disabled) {
        i_selectedPhenotype.setDisabled(disabled);
        i_executeButton.setDisabled(disabled);
        i_fromDate.setDisabled(disabled);
        i_toForm.setDisabled(disabled);
        if (disabled)
            execPanel.hide();
    }

    /**
     * Convert milliseconds to seconds. Example :"14550 ms" => "14 Seconds"
     * 
     * @param time
     * @return
     */
    private String formatTime(String time) {
        int ms;
        int seconds = 0;

        try {
            // remove the "ms" string appended to the time
            String timeWithoutString = time.substring(0, time.length() - 3);
            ms = Integer.parseInt(timeWithoutString);
            seconds = ms / 1000;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Couldn't convert execution time to seconds. ");
            return time;
        }
        return seconds + " Seconds";
    }

    /**
     * Display the results returned from the server.
     * 
     * @param result
     */
    private void printExecutionResults(Execution result) {
        if (result != null) {
            List<Demographic> demographics = result.getDemographics();
            if (demographics != null) {
                for (int i = 0; i < demographics.size(); i++) {

                    Demographic demoIterate = demographics.get(i);
                    System.out.println("Type:" + demoIterate.getType());
                    for (int k = 0; k < demoIterate.getDemoCategoryList().size(); k++) {
                        DemographicsCategory category1 = demoIterate.getDemoCategoryList().get(k); // System.out.println("Name:"
                                                                                                   // +
                        // category1.getName());
                        ;
                        List<DemographicStat> statlist1 = category1.getDemoStatList();

                        for (int l = 0; l < statlist1.size(); l++) {
                            DemographicStat statValue = statlist1.get(l);
                            System.out.println("Label" + statValue.getLabel());
                            System.out.println("Value" + statValue.getValue());
                        }

                    }
                }
            }

        }
    }

}
