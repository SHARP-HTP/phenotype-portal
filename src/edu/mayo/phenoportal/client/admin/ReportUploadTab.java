package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.widgets.tab.Tab;

public class ReportUploadTab extends Tab implements AdminTab {

    private final UploadListGrid i_uploadListGrid;

    public ReportUploadTab(String title) {
        super(title);

        i_uploadListGrid = new UploadListGrid();
        setPane(i_uploadListGrid);
    }

    @Override
    public String getTabDescription() {
        return "Displays a list of Phenotypes that have been uploaded by user.";
    }

    @Override
    public void updateContents() {

        // invalidate the cache to force the datasource to fetch the data again.
        i_uploadListGrid.invalidateCache();
        i_uploadListGrid.getDataSource().setTestData(null);
        i_uploadListGrid.getDataSource().invalidateCache();
    }
}
