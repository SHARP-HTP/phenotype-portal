package edu.mayo.phenoportal.client.help;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class HelpRecord extends ListGridRecord {

    public HelpRecord(String faqTitle, String faqAnswer) {
        setTitle(faqTitle);
        setAnswer(faqAnswer);
    }

    public void setTitle(String title) {
        // set the title in bold
        setAttribute("title", "<b>" + title + "</b>");
    }

    public void setAnswer(String answer) {
        // indent the answer
        setAttribute("answer", "<p style=\"margin-left:20px\"}>" + answer + "</p>");
    }

    public String getTitle() {
        return getAttributeAsString("title");
    }

    public String getAnswer() {
        return getAttributeAsString("answer");
    }

    public String getFieldValue(String field) {
        return getAttributeAsString(field);
    }
}
