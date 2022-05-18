package com.steeper.ben;

import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

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


// FileWork class contains general methods that can be applied to all file types:
// (jar, xml and txt for example)
public class FileWork {

    private final static Logger LOGGER =  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    // Delete file
    public void deleteFile(String filePath) {
	File myObj = new File(filePath);
	if (myObj.delete()) {
	    LOGGER.log(Level.INFO, "Deleted the file: " + myObj.getName()); 
	} else {
	    LOGGER.log(Level.INFO, "Failed to delete the file."); 
	}
    }
    // Install - uses "Maven Invocation API" to run "mvn install:...agent.jar..."
    // referenced: https://maven.apache.org/shared/maven-invoker/usage.html
    public void invokeMaven(String pomPath, String command) {

	InvocationRequest request = new DefaultInvocationRequest();
	request.setPomFile(new File(pomPath));
	request.setGoals(Collections.singletonList(command));

	Invoker invoker = new DefaultInvoker();
//            invoker.setMavenHome();

	try { 
	    LOGGER.log(Level.INFO, "Executing Maven invoker request...");
	    invoker.execute(request);
	}
	catch (MavenInvocationException e) {
	    e.printStackTrace();
	}
    }
}
