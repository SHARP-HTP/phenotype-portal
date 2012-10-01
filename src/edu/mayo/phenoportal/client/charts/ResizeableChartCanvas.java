package edu.mayo.phenoportal.client.charts;

import org.moxieapps.gwt.highcharts.client.BaseChart;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.WidgetCanvas;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;

/**
 * A SmartGWT container widget that will contain a GWT Highchart and
 * automatically handle growing/shrinking the chart as the SmartGWT container
 * changes in size.
 * 
 * @author squinn@moxiegroup.com (Shawn Quinn)
 * @since 1.0
 */
public class ResizeableChartCanvas extends WidgetCanvas {

    public ResizeableChartCanvas(final BaseChart chart) {
        super(chart);
        chart.setReflow(false);
        final WidgetCanvas wc = this;
        this.addResizedHandler(new ResizedHandler() {
            @Override
            public void onResized(ResizedEvent event) {
                chart.setSize(wc.getWidth(), wc.getHeight(), false);
            }
        });
        this.addDrawHandler(new DrawHandler() {
            @Override
            public void onDraw(DrawEvent event) {
                chart.setSize(wc.getWidth(), wc.getHeight(), true);
            }
        });
        wc.setOverflow(Overflow.HIDDEN);
    }
}
