package edu.mayo.phenoportal.server.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import edu.mayo.phenoportal.server.upload.FileServiceImpl;

/**
 * Utility class for building xml and then converting it to a string.
 */
public class DOMXmlGenerator {

    protected Document i_document;
    protected Element i_rootElement;

    public DOMXmlGenerator() {
        super();

    }

    // XML Settings
    private static final String XML_VERSION = "1.0";
    private static final String XML_ENCODING = "UTF-8";

    /**
     * Using JAXP in implementation independent manner create a i_document
     * object using which we create a xml tree in memory
     */
    public Document createDocumentAndRootElement(String rootName) {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

        try {
            // get an instance of builder
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // create an instance of DOM
            i_document = docBuilder.newDocument();

            // Created the Root element
            i_rootElement = i_document.createElement(rootName);

            // Appended the root element to Document
            i_document.appendChild(i_rootElement);

        } catch (ParserConfigurationException ex) {
            // dump it
            Logger lgr = Logger.getLogger(FileServiceImpl.class.getName());
            lgr.log(Level.SEVERE, "Parser exception" + ex.getStackTrace(), ex);
        }
        return i_document;
    }

    public String xmlToString() {

        StringWriter strWriter = null;
        XMLSerializer probeMsgSerializer = null;
        OutputFormat outFormat = null;

        String xmlStr = "";

        try {
            probeMsgSerializer = new XMLSerializer();
            strWriter = new StringWriter();
            outFormat = new OutputFormat();

            // Setup format settings
            outFormat.setEncoding(XML_ENCODING);
            outFormat.setVersion(XML_VERSION);
            outFormat.setIndenting(true);
            outFormat.setIndent(4);

            // Define a Writer
            probeMsgSerializer.setOutputCharStream(strWriter);

            // Apply the format settings
            probeMsgSerializer.setOutputFormat(outFormat);

            // Serialize XML Document
            probeMsgSerializer.serialize(i_document);
            xmlStr = strWriter.toString();
            strWriter.close();

        } catch (IOException ex) {
            Logger lgr = Logger.getLogger(FileServiceImpl.class.getName());
            lgr.log(Level.SEVERE, "Failed to convert xml to string" + ex.getStackTrace(), ex);
        }

        return xmlStr;
    }

}
