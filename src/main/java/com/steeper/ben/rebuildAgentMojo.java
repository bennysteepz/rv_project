package com.steeper.ben;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@Mojo(name = "rebuildAgent", defaultPhase = LifecyclePhase.INITIALIZE)
public class rebuildAgentMojo extends AbstractMojo {

    @Parameter(property = "project", readonly = true)
    private MavenProject project;

    // agentJarPath: Path to the parent directory of JavaMOPAgent.jar
    @Parameter(property = "agentsPath", defaultValue = "")
    private String agentsPath;

    // : Path to the parent directory of JavaMOPAgent.jar
    @Parameter(property = "specListPath", defaultValue = "")
    private List<String> specListPath;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Starting rebuildAgent execute() method...");

        // 0. CREATE FILE PATH VARIABLES
        String jarFilePath = agentsPath + "/JavaMOPAgent.jar";
        String xmlFilePath = agentsPath + "/META-INF/aop-ajc.xml";
        List<String> txtSpecsFilePath = specListPath;

        // 1. EXTRACT JAR
        // Create new instance of JarWork class
        JarWork jarWork = new JarWork();
        // try extracting Jar file
        try {
            jarWork.extractJar(jarFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // CLASSES
    // JarWork class includes 2 methods: ExtractJar and CreateJar
    // Both methods take in a file path
    public class JarWork {

        public void main(String[] args) throws java.io.IOException {
            getLog().info("New JarWork class running...");
        }
        // Extracts jar located at filePath
        // Referenced from:
        // https://stackoverflow.com/questions/1529611/how-to-write-a-java-program-which-can-extract-a-jar-file-and-store-its-data-in-s
        public void extractJar(String filePath) throws java.io.IOException {
            getLog().info("Extracting Jar...");
            //jar file path
            java.util.jar.JarFile jarfile = new java.util.jar.JarFile(new java.io.File(filePath));
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
}


