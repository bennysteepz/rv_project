package com.steeper.ben;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import org.w3c.dom.*;
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
import javax.xml.transform.stream.StreamSource;
import java.io.*;

@Mojo(name = "updateMETA")
public class fileMojo extends AbstractMojo {
    @Parameter(property = "project", readonly = true)
    private MavenProject project;

    // The path where you want the text file created
//    @Parameter(property = "txtFilePath", defaultValue = "/../rv_project/testttt.txt")
//    private String txtFilePath;

    // The path agent .jar file is located
    // extracted files and specs text file can also go here
    @Parameter(property = "agentJarPath", defaultValue = "")
    private String agentJarPath;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        getLog().info("Executing updateMETA Goal...");
        getLog().info("Agent Jar Path: " + agentJarPath);

        // Path Variables
        String txtFilePath = "specs2ignore.txt";

        // Create instance of txtFile class called txtFile (type .txt file)
        TxtFile txtFile = new TxtFile();
        // read from path2specs

        // Get lines of txtFile (type is a .txt file)
        List<String> allLines = txtFile.readLines(txtFilePath);
        // Loop through each line of .txt file
        for (int i = 0; i < allLines.size(); i++) {
            getLog().info(allLines.get(i));
        }

        // Create JarWork class to start working with Jar
//        JarWork agentJar = new JarWork();
        // Extract the agent jar file
//        try {
//            agentJar.ExtractJar(agentJarPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // modify xml
//        ModifyXmlDomParser xmlParser = new ModifyXmlDomParser();
//        xmlParser.modifyXml();

        // create xml from scratch
        WriteXmlDom1 makeXml = new WriteXmlDom1();
//        try {
//            makeXml.createXML();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (TransformerException e) {
//            e.printStackTrace();
//        }
    }

    public class JarWork {
        public void main(String[] args) throws java.io.IOException {
            getLog().info("New JarWork class running...");
        }

        public void ExtractJar(String filePath) throws java.io.IOException {
            getLog().info("Extracting Jar...");
            java.util.jar.JarFile jarfile = new java.util.jar.JarFile(new java.io.File(filePath + "/JavaMOPAgent.jar")); //jar file path(here sqljdbc4.jar)
            java.util.Enumeration<java.util.jar.JarEntry> enu = jarfile.entries();
            while (enu.hasMoreElements()) {
                String destdir = filePath;    // destination directory
                java.util.jar.JarEntry je = enu.nextElement();

                System.out.println(je.getName());

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
        }
    }

    // Class with various methods to work with text files
    public class TxtFile {

        public void main(String[] args) throws java.io.IOException {
            getLog().info("FileWork class running...");
        }

        public void createTxtFile(String filePath) {
            try {
                getLog().info("INside create text file method..filePath:");
                getLog().info(filePath);
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
        public void writeToTxtFile(String filePath, List<String> content) {
            try {
                getLog().info("inside write method...");
                getLog().info(filePath);
                FileWriter myWriter = new FileWriter(filePath);

                for (int i = 0; i < content.size(); i++) {
                    String this_line = content.get(i);
                    getLog().info(this_line);
                    myWriter.write(this_line);
                }
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        // Generates and returns the list of specs to ignore for the text file
        public List<String> generateContent() {
            List<String> content = new ArrayList<>();
            content.add("mop.Collections_SynchronizedCollectionMonitorAspect\n");
            content.add("mop.SortedSet_ComparableMonitorAspect\n");
            return content;
        }

        // Takes in the path to a text file and returns a
        // Sting List with each element being one line of the txt file
        public List<String> readLines(String filePath) {
            List<String> fileLines = new ArrayList<>();

            try {
                File myObj = new File(filePath);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
//                    System.out.println(data);
                    fileLines.add(data);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred, FileNotFoundException");
                e.printStackTrace();
            }
            return fileLines;
        }
    }

    // Modify XML file
    // referenced from:
    // https://mkyong.com/java/how-to-modify-xml-file-in-java-dom-parser/
    public class ModifyXmlDomParser {

        private final String FILENAME = (agentJarPath + "/META-INF/aop-ajc.xml");
        // xslt for pretty print only, no special task
//        private static final String FORMAT_XSLT = "src/main/resources/xslt/staff-format.xslt";

        public void modifyXml() {
            getLog().info("inside modifyxmldomparser");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            try (InputStream is = new FileInputStream(FILENAME)) {
                DocumentBuilder db = dbf.newDocumentBuilder();

                Document doc = db.parse(is);

                NodeList aspectTags = doc.getElementsByTagName("aspect");
                //System.out.println(AbstractsTags.getLength()); // 2
                getLog().info("length of aspectstag:");
                getLog().info(Integer.toString(aspectTags.getLength()));

                for (int i = 0; i < aspectTags.getLength(); i++) {
                    getLog().info("inside aspects tag loop");
                    Node this_aspect = aspectTags.item(i);
                    this_aspect.getParentNode().removeChild(this_aspect);

                    // remove all aspect tags from xml
//                    for (int j = 0; j < childNodes.getLength(); j++) {
//                        getLog().info("inside child node aspect looping");
//                        Node abstractTag = childNodes.item(j);
//                        abstractsTag.removeChild(abstractTag);
//                    }
                }
                try (FileOutputStream output =
                             new FileOutputStream("modified.xml")) {
                    writeXml(doc, output);
                    getLog().info("built new xml file");
                }
            } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
                e.printStackTrace();
            }
        }
        // write doc to output stream
        private void writeXml(Document doc,
                                     OutputStream output)
                throws TransformerException, UnsupportedEncodingException {

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            // The default add many empty new line, not sure why?
            // https://mkyong.com/java/pretty-print-xml-with-java-dom-and-xslt/
             Transformer transformer = transformerFactory.newTransformer();

            // add a xslt to remove the extra newlines
//            Transformer transformer = transformerFactory.newTransformer(
//                    new StreamSource(new File(FORMAT_XSLT)));

            // pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(output);

            transformer.transform(source, result);
        }
    }

    // reference: https://mkyong.com/java/how-to-create-xml-file-in-java-dom/
    public class WriteXmlDom1 {

        public void createXML(List<String> specs2include)
                throws ParserConfigurationException, TransformerException {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("aspectj");
            doc.appendChild(rootElement);

            Element aspectsElement = doc.createElement("aspects");
            rootElement.appendChild(aspectsElement);

            // repeat for every aspect
            Element aspectElement = doc.createElement("aspect");
            aspectsElement.appendChild(aspectElement);
            aspectElement.setAttribute("name","mop.ShutdownHook_UnsafeAWTCallMonitorAspect");

            //...create XML elements, and others...

            // write dom document to a file
            try (FileOutputStream output =
                         new FileOutputStream("modified2.xml")) {
                writeXml(doc, output);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // write doc to output stream
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
}


