package edu.mayo.phenoportal.client.phenotype.report;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.tab.Tab;

import edu.mayo.phenoportal.client.core.AlgorithmData;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;

/**
 * Tab for displaying the Criteria info for the selected phenotype.
 */
public class CriteriaTab extends Tab implements ReportTab {

    private AlgorithmData i_algoAlgorithmData;
    private final HTMLPane i_htmlPane;

    public CriteriaTab(String title) {
        super(title);

        i_htmlPane = new HTMLPane();
        i_htmlPane.setWidth100();
        i_htmlPane.setHeight100();
        setPane(i_htmlPane);
    }

    /**
     * Retrieve the new algorithm criteria info from the DB and display it.
     * 
     * @param algorithmData
     */
    public void updateSelection(AlgorithmData algorithmData) {
        i_algoAlgorithmData = algorithmData;
        setCriteriaInfo();
    }

    protected void setCriteriaInfo() {

        PhenotypeServiceAsync async = (PhenotypeServiceAsync) GWT.create(PhenotypeService.class);
        async.getCriteria(i_algoAlgorithmData.getAlgorithmName(),
                i_algoAlgorithmData.getParentId(), i_algoAlgorithmData.getAlgorithmVersion(),
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        i_htmlPane.setContents(result);
                        // i_htmlPane.draw();
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        GWT.log("Error getting criteria: " + caught);
                    }
                });
    }

    @Override
    public void clearTab() {
        i_htmlPane.setContents("");
    }

}
