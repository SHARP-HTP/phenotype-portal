package edu.mayo.phenoportal.client.phenotype.report;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

import edu.mayo.phenoportal.client.core.AlgorithmData;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;

public class SupplementalDataListGrid extends ListGrid {

    private AlgorithmData i_algorithmData;

    public SupplementalDataListGrid() {
        super();

        setWidth100();
        setMargin(2);

        setShowAllRecords(true);
        setAutoFitData(Autofit.BOTH);

        setShowRecordComponents(true);
        setShowRecordComponentsByCell(true);
        setShowAllRecords(true);

        ListGridField iconField = new ListGridField("iconField", "Edit", 40);
        ListGridField oidField = new ListGridField("oid", "OID", 280);
        ListGridField descriptionField = new ListGridField("description", "Description");

        setFields(iconField, descriptionField, oidField);
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
        } else {
            return null;
        }

    }

    public void update(AlgorithmData algorithmData) {
        i_algorithmData = algorithmData;

        setData(new ListGridRecord[0]);

        PhenotypeServiceAsync async = (PhenotypeServiceAsync) GWT.create(PhenotypeService.class);

        async.getSupplementalCriteriaOids(i_algorithmData,
                new AsyncCallback<Map<String, String>>() {
                    @Override
                    public void onSuccess(Map<String, String> result) {
                        setGridData(result);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        GWT.log("Error getting supplemental criteria: " + caught);
                    }
                });
    }

    public void setGridData(Map<String, String> oids) {

        int length = oids.size();
        DataCriteriaRecord[] records = new DataCriteriaRecord[length];

        int i = 0;
        for (String key : oids.keySet()) {
            records[i++] = new DataCriteriaRecord(key, oids.get(key), "");
        }

        setData(records);
        redraw();
    }

}
