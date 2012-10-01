package edu.mayo.phenoportal.client.dashboard;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

import edu.mayo.phenoportal.client.charts.GraphPanel;

/**
 * Tab to hold the chart panel.
 */
public class DashboardTab extends Tab {

    private final GraphPanel i_graphPanel;
    private final VLayout i_mainLayout;

    public DashboardTab(String title, String[] xAxisValues, Number[] yAxisValues,
            boolean showChartLegend) {
        super(title);

        i_mainLayout = new VLayout();
        i_mainLayout.setWidth100();
        i_mainLayout.setHeight100();

        i_graphPanel = new GraphPanel(i_mainLayout);
        i_graphPanel.createPieChart(title, xAxisValues, yAxisValues, showChartLegend);

        setPane(i_mainLayout);
    }

}
