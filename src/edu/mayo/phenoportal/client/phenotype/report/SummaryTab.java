package edu.mayo.phenoportal.client.phenotype.report;

import java.util.List;

import org.moxieapps.gwt.highcharts.client.Chart;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.events.VisibilityChangedEvent;
import com.smartgwt.client.widgets.events.VisibilityChangedHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

import edu.mayo.phenoportal.client.charts.ColumnChart;
import edu.mayo.phenoportal.client.charts.PieChart;
import edu.mayo.phenoportal.client.charts.ResizeableChartCanvas;
import edu.mayo.phenoportal.shared.Demographic;

/**
 * Tab for displaying the summary info for the selected phenotype.
 */
public class SummaryTab extends Tab implements ReportTab {

    private static final String BACKGROUND_COLOR = "#d7d5ec";
    private static final String MESSAGE_NO_DATA = "<b>No summary data available</b>";
    private static final String GRAPH_SELECTION_TITLE = "Graph Option";
    private static final String COLUMN_CHART = "Column Chart";
    private static final String PIE_CHART = "Pie Chart";
    private static final String CHART_TITLE = "Summary Chart";

    private VLayout i_graphOptionLayout;
    private DynamicForm i_graphOptionForm;
    private RadioGroupItem i_radioGroupItem;
    private String i_chartOptionSelected;

    private Chart i_summaryChart;
    private VLayout i_mainLayout;
    private HTMLPane i_messagePane;
    private ColumnChart i_columnChart;
    private PieChart i_pieChart;
    private ResizeableChartCanvas i_resizeableChartCanvas;

    private List<Demographic> i_demographicResult;

    public SummaryTab(String title) {
        super(title);

        i_chartOptionSelected = PIE_CHART;
        createMainPanel();
    }

    private void createMainPanel() {

        i_mainLayout = new VLayout();
        i_mainLayout.setHeight100();
        i_mainLayout.setWidth100();

        // We need to detect when the tab (or main panel) is hidden/displayed
        // and explicitly hide/show the GWT chart panel.
        i_mainLayout.addVisibilityChangedHandler(new VisibilityChangedHandler() {

            @Override
            public void onVisibilityChanged(VisibilityChangedEvent event) {
                setChartVisibility(event.getIsVisible());
            }
        });

        createGraphOptions();
        setPane(i_mainLayout);
    }

    public void createGraphs(List<Demographic> demographicResult) {
        i_demographicResult = demographicResult;
        clearTab();

        if (i_demographicResult == null) {
            i_messagePane = new HTMLPane();
            i_messagePane.setWidth100();
            i_messagePane.setContents(MESSAGE_NO_DATA);
            i_messagePane.redraw();
            setPane(i_messagePane);
        } else if (i_demographicResult.size() == 0) {
            i_messagePane = new HTMLPane();
            i_messagePane.setWidth100();
            i_messagePane.setContents("<b>No patients matched the criteria.</b>");
            i_messagePane.redraw();
            setPane(i_messagePane);
        } else {

            String[] xAxisValue = new String[i_demographicResult.size()];
            Number[] yAxisValue = new Number[i_demographicResult.size()];

            if (i_demographicResult != null) {
                for (int i = 0; i < i_demographicResult.size(); i++) {
                    Demographic demoIterate = i_demographicResult.get(i);
                    yAxisValue[i] = demoIterate.getTotal();
                    xAxisValue[i] = demoIterate.getType();
                }

                if (i_chartOptionSelected.equals(PIE_CHART)) {
                    createPieChart(xAxisValue, yAxisValue);
                } else if (i_chartOptionSelected.equals(COLUMN_CHART)) {
                    createColumnChart(xAxisValue, yAxisValue);
                }
            }
        }
    }

    /**
     * Create a column chart.
     * 
     * @param xAxisValues
     * @param yAxisValues
     */
    private void createColumnChart(String[] xAxisValues, Number[] yAxisValues) {
        i_columnChart = new ColumnChart();
        i_summaryChart = i_columnChart.createSummaryChart(CHART_TITLE, xAxisValues, yAxisValues);
        i_resizeableChartCanvas = new ResizeableChartCanvas(i_summaryChart);
        i_mainLayout.addMember(i_resizeableChartCanvas);

        setPane(i_mainLayout);
    }

    /**
     * Create a pie chart.
     * 
     * @param xAxisValues
     * @param yAxisValues
     */
    private void createPieChart(String[] xAxisValues, Number[] yAxisValues) {
        i_pieChart = new PieChart();
        i_summaryChart = i_pieChart
                .createPieChart(CHART_TITLE, "", xAxisValues, yAxisValues, false);
        i_resizeableChartCanvas = new ResizeableChartCanvas(i_summaryChart);
        i_mainLayout.addMember(i_resizeableChartCanvas);
        setPane(i_mainLayout);
    }

    /**
     * This methods will create an option layout to create different options to
     * create graph
     * 
     */
    public void createGraphOptions() {

        i_graphOptionLayout = new VLayout();
        i_graphOptionLayout.setBackgroundColor(BACKGROUND_COLOR);
        i_graphOptionLayout.setWidth100();
        i_graphOptionLayout.setHeight(20);

        i_graphOptionForm = createOptionsForm();
        i_graphOptionLayout.addMember(i_graphOptionForm);

        i_mainLayout.addMember(i_graphOptionLayout);
    }

    private DynamicForm createOptionsForm() {

        // Creates the main Dynamic form to add the formitems
        DynamicForm form = new DynamicForm();
        form.setWidth100();
        form.setHeight100();

        // creates the Radio Button formitem
        i_radioGroupItem = new RadioGroupItem();
        i_radioGroupItem.setTitle(GRAPH_SELECTION_TITLE);
        i_radioGroupItem.setWrapTitle(false);

        i_radioGroupItem.setAlign(Alignment.LEFT);
        i_radioGroupItem.setValueMap(PIE_CHART, COLUMN_CHART);
        i_radioGroupItem.setWidth(200);
        i_radioGroupItem.setDefaultValue(i_chartOptionSelected);

        i_radioGroupItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                i_chartOptionSelected = event.getValue().toString();
                createGraphs(i_demographicResult);
            }
        });

        form.setFields(i_radioGroupItem);
        return form;
    }

    public void setChartVisibility(boolean visible) {
        if (i_summaryChart != null) {
            i_summaryChart.setVisible(visible);
        }
        if (i_resizeableChartCanvas != null) {
            i_resizeableChartCanvas.setVisible(visible);
        }

        i_mainLayout.redraw();
    }

    @Override
    public void clearTab() {
        i_mainLayout = null;
        i_summaryChart = null;
        i_messagePane = null;
        i_columnChart = null;
        i_pieChart = null;
        i_resizeableChartCanvas = null;
        createMainPanel();
        i_mainLayout.redraw();
    }
}
