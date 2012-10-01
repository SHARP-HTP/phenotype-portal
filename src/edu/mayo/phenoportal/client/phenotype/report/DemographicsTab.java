package edu.mayo.phenoportal.client.phenotype.report;

import java.util.ArrayList;
import java.util.List;

import org.moxieapps.gwt.highcharts.client.Chart;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.events.VisibilityChangedEvent;
import com.smartgwt.client.widgets.events.VisibilityChangedHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

import edu.mayo.phenoportal.client.charts.ColumnChart;
import edu.mayo.phenoportal.client.charts.PieChart;
import edu.mayo.phenoportal.client.charts.ResizeableChartCanvas;
import edu.mayo.phenoportal.client.dashboard.ChartUtils;
import edu.mayo.phenoportal.shared.Demographic;
import edu.mayo.phenoportal.shared.DemographicStat;
import edu.mayo.phenoportal.shared.DemographicsCategory;

/**
 * Tab for displaying the demographic charts for the selected phenotype. There
 * will be a any number of rows with any number of charts in each row.
 */
public class DemographicsTab extends Tab implements ReportTab {

    private static final String BACKGROUND_COLOR = "#d7d5ec";
    private static final String GRAPH_SELECTION_TITLE = "Graph Option";
    private static final String COLUMN_CHART = "Column Chart";
    private static final String PIE_CHART = "Pie Chart";
    private static final String MESSAGE_NO_DATA = "<b>No demographic data available</b>";
    private RadioGroupItem i_radioGroupItem;
    private DynamicForm i_graphOptionForm;
    private SelectItem i_demographicSelectItem;
    private String i_demographicSelected;
    private String i_chartOptionSelected;
    private VLayout i_mainChartPanel;
    private HTMLPane i_messagePane;
    private final List<Chart> i_chartList;

    private MultiChartPanelVerticleLayout i_multiChartVerticleLayout;

    public DemographicsTab(String title) {
        super(title);

        createMainPanel();
        i_chartList = new ArrayList<Chart>();
    }

    private void createMainPanel() {

        // Main layout that is the entire size of the tab. MultiChartPanel
        // should be added to this main panel.
        i_mainChartPanel = new VLayout();
        i_mainChartPanel.setWidth100();
        i_mainChartPanel.setHeight100();

        // We need to detect when the tab (or main panel) is hidden/displayed
        // and explicitly hide/show the GWT chart panel.
        i_mainChartPanel.addVisibilityChangedHandler(new VisibilityChangedHandler() {

            @Override
            public void onVisibilityChanged(VisibilityChangedEvent event) {
                // set each chart's visibility
                setChartVisibility(event.getIsVisible());
            }
        });

        setPane(i_mainChartPanel);
    }

    /**
     * This method will create the main option layout in the demographics tab
     * Checks to see if server side result is valid, and if valid creates main
     * option layout in the Demographics tab
     */
    public void createGraphs(List<Demographic> demographics) {

        if (demographics == null || demographics.size() == 0) {
            i_messagePane = new HTMLPane();
            i_messagePane.setWidth100();
            i_messagePane.setContents(MESSAGE_NO_DATA);
            i_messagePane.redraw();
            setPane(i_messagePane);
        } else {
            clearTab();
            createGraphOptions(demographics);
            setPane(i_mainChartPanel);
        }
    }

    /**
     * This methods will create an option layout and call the method
     * createOptionForms() to create different options to create graph
     * 
     * @param demographics
     */
    public void createGraphOptions(final List<Demographic> demographics) {
        final VLayout graphOptionLayout = new VLayout();
        graphOptionLayout.setBackgroundColor(BACKGROUND_COLOR);
        graphOptionLayout.setWidth(100);
        graphOptionLayout.setHeight(20);

        i_graphOptionForm = createOptionForms(demographics);
        graphOptionLayout.setMembers(i_graphOptionForm);

        i_mainChartPanel.addMember(graphOptionLayout);
    }

