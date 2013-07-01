package edu.mayo.phenoportal.client.phenotype.report;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLPane;

import edu.mayo.phenoportal.client.core.AlgorithmData;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;

public class CriteriaHTMLPage extends HTMLPane {

    private AlgorithmData i_algorithmData;
    private String i_populationCriteria;

    public CriteriaHTMLPage() {
        super();

        setWidth100();
        setHeight100();
        setOverflow(Overflow.VISIBLE);

    }

    public void udpateHTMLPage(AlgorithmData algorithmData) {
        i_algorithmData = algorithmData;
        setCriteriaInfo();
    }

    private void setCriteriaInfo() {

        clear();

        PhenotypeServiceAsync async = (PhenotypeServiceAsync) GWT.create(PhenotypeService.class);

        async.getPopulationCriteria(i_algorithmData, new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                i_populationCriteria = result;

                setContents(i_populationCriteria);
                markForRedraw();
            }

            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Error getting criteria: " + caught);
            }
        });

    }

    @Override
    public void clear() {
        setContents("");

        i_populationCriteria = "";
    }

}
