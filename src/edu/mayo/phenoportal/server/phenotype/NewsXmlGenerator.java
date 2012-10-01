package edu.mayo.phenoportal.server.phenotype;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.mayo.phenoportal.server.utils.DOMXmlGenerator;

/**
 * XML representation of a news item.
 */
public class NewsXmlGenerator extends DOMXmlGenerator {

    protected static final String NEW_ITEMS = "newsItems";
    protected static final String NEW_ITEM = "newsItem";

    protected static final String ID = "id";
    protected static final String DATE = "date";
    protected static final String INFORMATION = "information";

    public void createNewsXml(String id, String date, String info) {

        Element newsItemElement = i_document.createElement(NEW_ITEM);

        // add the id to the newsItem element
        Element idElement = i_document.createElement(ID);
        Text idText = i_document.createTextNode(id);
        idElement.appendChild(idText);
        newsItemElement.appendChild(idElement);

        // add the date to the newsItem element
        Element dateElement = i_document.createElement(DATE);
        Text dateText = i_document.createTextNode(date);
        dateElement.appendChild(dateText);
        newsItemElement.appendChild(dateElement);

        // add the information to the newsItem element
        Element infoElement = i_document.createElement(INFORMATION);
        Text infoText = i_document.createTextNode(info);
        infoElement.appendChild(infoText);
        newsItemElement.appendChild(infoElement);

        i_rootElement.appendChild(newsItemElement);
    }
}
