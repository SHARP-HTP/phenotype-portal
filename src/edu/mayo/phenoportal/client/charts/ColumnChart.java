package edu.mayo.phenoportal.client.charts;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Credits;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.Style;
import org.moxieapps.gwt.highcharts.client.ToolTip;
import org.moxieapps.gwt.highcharts.client.ToolTipData;
import org.moxieapps.gwt.highcharts.client.ToolTipFormatter;
import org.moxieapps.gwt.highcharts.client.labels.AxisLabelsData;
import org.moxieapps.gwt.highcharts.client.labels.AxisLabelsFormatter;
import org.moxieapps.gwt.highcharts.client.labels.DataLabels;
import org.moxieapps.gwt.highcharts.client.labels.DataLabelsData;
import org.moxieapps.gwt.highcharts.client.labels.DataLabelsFormatter;
import org.moxieapps.gwt.highcharts.client.labels.Labels;
import org.moxieapps.gwt.highcharts.client.labels.XAxisLabels;
import org.moxieapps.gwt.highcharts.client.labels.YAxisLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.ColumnPlotOptions;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * Class to wrap the details of creating a Column chart
 */
public class ColumnChart {

    static Logger lgr = Logger.getLogger(ColumnChart.class.getName());

    public ColumnChart() {
        super();
    }

    // Sample Highchart to draw a Column chart
    public Chart createChart() {

        lgr.log(Level.INFO, "Creating Graph!!");
        final Chart chart = new Chart()

                .setType(Series.Type.COLUMN)
                .setChartTitleText("Monthly Average Rainfall")
                .setChartSubtitleText("Source: WorldClimate.com")
                .setColumnPlotOptions(
                        new ColumnPlotOptions().setPointPadding(0.2).setBorderWidth(0))
                .setLegend(
                        new Legend().setLayout(Legend.Layout.VERTICAL).setAlign(Legend.Align.LEFT)
                                .setVerticalAlign(Legend.VerticalAlign.TOP).setX(100).setY(70)
                                .setFloating(true).setBackgroundColor("#FFFFFF").setShadow(true))
                .setToolTip(new ToolTip().setFormatter(new ToolTipFormatter() {
                    @Override
                    public String format(ToolTipData toolTipData) {
                        return toolTipData.getXAsString() + ": " + toolTipData.getYAsLong() + " mm";
                    }
                }));

        chart.setCredits(new Credits().setEnabled(false));

        chart.getXAxis().setCategories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec");

        chart.getYAxis().setAxisTitleText("Rainfall (mm)").setMin(0);

        chart.addSeries(chart
                .createSeries()
                .setName("Tokyo")
                .setPoints(
                        new Number[] { 49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4,
                                194.1, 95.6, 54.4 }));
        return chart;
    }

    /**
     * Method to draw the Demographic Column chart
     * 
     * @param title
     *            - Title for generating summary chart
     * @param subTitle
     *            -Subtitle for the graph
     * @param xAxis
     *            - XAxis value to plot the graph
     * @param yAxisValue
     *            - YAxis value to plot the graph
     * @return
     */
    public Chart createDemographicChart(String title, String subTitle, String[] xAxis,
            Number[] yAxisValue) {

        final Chart chart = new Chart()
                .setType(Series.Type.COLUMN)
                .setChartTitleText(title)
                .setChartSubtitleText(subTitle)
                .setColumnPlotOptions(new ColumnPlotOptions().setPointWidth(27))
                .setLegend(
                        new Legend().setLayout(Legend.Layout.VERTICAL).setAlign(Legend.Align.LEFT)
                                .setEnabled(false).setVerticalAlign(Legend.VerticalAlign.TOP)
                                .setX(100).setY(70).setFloating(true).setBackgroundColor("#FFFFFF")
                                .setShadow(true))

                .setToolTip(new ToolTip().setFormatter(new ToolTipFormatter() {
                    @Override
                    public String format(ToolTipData toolTipData) {
                        return "<b>" + toolTipData.getXAsString() + "</b>: "
                                + toolTipData.getYAsLong();
                    }
                }

                ));

        // Set XAxis Value
        chart.getXAxis().setCategories(xAxis);
        chart.getXAxis()
                .setAxisTitleText(subTitle)
                .setLabels(
                        new XAxisLabels().setColor("#000000").setFormatter(
                                new AxisLabelsFormatter() {

                                    @Override
                                    public String format(AxisLabelsData axisLabelsData) {
                                        return "<b>" + axisLabelsData.getValueAsString() + "</b>";
                                    }
                                }));

        chart.getYAxis()
                .setAxisTitleText("Count")
                .setMin(0)
                .setLabels(
                        new YAxisLabels().setColor("#000000").setFormatter(
                                new AxisLabelsFormatter() {

                                    @Override
                                    public String format(AxisLabelsData axisLabelsData) {
                                        return "<b>" + axisLabelsData.getValueAsString() + "</b>";
                                    }
                                }));

        chart.setCredits(new Credits().setEnabled(false));

        // Set YAxis Value
        chart.addSeries(chart.createSeries()
                .setPoints(yAxisValue)
                .setOption("colorByPoint", true)

                // Set the DataValue
                .setPlotOptions(
                        new ColumnPlotOptions().setDataLabels(new DataLabels().setEnabled(true)
                                .setColor("#FFFFFF").setAlign(Labels.Align.CENTER).setX(-3)
                                .setY(10).setFormatter(new DataLabelsFormatter() {
                                    @Override
                                    public String format(DataLabelsData dataLabelsData) {
                                        return NumberFormat.getFormat("0").format(
                                                dataLabelsData.getYAsDouble());
                                    }
                                }).setStyle(new Style().setFont("normal 13px Verdana, sans-serif")))));

        return chart;
    }

