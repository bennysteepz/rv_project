package com.steeper.ben;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
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

@Mojo(name = "init", requiresDependencyResolution = ResolutionScope.TEST)
public class InitMojo extends AbstractMojo {

    @Parameter(property = "project", readonly = false)
    private MavenProject project;

    // agentJarPath: Path to the parent directory of JavaMOPAgent.jar                                                                                                             
    @Parameter(property = "agentsPath", defaultValue = "")
    private String agentsPath;

    // specListPath: path to the .txt file listing specs to include                                                                                                               
    @Parameter(property = "specsPath", defaultValue = "/src/main/resources/specs.txt")
    private String specsPath;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Starting init execute() method...");

	String jarFilePath = agentsPath + "/JavaMOPAgent.jar";
	String extractedPath = agentsPath + "/extracted"; // to be created before extracting jar
	String txtAllSpecsFilePath = "allSpecs.txt"; // store in client plugin root directory
	String xmlFilePath = agentsPath + "/extracted/META-INF/aop-ajc.xml";
	
	JarWork jarWork = new JarWork(); // contains methods for working with .jar files
	XmlWork xmlWork = new XmlWork(); // contains methods for working with .xml files
	TxtWork txtWork = new TxtWork(); // contains methods for working with .txt files
	FileWork fileWork = new FileWork(); // contains general methods for all file types
	
        // 2. EXTRACT JAR
        // Make directory in agents called "extracted" to put extracted files in
        new File(extractedPath).mkdirs();
        try {
            // Extract Jar file - arguments: jar path followed by destination path
            jarWork.extractJar(jarFilePath, extractedPath);
            // Delete jar that was just extracted

	    // I'm pretty sure we should remove this step just for the init stage
	    //fileWork.deleteFile(jarFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
	
	List<String> allSpecs = xmlWork.readXml(xmlFilePath);

	// Create allSpecs.txt and write allSpecs to it
        txtWork.createTxtFile(txtAllSpecsFilePath);
        // Write aop-ajc.xml spec strings to specListAll.txt
        txtWork.writeTxtFile(txtAllSpecsFilePath, allSpecs);
	

	// initialize starts
	invokeMaven("pom.xml", "starts:run");
	
    }

    private void invokeMaven(String pomPath, String command) {

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(pomPath));
        request.setGoals(Collections.singletonList(command));

        Invoker invoker = new DefaultInvoker();
	//invoker.setMavenHome();

        try {
            getLog().info("Executing Maven invoker request...");
            invoker.execute(request);
        }
        catch (MavenInvocationException e) {
            e.printStackTrace();
        }
    }
}
