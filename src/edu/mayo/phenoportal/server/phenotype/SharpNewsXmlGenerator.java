package edu.mayo.phenoportal.server.phenotype;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.mayo.phenoportal.server.utils.DOMXmlGenerator;

public class SharpNewsXmlGenerator extends DOMXmlGenerator {

    protected static final String SHARP_NEWS_ITEMS = "sharpNewsItems";
    protected static final String SHARP_NEWS_ITEM = "sharpNewsItem";

    protected static final String ID = "id";
    protected static final String INFORMATION = "information";

    public void createSharpNewsXml(String id, String info) {

        Element sharpNewsItemElement = i_document.createElement(SHARP_NEWS_ITEM);

        // add the id to the SharpNewsItem element
        Element idElement = i_document.createElement(ID);
        Text idText = i_document.createTextNode(id);
        idElement.appendChild(idText);
        sharpNewsItemElement.appendChild(idElement);

        // add the information to the newsItem element
        Element infoElement = i_document.createElement(INFORMATION);
        Text infoText = i_document.createTextNode(info);
        infoElement.appendChild(infoText);
        sharpNewsItemElement.appendChild(infoElement);

        i_rootElement.appendChild(sharpNewsItemElement);
    }
}
