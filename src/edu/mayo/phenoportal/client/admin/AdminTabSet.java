package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * Set of tabs for performing administrative functions.
 */
public class AdminTabSet extends TabSet {

    private ReportUploadTab i_uploadReportTab;
    private ReportExecutionTab i_executeReportTab;
    private CategoryTab i_categoryTab;
    private UserTab i_userTab;
    private NewsTab i_newsTab;

    public AdminTabSet() {
        super();
        init();
    }

    public void init() {

        // create the tabset
        setTabBarAlign(Side.LEFT);
        setTabBarPosition(Side.TOP);
        setWidth100();
        setHeight100();

        // create the individual tabs
        i_uploadReportTab = new ReportUploadTab("Uploader Report");
        i_executeReportTab = new ReportExecutionTab("Execution Report");
        i_categoryTab = new CategoryTab("Categories");
        i_userTab = new UserTab("Users");
        i_newsTab = new NewsTab("News");

        // add them to the tabset
        addTab(i_userTab);

        // put back in when we implement this tab.
        // addTab(i_categoryTab);
        addTab(i_uploadReportTab);
        addTab(i_executeReportTab);
        addTab(i_newsTab);
    }

    public void selectInitialTab() {
        // initially select this tab
        selectTab(i_userTab);
    }
}
