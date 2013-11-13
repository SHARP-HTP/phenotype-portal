package edu.mayo.phenoportal.client.phenotype.report;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.MouseDownEvent;
import com.smartgwt.client.widgets.events.MouseDownHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.events.MouseUpEvent;
import com.smartgwt.client.widgets.events.MouseUpHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

import edu.mayo.phenoportal.client.core.AlgorithmData;
import edu.mayo.phenoportal.client.upload.ClientUploadItems;
import edu.mayo.phenoportal.client.upload.FileService;
import edu.mayo.phenoportal.client.upload.FileServiceAsync;

/**
 * Tab for showing the file/upload info of a selected phenotype.
 */
public class FileInfoTab extends Tab implements ReportTab {

    private VLayout i_fileInfoPanel;
    private AlgorithmData i_algoAlgorithmData;

    private DynamicForm i_fileInfoForm = null;
    private DynamicForm i_downloadForm = null;
    private Label i_downloadLabel = null;

    public FileInfoTab(String title) {
        super(title);

        init();
    }

    private void init() {
        // panel to display the file information
        i_fileInfoPanel = new VLayout();
        i_fileInfoPanel.setWidth100();
        i_fileInfoPanel.setHeight100();
        i_fileInfoPanel.setMembersMargin(3);
        i_fileInfoPanel.setMargin(5);

        setPane(i_fileInfoPanel);
    }

    @Override
    public void clearTab() {
        if (i_fileInfoForm != null) {
            i_fileInfoPanel.removeMember(i_fileInfoForm);
        }
        if (i_downloadForm != null) {
            i_fileInfoPanel.removeMember(i_downloadForm);
        }
        if (i_downloadLabel != null) {
            i_fileInfoPanel.removeMember(i_downloadLabel);
        }
        i_fileInfoPanel.setContents("");
    }

    /**
     * Retrieve the new algorithm info from the DB and display it.
     * 
     * @param algorithmData
     */
    public void updateSelection(AlgorithmData algorithmData) {
        i_algoAlgorithmData = algorithmData;
        clearTab();
        getFileInfo();
    }

