package edu.mayo.phenoportal.client.help;

import com.smartgwt.client.widgets.HTMLPane;
import com.google.gwt.core.client.GWT;

/**
 */
public class FaqHtmlPane extends HTMLPane {

    public FaqHtmlPane() {
        super();
        init();
    }

    /**
     * Load the help html page.
     */
    private void init() {
        String contextPath = GWT.getHostPageBaseURL();
        setContentsURL(contextPath + "data/pages/help/faq.htm");
    }
}
