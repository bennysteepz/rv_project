package com.steeper.ben;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File; // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.List;
import java.util.ArrayList;

@Mojo(name = "updateMETA")
public class fileMojo extends AbstractMojo {
    @Parameter(property = "project", readonly = true)
    private MavenProject project;

    @Parameter(property = "txtFilePath", defaultValue = "/../rv_project/test.txt")
    private String txtFilePath;

    @Parameter(property = "agentJarPath", defaultValue = "/../agents")
    private String agentJarPath;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        getLog().info("Executing updateMETA Goal...");

        // create instance of ReadFile class called myFile (type is a .txt file)
        ReadFile myFile = new ReadFile();
        // get lines of myFile (type is a .txt file)
        List<String> allLines = myFile.getLines();
        // loop through each line of .txt file
        for (int i = 0; i < allLines.size(); i++) {
            getLog().info(allLines.get(i));
        }
        getLog().info(agentJarPath);
        getLog().info(txtFilePath);

        // Create JarWork class to start working with Jar
        JarWork agentJar = new JarWork();
        // Try extracting the agent jar file
        try {
            agentJar.ExtractJar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class JarWork {

        public void main(String[] args) throws java.io.IOException {
            getLog().info("New JarWork class created...");
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

    public class ReadFile {

        public List<String> getLines() {
            List<String> fileLines = new ArrayList<>();

            try {
                File myObj = new File("test.txt");
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


