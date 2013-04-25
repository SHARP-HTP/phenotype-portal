package edu.mayo.phenoportal.client.upload;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.mayo.phenoportal.client.Htp;
import edu.mayo.phenoportal.client.core.AlgorithmData;
import edu.mayo.phenoportal.client.events.UploadCanceledEvent;
import edu.mayo.phenoportal.client.events.UploadCompletedEvent;
import edu.mayo.phenoportal.client.events.UploadStartedEvent;
import edu.mayo.phenoportal.shared.database.UploadColumns;

import java.util.Date;

/**
 * This panel displays the widgets shown in the display when uploading a file.
 */
public class UploadPanel extends VLayout {

    private static final int WIDTH = 550;
    private static final int HEIGHT = 450;

    private static final String BACKGROUND_COLOR = "#f4f4fa";
    private static final int WIDGET_WIDTH = 350;
    private static final int WIDGET_HEIGHT = 50;
    private static final String TITLE = "<p style=\"color: #51524e;font-family: Arial,Helvetica,sans-serif;font-size: 18px;font-weight:bold;text-decoration:none\"><nobr> Upload Phenotype</nobr> </p>";
    private static final String CATEGORY_NAME = "Category_Name";
    private static final String CATEGORY_TITLE = "Category";

    private VLayout i_formLayout;
    private static DynamicForm i_form;
    private HiddenItem i_categoryId;
	private TextItem nameText;
	private TextItem versionText;
	private TextItem institutionText;
	private TextAreaItem descriptionText;
	private TextItem assocNameText;
	private TextItem assocLinkText;
	private UploadItem zipFileItem;
	private TextItem zipTextItem;
	private HiddenItem zipPath;

    public UploadPanel() {
        super();
        init();
    }

	public void setNameText(String nameText) {
		this.nameText.setValue(nameText);
	}

	public void setVersionText(String versionText) {
		this.versionText.setValue(versionText);
	}

	public void setInstitutionText(String institutionText) {
		this.institutionText.setValue(institutionText);
	}

	public void setDescriptionText(String descriptionText) {
		this.descriptionText.setValue(descriptionText);
	}

	public void setAssocNameText(String assocNameText) {
		this.assocNameText.setValue(assocNameText);
	}

	public void setAssocLinkText(String assocLinkText) {
		this.assocLinkText.setValue(assocLinkText);
	}

	public void setNqfFilePath(String path) {
		if (path != null && !path.isEmpty()) {
			zipPath.setValue(path);
			zipTextItem.enable();
			zipTextItem.show();

			zipFileItem.disable();
			zipFileItem.hide();
		}
		else {
			zipPath.setValue("");
			zipTextItem.hide();
			zipTextItem.disable();

			zipFileItem.enable();
			zipFileItem.show();
		}
	}

	public void clearTextFields() {
		this.nameText.clearValue();
		this.versionText.clearValue();
		this.institutionText.clearValue();
		this.descriptionText.clearValue();
		this.assocNameText.clearValue();
		this.assocLinkText.clearValue();
	}

    /**
     * After the upload is completed, display success/failure message.
     * 
     * @param success
     * @param message
     */
    public static void uploadStatus(boolean success, String message) {

        AlgorithmData algorithmData = new AlgorithmData();

        if (success) {
            SC.say("Your file has been successfully uploaded.<br/><br/>" + message);
            resetForm();
            algorithmData.setAlgorithmName(i_form.getValueAsString(UploadColumns.NAME.colName()));

        } else {
            SC.warn("Your file could not be uploaded.<br/><br/><b>Cause:</b><br/>" + message);
        }

        Htp.EVENT_BUS.fireEvent(new UploadCompletedEvent(algorithmData));
    }

    private void init() {
        setWidth(WIDTH);
        setHeight100();
        setAlign(Alignment.CENTER);

        Label titleLabel = new Label();
        titleLabel.setContents(TITLE);

        // header layout
        HLayout titleLayout = new HLayout();
        titleLayout.setWidth(WIDTH);
        titleLayout.setHeight(10);
        titleLayout.setAlign(Alignment.CENTER);
        titleLayout.addMember(titleLabel);

        // layout to hold the form
        i_formLayout = new VLayout();
        i_formLayout.setBackgroundColor(BACKGROUND_COLOR);
        i_formLayout.setAlign(Alignment.CENTER);
        i_formLayout.setWidth(WIDTH);
        i_formLayout.setHeight(HEIGHT);
        i_formLayout.setBorder("2px solid gray");

        i_formLayout.addMember(titleLayout);

        getUploadFormDetails();

        addMember(i_formLayout);

        exportNativeMethods();

	    zipTextItem.disable();
		zipTextItem.hide();
    }

    private static native void exportNativeMethods() /*-{
        $wnd.uploadStatus = $entry(@edu.mayo.phenoportal.client.upload.UploadPanel::uploadStatus(ZLjava/lang/String;));
    }-*/;

