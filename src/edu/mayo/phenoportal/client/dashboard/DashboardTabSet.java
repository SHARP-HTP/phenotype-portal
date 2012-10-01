package edu.mayo.phenoportal.client.dashboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.tab.TabSet;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;
import edu.mayo.phenoportal.shared.Demographic;
import edu.mayo.phenoportal.shared.DemographicStat;
import edu.mayo.phenoportal.shared.DemographicsCategory;
import edu.mayo.phenoportal.shared.Execution;

import java.util.List;

public class DashboardTabSet extends TabSet {

    private final boolean i_showChartLegend;

    public DashboardTabSet(boolean showChartLegend) {
        super();

        i_showChartLegend = showChartLegend;

        setTabBarPosition(Side.TOP);
        setWidth100();
        setHeight100();

        getStatsData();
    }

    /**
     * Make RPC call to get the DB stats data
     *
     */
    protected void getStatsData() {
        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);

        // TODO change API to not send Title
        service.getDbStats("", new AsyncCallback<Execution>() {

            @Override
            public void onSuccess(Execution result) {
                // System.out.println("get the DB Stats reultls for " + title);

                List<Demographic> demographics = result.getDemographics();

                // go through the demographics - numberartor, demominator,
                // patient population (use the first one)
                for (Demographic demographic : demographics) {

                    String demographicType = demographic.getType();
                    List<DemographicsCategory> categories = demographic.getDemoCategoryList();

                    // go through the categories - gender, ethnicity, age,
                    // gender - and create a tab with a chart for each
                    for (DemographicsCategory category : categories) {

                        String categoryType = category.getName();
                        List<DemographicStat> stats = category.getDemoStatList();

                        String[] xAxisValues = ChartUtils.getXAxisValues(stats);
                        Number[] yAxisValues = ChartUtils.getYAxisValues(stats);

                        DashboardTab dashboardTab = new DashboardTab(categoryType, xAxisValues,
                                yAxisValues, i_showChartLegend);
                        addTab(dashboardTab);
                    }

                    // TODO CME - change when we get real db stats...
                    // Just loop through once. This is not real data!
                    break;
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                SC.warn("Unable to retrieve DB statistics");
            }
        });
    }
}
