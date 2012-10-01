package edu.mayo.phenoportal.client.news;

import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Panel to display news in the main panel.
 */
public class NewsPanel extends VLayout {

    private NewsSectionStack i_newsSectionStack;

    public NewsPanel() {
        super();
        init();
    }

    private void init() {
        setWidth("35%");
        setHeight100();

        i_newsSectionStack = new NewsSectionStack();
        addMember(i_newsSectionStack);
    }

    public void refreshNews() {
        i_newsSectionStack.refreshData();
    }
}
