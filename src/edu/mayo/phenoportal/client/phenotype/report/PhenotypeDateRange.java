package edu.mayo.phenoportal.client.phenotype.report;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
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
import edu.mayo.phenoportal.client.events.PhenotypeExecuteSetupEvent;
import edu.mayo.phenoportal.client.events.PhenotypeExecuteStartedEvent;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;
import edu.mayo.phenoportal.client.utils.MessageWindow;
import edu.mayo.phenoportal.client.utils.UiHelper;
import edu.mayo.phenoportal.shared.Execution;
import edu.mayo.phenoportal.shared.User;
import edu.mayo.phenoportal.shared.ValueSet;

/**
 * Panel to select the from and to dates for executing a specific
 */
public class PhenotypeDateRange extends VLayout {

    DynamicForm i_fromForm;
    DynamicForm i_toForm;
    IButton i_executeButton;

    private DateItem i_fromDate = new DateItem();
    private DateItem i_toDate = new DateItem();
    private Label i_selectedPhenotype;
    private AlgorithmData i_algorithmData;

    // Original algorithm data from the server.
    private AlgorithmData i_algorithmDataOriginal;

    private DynamicForm execPanel;
    private StaticTextItem executionDate;
    private StaticTextItem executionTime;
    private StaticTextItem rangeFrom;
    private StaticTextItem rangeTo;
    private Execution lastExecution;
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

        ButtonItem execLastExecutionBtn = new ButtonItem("execLastExecutionBtn", "Re-Execute");
        execLastExecutionBtn.setStartRow(false);
        execLastExecutionBtn.setEndRow(false);
        execLastExecutionBtn.setColSpan(1);
        execLastExecutionBtn.setAlign(Alignment.LEFT);

        execLastExecutionBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                // Execute the algorithm with the last executed info.
                runLastExecution();
            }
        });

        ButtonItem valueSetsBtn = new ButtonItem("valueSetsBtn", "Value Set Details");
        valueSetsBtn.setStartRow(false);
        valueSetsBtn.setEndRow(false);
        valueSetsBtn.setColSpan(1);
        valueSetsBtn.setAlign(Alignment.RIGHT);

        valueSetsBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                displayValueSets();
            }
        });

        execPanel.setFields(executionDate, executionTime, rangeFrom, rangeTo, valueSetsBtn,
                execLastExecutionBtn);
        clearLastExecutionDetails();

        hLayout.addMember(execPanel);
        return hLayout;
    }

    private void displayValueSets() {
        if (lastExecution != null) {
            PhenotypeServiceAsync phenotypeService = GWT.create(PhenotypeService.class);
            phenotypeService.getExecutionValueSets(lastExecution.getId(),
                    new AsyncCallback<List<ValueSet>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            SC.warn("Failed to retrieve value sets for the last executed algorithm.");
                        }

                        @Override
                        public void onSuccess(List<ValueSet> result) {
                            LatestExecutionWindow latestExecutionWindow = new LatestExecutionWindow(
                                    result);
                            latestExecutionWindow.centerInPage();
                            latestExecutionWindow.show();
                            latestExecutionWindow.redraw();
                        }
                    });

        }
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
        i_algorithmDataOriginal = copyAlgorithm(i_algorithmData);

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
        lastExecution = execution;
        if (execution != null) {

            // TODO - Update the from and to dates from the last execution...
            // not working.

            // update the to/from date range for execution
            i_fromDate.setValue(execution.getDateRangeFrom());
            i_toDate.setValue(execution.getDateRangeTo());

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

    public Date getFromDate() {
        return this.i_fromDate.getValueAsDate();
    }

    public Date getToDate() {
        return this.i_toDate.getValueAsDate();
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
        i_executeButton = new IButton("Execute...");

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
                    Htp.EVENT_BUS.fireEvent(new PhenotypeExecuteSetupEvent());
                }
            }
        });

        buttonLayout.addMember(i_executeButton);
        dateLayout.addMember(buttonLayout);

        return dateLayout;
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
                i_algorithmDataOriginal = null;
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

    /**
     * Run the last execution that this user ran
     */
    private void runLastExecution() {

        PhenotypeServiceAsync phenotypeService = GWT.create(PhenotypeService.class);
        phenotypeService.getLatestExecution(i_algorithmDataOriginal.getAlgorithmName(),
                i_algorithmDataOriginal.getAlgorithmVersion(),
                i_algorithmDataOriginal.getParentId(), Htp.getLoggedInUser().getUserName(),
                new AsyncCallback<Execution>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        clearLastExecutionDetails();
                        logger.log(Level.INFO, "Failed to get the user's latest execution.");
                    }

                    @Override
                    public void onSuccess(Execution execution) {

                        getVersionsFromLastExecution(i_algorithmDataOriginal, execution);

                    }
                });

    }

    /**
     * Get the values sets from the last execution.
     * 
     * @param algorithmData
     * @param execution
     */
    public void getVersionsFromLastExecution(final AlgorithmData algorithmData,
            final Execution execution) {

        if (execution != null) {
            PhenotypeServiceAsync phenotypeService = GWT.create(PhenotypeService.class);
            phenotypeService.getExecutionValueSets(execution.getId(),
                    new AsyncCallback<List<ValueSet>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            SC.warn("Failed to retrieve value sets for the last executed algorithm.");
                        }

                        @Override
                        public void onSuccess(List<ValueSet> valueSets) {

                            // got latest execution and valuesets... now
                            // re-execute this one.

                            algorithmData.setValueSets(valueSets);

                            String dateRangeFrom = execution.getDateRangeFrom();
                            String dateRangeTo = execution.getDateRangeTo();

                            Date dateFrom = new Date(dateRangeFrom);
                            Date dateTo = new Date(dateRangeTo);

                            executeLastExecution(algorithmData, dateFrom, dateTo);
                        }
                    });

        }

    }

    /**
     * Execute the last execution
     * 
     * @param algorithmData
     * @param fromDate
     * @param toDate
     */
    private void executeLastExecution(AlgorithmData algorithmData, Date fromDate, Date toDate) {

        Htp.EVENT_BUS.fireEvent(new PhenotypeExecuteStartedEvent());

        PhenotypeServiceAsync phenotypeService = GWT.create(PhenotypeService.class);
        phenotypeService.executePhenotype(algorithmData, fromDate, toDate, Htp.getLoggedInUser()
                .getUserName(), new AsyncCallback<Execution>() {

            @Override
            public void onSuccess(Execution result) {

                if (result.isError()) {
                    SC.warn("An error occurred while executing the algorithm.");
                } else {
                    String title = "Phenotype Execution Complete";
                    String message = "The Phenotype execution is complete.  You can view the results in the Summary, Demographics and WorkFlow tabs.";
                    MessageWindow messageWindow = new MessageWindow(title, message);
                    messageWindow.show();
                }

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

    private AlgorithmData copyAlgorithm(AlgorithmData algorithmData) {
        AlgorithmData copy = new AlgorithmData();

        copy.setAlgorithmDescription(algorithmData.getAlgorithmDescription());
        copy.setAlgorithmName(algorithmData.getAlgorithmName());
        copy.setAlgorithmVersion(algorithmData.getAlgorithmVersion());
        copy.setAlgorithmUser(algorithmData.getAlgorithmUser());
        copy.setId(algorithmData.getId());
        copy.setCategoryId(algorithmData.getCategoryId());
        copy.setParentId(algorithmData.getParentId());

        return copy;

    }

}
