package edu.mayo.phenoportal.client.phenotype.report;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Panel for adding any number of charts left to right.
 */

public class MultiChartPanelVerticleLayout extends VLayout {

    private static final String BACKGROUND_COLOR = "#f4f4fa";

    public MultiChartPanelVerticleLayout() {
        super();
        init();
    }

    private void init() {
        setBackgroundColor(BACKGROUND_COLOR);
        setAlign(Alignment.CENTER);
        setWidth(600);
        setHeight(1000);
        setMembersMargin(15);
        setMargin(10);
    }

    public void addChart(Canvas chart) {
        addMember(chart);
    }

}
