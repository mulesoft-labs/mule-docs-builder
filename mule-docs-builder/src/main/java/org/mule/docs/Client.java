package org.mule.docs;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/9/15.
 */
public class Client {
    private static String clientName = "MuleSoft Docs Builder: ";

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
            buildSite(line);
        }
        catch( ParseException exp ) {
            System.out.println( clientName + exp.getMessage() );
        } catch (ClientException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printStartMessage(CommandLine line) {
        String output = "====================================================";
        output += "Starting MuleSoft Documentation Builder with options:\n";
        output += "Source: \t" + line.getOptionValue("s") + "\n";
        output += "Destination: \t" + line.getOptionValue("d") + "\n";

        String gitHubRepo = line.getOptionValue("github-repo");
        String gitHubBranch = line.getOptionValue("github-branch");

        if(StringUtils.isBlank(gitHubRepo)) {
            gitHubRepo = "Not Specified";
            gitHubBranch = "Not Specified";
        }

        output += "GitHub Repo: \t" + gitHubRepo + "\n";
        output += "GitHub Branch: \t" + gitHubBranch + "\n";
        output += "====================================================\n";

        System.out.println(output);
    }

    public static Options getCliOptions() {
        Options options = new Options();
        options.addOption( "s", "source", true, "The source directory to build the site from." );
        options.addOption( "d", "dest", true, "The destination directory to output the site contents." );
        options.addOption( "ghr", "github-repo", true, "(Optional) The fully-qualified path to the " +
                "GitHub repo to create links to edit the page.");
        options.addOption( "ghb", "github-branch", true, "(Optional) The name of the GitHub branch. Required " +
                "if you specified a GitHub repo.");
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
        if(line.hasOption("github-repo") && !line.hasOption("github-branch")) {
            throw new ClientException(clientName + "No branch was specified for GitHub repo");
        }
        if(!line.hasOption("github-repo") && line.hasOption("github-branch")) {
            throw new ClientException(clientName + "No repo was specified for GitHub branch");
        }
    }

    private static void validateHelp(CommandLine line) {
        if(line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            String usage = "-s /path/to/source/dir -d /path/to/dest/dir " +
                    "-ghr https://github.com/my-org-name/my-repo-name -ghb master";
            formatter.printHelp(usage, getCliOptions());
            System.exit(0);
        }
    }

    private static void buildSite(CommandLine line) {
        File sourceDir = new File(line.getOptionValue("s"));
        File destDir = new File(line.getOptionValue("d"));
        String gitHubRepo = line.getOptionValue("github-repo");
        String gitHubBranch = line.getOptionValue("github-branch");

        SiteBuilder builder = new SiteBuilder(sourceDir, destDir, gitHubRepo, gitHubBranch);
        builder.buildSite();
    }
}
