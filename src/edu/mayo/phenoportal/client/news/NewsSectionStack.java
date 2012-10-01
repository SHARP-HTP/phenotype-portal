package edu.mayo.phenoportal.client.news;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

import edu.mayo.phenoportal.client.dashboard.DashboardPanel;

/**
 * SectionStack that contains a SectionStackSection for different news sections.
 */
public class NewsSectionStack extends SectionStack {

    private static Logger lgr = Logger.getLogger(NewsSectionStack.class.getName());

    private static final String ALGORITHM_UPLOADED_TITLE = "Recently Uploaded";
    private static final String NEWS_TITLE = "News and Updates";
    private static final String DB_STATISTICS_TITLE = "Current Database Statistics";

    private static final String SHARP_TITLE = "SHARP";

    private SectionStackSection i_uploadedAlgorithmSection;
    private SectionStackSection i_newsSection;
    private SectionStackSection i_dbStatisticsSection;
    private SectionStackSection i_sharpNewsSection;

    private UploadedAlgorithmListGrid i_uploadedAlgorithmListGrid;
    private NewsListGrid i_newsListGrid;
    private DashboardPanel i_dashboardPanel;

    private SharpNewsListGrid i_sharpNewsListGrid;

    public NewsSectionStack() {
        super();

        lgr.log(Level.INFO, "init Navigation Pane()...");
        init();
    }

    private void init() {
        setVisibilityMode(VisibilityMode.MULTIPLE);
        setWidth100();
        setHeight100();

        // create the news/updates section
        addNewsSection();

        // TODO CME combine the SHARP news and Regular news above.
        // create the SHARP section
        // addSharpSection();

        // create the recently uploaded algorithm section
        addRecentlyUploadedAlgorithmsSection();

        addCurrentDBStatisicsSection();
    }

    private void addSharpSection() {
        i_sharpNewsSection = new SectionStackSection(SHARP_TITLE);
        i_sharpNewsSection.setExpanded(true);
        i_sharpNewsSection.setCanCollapse(true);

        // create the SHARP news table and add it to the section
        i_sharpNewsListGrid = new SharpNewsListGrid();
        i_sharpNewsSection.addItem(i_sharpNewsListGrid);

        addSection(i_sharpNewsSection);
    }

    private void addNewsSection() {
        i_newsSection = new SectionStackSection(NEWS_TITLE);
        i_newsSection.setExpanded(true);
        i_newsSection.setCanCollapse(true);

        // create the news/updates table and add it to the section
        i_newsListGrid = new NewsListGrid();
        i_newsSection.addItem(i_newsListGrid);

        addSection(i_newsSection);
    }

    private void addRecentlyUploadedAlgorithmsSection() {
        i_uploadedAlgorithmSection = new SectionStackSection(ALGORITHM_UPLOADED_TITLE);
        i_uploadedAlgorithmSection.setExpanded(true);
        i_uploadedAlgorithmSection.setCanCollapse(true);

        // create the algorithm panel and add it to this section.
        i_uploadedAlgorithmListGrid = new UploadedAlgorithmListGrid();
        i_uploadedAlgorithmSection.addItem(i_uploadedAlgorithmListGrid);

        addSection(i_uploadedAlgorithmSection);
    }

    private void addCurrentDBStatisicsSection() {
        i_dbStatisticsSection = new SectionStackSection(DB_STATISTICS_TITLE);
        i_dbStatisticsSection.setExpanded(true);
        i_dbStatisticsSection.setCanCollapse(true);

        i_dashboardPanel = new DashboardPanel();
        i_dbStatisticsSection.addItem(i_dashboardPanel);
        // create the db stats panel and add it to this section.
        // i_uploadedAlgorithmListGrid = new UploadedAlgorithmListGrid();
        // i_uploadedAlgorithmSection.addItem(i_uploadedAlgorithmListGrid);

        addSection(i_dbStatisticsSection);
    }

    public void refreshData() {

        i_newsListGrid.invalidateCache();
        i_newsListGrid.getDataSource().setTestData(null);
        i_newsListGrid.getDataSource().invalidateCache();

        // i_sharpNewsListGrid.invalidateCache();
        // i_sharpNewsListGrid.getDataSource().setTestData(null);
        // i_sharpNewsListGrid.getDataSource().invalidateCache();
    }
}