    private void getUploadFormDetails() {
        i_form = new DynamicForm();
        i_form.setTarget("uploadCallbackFrame");
        i_form.setAction(GWT.getModuleBaseURL() + "algorithmupload");
        i_form.setEncoding(Encoding.MULTIPART);
        i_form.setCanSubmit(true);
        i_form.setMargin(10);
        i_form.setCellPadding(5);

        nameText = createTextItem(UploadColumns.NAME.colName(),
                "<nobr>What is the name for this algortihm file?</nobr>", WIDGET_WIDTH,
                UploadColumns.NAME.normName(), true);

        versionText = createTextItem(UploadColumns.VERSION.colName(),
                "<nobr>Version Number Ex.(2.5)</nobr>", WIDGET_WIDTH,
                UploadColumns.VERSION.normName(), true);
        versionText.setKeyPressFilter("[0-9.]");

        SelectItem selectItem = new SelectItem(UploadColumns.STATUS.colName());
        selectItem.setWidth(WIDGET_WIDTH);
        selectItem.setWrapTitle(false);
        selectItem.setTitle(UploadColumns.STATUS.normName());
        selectItem.setPrompt("<nobr>What is the status of this version?</nobr>");
        selectItem.setType("comboBox");
        selectItem.setRequired(true);
        selectItem.setValueMap("Final", "Testing", "Under Development");

        institutionText = createTextItem(UploadColumns.INSTITUTION.colName(),
                "<nobr>Where do you work?</nobr>", WIDGET_WIDTH,
                UploadColumns.INSTITUTION.normName(), false);

        DateItem createDate = new DateItem(UploadColumns.CREATEDATE.colName());
        createDate.setWidth(WIDGET_WIDTH);
        createDate.setWrapTitle(false);
        createDate.setTitle(UploadColumns.CREATEDATE.normName());
        createDate.setPrompt("<nobr>When did you create this algorithm?</nobr>");

        descriptionText = new TextAreaItem(UploadColumns.DESCRIPTION.colName());
        descriptionText.setWidth(WIDGET_WIDTH);
        descriptionText.setWrapTitle(false);
        descriptionText.setHeight(WIDGET_HEIGHT);
        descriptionText.setTitle(UploadColumns.DESCRIPTION.normName());
        descriptionText.setPrompt("<nobr>What is this algorithm about?</nobr>");
        descriptionText.setRequired(true);

        TextAreaItem commentsText = new TextAreaItem(UploadColumns.COMMENT.colName());
        commentsText.setWidth(WIDGET_WIDTH);
        commentsText.setWrapTitle(false);
        commentsText.setHeight(WIDGET_HEIGHT);
        commentsText.setTitle(UploadColumns.COMMENT.normName());
        commentsText.setPrompt("<nobr>Any additional comments?</nobr>");

        zipFileItem = new UploadItem(UploadColumns.ZIP_FILE.colName());
        zipFileItem.setWidth(WIDGET_WIDTH);
        zipFileItem.setWrapTitle(false);
        zipFileItem.setTitle(UploadColumns.ZIP_FILE.normName());
        zipFileItem.setRequired(true);
        zipFileItem.setPrompt("<nobr>Path to the zip file.</nobr>");

	    zipTextItem = new TextItem("ZIP_TEXT");
	    zipTextItem.setWidth(WIDGET_WIDTH);
	    zipTextItem.setWrapTitle(false);
	    zipTextItem.setTitle((UploadColumns.ZIP_FILE.normName()));
	    zipTextItem.setEmptyDisplayValue("Import From MAT");
	    zipTextItem.setCanEdit(false);

	    zipPath = new HiddenItem("ZIP_PATH");

        UploadItem docFileItem = new UploadItem(UploadColumns.WORD_FILE.colName());
        docFileItem.setWidth(WIDGET_WIDTH);
        docFileItem.setWrapTitle(false);
        docFileItem.setTitle(UploadColumns.WORD_FILE.normName());
        docFileItem.setPrompt("<nobr>Path to the Word file.</nobr>");

        assocNameText = createTextItem(UploadColumns.ASSOC_NAME.colName(),
                "<nobr>Is there a name for this algorithm (e.g. PheKB, NQF, etc.)?</nobr>",
                WIDGET_WIDTH, "PheKB/NQF Name", false);

        assocLinkText = createTextItem(
                UploadColumns.ASSOC_LINK.colName(),
                "<nobr>Is there an associated link for this algorithm (e.g. PheKB, NQF, etc.)</nobr>",
                WIDGET_WIDTH, "PheKB/NQF Link", false);

        FormItemIcon categoryIcon = new FormItemIcon();
        categoryIcon.setSrc("folder_opened.png");
        categoryIcon.setPrompt("Select the category");

        // HiddenItem to store the id/int value of the category
        i_categoryId = new HiddenItem(UploadColumns.ID.colName());

        // This TextItem is just used to display the category
        final TextItem categoryText = createTextItem(CATEGORY_NAME,
                "<nobr>Click on the folder icon to select a category for this algorithm.</nobr>",
                WIDGET_WIDTH, CATEGORY_TITLE, true);

        // Callback to get the category tree selection from the popup window
        final Callback<AlgorithmData, String> selectionCallback = new Callback<AlgorithmData, String>() {

            @Override
            public void onFailure(String reason) {
                System.out.println("onError");
            }

            @Override
            public void onSuccess(AlgorithmData selectedCategoryData) {
                categoryText.setValue(selectedCategoryData.getAlgorithmName());
                i_categoryId.setValue(Integer.parseInt(selectedCategoryData.getCategoryId()));
            }
        };

        // Show the Category selection tree window
        categoryIcon.addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                CategorySelectionWindow window = new CategorySelectionWindow(selectionCallback);
                window.show();
            }
        });

        categoryText.setIcons(categoryIcon);

        final HiddenItem userItem = new HiddenItem(UploadColumns.USER.colName());

        i_form.setFields(nameText, versionText, selectItem, institutionText, createDate,
                descriptionText, commentsText, zipFileItem, zipTextItem, zipPath, docFileItem, assocLinkText,
                assocNameText, categoryText, userItem, i_categoryId);

        ButtonItem uploadButton = new ButtonItem("Upload");
        uploadButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (i_form.validate()) {

                    if (Htp.getLoggedInUser() != null) {
                        userItem.setValue(Htp.getLoggedInUser().getUserName());
                    }

                    Htp.EVENT_BUS.fireEvent(new UploadStartedEvent());
                    i_form.submit();
                }
            }
        });

        ButtonItem cancelButton = new ButtonItem("Cancel");
        cancelButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Htp.EVENT_BUS.fireEvent(new UploadCanceledEvent());
                resetForm();
            }
        });

        DynamicForm uploadButtonForm = new DynamicForm();
        uploadButtonForm.setFields(uploadButton);

        DynamicForm cancelButtonForm = new DynamicForm();
        cancelButtonForm.setFields(cancelButton);

        // button panel to stretch the whole width
        HLayout buttonContainer = new HLayout();
        buttonContainer.setWidth100();
        buttonContainer.setAlign(Alignment.CENTER);

        // button panel that holds the buttons together.
        HLayout buttonLayout = new HLayout(20);
        buttonLayout.setWidth(150);
        buttonLayout.setHeight(30);
        buttonLayout.setAlign(Alignment.CENTER);

        buttonLayout.addMember(uploadButtonForm);
        buttonLayout.addMember(cancelButtonForm);

        buttonContainer.addMember(buttonLayout);

        i_formLayout.addMember(i_form);
        i_formLayout.addMember(buttonContainer);
        draw();
    }

    /**
     * Convenience method to create a TextItem.
     * 
     * @param name
     * @param prompt
     * @param width
     * @param title
     * @param required
     * @return
     */
    private TextItem createTextItem(String name, String prompt, int width, String title,
            boolean required) {
        TextItem item = new TextItem(name);
        item.setWrapTitle(false);
        item.setPrompt(prompt);
        item.setWidth(width);
        item.setTitle(title);
        item.setRequired(required);

        return item;
    }

    private String formatDate(String inDate) {
        String outDate = new String();

        Date date = new Date(inDate);
        // outDate = DateUtil.formatAsShortDate(date);
        DateTimeFormat fmt = DateTimeFormat.getFormat("EEEE, MMMM dd, yyyy");
        outDate = fmt.format(date);

        return outDate;
    }

    private static void resetForm() {
        i_form.setValue(UploadColumns.NAME.colName(), "");
        i_form.setValue(UploadColumns.VERSION.colName(), "");
        i_form.setValue(UploadColumns.STATUS.colName(), "");
        i_form.setValue(UploadColumns.INSTITUTION.colName(), "");
        i_form.setValue(UploadColumns.CREATEDATE.colName(), "");
        i_form.setValue(UploadColumns.DESCRIPTION.colName(), "");
        i_form.setValue(UploadColumns.COMMENT.colName(), "");
        i_form.setValue(UploadColumns.ZIP_FILE.colName(), "");
        i_form.setValue(UploadColumns.WORD_FILE.colName(), "");
        i_form.setValue(UploadColumns.ASSOC_LINK.colName(), "");
        i_form.setValue(UploadColumns.ASSOC_NAME.colName(), "");
        i_form.setValue(UploadColumns.ID.colName(), "");

        // hidden form items
        i_form.setValue(UploadColumns.USER.colName(), "");
        i_form.setValue(CATEGORY_NAME, "");

    }

}