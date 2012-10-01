package edu.mayo.phenoportal.client.charts;

import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Credits;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.ToolTip;
import org.moxieapps.gwt.highcharts.client.ToolTipData;
import org.moxieapps.gwt.highcharts.client.ToolTipFormatter;
import org.moxieapps.gwt.highcharts.client.labels.DataLabelsData;
import org.moxieapps.gwt.highcharts.client.labels.DataLabelsFormatter;
import org.moxieapps.gwt.highcharts.client.labels.LegendLabelsData;
import org.moxieapps.gwt.highcharts.client.labels.LegendLabelsFormatter;
import org.moxieapps.gwt.highcharts.client.labels.PieDataLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.PiePlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.PlotOptions;

/**
 * Class to wrap the details of creating a Pie chart
 */
public class PieChart {

    public Chart createPieChart() {

        final Chart chart = new Chart()
                .setType(Series.Type.PIE)
                .setChartTitleText("Browser market shares at a specific website, 2010")
                .setPlotBackgroundColor((String) null)
                .setPlotBorderWidth(null)
                .setPlotShadow(true)
                .setPiePlotOptions(
                        new PiePlotOptions().setAllowPointSelect(true)
                                .setCursor(PlotOptions.Cursor.POINTER)
                                .setPieDataLabels(new PieDataLabels().setEnabled(false))
                                .setShowInLegend(true))
                .setToolTip(new ToolTip().setFormatter(new ToolTipFormatter() {
                    @Override
                    public String format(ToolTipData toolTipData) {
                        return "<b>" + toolTipData.getPointName() + "</b>: "
                                + toolTipData.getYAsDouble() + " %";
                    }
                }));

        chart.addSeries(chart
                .createSeries()
                .setName("Browser share")
                .setPoints(
                        new Point[] { new Point("Firefox", 45.0), new Point("IE", 26.8),
                                new Point("Chrome", 12.8).setSliced(true).setSelected(true),
                                new Point("Safari", 8.5), new Point("Opera", 6.2),
                                new Point("Others", 0.7) }));

        return chart;
    }

    /**
     * Mathod to create Pie chart
     * 
     * @param title
     *            - Title of the graph. Ex-Numerator, Denominator
     * @param subTitle
     *            - Subtitle of the graph. Ex - Age, Race, Ethnicity
     * @param name
     *            - String[]Name - Xaxis value - Ex. Male, Female
     * @param value
     *            - Number[]Values - YAxis values - Ex. value of Male-423
     * @return
     */
    public Chart createPieChart(String title, String subTitle, String[] name, Number[] value,
            boolean showLegend) {

        final int total = getTotalValue(value);

        final Point[] points = getPoints(name, value);

        final Chart chart = new Chart()
                .setType(Series.Type.PIE)
                .setChartTitleText(title)
                .setChartSubtitleText(subTitle)
                .setPlotBackgroundColor((String) null)
                .setPlotBorderWidth(null)
                .setPlotShadow(false)
                .setPiePlotOptions(
                        new PiePlotOptions()
                                .setAllowPointSelect(true)
                                .setCursor(PlotOptions.Cursor.POINTER)
                                .setPieDataLabels(

                                        new PieDataLabels().setEnabled(true).setFormatter(
                                                new DataLabelsFormatter() {
                                                    @Override
                                                    public String format(
                                                            DataLabelsData dataLabelsData) {
                                                        return "<b>"
                                                                + dataLabelsData.getPointName()
                                                                + "</b>: "
                                                                + dataLabelsData.getYAsLong();
                                                    }
                                                })).setShowInLegend(true))

                .setToolTip(new ToolTip().setFormatter(new ToolTipFormatter() {
                    @Override
                    public String format(ToolTipData toolTipData) {
                        return "<b>" + toolTipData.getPointName() + "</b>: "
                                + getPercentage(total, toolTipData.getYAsDouble()) + "%";
                    }
                }));

        chart.setCredits(new Credits().setEnabled(false));

        // determine if the legend should be displayed
        if (showLegend) {
            chart.setLegend(new Legend().setLayout(Legend.Layout.VERTICAL)
                    .setAlign(Legend.Align.LEFT).setEnabled(true)
                    .setVerticalAlign(Legend.VerticalAlign.BOTTOM).setBackgroundColor("#FFFFFF")
                    .setShadow(true).setLabelsFormatter(new LegendLabelsFormatter() {

                        @Override
                        public String format(LegendLabelsData legendLabelsData) {
                            String name = legendLabelsData.getPointName();
                            Double value = legendLabelsData.getPoint().getY().doubleValue();

                            return name + ": " + value + " (" + getPercentage(total, value) + "%)";
                        }
                    }));
        } else {
            // set the legend off the screen so you can't see it.
            chart.setLegend(new Legend().setX(-200).setY(100));
        }

        // Set the points to generate graph
        chart.addSeries(chart.createSeries().setName(subTitle).setPoints(points)
                .setOption("colorByPoint", true));

        return chart;
    }

    // Method to set the points to generate the graph
    private Point[] getPoints(String[] name, Number[] value) {
        Point[] points = new Point[name.length];
        for (int i = 0; i < name.length; i++) {
            points[i] = new Point(name[i], value[i]);
        }

        // select the first wedge.
        if (points.length > 0) {
            points[0].setSliced(true).setSelected(true);
        }

        return points;
    }

    /**
     * Get a sum of all of the values in the array
     * 
     * @param values
     * @return
     */
    private int getTotalValue(Number[] values) {
        int total = 0;

        for (int i = 0; i < values.length; i++) {
            total += values[i].intValue();
        }

        return total;

    }

    /**
     * Get the percentage and truncate to one decimal place. ex. 34.8
     * 
     * @param total
     * @param value
     * @return
     */
    private String getPercentage(int total, double value) {
        double percent = value / total * 100;

        String percentStr = percent + "";

        if (percentStr.contains(".")) {
            int decimalPointIndex = percentStr.indexOf(".");
            if (percentStr.length() > decimalPointIndex) {
                percentStr = percentStr.substring(0, decimalPointIndex + 2);
            }
        }

        return percentStr;
    }

}
