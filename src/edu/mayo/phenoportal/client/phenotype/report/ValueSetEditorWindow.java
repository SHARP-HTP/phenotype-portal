package edu.mayo.phenoportal.client.phenotype.report;

import java.util.ArrayList;

import mayo.edu.cts2.editor.client.Cts2Editor;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * Window for displaying the value set editor for a selected oid.
 */
public class ValueSetEditorWindow extends Window {

    protected Button i_okButton;
    protected Button i_cancelButton;
    private final String i_oid;

    public ValueSetEditorWindow(String oid) {
        super();

        i_oid = oid;

        setTitle("Value Set Editor");
        setWidth("95%");
        setHeight("95%");

        setModalMaskOpacity(90);

        setCanDragReposition(false);
        setCanDragResize(true);
        setAnimateMinimize(true);

        addValueSetInfo();
        addItem(getButton());
    }

    private void addValueSetInfo() {

        ArrayList<String> oidList = new ArrayList<String>();
        oidList.add(i_oid);

        Cts2Editor editor = new Cts2Editor();
        addItem(editor.getMainLayout(oidList));
    }

    private HLayout getButton() {

        // Buttons on the bottom
        HLayout buttonLayout = new HLayout();
        buttonLayout.setWidth100();
        buttonLayout.setHeight(40);
        buttonLayout.setLayoutMargin(6);
        buttonLayout.setMembersMargin(10);
        buttonLayout.setAlign(Alignment.CENTER);

        i_cancelButton = new Button("Close");
        i_cancelButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // close the window
                destroy();
            }
        });

        buttonLayout.addMember(i_cancelButton);

        return buttonLayout;
    }

}
