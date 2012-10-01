package edu.mayo.phenoportal.client.phenotype.report;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * Panel for adding any number of charts left to right.
 */
public class MultiChartPanel extends HLayout {

    public MultiChartPanel() {
        super();
        init();
    }

    private void init() {

        setWidth100();
        setHeight(250);
        setMembersMargin(15);
        setMargin(10);
    }

    public void addChart(Canvas chart) {
        addMember(chart);
    }

}
