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

@Mojo(name = "updateMETA")
public class fileMojo extends AbstractMojo {
    @Parameter(property = "project", readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        GetYo myObj = new GetYo();

        getLog().info("hello world, this is my maven plug!!!");
        getLog().info(myObj.returnYo());
    }

    public class GetYo {
        public String returnYo() {
            return "Yo";
        }
    }

    public class ReadFile {
        public void main(String[] args) {
            try {
                File myObj = new File("test.txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    System.out.println(data);
                    getLog().info("in while loop");
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
}


