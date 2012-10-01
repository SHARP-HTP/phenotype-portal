package edu.mayo.phenoportal.client.charts;

import org.moxieapps.gwt.highcharts.client.Chart;

import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.events.VisibilityChangedEvent;
import com.smartgwt.client.widgets.events.VisibilityChangedHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Graph pane that manages creating the chart based on ExecutionResults.
 */
public class GraphPanel extends VLayout {

    private static final String CHART_TITLE = "DB Stats Chart for ";
    private static final String MESSAGE_NO_STATS_DATA = "<b>No DB stats data available.</b>";

    private Chart i_summaryChart;
    private VLayout i_mainChartLayout;
    private HTMLPane i_messagePane;
    private PieChart i_pieChart;
    private ResizeableChartCanvas i_resizeableChartCanvas;
    private final Layout i_parent;

    public GraphPanel(Layout parent) {
        super();

        i_parent = parent;
        createMainChartPanel();
    }

    /**
     * Create a pie chart.
     * 
     * @param xAxisValues
     * @param yAxisValues
     */
    public void createPieChart(String title, String[] xAxisValues, Number[] yAxisValues,
            boolean showChartLegend) {

        clearPanel();

        if (xAxisValues == null || xAxisValues.length == 0 || yAxisValues == null
                || yAxisValues.length == 0) {
            i_messagePane = new HTMLPane();
            i_messagePane.setWidth100();
            i_messagePane.setContents(MESSAGE_NO_STATS_DATA);
            i_messagePane.redraw();
            i_parent.addMember(i_messagePane);
        } else {

            i_pieChart = new PieChart();
            i_summaryChart = i_pieChart.createPieChart(CHART_TITLE + title, "", xAxisValues,
                    yAxisValues, showChartLegend);
            i_resizeableChartCanvas = new ResizeableChartCanvas(i_summaryChart);
            i_mainChartLayout.addMember(i_resizeableChartCanvas);
            i_parent.addMember(i_mainChartLayout);
        }
    }

    public void createMainChartPanel() {

        i_mainChartLayout = new VLayout();
        i_mainChartLayout.setHeight100();
        i_mainChartLayout.setWidth100();

        // We need to detect when the tab (or main panel) is hidden/displayed
        // and explicitly hide/show the GWT chart panel.
        i_mainChartLayout.addVisibilityChangedHandler(new VisibilityChangedHandler() {

            @Override
            public void onVisibilityChanged(VisibilityChangedEvent event) {
                setChartVisibility(event.getIsVisible());
            }
        });

        i_parent.addMember(i_mainChartLayout);
    }

    public void clearPanel() {
        i_parent.removeMember(i_mainChartLayout);
        i_mainChartLayout = null;
        i_summaryChart = null;
        i_messagePane = null;
        i_pieChart = null;
        i_resizeableChartCanvas = null;

        createMainChartPanel();
        i_mainChartLayout.redraw();
    }

    public void setChartVisibility(boolean visible) {
        if (i_summaryChart != null) {
            i_summaryChart.setVisible(visible);
        }
        if (i_resizeableChartCanvas != null) {
            i_resizeableChartCanvas.setVisible(visible);
        }

        i_mainChartLayout.redraw();
    }
}
