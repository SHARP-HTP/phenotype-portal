package edu.mayo.phenoportal.client.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.phenoportal.client.news.NewsPanel;
import edu.mayo.phenoportal.client.utils.UiHelper;

/**
 * The Welcome Panel.
 */
public class WelcomePanel extends HLayout {

    private static Logger lgr = Logger.getLogger(WelcomePanel.class.getName());

    private HTMLPane i_mainPane;
    private NewsPanel i_newsPanel;

    public WelcomePanel() {
        super();
        init();
    }

    private void init() {
        setWidth100();
        setHeight100();
        getWelcomeSharpProjectDetails();
    }

    /*
     * Welcome page contents about SHARP Project
     */
    private void getWelcomeSharpProjectDetails() {

        i_mainPane = createMainPanel();
        i_newsPanel = new NewsPanel();

        // add rounded borders to the layout.
        UiHelper.createLayoutWithBorders(i_newsPanel);

        VLayout mainLayout = new VLayout();
        mainLayout.setHeight100();
        mainLayout.setWidth("65%");
        mainLayout.addMember(i_mainPane);

        // add rounded borders to the layout.
        UiHelper.createLayoutWithBorders(mainLayout);

        addMember(mainLayout);
        addMember(i_newsPanel);
    }

    /**
     * Main panel is the home page text/image.
     * 
     * @return
     */
    private HTMLPane createMainPanel() {
        HTMLPane htmlPane = new HTMLPane();
        htmlPane.setWidth100();
        htmlPane.setHeight100();

        String contextPath = GWT.getHostPageBaseURL();
        lgr.log(Level.INFO, "ContextPath: " + contextPath);
        String fileLocation = contextPath + "data/pages/home/home.html";
        htmlPane.setContentsURL(fileLocation);

        return htmlPane;
    }

    public void refreshNews() {
        i_newsPanel.refreshNews();
    }

}
