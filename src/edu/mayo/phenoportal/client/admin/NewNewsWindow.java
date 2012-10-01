package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.HLayout;

import edu.mayo.phenoportal.client.datasource.NewsXmlDS;
import edu.mayo.phenoportal.shared.database.NewsColumns;

/**
 * Window to add a new News item.
 */
public class NewNewsWindow extends Window {

    private DynamicForm i_form;

    public NewNewsWindow() {
        super();

        setWidth(500);
        setHeight(200);
        setTitle("Add News Item");
        setShowMinimizeButton(false);
        setIsModal(true);
        setShowModalMask(true);
        centerInPage();

        addCloseClickHandler(new CloseClickHandler() {

            @Override
            public void onCloseClick(CloseClickEvent event) {
                destroy();
            }
        });

        addFormContent();
    }

    private void addFormContent() {
        i_form = new DynamicForm();
        i_form.setNumCols(2);
        i_form.setDataSource(NewsXmlDS.getInstance());

        // set this value so we know that the ADD is from the user (in the DS);
        i_form.setValue(NewsXmlDS.ATTR_FROM_USER, NewsXmlDS.ATTR_FROM_USER);

        DateItem dateItem = new DateItem(NewsColumns.DATE.colName(), "Date");
        TextAreaItem infoItem = new TextAreaItem(NewsColumns.INFO.colName(), "Information");
        infoItem.setWidth(400);
        infoItem.setHeight(100);

        i_form.setItems(dateItem, infoItem);

        addItem(i_form);
        addButtons();
    }

    private void addButtons() {
        HLayout buttonLayout = new HLayout();
        buttonLayout.setWidth100();
        buttonLayout.setHeight(30);

        buttonLayout.setMargin(10);
        buttonLayout.setMembersMargin(10);

        buttonLayout.setAlign(Alignment.RIGHT);

        IButton addButton = new IButton("Add News");
        addButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                i_form.saveData(new DSCallback() {

                    @Override
                    public void execute(DSResponse response, Object rawData, DSRequest request) {
                        // destroy the window after the add is completed.
                        destroy();
                    }
                });

            }
        });

        IButton cancelButton = new IButton("Cancel");
        cancelButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                destroy();
            }
        });

        buttonLayout.addMember(addButton);
        buttonLayout.addMember(cancelButton);

        addItem(buttonLayout);
    }

}
