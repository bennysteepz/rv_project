package com.steeper.ben;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import java.lang.ProcessBuilder;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.List;
import java.util.HashSet;
import java.util.Arrays;

public class Util {

    public static HashSet<String> getAffectedSpecs(boolean verbose, final String... args) throws IOException {

	final HashSet<String> affectedSpecs = new HashSet<String>();
	
	try {
            final ProcessBuilder builder = new ProcessBuilder();
            builder.command(args);

	    if (verbose) { // -v
                builder.inheritIO();
            } else {
                builder.redirectErrorStream(true);
            }
            final Process proc = builder.start();

            if (!verbose) {
                // Consume output/error stream
                final StringWriter writer = new StringWriter();
                new Thread(new Runnable() {
                    public void run() {
                        try {
			    InputStream stream = proc.getInputStream();
			    BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			    String searchString = "advice from";
			    String line = reader.readLine();
			    while (line != null) {
				System.out.println("consuming line: " + line);
				int searchIndex = line.indexOf(searchString);
				if (searchIndex > -1) {
				    int startIndex = searchIndex + searchString.length() + 2;
				    int endIndex = line.indexOf("(", startIndex) - 2;
				    String spec = line.substring(startIndex, endIndex);
				    affectedSpecs.add(spec);
				}
				line = reader.readLine();
			    }
			    reader.close();
			    
                        } catch (IOException e) {
                            System.err.println("Exception in reading subprocess output: " + e.getMessage());
                        }
                    }
                }).start();
            }

            proc.waitFor();
	    return affectedSpecs;
	} catch (InterruptedException ie) {
            ie.printStackTrace();
            //return -1;
	    return affectedSpecs;
        }
    }
    
}