    /**
     * Method for creating Summary Chart
     * 
     * @param title
     *            - Title for generating summary chart
     * @param subTitle
     *            - Subtitle for the graph
     * @param xAxis
     *            - X Axis Value to plot the graph
     * @param yAxis
     *            - Y Axix Value to plot the graph
     * @return chart
     */

    public Chart createSummaryChart(String title, String[] xAxis, Number[] yAxisValue) {

        final Chart chart = new Chart()
                .setType(Series.Type.COLUMN)
                .setChartTitleText(title)

                .setColumnPlotOptions(
                        new ColumnPlotOptions()
                                .setPointWidth(70)
                                .setColor("#000000")
                                .setDataLabels(
                                        new DataLabels().setFormatter(new DataLabelsFormatter() {

                                            @Override
                                            public String format(DataLabelsData dataLabelsData) {
                                                return "<b>" + dataLabelsData.getPointName()
                                                        + "</b>: " + dataLabelsData.getYAsLong();
                                            }
                                        })))
                .setLegend(
                        new Legend().setLayout(Legend.Layout.VERTICAL).setAlign(Legend.Align.LEFT)
                                .setEnabled(false).setVerticalAlign(Legend.VerticalAlign.TOP)
                                .setX(100).setY(70).setFloating(true).setBackgroundColor("#FFFFFF")
                                .setShadow(true))

                .setToolTip(new ToolTip().setFormatter(new ToolTipFormatter() {
                    @Override
                    public String format(ToolTipData toolTipData) {
                        return "<b>" + toolTipData.getXAsString() + "</b>: "
                                + toolTipData.getYAsLong();
                    }
                }

                ));
        chart.getXAxis().setLabels(
                new XAxisLabels().setColor("#000000").setFormatter(new AxisLabelsFormatter() {

                    @Override
                    public String format(AxisLabelsData axisLabelsData) {
                        return "<b>" + axisLabelsData.getValueAsString() + "</b>";
                    }
                }));

        // Set XAxis Value
        chart.getXAxis().setCategories(xAxis);

        chart.setCredits(new Credits().setEnabled(false));

        // Set YAxis Value
        chart.getYAxis()
                .setAxisTitleText("Count")
                .setMin(0)
                .setLabels(
                        new YAxisLabels().setColor("#000000").setFormatter(
                                new AxisLabelsFormatter() {

                                    @Override
                                    public String format(AxisLabelsData axisLabelsData) {
                                        return "<b>" + axisLabelsData.getValueAsString() + "</b>";
                                    }
                                }));
        chart.addSeries(chart
                .createSeries()
                .setPoints(yAxisValue)
                .setOption("colorByPoint", true)

                // Set the DataValue -- numbers displayed in the Bars
                .setPlotOptions(
                        new ColumnPlotOptions().setDataLabels(new DataLabels().setEnabled(true)
                                .setColor("#FFFFFF").setAlign(Labels.Align.CENTER).setX(-3)
                                .setY(10).setFormatter(new DataLabelsFormatter() {
                                    @Override
                                    public String format(DataLabelsData dataLabelsData) {
                                        return "<b>"
                                                + NumberFormat.getFormat("0").format(
                                                        dataLabelsData.getYAsDouble()) + "</b>";
                                    }
                                })/*
                                   * .setStyle(new Style().setFont(
                                   * "normal 13px Verdana, sans-serif"))
                                   */)));

        return chart;
    }
}