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


// CLASSES
// JarWork class includes 2 methods: ExtractJar and CreateJar
// Both methods take in a file path
public class JarWork {

    private final static Logger LOGGER =  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public void main(String[] args) throws java.io.IOException {
	LOGGER.log(Level.INFO, "JarWork class running..."); 
    }
    // Extracts jar located at filePath
    // inputs filePath: path to .jar file, and destPath: path to destination folder
    // Reference:
    // https://stackoverflow.com/questions/1529611/how-to-write-a-java-program-which-can-extract-a-jar-file-and-store-its-data-in-s
    public void extractJar(String filePath, String destPath) throws java.io.IOException {
	LOGGER.log(Level.INFO, "Extracting .jar file...");
	//jar file path
	java.util.jar.JarFile jarfile = new java.util.jar.JarFile(new java.io.File(filePath));
	java.util.Enumeration<java.util.jar.JarEntry> enu = jarfile.entries();
	while (enu.hasMoreElements()) {
	    String destdir = destPath;    // destination directory
	    java.util.jar.JarEntry je = enu.nextElement();
	    java.io.File fl = new java.io.File(destdir, je.getName());

	    if (!fl.exists()) {
		fl.getParentFile().mkdirs();
		fl = new java.io.File(destdir, je.getName());
	    }
	    if (je.isDirectory()) {
		continue;
	    }
	    java.io.InputStream is = jarfile.getInputStream(je);
	    java.io.FileOutputStream fo = new java.io.FileOutputStream(fl);
	    while (is.available() > 0) {
		fo.write(is.read());
	    }
	    fo.close();
	    is.close();
	}
	LOGGER.log(Level.INFO, "Finished extracting .jar file..");
    }
    // Create jar file, reference:
    // https://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file/
    // takes in source path .jar file, target path, and Manifest file from getManifest() method
    public void createJar(String sourcePath, String targetPath, Manifest manifest_custom) throws IOException {
	LOGGER.log(Level.INFO, "Creating .jar file...");
	JarOutputStream target = new JarOutputStream(new FileOutputStream(targetPath), manifest_custom);
	File inputDirectory = new File(sourcePath);
	for (File nestedFile : inputDirectory.listFiles())
	    add("", nestedFile, target);
	target.close();
    }
    // Helper function for createJar
    private void add(String parents, File source, JarOutputStream target) throws IOException {
	BufferedInputStream in = null;
	try {
	    String name = (parents + source.getName()).replace("\\", "/");

	    if (source.isDirectory()) {
		if (!name.isEmpty()) {
		    if (!name.endsWith("/"))
			name += "/";
		    JarEntry entry = new JarEntry(name);
		    entry.setTime(source.lastModified());
		    target.putNextEntry(entry);
		    target.closeEntry();
		}
		for (File nestedFile : source.listFiles())
		    add(name, nestedFile, target);
		return;
	    }

	    JarEntry entry = new JarEntry(name);
	    entry.setTime(source.lastModified());
	    target.putNextEntry(entry);
	    in = new BufferedInputStream(new FileInputStream(source));

	    byte[] buffer = new byte[1024];
	    while (true) {
		int count = in.read(buffer);
		if (count == -1)
		    break;
		target.write(buffer, 0, count);
	    }
	    target.closeEntry();
	}
	finally {
	    if (in != null)
		in.close();
	}
    }
    // reference:
    // https://www.tabnine.com/code/java/methods/java.util.jar.Manifest/%3Cinit%3E?snippet=5ce707df2fd38000041486a4
    // ^ to use existing manifest in META-INF instead of creating new default one (otherwise agent won't work)
    // Takes in path to manifest file, returns MANIFEST file
    public Manifest getManifest(String filePath) throws IOException {
	File file = new File(filePath, "MANIFEST.MF");
	if (file.exists()) {
	    InputStream inputStream = new FileInputStream(file);
	    try {
		LOGGER.log(Level.INFO, "Manifest found...");
		return new Manifest(inputStream);
	    } finally {
		inputStream.close();
	    }
	} else {
	    LOGGER.log(Level.INFO, "No manifest found.");
	    return new Manifest(); // empty manifest
	}
    }
}
