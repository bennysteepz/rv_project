package com.steeper.ben;

import java.io.*;
import java.util.List;
import java.util.jar.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.HashSet;
import java.util.logging.Level; 
import java.util.logging.Logger; 
import java.util.logging.*;
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


// CLASS with various methods to work with .txt files
public class TxtWork {

    private final static Logger LOGGER =  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public void main(String[] args) throws java.io.IOException {
	LOGGER.log(Level.INFO, "TxtWork class running..."); 
    }
    public void createTxtFile(String filePath) {
	try {
	    LOGGER.log(Level.INFO, "Creating text file to path: " + filePath);
	    File myObj = new File(filePath);
	    if (myObj.createNewFile()) {
		System.out.println("File created: " + myObj.getName());
	    } else {
		System.out.println("File already exists.");
	    }
	} catch (IOException e) {
	    System.out.println("An error occurred.");
	    e.printStackTrace();
	}
    }
    // Write to text file
    // filePath: path to text file, content: List of strings for each line
    public void writeTxtFile(String filePath, List<String> content) {
	try {
	    FileWriter myWriter = new FileWriter(filePath);

	    for (int i = 0; i < content.size(); i++) {
		String this_line = content.get(i);
		myWriter.write(this_line);
	    }
	    myWriter.close();
	    LOGGER.log(Level.INFO, "Wrote to file: " + filePath);
	} catch (IOException e) {
	    LOGGER.log(Level.INFO, "An error occurred.");
	    e.printStackTrace();
	}
    }
    // Reads txt file pointed to by filePath
    // returns String List of each line in txt file
    public List<String> getLines(String filePath) {
	//getLog().info("Read .txt file: " + filePath + ", and return lines...");
	List<String> fileLines = new ArrayList<>();
	try {
	    File myObj = new File(filePath);
	    Scanner myReader = new Scanner(myObj);
	    while (myReader.hasNextLine()) {
		String data = myReader.nextLine();
		fileLines.add(data);
	    }
	    myReader.close();
	} catch (FileNotFoundException e) {
	    LOGGER.log(Level.INFO, "An error occurred, FileNotFoundException");
	    e.printStackTrace();
	}
	return fileLines;
    }
}
