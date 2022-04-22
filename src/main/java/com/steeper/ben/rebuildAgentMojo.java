package com.steeper.ben;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "rebuildAgent", defaultPhase = LifecyclePhase.INITIALIZE)
public class rebuildAgentMojo extends AbstractMojo {

    @Parameter(property = "project", readonly = true)
    private MavenProject project;

    // agentJarPath: Path to the parent directory of JavaMOPAgent.jar
    @Parameter(property = "agentJarPath", defaultValue = "")
    private String agentJarPath;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Starting rebuildAgent execute() method...");

        // Create file path variables
        String jarFilePath = agentJarPath + "/JavaMOPAgent.jar";
        String xmlFilePath = agentJarPath + "/META-INF/aop-ajc.xml";
    }
}


