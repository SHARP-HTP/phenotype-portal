package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.widgets.HTMLPane;

public class AdminInfoPanel extends HTMLPane {

    private static final String BACKGROUND_COLOR = "#f5f4fa";

    String i_contents = "<p style=\"margin:10px 5px\"><b>This is the Admin page where you can control users, categories, view user reports and update the news items.</b></p>";

    public AdminInfoPanel() {
        super();

        init();
    }

    private void init() {
        setWidth100();
        setHeight(140);
        setMargin(10);

        setBackgroundColor(BACKGROUND_COLOR);
    }

    public void setDescription(String description) {
        setContents(i_contents + "<p style=\"margin:10px 5px\">" + description + "</p>");
    }

}
