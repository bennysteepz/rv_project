package com.steeper.ben;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import java.io.File; // Import the File class
import java.io.FileWriter;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.List;
import java.util.ArrayList;

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

        // Create instance of txtFile class called txtFile (type .txt file)
        String txtFilePath = agentJarPath + "/specs2ignore.txt";
        TxtFile txtFile = new TxtFile();
        txtFile.createTxtFile(agentJarPath);

        // Get lines of txtFile (type is a .txt file)
//        List<String> allLines = txtFile.getLines();
//        // Loop through each line of .txt file
//        for (int i = 0; i < allLines.size(); i++) {
//            getLog().info(allLines.get(i));
//        }

        // Create JarWork class to start working with Jar
//        JarWork agentJar = new JarWork();
        // Try extracting the agent jar file
//        try {
//            agentJar.ExtractJar();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public class JarWork {

        public void main(String[] args) throws java.io.IOException {
            getLog().info("New JarWork class running...");
        }

        public void ExtractJar() throws java.io.IOException {
            getLog().info("Extracting Jar...");
            java.util.jar.JarFile jarfile = new java.util.jar.JarFile(new java.io.File(agentJarPath + "/JavaMOPAgent.jar")); //jar file path(here sqljdbc4.jar)
            java.util.Enumeration<java.util.jar.JarEntry> enu = jarfile.entries();
            while (enu.hasMoreElements()) {
                String destdir = agentJarPath;    // destination directory
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
        public void writeToTxtFile(String filePath) {
            try {
                FileWriter myWriter = new FileWriter(filePath);
                myWriter.write("Files in Java might be tricky, but it is fun enough!");
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        // Takes in the path to a text file and returns a
        // Sting List with each element being one line of the txt file
        public List<String> getLines(String filePath) {
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
}


