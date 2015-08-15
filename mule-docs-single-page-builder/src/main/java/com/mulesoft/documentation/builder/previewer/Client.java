package com.mulesoft.documentation.builder.previewer;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

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
        options.addOption( "d", "dest", true, "The destination directory to output the HTML file." );
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
            throw new ClientException(clientName + "No destination directory specified");
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
        String sourceDir = line.getOptionValue("s");
        String destDir = line.getOptionValue("d");
        SinglePage.fromAsciiDocFile(sourceDir, destDir);
    }
}
