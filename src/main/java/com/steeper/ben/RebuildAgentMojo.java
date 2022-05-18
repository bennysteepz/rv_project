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

@Mojo(name = "rebuildAgent", requiresDependencyResolution = ResolutionScope.TEST)
public class RebuildAgentMojo extends AbstractMojo {

    @Parameter(property = "project", readonly = false)
    private MavenProject project;

    // agentJarPath: Path to the parent directory of JavaMOPAgent.jar
    @Parameter(property = "agentsPath", defaultValue = "")
    private String agentsPath;

    // specListPath: path to the .txt file listing specs to include
    @Parameter(property = "specsPath", defaultValue = "/src/main/resources/specs.txt")
    private String specsPath;

    @Parameter(property = "affectedClassesPath", defaultValue = "/src/main/resources/affectedClasses.txt")
    private String affectedClassesPath;
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Starting rebuildAgent execute() method...");

        // 1. CREATE FILE PATH VARIABLES and INSTANTIATE CLASSES
        String jarFilePath = agentsPath + "/JavaMOPAgent.jar";
        String xmlFilePath = agentsPath + "/extracted/META-INF/aop-ajc.xml";
        String txtAllSpecsFilePath = "allSpecs.txt"; // store in client plugin root directory
        String metaFilePath = agentsPath + "/extracted/META-INF/";
        String manifestPath = agentsPath + "/extracted/META-INF/MANIFEST.MF";
        String extractedPath = agentsPath + "/extracted"; // to be created before extracting jar
        String clientPomPath = "pom.xml"; // get this programmatically later, for now it's just hard coded

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
            fileWork.deleteFile(jarFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3. CREATE specListAll.txt in client plugin root dir FROM aop-ajc.xml in agents
        // Read aop-ajc.xml file
        // Store specs from xml tags in List<String> allSpecs
        List<String> allSpecs = xmlWork.readXml(xmlFilePath);
        List<String> affectedClasses = txtWork.getLines(affectedClassesPath);
        HashSet<String> affectedSpecs = getAffectedSpecs(allSpecs, affectedClasses);
        List<String> specsToInclude = new ArrayList<String>();
        getLog().info("before spec for loop");
        for (String spec : affectedSpecs) {
            getLog().info("log spec below!!:");
            getLog().info(spec);
            specsToInclude.add(spec);
        }

	
	// Create allSpecs.txt and write allSpecs to it
        txtWork.createTxtFile(txtAllSpecsFilePath);
        // Write aop-ajc.xml spec strings to specListAll.txt
        txtWork.writeTxtFile(txtAllSpecsFilePath, allSpecs);

        // 4. RECREATE XML file from specs.txt (which is located in my plugin's resources directory)
        // ** specs.txt is given for now, but later it will be updated programatically **

	// Read specs.txt and store lines in List<String> specsToInclude variable
        //List<String> specsToInclude = txtWork.getLines(specsPath);

	// First remove old xml file to replace
        // (later found out this is unnecessary, but I suppose it can't hurt to assure old file is gone)
        fileWork.deleteFile(xmlFilePath);
        // Create new XML file with specsToInclude
        try {
            xmlWork.createXML(xmlFilePath, specsToInclude);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        // 5. REBUILD the JAR
        // Create new jar in agents directory
        try {
            // Get the current manifest and pass it into the createJar() method
            Manifest manifest = jarWork.getManifest(metaFilePath);
            // remove the manifest before creating jar to avoid duplicate manifest error
            fileWork.deleteFile(manifestPath);
            // createJar takes in path with files, path to .jar and cached Manifest file
            jarWork.createJar(extractedPath, jarFilePath, manifest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 6. INSTALL JAR AGENT in the client plugin
        fileWork.invokeMaven(clientPomPath, "install:install-file");
    }

    private HashSet<String> getAffectedSpecs(List<String> aspects, List<String> affectedClasses) {
	String runtimeMonitor = agentsPath + "../props/classes/mop/MultiSpec_1RuntimeMonitor.java";
	try {
	    ArrayList<String> args = new ArrayList<String>();
	    args.add("java");
	    args.add("org.aspectj.tools.ajc.Main");
	    args.add("-1.6");
	    args.add("-d");
	    args.add(".");
	    for (String aspect : aspects) {
		    String aspectChop = aspect.substring(4, aspect.length() - 1);
		    String aspectPath = agentsPath + "../props/" +  aspectChop + ".aj";
		    args.add(aspectPath);
	    }
	    args.add(runtimeMonitor);
	    args.add("-Xlint:ignore");
	    for (String affectedClass : affectedClasses) {
		    args.add(affectedClass);
	    }
	    args.add("-showWeaveInfo");
	    return Util.getAffectedSpecs(false, args.toArray(new String[0]));
	} catch (IOException e) {
	    e.printStackTrace();
	    return new HashSet<String>();
	}

    }
}


