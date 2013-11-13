package edu.mayo.phenoportal.client.help;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class UserManual extends VLayout {

    public UserManual() {
        super();
        init();
    }

    private void init() {
        setWidth100();
        setHeight100();

        getManualDetails();
    }

    private void getManualDetails() {

        final TabSet tabSet = new TabSet();
        tabSet.setTabBarPosition(Side.TOP);
        tabSet.setTabBarAlign(Side.LEFT);
        tabSet.setWidth100();
        tabSet.setHeight100();

        Tab tabGuide = new Tab("Guide");
        tabGuide.setPane(new GuideHtmlPane());

        Tab tabFaq = new Tab("FAQ");
        //tabFaq.setPane(new FaqListGrid());
        tabFaq.setPane(new FaqHtmlPane());

        Tab tabContact = new Tab("Contact");
        Canvas tabPane3 = new Canvas();
        tabPane3.addChild(getContacts());
        tabContact.setPane(tabPane3);

        tabSet.addTab(tabGuide);
        tabSet.addTab(tabFaq);
        tabSet.addTab(tabContact);

        addMember(tabSet);
    }

    private Canvas getContacts() {
        HTMLPane contactPane = new HTMLPane();
        contactPane.setWidth100();
        contactPane.setHeight100();

        String contextPath = GWT.getHostPageBaseURL();
        contactPane.setContentsURL(contextPath + "data/pages/help/contactUs.html");

        return contactPane;

    }
}
