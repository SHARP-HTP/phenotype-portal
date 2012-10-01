package edu.mayo.phenoportal.client.login;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class LoginPanel extends HLayout {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 450;
    protected static final String TAB_BACKGROUND_COLOR = "#f4f4fa";
    protected static final int WIDGET_WIDTH = 200;

    private TabSet i_tabSet;
    private Tab i_tabLogin;
    private Tab i_tabRequestNewPassword;
    private Tab i_tabRegister;

    public LoginPanel() {
        super();
        init();
    }

    private void init() {
        // setBackgroundColor("#dcdedf");

        setWidth100();
        setHeight100();
        setAlign(Alignment.CENTER);

        createTabArea();
    }

    private void createTabArea() {

        // layout to hold the tabset
        VLayout tabAreaLayout = new VLayout();
        tabAreaLayout.setAlign(Alignment.CENTER);
        tabAreaLayout.setWidth(WIDTH);
        tabAreaLayout.setHeight100();

        i_tabSet = new TabSet();
        i_tabSet.setTabBarPosition(Side.TOP);
        i_tabSet.setWidth(WIDTH);
        i_tabSet.setHeight(HEIGHT);

        i_tabLogin = new TabLogin();
        i_tabRequestNewPassword = new TabRequestNewPassword();
        i_tabRegister = new TabRegister();

        i_tabSet.addTab(i_tabLogin);
        i_tabSet.addTab(i_tabRegister);
        i_tabSet.addTab(i_tabRequestNewPassword);

        tabAreaLayout.addMember(i_tabSet);

        addMember(tabAreaLayout);

    }

    public void setTabRegister() {
        i_tabSet.selectTab(i_tabRegister);
    }

    public void setTabLogin() {
        i_tabSet.selectTab(i_tabLogin);
    }

}
