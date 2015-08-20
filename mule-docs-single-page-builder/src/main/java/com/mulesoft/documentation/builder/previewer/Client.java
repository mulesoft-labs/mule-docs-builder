package com.mulesoft.documentation.builder.previewer;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/9/15.
 */
public class Client {
    private static String clientName = "MuleSoft Doc Previewer: ";

    public static void main(String[] args) {
        parseInput(args);
    }

    public static void parseInput(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = getCliOptions();
        try {
            CommandLine line = parser.parse(options, args);
            validateHelp(line);
            validateInput(line);
            buildSite(line);
        }
        catch( ParseException exp ) {
            System.out.println( clientName + exp.getMessage() );
        } catch (ClientException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Options getCliOptions() {
        Options options = new Options();
        options.addOption( "s", "source", true, "The source AsciiDoc file." );
        options.addOption( "d", "dest", true, "(Optional) The destination directory to output the HTML file. If you do not " +
                "specify a destination directory, the file will be saved in your /tmp directory." );
        options.addOption( "h", "help", false, "Help/usage information." );

        return options;
    }

    private static void validateInput(CommandLine line) {
        if(line.hasOption("s")) {
            File sourceFile = new File(line.getOptionValue("s"));
            if(!sourceFile.exists()) {
                throw new ClientException(clientName + "Source file doesn't exist");
            }
        } else {
            throw new ClientException(clientName + "No source file specified");
        }
        if(!line.hasOption("d")) {
            System.out.println("Didn't specify destination directory. Building to /tmp directory instead.");
        }
    }


    /**
     * Copy the _img path to /tmp if we can write to it, and also write the output file there by default
     */
    public static void copyFiles(String sourceFilePath) {
        String failureMessage = "Couldn't copy section image files to /tmp directory.";
        File sourceFile = new File(sourceFilePath);
        File parent = sourceFile.getParentFile();
        if(parent.isDirectory()) {
            File imageDir = new File(Utilities.getConcatPath(new String[] { parent.getAbsolutePath(), "_images" }));
            if(imageDir.exists()) {
                File tempDir = new File("/tmp");
                if(tempDir.exists() && tempDir.canWrite()) {
                    try {
                        String newImgPath = Utilities.getConcatPath(new String[] { tempDir.getAbsolutePath(), "_images" } );
                        FileUtils.copyDirectory(imageDir, new File(newImgPath));
                    } catch(IOException e) {
                        System.out.println(failureMessage);
                    }
                } else {
                    System.out.println(failureMessage);
                }
            } else {
                System.out.println("Couldn't copy section image files to /tmp directory." + " _image directory doesn't exist.");
            }
        }
    }

    private static void validateHelp(CommandLine line) {
        if(line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            String usage = "-s /path/to/asciidoc-file.adoc -d /path/to/dest/dir ";
            formatter.printHelp(usage, getCliOptions());
            System.exit(0);
        }
    }

    private static void buildSite(CommandLine line) {
        String destDir;
        String sourceFile = line.getOptionValue("s");
        if(!line.hasOption("d")) {
            copyFiles(sourceFile);
            destDir = "/tmp";
        } else {
            destDir = line.getOptionValue("d");
        }
        copyFiles(sourceFile);
        SinglePage.fromAsciiDocFile(sourceFile, destDir);
    }
}
