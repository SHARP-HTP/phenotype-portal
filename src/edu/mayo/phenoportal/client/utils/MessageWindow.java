package edu.mayo.phenoportal.client.utils;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Window used to display messages
 */
public class MessageWindow extends Window {
    private static final String BACKGROUND_COLOR = "#b0abdb";
    protected TextItem i_searchItem;
    protected ButtonItem i_searchButton;

    protected Label i_label;
    protected Button i_okButton;
    protected Button i_launchButton;
    protected Button i_cancelButton;

    public MessageWindow(String title, String message) {
        super();

        VLayout layout = new VLayout(5);

        setWidth(550);
        setHeight(400);
        setMargin(20);

        setTitle(title);
        setShowMinimizeButton(false);
        setIsModal(true);
        setShowModalMask(true);
        setCanDragResize(true);
        centerInPage();

        addCloseClickHandler(new CloseClickHandler() {

            @Override
            public void onCloseClick(CloseClickEvent event) {
                destroy();
            }
        });

        layout.addMember(createDisplayLabel());
        layout.addMember(createMessagePanel(message));
        layout.addMember(getButtons());

        addItem(layout);
        show();
    }

    private HLayout createMessagePanel(String message) {

        Label messageLabel = new Label(message);
        messageLabel.setWidth100();
        messageLabel.setHeight100();

        HLayout messagePanel = new HLayout();
        messagePanel.setWidth100();
        messagePanel.setHeight100();
        messagePanel.setLayoutMargin(10);
        messagePanel.setLayoutMargin(6);
        messagePanel.setMembersMargin(6);
        messagePanel.addMember(messageLabel);

        return messagePanel;
    }

    private VLayout createDisplayLabel() {
        i_label = new Label("<b>HTP Information Message<b>");
        i_label.setWidth100();
        i_label.setHeight(30);
        i_label.setMargin(2);
        i_label.setValign(VerticalAlignment.CENTER);
        i_label.setBackgroundColor(BACKGROUND_COLOR);

        final VLayout vLayoutLayoutSpacers = new VLayout();
        vLayoutLayoutSpacers.setWidth100();
        vLayoutLayoutSpacers.setHeight(30);
        vLayoutLayoutSpacers.setBackgroundColor(BACKGROUND_COLOR);
        vLayoutLayoutSpacers.setLayoutMargin(6);
        vLayoutLayoutSpacers.setMembersMargin(6);
        // vLayoutLayoutSpacers.setBorder("1px dashed red");

        vLayoutLayoutSpacers.addMember(i_label);

        return vLayoutLayoutSpacers;
    }

    private HLayout getButtons() {

        // Buttons on the bottom
        HLayout buttonLayout = new HLayout();
        buttonLayout.setWidth100();
        buttonLayout.setHeight(40);
        buttonLayout.setLayoutMargin(6);
        buttonLayout.setMembersMargin(10);
        buttonLayout.setAlign(Alignment.CENTER);

        i_okButton = new Button("Ok");
        i_okButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // close the window
                destroy();
            }
        });

        i_cancelButton = new Button("Close");
        i_cancelButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // close the window
                destroy();
            }
        });

        buttonLayout.addMember(i_okButton);
        // buttonLayout.addMember(i_cancelButton);

        return buttonLayout;
    }

    public Button getCloseButton() {
        return i_okButton;
    }

}
