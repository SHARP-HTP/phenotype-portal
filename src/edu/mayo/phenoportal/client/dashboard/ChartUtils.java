package edu.mayo.phenoportal.client.dashboard;

import java.util.List;

import edu.mayo.phenoportal.shared.DemographicStat;

/**
 * Utility class for charts
 */
public class ChartUtils {

    /**
     * Get the X axis values
     * 
     * @param stats
     * @return
     */
    public static String[] getXAxisValues(List<DemographicStat> stats) {
        String[] xAxisValues = new String[stats.size()];

        for (int i = 0; i < stats.size(); i++) {
            xAxisValues[i] = stats.get(i).getLabel();
        }

        return xAxisValues;
    }

    /**
     * Get the Y axis values
     * 
     * @param stats
     * @return
     */
    public static Number[] getYAxisValues(List<DemographicStat> stats) {
        Number[] yAxisValues = new Number[stats.size()];

        for (int i = 0; i < stats.size(); i++) {
            yAxisValues[i] = stats.get(i).getValue();
        }

        return yAxisValues;
    }
}
