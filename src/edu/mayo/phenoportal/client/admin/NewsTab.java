package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class NewsTab extends Tab implements AdminTab {

    private static final int BUTTON_WIDTH = 160;
    private static final int LAYOUT_HEIGHT = 600;

    private static final String BACKGROUND_COLOR_LABEL = "#d7d5ec";
    private static final String NEWS_TITLE = "News Items";
    private static final String SHARP_NEWS_TITLE = "SHARP News Items";
    private static final String NEW_NEWS_BUTTON = "Add News Item";
    private static final String NEW_SHARP_NEWS_BUTTON = "Add SHARP News Item";

    private final VLayout i_newsLayout;

    // News widgets
    private final Label i_newsTitle;
    private final IButton i_addNewsButton;
    private final NewsListGrid i_newsListGrid;

    // SHARP news widgets
    private final Label i_sharpNewsTitle;
    private final IButton i_addSharpNewsButton;
    private final SharpNewsListGrid i_sharpNewsListGrid;

    public NewsTab(String title) {
        super(title);

        i_newsLayout = createNewsLayout();

        // News widgets
        i_newsTitle = createTitle(NEWS_TITLE);
        i_newsListGrid = new NewsListGrid();
        i_addNewsButton = createNewsButton();

        // SHARP News widgets
        i_sharpNewsTitle = createTitle(SHARP_NEWS_TITLE);
        i_sharpNewsListGrid = new SharpNewsListGrid();
        i_addSharpNewsButton = createSharpNewsButton();

        // Add all of the news widgets to the main layout
        i_newsLayout.addMember(i_newsTitle);
        i_newsLayout.addMember(i_newsListGrid);
        i_newsLayout.addMember(i_addNewsButton);

        // Add all of the SHARP news widgets to the main layout
        i_newsLayout.addMember(i_sharpNewsTitle);
        i_newsLayout.addMember(i_sharpNewsListGrid);
        i_newsLayout.addMember(i_addSharpNewsButton);

        setPane(i_newsLayout);
    }

    private Label createTitle(String text) {
        Label newsTitle = createLabel("<b>" + text + "</b>");
        return newsTitle;
    }

    private IButton createNewsButton() {
        IButton button = new IButton(NEW_NEWS_BUTTON);
        button.setWidth(BUTTON_WIDTH);
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                NewNewsWindow window = new NewNewsWindow();
                window.show();
            }
        });

        return button;
    }

    private IButton createSharpNewsButton() {
        IButton button = new IButton(NEW_SHARP_NEWS_BUTTON);
        button.setWidth(BUTTON_WIDTH);
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                NewSharpNewsWindow window = new NewSharpNewsWindow();
                window.show();
            }
        });

        return button;
    }

    /**
     * This layout holds the News ListGrid and the form to add new news items.
     */
    private VLayout createNewsLayout() {
        VLayout newsLayout = new VLayout();

        newsLayout.setWidth100();
        newsLayout.setHeight(LAYOUT_HEIGHT);

        newsLayout.setMargin(10);
        newsLayout.setMembersMargin(10);

        return newsLayout;
    }

    @Override
    public String getTabDescription() {
        return "Displays the regular news and SHARP news items on the home page.  Administrators can add, update, and delete news items.";
    }

    @Override
    public void updateContents() {
        // invalidate the cache to force the datasource to fetch the data again.
        i_newsListGrid.invalidateCache();
        i_newsListGrid.getDataSource().setTestData(null);
        i_newsListGrid.getDataSource().invalidateCache();

        i_sharpNewsListGrid.invalidateCache();
        i_sharpNewsListGrid.getDataSource().setTestData(null);
        i_sharpNewsListGrid.getDataSource().invalidateCache();
    }

    /**
     * Create a label.
     * 
     * @param textToShow
     * @return
     */
    private Label createLabel(String textToShow) {

        Label label = new Label();
        label.setBackgroundColor(BACKGROUND_COLOR_LABEL);
        label.setHeight(30);
        label.setWidth100();
        label.setPadding(0);
        label.setMargin(3);
        label.setAlign(Alignment.CENTER);
        label.setValign(VerticalAlignment.CENTER);
        label.setContents(textToShow);
        label.setWrap(false);

        return label;
    }
}
