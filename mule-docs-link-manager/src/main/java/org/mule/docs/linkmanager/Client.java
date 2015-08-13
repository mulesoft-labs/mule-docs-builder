package org.mule.docs.linkmanager;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.mule.docs.ClientException;
import org.mule.docs.util.Utilities;

import java.io.File;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/9/15.
 */
public class Client {
    private static String clientName = "MuleSoft Docs Link Generator: ";
    private static String csvFileName = "links.csv";

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
            printStartMessage(line);
            buildCsv(line);
        }
        catch( ParseException exp ) {
            System.out.println( clientName + exp.getMessage() );
        } catch (ClientException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void buildCsv(CommandLine line) {
        LinkFile file = new LinkFile(line.getOptionValue("s"));
        file.createFile(Utilities.getConcatPath(new String[] { line.getOptionValue("d"), csvFileName }));
    }

    public static void printStartMessage(CommandLine line) {
        String output = "====================================================\n";
        output += "Starting " + clientName + "\n";
        output += "Source: \t" + line.getOptionValue("s") + "\n";
        output += "Destination: \t" + line.getOptionValue("d") + "\n";
        output += "\n*NOTE* The CSV will only contain links for the latest version \n" +
                "of a section, not all the versions. This is by design.\n";
        output += "====================================================\n";

        System.out.println(output);
    }

    public static Options getCliOptions() {
        Options options = new Options();
        options.addOption( "s", "source", true, "The source directory to generate link file. *NOTE*" +
                " This path must have valid site builder structure." );
        options.addOption( "d", "dest", true, "The destination directory to output the link file." );
        options.addOption( "h", "help", false, "Help/usage information." );

        return options;
    }

    private static void validateInput(CommandLine line) {
        if(line.hasOption("s")) {
            File sourceFile = new File(line.getOptionValue("s"));
            if(!sourceFile.exists()) {
                throw new ClientException(clientName + "Source directory doesn't exist");
            }
        } else {
            throw new ClientException(clientName + "No source directory specified");
        }
        if(!line.hasOption("d")) {
            throw new ClientException(clientName + "No destination directory specified");
        }
    }

    private static void validateHelp(CommandLine line) {
        if(line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            String usage = "-s /path/to/source/dir -d /path/to/dest/dir ";
            formatter.printHelp(usage, getCliOptions());
            System.exit(0);
        }
    }
}
