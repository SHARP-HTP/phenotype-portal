package edu.mayo.phenoportal.client.phenotype.report;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;
import edu.mayo.phenoportal.shared.Image;

/**
 * Tab for displaying the Criteria info for the selected phenotype.
 */
public class WorkflowTab extends Tab implements ReportTab {

    private final String serverUrl = "http://localhost:8080";
    private final String editorUrl = serverUrl + "/designer/editor?profile=jbpm&uuid=";
    private Canvas imagePanel;
    private Layout mainPanel;
    private Layout editorPanel;
    private Window editorWindow;
    private Img wfImage;

    public WorkflowTab(String title) {
        super(title);
        exportNativeMethods();
        createMainPanel();
        createEditorPanel();
    }

    private void createMainPanel() {
        wfImage = new Img();

        imagePanel = new Canvas();
        imagePanel.setWidth100();
        imagePanel.setHeight100();

        imagePanel.addChild(wfImage);

        final WorkflowTab tab = this;
        IButton editButton = new IButton("Edit");
        editButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                /* TODO: insert gunvor id into url */
	            String url = editorUrl;
                editorWindow.setSrc(url);
                tab.setPane(editorPanel);
            }
        });
	    /* TODO: re-enable the edit functionality */
	    editButton.disable();
	    editButton.hide();

	    mainPanel = new VLayout();
        mainPanel.addMember(editButton);
	    mainPanel.addMember(imagePanel);

        setPane(mainPanel);
    }

    private void createEditorPanel() {
        editorPanel = new VLayout();
        editorWindow = new Window();
        editorWindow.setWidth100();
        editorWindow.setHeight100();
        editorWindow.setShowHeader(false);

        IButton saveButton = new IButton("Save");
        saveButton.addClickHandler(new ClickHandler() {
            @Override
            public native void onClick(ClickEvent event) /*-{
				var oryx = window.parent.frames[3].window.ORYX;
				var editor = oryx.EDITOR;
				var ajax = window.parent.frames[3].window.Ext.Ajax;
				var processJSON = editor.getSerializedJSON();

				ajax.request({
					url : oryx.PATH + "uuidRepository",
					method : 'POST',
					success : function(request) {
						parent.handleJbpmSave(editor.modelMetaData.id, request.responseText);
					},
					failure : function(request) {
						parent.jbpmSaveError(request.responseText);
					},
					params : {
						action : "toXML",
						pp : oryx.PREPROCESSING,
						profile : oryx.PROFILE,
						data : processJSON
					}
				});
    		}-*/;
        });

        IButton closeButton = new IButton("Close");
        final WorkflowTab tab = this;
        closeButton.addClickHandler(new ClickHandler() {
	        @Override
	        public void onClick(ClickEvent event) {
		        tab.setPane(mainPanel);
	        }
        });

        HLayout buttonPanel = new HLayout();
        buttonPanel.addMember(saveButton);
        buttonPanel.addMember(closeButton);

        editorPanel.addMember(editorWindow);
        editorPanel.addMember(buttonPanel);
    }

    public void insertImage(Image image) {
        clearTab();

        if (image != null && image.getImagePath() != null) {
            wfImage = new Img();
            wfImage.setImageType(ImageStyle.NORMAL);
	        wfImage.setSrc(URL.encode(GWT.getModuleBaseURL() + "images?id=" + image.getImagePath()));
            wfImage.setDefaultWidth(image.getWidth());
            wfImage.setDefaultHeight(image.getHeight());
            imagePanel.setContents("");

            imagePanel.addChild(wfImage);
        } else {
            imagePanel.setContents("<b>There is no workflow image for this algorithm</b>");
        }
        imagePanel.markForRedraw();
    }

    @Override
    public void clearTab() {

        if (wfImage != null) {
            imagePanel.removeChild(wfImage);
            wfImage = null;
        }
    }

    public static void handleJbpmSave(final String uuid, final String bpmn) {
	    final Window saveAsWindow = new Window();
	    saveAsWindow.setWidth(360);
	    saveAsWindow.setHeight(256);
	    saveAsWindow.setTitle("Save As");
	    saveAsWindow.setShowMinimizeButton(false);
	    saveAsWindow.setIsModal(true);
	    saveAsWindow.setShowModalMask(true);
	    saveAsWindow.centerInPage();
	    saveAsWindow.addCloseClickHandler(new CloseClickHandler() {
		    public void onCloseClick(CloseClickEvent event) {
			    saveAsWindow.destroy();
		    }
	    });

	    DynamicForm saveAsForm = new DynamicForm();
	    saveAsForm.setTitle("Save As");

	    final TextItem titleText = new TextItem("Title");
	    titleText.setWidth("*");

	    final TextItem commentText = new TextItem("Comment");
	    commentText.setWidth("*");
	    commentText.setHeight("*");

	    saveAsForm.setFields(titleText, commentText);

	    VLayout mainPanel = new VLayout();
	    HLayout buttonsPanel = new HLayout();

	    IButton saveButton = new IButton("Save");
	    saveButton.addClickHandler(new ClickHandler() {
		    @Override
		    public void onClick(ClickEvent clickEvent) {
			    if (save(uuid, bpmn, (String) titleText.getValue(), (String) commentText.getValue())) {
				    saveAsWindow.destroy();
                    SC.say("Your workflow has been saved.");
			    }
			    else {
				    SC.warn("An error occurred while saving the workflow.");
			    }
		    }
	    });

	    IButton cancelButton = new IButton("Cancel");
	    cancelButton.addClickHandler(new ClickHandler() {
		    @Override
		    public void onClick(ClickEvent clickEvent) {
			    saveAsWindow.destroy();
		    }
	    });

	    buttonsPanel.addMember(saveButton);
	    buttonsPanel.addMember(cancelButton);
	    mainPanel.addMember(saveAsForm);
	    mainPanel.addMember(buttonsPanel);
	    saveAsWindow.addItem(mainPanel);

	    saveAsWindow.show();
    }

	private static boolean save(String uuid, String bpmn, String title, String comment) {
		boolean success = true;
		PhenotypeServiceAsync phenotypeService = GWT.create(PhenotypeService.class);
        /* TODO: THe following needs to return a boolean! */
		phenotypeService.saveJbpm(uuid, bpmn, title, comment, null);

		return success;
	}

	public static void jbpmSaveError(String error) {
        SC.warn("An Error occurred while saving the workflow.\n\nError: " + error);
    }

    private native static void exportNativeMethods() /*-{
		$wnd.handleJbpmSave = 
			$entry(@edu.mayo.phenoportal.client.phenotype.report.WorkflowTab::handleJbpmSave(Ljava/lang/String;Ljava/lang/String;));
		$wnd.jbpmSaveError = 
            $entry(@edu.mayo.phenoportal.client.phenotype.report.WorkflowTab::jbpmSaveError(Ljava/lang/String;));
    }-*/;

}