    private void getFileInfo() {

        FileServiceAsync uploadFileService = GWT.create(FileService.class);

        uploadFileService.retrieveUploadMetadata(i_algoAlgorithmData.getId(),
                new AsyncCallback<ClientUploadItems>() {

                    @Override
                    public void onSuccess(ClientUploadItems uploadItems) {

                        if (uploadItems.getInstitution() == null) {
                            uploadItems.setInstitution("");
                        }
                        if (uploadItems.getDescription() == null) {
                            uploadItems.setDescription("");
                        }
                        if (uploadItems.getComment() == null) {
                            uploadItems.setComment("");
                        }

                        i_downloadForm = new DynamicForm();
                        i_downloadForm.setTarget("uploadCallbackFrame");
                        i_downloadForm.setAction(GWT.getModuleBaseURL() + "algorithmupload");
                        i_downloadForm.setEncoding(Encoding.NORMAL);
                        i_downloadForm.setCanSubmit(true);

                        HiddenItem zipPath = new HiddenItem("ZipFilePath");
                        zipPath.setValue(uploadItems.getZipFile());

	                    HiddenItem fileName = new HiddenItem("FileName");
	                    String name = uploadItems.getName().replaceAll("\\s", "");
	                    fileName.setValue(name.substring(0, (Math.min(name.length(), 12))) + ".zip");

                        i_downloadLabel = createDownload();
                        i_fileInfoPanel.addMember(i_downloadLabel);

                        i_downloadForm.setFields(zipPath, fileName);

                        i_fileInfoForm = new DynamicForm();
                        i_fileInfoForm.setWidth(800);
                        i_fileInfoForm.setMargin(10);

                        StaticTextItem nameItem = createItem("File Name", uploadItems.getName());
                        StaticTextItem statusItem = createItem("Status", uploadItems.getStatus());
                        StaticTextItem versionItem = createItem("Version", uploadItems.getVersion());
                        StaticTextItem typeItem = createItem("Algorithm Type", uploadItems.getType().toString());
                        StaticTextItem creationDateItem = createItem("Creation Date",
                                uploadItems.getCreateDate());
                        StaticTextItem userItem = createItem("Uploader/Creator",
                                uploadItems.getUser());
                        StaticTextItem institutionItem = createItem("Institution",
                                uploadItems.getInstitution());
                        StaticTextItem descriptionItem = createItem("Description",
                                uploadItems.getDescription());
                        StaticTextItem commentsItem = createItem("Comments",
                                uploadItems.getComment());
                        StaticTextItem associatedNameItem = createItem("PheKB/NQF Name",
                                uploadItems.getAssocName());

                        final String link = "<a class=\"htpLinks\" href=\""
                                + uploadItems.getAssocLink() + "\" target=\"_blank\">"
                                + uploadItems.getAssocLink() + "</a>";

                        LinkItem associatedLinkItem = new LinkItem("assocLink");
                        associatedLinkItem.setTitle("PheKB/NQF Link");
                        associatedLinkItem.setLinkTitle(link);
                        associatedLinkItem.setWidth(600);
                        associatedLinkItem.addClickHandler(new ClickHandler() {

                            @Override
                            public void onClick(ClickEvent event) {
                                com.google.gwt.user.client.Window.open(link, "_blank", null);
                            }
                        });

                        i_fileInfoForm.setItems(nameItem, statusItem, versionItem, typeItem,
                                creationDateItem, userItem, institutionItem, descriptionItem,
                                commentsItem, associatedNameItem, associatedLinkItem);

                        i_fileInfoPanel.addMember(i_downloadForm);
                        i_fileInfoPanel.addMember(i_fileInfoForm);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        SC.warn(caught.getMessage());
                    }
                });
    }

    private StaticTextItem createItem(String title, String value) {

        StaticTextItem staticTextItem = new StaticTextItem();
        staticTextItem.setTitle(title);
        staticTextItem.setWrap(true);
        staticTextItem.setWrapTitle(false);
        staticTextItem.setDefaultValue(value);
        staticTextItem.setEscapeHTML(true);
        staticTextItem.setVAlign(VerticalAlignment.CENTER);

        return staticTextItem;

    }

    /**
     * create a download "button".
     * 
     * @return
     */
    private Label createDownload() {

        final Label label = new Label();
        label.setHeight(36);
        label.setWidth(210);
        label.setPadding(3);
        label.setAlign(Alignment.CENTER);
        label.setValign(VerticalAlignment.CENTER);
        label.setWrap(false);
        label.setIcon("download.png");
        label.setIconHeight(32);
        label.setIconWidth(32);
        label.setShowEdges(true);
        label.setEdgeSize(1);
        label.setEdgeBackgroundColor("#6056b7");
        label.setContents("Download algorithm");
        label.setBackgroundColor("#f5f4fa");

        label.addMouseOverHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent event) {
                label.setBackgroundColor("#d7d5ec");
                label.setContents("<i>Download</i> algorithm");
            }
        });

        label.addMouseOutHandler(new MouseOutHandler() {

            @Override
            public void onMouseOut(MouseOutEvent event) {
                label.setBackgroundColor("#f5f4fa");
                label.setContents("Download algorithm");
            }
        });

        label.addMouseDownHandler(new MouseDownHandler() {

            @Override
            public void onMouseDown(MouseDownEvent event) {
                label.setContents("<b><i>Download</i> algorithm<b>");
            }
        });

        label.addMouseUpHandler(new MouseUpHandler() {

            @Override
            public void onMouseUp(MouseUpEvent event) {
                label.setContents("<i>Download</i> algorithm");
            }
        });
        label.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                i_downloadForm.submit();
            }
        });

        return label;
    }
}
