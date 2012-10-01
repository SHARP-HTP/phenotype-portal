package edu.mayo.phenoportal.client.help;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.HTMLPane;

/**
 * HTMLPane to display the help contents
 */
public class GuideHtmlPane extends HTMLPane {

    public GuideHtmlPane() {
        super();
        init();
    }

    /**
     * Load the help html page.
     */
    private void init() {
        String contextPath = GWT.getHostPageBaseURL();
        setContentsURL(contextPath + "data/pages/help/manual.htm");
    }
}
