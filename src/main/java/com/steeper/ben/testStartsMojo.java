package com.steeper.ben;

import edu.illinois.starts.jdeps.CleanMojo;
import edu.illinois.starts.jdeps.DiffMojo;
import edu.illinois.starts.jdeps.HelpMojo;
import edu.illinois.starts.jdeps.RunMojo;
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
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import java.util.Collections;
import java.io.File;

@Mojo(name = "testStarts", requiresDependencyResolution = ResolutionScope.TEST)
public class testStartsMojo extends AbstractMojo {

    @Parameter(property = "project", readonly = false)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Starting testStarts execute() method...");

//        invokeMaven("pom.xml", "starts:diff");
//        new edu.illinois.starts.jdeps.;
        RunMojo runMojo = new RunMojo();
        DiffMojo diffMojo = new DiffMojo();
        CleanMojo cleanMojo = new CleanMojo();
//        runMojo.execute();
        getLog().info("TRYING TO GET ARTIFACT:");
        int x = runMojo.getThreadCountClasses();
        getLog().info(Integer.toString(x));

        runMojo.execute();
    }

    private void invokeMaven(String pomPath, String command) {

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(pomPath));
        request.setGoals(Collections.singletonList(command));

        Invoker invoker = new DefaultInvoker();
//            invoker.setMavenHome();

        try {
            getLog().info("Executing Maven invoker request...");
            invoker.execute(request);
        }
        catch (MavenInvocationException e) {
            e.printStackTrace();
        }
    }
}
