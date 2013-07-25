package edu.mayo.phenoportal.client.phenotype.report;

import java.util.Map;

import mayo.edu.cts2.editor.client.Cts2Editor;
import mayo.edu.cts2.editor.client.events.UpdateValueSetVersionEvent;
import mayo.edu.cts2.editor.client.events.UpdateValueSetVersionEventHandler;
import mayo.edu.cts2.editor.client.widgets.BaseValueSetsListGrid;
import mayo.edu.cts2.editor.client.widgets.versions.VersionWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

import edu.mayo.phenoportal.client.core.AlgorithmData;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;

public class DataCriteriaListGrid extends ListGrid {

    private static final String INITIAL_VERSION = "1 (Initial Version)";

    private AlgorithmData i_algorithmData;

    public DataCriteriaListGrid() {
        super();

        setWidth100();
        setMargin(2);

        setShowAllRecords(true);
        setAutoFitData(Autofit.BOTH);

        setShowRecordComponents(true);
        setShowRecordComponentsByCell(true);
        setShowAllRecords(true);
        setHoverWidth(400);

        ListGridField iconField = new ListGridField("iconField", "Edit", 40);
        ListGridField oidField = new ListGridField("oid", "OID", 280);
        ListGridField descriptionField = new ListGridField("description", "Description");
        descriptionField.setShowHover(true);

        descriptionField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                String description = record.getAttribute("description");
                description = description + " (OID: " + record.getAttribute("oid") + ")";
                return description;
            }
        });

        ListGridField versionField = new ListGridField("version", "Change Version", 150);

        setFields(iconField, descriptionField, /* oidField, */versionField);

        createUpdateVersionEvent();
    }

    @Override
    protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {

        String fieldName = this.getFieldName(colNum);

        if (fieldName.equals("iconField")) {
            HLayout recordCanvas = new HLayout(1);
            recordCanvas.setHeight(22);
            recordCanvas.setWidth(20);
            recordCanvas.setAlign(Alignment.CENTER);
            ImgButton editImg = new ImgButton();
            editImg.setShowDown(false);
            editImg.setShowRollOver(false);
            editImg.setLayoutAlign(Alignment.CENTER);
            editImg.setSrc("edit.png");
            editImg.setPrompt("Edit Value Set");
            editImg.setHeight(16);
            editImg.setWidth(16);
            editImg.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    ValueSetEditorWindow vsWindow = new ValueSetEditorWindow(record
                            .getAttribute("oid"));
                    vsWindow.centerInPage();
                    vsWindow.show();
                }
            });

            recordCanvas.addMember(editImg);
            return recordCanvas;
        } else if (fieldName.equals("version")) {
            HLayout recordCanvas = new HLayout(1);
            recordCanvas.setHeight(22);
            recordCanvas.setWidth100();
            recordCanvas.setAlign(Alignment.RIGHT);
            ImgButton versionImg = new ImgButton();
            versionImg.setShowDown(false);
            versionImg.setShowRollOver(false);
            versionImg.setLayoutAlign(Alignment.RIGHT);
            versionImg.setSrc("version.png");
            versionImg.setPrompt("Change Version");
            versionImg.setHeight(16);
            versionImg.setWidth(16);

            versionImg.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {

                    // ********************************************
                    // ** Open up the version window from the Value
                    // ** Set Editor to select the version.
                    // ********************************************

                    String userName = Cts2Editor.getUserName();
                    String valueSetId = record
                            .getAttribute(BaseValueSetsListGrid.ID_VALUE_SET_NAME);

                    Criteria criteria = new Criteria();
                    criteria.setAttribute(BaseValueSetsListGrid.ID_VALUE_SET_NAME, "Value Set Name");
                    criteria.setAttribute(BaseValueSetsListGrid.ID_FORMAL_NAME, "Formal Name");
                    criteria.setAttribute(BaseValueSetsListGrid.ID_URI, "URI");
                    criteria.setAttribute(BaseValueSetsListGrid.ID_COMMENT, "Comment");
                    criteria.setAttribute("userName", userName);

                    VersionWindow versionWindow = new VersionWindow(criteria);
                    versionWindow.show();
                }
            });

            recordCanvas.addMember(versionImg);
            return recordCanvas;
        }

        else {
            return null;
        }

    }

    public void update(AlgorithmData algorithmData) {
        i_algorithmData = algorithmData;

        setData(new ListGridRecord[0]);

        PhenotypeServiceAsync async = (PhenotypeServiceAsync) GWT.create(PhenotypeService.class);
        async.getDataCriteriaOids(i_algorithmData, new AsyncCallback<Map<String, String>>() {
            @Override
            public void onSuccess(Map<String, String> result) {
                setGridData(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Error getting data criteria: " + caught);
            }
        });
    }

    public void setGridData(Map<String, String> oids) {

        int length = oids.size();
        DataCriteriaRecord[] records = new DataCriteriaRecord[length];

        int i = 0;
        for (String key : oids.keySet()) {
            records[i++] = new DataCriteriaRecord(key, oids.get(key), INITIAL_VERSION);
        }

        setData(records);
        redraw();
    }

    /**
     * Listen for when a value set version has been updated.
     */

    private void createUpdateVersionEvent() {

        // ********************************************
        // ** Add VersionWindow Listener from the Value
        // ** Set Editor listen for version changes.
        // **************************************************

        Cts2Editor.EVENT_BUS.addHandler(UpdateValueSetVersionEvent.TYPE,
                new UpdateValueSetVersionEventHandler() {

                    @Override
                    public void onValueSetVersionUpdated(UpdateValueSetVersionEvent event) {
                        String comment = event.getComment();
                        String valueSetId = event.getValueSetId();
                        String versionId = event.getVersionId();
                        String changeSetId = event.getChangeSetUri();
                        String documentUri = event.getDocumentUri();

                        // find the record to update and update it.
                        // ListGridRecord recordToUpdate =
                        // findRecord(valueSetId);
                        //
                        // if (recordToUpdate != null) {
                        // i_valueSetsListGrid.updateRecord(recordToUpdate,
                        // valueSetId, versionId, comment, changeSetId,
                        // documentUri);
                        // }
                    }
                });
    }

}