    /**
     * This method will create a new Dynamic form, which will add DropDown and
     * RadioButtton formitems into the main form. Onchange handler for both the
     * subitems will create the graph.
     * 
     * @param demographics
     */
    private DynamicForm createOptionForms(final List<Demographic> demographics) {

        // Creates the main Dynamic form to add the formitems
        DynamicForm form = new DynamicForm();

        form.setWidth(600);
        form.setHeight100();

        // Creating the DropDown formitem
        i_demographicSelectItem = new SelectItem();
        i_demographicSelectItem.setTitle("Graph");
        i_demographicSelectItem.setWidth(200);
        i_demographicSelectItem.setAlign(Alignment.LEFT);
        i_demographicSelectItem.setHint("<nobr>Select a criteria to view the graph.</nobr>");
        i_demographicSelectItem.setValueMap(getItemsAsArray(demographics));
        i_demographicSelectItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {

                // Demographic Selected by the user
                i_demographicSelected = event.getValue().toString();
                // onChange handler will be triggered to create the graph, based
                // on user selection
                redrawGraph(i_demographicSelected, i_chartOptionSelected, demographics);
            }
        });

        // creates the Radio Button formitem
        i_radioGroupItem = new RadioGroupItem();
        i_radioGroupItem.setTitle(GRAPH_SELECTION_TITLE);
        i_radioGroupItem.setWrapTitle(false);

        i_radioGroupItem.setAlign(Alignment.LEFT);
        i_radioGroupItem.setValueMap(PIE_CHART, COLUMN_CHART);
        i_radioGroupItem.setWidth(200);
        i_radioGroupItem.setDefaultValue(PIE_CHART);

        i_radioGroupItem.setVisible(false);

        i_radioGroupItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {

                i_chartOptionSelected = event.getValue().toString();
                // onChange handler to create the graph based on user selection
                redrawGraph(i_demographicSelected, i_chartOptionSelected, demographics);
            }
        });

        if (i_chartOptionSelected == null) {
            i_chartOptionSelected = PIE_CHART;
        }

        form.setFields(i_demographicSelectItem, i_radioGroupItem);
        return form;
    }

    /**
     * Method to draw the graph(Either Column or Pie) based on user selection
     * 
     * @param i_demographicSelected
     * @param chartOption
     * @param demographics
     */
    private void redrawGraph(String demographicSelected, String chartOption,
            List<Demographic> demographics) {

        i_radioGroupItem.show();
        if (i_multiChartVerticleLayout != null) {
            i_mainChartPanel.removeMember(i_multiChartVerticleLayout);
        }

        for (Demographic demographic : demographics) {

            String demographicType = demographic.getType();
            List<DemographicsCategory> categories = demographic.getDemoCategoryList();
            if (chartOption.equalsIgnoreCase(COLUMN_CHART)) {

                // Graphs will be defaulted to pie chart and
                // createColumnOfCharts() will be called to draw the column
                // chart
                if (demographicType.equalsIgnoreCase(demographicSelected)) {

                    i_multiChartVerticleLayout = createColumnOfCharts(demographicSelected,
                            categories);
                    i_mainChartPanel.addMember(i_multiChartVerticleLayout);

                }
                // If user selected Pie_chart createColumnOfPieCharts() will be
                // called to create pie chart
            } else if (chartOption == null || chartOption.equalsIgnoreCase(PIE_CHART)) {
                if (demographicType.equalsIgnoreCase(demographicSelected)) {
                    i_multiChartVerticleLayout = createColumnOfPieCharts(demographicSelected,
                            categories);
                    i_mainChartPanel.addMember(i_multiChartVerticleLayout);
                }
            }
            setPane(i_mainChartPanel);
        }
    }

    /**
     * @param demographicType
     * @param categories
     *            Method to create a column of pie chart. Will call the pie
     *            chart method to draw pie charts.
     */

    private MultiChartPanelVerticleLayout createColumnOfPieCharts(String demographicType,
            List<DemographicsCategory> categories) {

        MultiChartPanelVerticleLayout verticalChartPanel = new MultiChartPanelVerticleLayout();
        for (DemographicsCategory category : categories) {

            String categoryType = category.getName();
            List<DemographicStat> stats = category.getDemoStatList();

            String[] xAxisValues = ChartUtils.getXAxisValues(stats);
            Number[] yAxisValues = ChartUtils.getYAxisValues(stats);

            // create a pie chart
            PieChart pieChart = new PieChart();
            Chart demographicChart = pieChart.createPieChart(demographicType, categoryType,
                    xAxisValues, yAxisValues, false);

            i_chartList.add(demographicChart);

            ResizeableChartCanvas resizeableChartCanvas = new ResizeableChartCanvas(
                    demographicChart);

            verticalChartPanel.addChart(resizeableChartCanvas);
        }
        return verticalChartPanel;
    }

    /**
     * @param demographicType
     * @param categories
     *            Method to create a column of Column chart. Will call the
     *            column chart method to draw column charts.
     */
    private MultiChartPanelVerticleLayout createColumnOfCharts(String demographicType,
            List<DemographicsCategory> categories) {

        MultiChartPanelVerticleLayout verticalChartPanel = new MultiChartPanelVerticleLayout();
        for (DemographicsCategory category : categories) {

            String categoryType = category.getName();
            List<DemographicStat> stats = category.getDemoStatList();

            String[] xAxisValues = ChartUtils.getXAxisValues(stats);
            Number[] yAxisValues = ChartUtils.getYAxisValues(stats);

            // create a column chart
            ColumnChart columnChart = new ColumnChart();
            Chart demographicChart = columnChart.createDemographicChart(demographicType,
                    categoryType, xAxisValues, yAxisValues);

            i_chartList.add(demographicChart);

            ResizeableChartCanvas resizeableChartCanvas = new ResizeableChartCanvas(
                    demographicChart);

            verticalChartPanel.addChart(resizeableChartCanvas);
        }
        return verticalChartPanel;
    }

    /**
     * Create a String[] of demographic categories.
     * 
     * @param demographics
     * @return
     */
    public String[] getItemsAsArray(List<Demographic> demographics) {

        String[] demographicType = new String[demographics.size()];

        for (int i = 0; i < demographics.size(); i++) {
            demographicType[i] = demographics.get(i).getType();
        }

        return demographicType;
    }

    public void setChartVisibility(boolean visible) {
        // set each chart's visibility
        for (Chart chart : i_chartList) {
            chart.setVisible(visible);
        }
        i_mainChartPanel.redraw();
    }

    @Override
    public void clearTab() {
        if (i_mainChartPanel != null) {
            i_mainChartPanel.clear();
            i_mainChartPanel = null;
        }
        if (i_messagePane != null) {
            i_messagePane.clear();
            i_messagePane = null;
        }

        i_chartList.clear();

        createMainPanel();
        i_mainChartPanel.redraw();
    }

}
