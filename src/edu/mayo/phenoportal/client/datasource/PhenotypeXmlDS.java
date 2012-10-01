package edu.mayo.phenoportal.client.datasource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;

public class PhenotypeXmlDS extends DataSource {

    private static PhenotypeXmlDS instance_defaultTree = null;

    private String i_categoryId;

    public static PhenotypeXmlDS getInstanceDefault() {
        if (instance_defaultTree == null) {
            instance_defaultTree = new PhenotypeXmlDS("PhenotypeDSDefault");
        }
        return instance_defaultTree;
    }

    /**
     * Returns a unique name for this instance. If the name has been used before
     * then we will see warnings in the console.
     * 
     * @return
     */
    public static PhenotypeXmlDS getUniqueSelectionTree() {

        int uniqueId = Math.abs(Random.nextInt());
        String uniqueIdStr = "PhenotypeDSSelectionTree" + Integer.toString(uniqueId);

        return new PhenotypeXmlDS(uniqueIdStr);
    }

    public PhenotypeXmlDS(String id) {

        setID(id);
        setTitleField("Phenotype");
        setRecordXPath("/List/phenotype");
        DataSourceTextField phenotypeNameField = new DataSourceTextField("Name", "PhenotypeName",
                158);

        DataSourceIntegerField categoryIdField = new DataSourceIntegerField("CategoryId",
                "Category ID");
        categoryIdField.setPrimaryKey(true);
        categoryIdField.setRequired(true);

        DataSourceIntegerField parentIdField = new DataSourceIntegerField("ParentId", "parent ID");
        parentIdField.setRequired(true);
        parentIdField.setForeignKey(id + ".CategoryId");
        parentIdField.setRootValue("0");

        DataSourceIntegerField countField = new DataSourceIntegerField("Count", "Count");
        DataSourceIntegerField levelField = new DataSourceIntegerField("Level", "Level");
        DataSourceTextField algoVersionField = new DataSourceTextField("AlgoVersion", "AlgoVersion");
        DataSourceTextField algoUserField = new DataSourceTextField("AlgoUser", "AlgoUser");
        DataSourceTextField algoDescField = new DataSourceTextField("AlgoDesc", "AlgoDesc");

        DataSourceBooleanField isFolderField = new DataSourceBooleanField("isFolder", "isFolder");
        isFolderField.setHidden(true);
        isFolderField.setRequired(true);

        setFields(phenotypeNameField, categoryIdField, parentIdField, countField, levelField,
                algoVersionField, algoUserField, algoDescField, isFolderField);

        setClientOnly(true);

        // setDataURL("data/xmlResult.xml");
        // setCacheAllData(true);
    }

    @Override
    protected void transformResponse(DSResponse response, DSRequest request, Object data) {
        if (request.getOperationType() != null) {
            switch (request.getOperationType()) {

                case ADD:
                    break;
                case FETCH: {
                    executeFetch(request);
                }
                    break;
                case REMOVE:
                    break;
                case UPDATE:
                    break;

                default:
                    break;
            }
        }
        super.transformResponse(response, request, data);

    }

    public void executeFetch(DSRequest request) {

        // Criteria criteria = request.getCriteria();
        // String parentId = criteria.getAttribute("parentId");

        PhenotypeServiceAsync phenotypeCategoriesService = GWT.create(PhenotypeService.class);
        phenotypeCategoriesService.getPhenotypeCategories(i_categoryId,
                new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        // TODO handle error.
                        System.out.println("Server Error " + caught.toString());
                    }

                    @Override
                    public void onSuccess(String result) {

                        Object results = XMLTools.selectNodes(result, "/List/phenotype");

                        Record[] fetchRecords = recordsFromXML(results);

                        if (fetchRecords != null) {
                            // add each record
                            for (Record record : fetchRecords) {

                                // set the "isFolder" attribute to false so the
                                // "+" sign doesn't show for algorithm.
                                if (record.getAttributeAsInt("Level") == 4) {
                                    record.setAttribute("isFolder", "false");
                                }

                                addData(record);
                            }
                        }

                    }
                });

    }

    public void setCategoryId(String categoryId) {
        i_categoryId = categoryId;
    }
}
