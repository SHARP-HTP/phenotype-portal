package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.widgets.tab.Tab;

public class ReportExecutionTab extends Tab implements AdminTab {

    private final ExecutionListGrid i_executionListGrid;

    public ReportExecutionTab(String title) {
        super(title);

        i_executionListGrid = new ExecutionListGrid();
        setPane(i_executionListGrid);
    }

    @Override
    public String getTabDescription() {
        return "Displays a list of Phenotypes that have been executed by user.";
    }

    @Override
    public void updateContents() {

        // invalidate the cache to force the datasource to fetch the data again.
        i_executionListGrid.invalidateCache();
        i_executionListGrid.getDataSource().setTestData(null);
        i_executionListGrid.getDataSource().invalidateCache();
    }
}
