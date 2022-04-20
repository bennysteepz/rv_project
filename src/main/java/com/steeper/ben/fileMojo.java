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
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.List;
import java.util.ArrayList;

@Mojo(name = "updateMETA")
public class fileMojo extends AbstractMojo {
    @Parameter(property = "project", readonly = true)
    private MavenProject project;

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


