package com.steeper.ben;

//import edu.illinois.starts.jdeps.CleanMojo;
//import edu.illinois.starts.jdeps.DiffMojo;
//import edu.illinois.starts.jdeps.HelpMojo;
//import edu.illinois.starts.jdeps.RunMojo;
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
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import java.util.Collections;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Mojo(name = "testStarts", requiresDependencyResolution = ResolutionScope.TEST)
public class testStartsMojo extends AbstractMojo {

    @Parameter(property = "project", readonly = false)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Starting testStarts execute() method...");

        // Attempting running starts programmatically below:
//        new edu.illinois.starts.jdeps.;
//        RunMojo runMojo = new RunMojo();
//        DiffMojo diffMojo = new DiffMojo();
//        CleanMojo cleanMojo = new CleanMojo();
//        runMojo.execute();
//        getLog().info("TRYING TO GET ARTIFACT:");
//        int x = runMojo.getThreadCountClasses();
//        getLog().info(Integer.toString(x));
//        runMojo.execute();

//        invokeMaven("pom.xml", "starts:run");
        invokeMaven("pom.xml", "starts:diff");
    }

    public class StartsOutputHandler implements InvocationOutputHandler {

	private ArrayList<String> affectedClasses = new ArrayList<String>();
	
	public void consumeLine(String line)
	    throws IOException {

	    String searchString = "file:";
	    int searchIndex = line.indexOf(searchString);
	    if (searchIndex > -1) {
		String affectedClass = line.substring(searchIndex + 5, line.length() - 6);
		affectedClasses.add(affectedClass + ".java");
		System.out.println(affectedClass + ".java");
	    }
	}

	public ArrayList<String> getAffectedClasses() {
	    return affectedClasses;
	}
	
    }

    private void invokeMaven(String pomPath, String command) {

	StartsOutputHandler handler = new StartsOutputHandler();
	
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(pomPath));
        request.setGoals(Collections.singletonList(command));
	request.setOutputHandler(handler);
	
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
