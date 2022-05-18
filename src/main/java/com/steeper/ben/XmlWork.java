package com.steeper.ben;

import java.io.*;
import java.util.List;
import java.util.jar.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.logging.Level; 
import java.util.logging.Logger; 
import java.util.logging.*;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Collections;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;



// CLASS with various methods to work with .xml files
// reference:
// https://mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
public class XmlWork {

    private final static Logger LOGGER =  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public void main(String[] args) {
	LOGGER.log(Level.INFO, "XmlWork class running..."); 
    }

    public List<String> readXml(String filePath) {
	LOGGER.log(Level.INFO, "Reading .xml file...");
	// Instantiate the Factory
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	// List<String> variable to put all xml file specs when parsing xml
	List<String> xmlContent = new ArrayList<>();
	try {
	    // optional, but recommended
	    // process XML securely, avoid attacks like XML External Entities (XXE)
	    // dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

	    // parse XML file
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document doc = db.parse(new File(filePath));

	    // optional, but recommended
	    // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	    doc.getDocumentElement().normalize();

	    // Get <aspect>
	    NodeList list = doc.getElementsByTagName("aspect");

	    for (int temp = 0; temp < list.getLength(); temp++) {
		Node node = list.item(temp);

		if (node.getNodeType() == Node.ELEMENT_NODE) {
		    Element element = (Element) node;

		    // Get <aspect>'s "name" attribute value (the spec)
		    String name = element.getAttribute("name");
		    // Add spec string to xmlContent variable
		    xmlContent.add(name + "\n");
		}
	    }
	} catch (ParserConfigurationException | SAXException | IOException e) {
	    e.printStackTrace();
	}
	return xmlContent;
    }
    // Generates <aspectj> xml file from specs List
    // calls helper function writeXml
    // reference: https://mkyong.com/java/how-to-create-xml-file-in-java-dom/
    public void createXML(String fileName, List<String> specList)
	    throws ParserConfigurationException, TransformerException {
	LOGGER.log(Level.INFO, "Create .xml file from specs...");
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

	// Root elements
	Document doc = docBuilder.newDocument();
	Element rootElement = doc.createElement("aspectj");
	doc.appendChild(rootElement);

	Element aspectsElement = doc.createElement("aspects");
	rootElement.appendChild(aspectsElement);

	// Loop through every spec in specsList and write it to xml file
	for (int i = 0; i < specList.size(); i++) {
	    String this_spec = specList.get(i);

	    System.out.println(this_spec);

	    Element aspectElement = doc.createElement("aspect");
	    aspectsElement.appendChild(aspectElement);
	    aspectElement.setAttribute("name", this_spec);
	}

	// Write dom document to a file
	try (FileOutputStream output =
		     new FileOutputStream(fileName)) {
	    writeXml(doc, output);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    // Write doc to output stream
    private void writeXml(Document doc,
			  OutputStream output)
	    throws TransformerException {

	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	Transformer transformer = transformerFactory.newTransformer();

	// pretty print XML
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");

	DOMSource source = new DOMSource(doc);
	StreamResult result = new StreamResult(output);

	transformer.transform(source, result);
    }
}
