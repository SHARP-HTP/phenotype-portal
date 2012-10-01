package edu.mayo.phenoportal.client.news;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

import edu.mayo.phenoportal.client.datasource.UploadedAlgorithmXmlDS;
import edu.mayo.phenoportal.shared.database.UploadColumns;

public class UploadedAlgorithmListGrid extends ListGrid {
    private final UploadedAlgorithmXmlDS i_uploadedAlgorithmXmlDS;

    public UploadedAlgorithmListGrid() {
        super();

        i_uploadedAlgorithmXmlDS = UploadedAlgorithmXmlDS.getInstance();

        setWidth100();
        setHeight("30%");
        setShowAllRecords(true);
        setShowEmptyMessage(false);
        setWrapCells(true);

        setDataSource(i_uploadedAlgorithmXmlDS);

        ListGridField uploadedDateField = new ListGridField(UploadColumns.UPLOAD_DATE.colName(),
                "Date");
        ListGridField algorithmNameField = new ListGridField(UploadColumns.NAME.colName(),
                "Algorithm");

        uploadedDateField.setWidth("30%");
        algorithmNameField.setWidth("*");

        setFields(uploadedDateField, algorithmNameField);

        setAutoFetchData(true);
        setCanEdit(false);

        setCanHover(true);
        setShowHover(true);
        setShowHoverComponents(true);
    }

    @Override
    protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {
        DetailViewer detailViewer = new DetailViewer();
        detailViewer.setWidth(320);

        // Define the fields that we want to display in the details popup. These
        // fields are populated from the record of the selected algorithm.
        DetailViewerField versionField = new DetailViewerField(UploadColumns.VERSION.colName(),
                "Version");
        DetailViewerField uploadedDateField = new DetailViewerField(
                UploadColumns.UPLOAD_DATE.colName(), "Date Uploaded");
        DetailViewerField algorithmNameField = new DetailViewerField(UploadColumns.NAME.colName(),
                "Algorithm");
        DetailViewerField userField = new DetailViewerField(UploadColumns.USER.colName(), "User");
        DetailViewerField descriptionField = new DetailViewerField(
                UploadColumns.DESCRIPTION.colName(), "Description");
        DetailViewerField institutionField = new DetailViewerField(
                UploadColumns.INSTITUTION.colName(), "Institution");
        DetailViewerField creationDateField = new DetailViewerField(
                UploadColumns.CREATEDATE.colName(), "Date Created");
        DetailViewerField commentsField = new DetailViewerField(UploadColumns.COMMENT.colName(),
                "Comments");
        DetailViewerField statusField = new DetailViewerField(UploadColumns.STATUS.colName(),
                "Status");

        detailViewer
                .setFields(algorithmNameField, statusField, institutionField, userField,
                        versionField, uploadedDateField, creationDateField, descriptionField,
                        commentsField);

        detailViewer.setData(new Record[] { record });
        return detailViewer;
    }

}
