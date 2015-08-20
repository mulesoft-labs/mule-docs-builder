package com.mulesoft.documentation.builder.linkmanager;

import com.mulesoft.documentation.builder.*;
import com.mulesoft.documentation.builder.util.Utilities;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 7/28/15.
 */
public class LinkFile {
    private File masterDirectory;
    public static Process _process;

    public LinkFile(String masterDirectory) {
        Utilities.validateIsDirectory(new File(masterDirectory));
        this.masterDirectory = new File(masterDirectory);
    }

    // Creates a manifest of every Asciidoc file in the master path like shown:
    //      "mule-fundamentals/v/3.6/foobar-file"
    //public List<String> createManifestOfAsciidocFilesInMasterDirectory() {

    //}

    public void appendUniqueAsciidocPathExcludingMasterDirectoryPath(Map<String, String> asciidocPathAndFilename,
                                                                     File parentDir,
                                                                     String masterPathToExclude) {
        File[] files = parentDir.listFiles();
        if(files != null) {
            for(File f : files) {
                if(f.isDirectory()) {
                    appendUniqueAsciidocPathExcludingMasterDirectoryPath(asciidocPathAndFilename, f, masterPathToExclude);
                } else {
                    if(Utilities.fileEndsWithValidAsciidocExtension(f.getName())) {
                        // /User/user.name/mule-fundamentals/v/3.6/anypoint-exchange.adoc --> mule-fundamentals/v/3.6/anypoint-exchange.adoc
                        String pathToAdd = f.getAbsolutePath().replace(masterPathToExclude, "");
                        String extension = FilenameUtils.getExtension(pathToAdd);

                        // mule-fundamentals/v/3.6/anypoint-exchange.adoc --> mule-fundamentals/v/3.6/anypoint-exchange
                        pathToAdd = pathToAdd.substring(0, extension.length() - 1);

                        // anypoint-exchange
                        String fileNameToAdd = FilenameUtils.getBaseName(pathToAdd);

                        asciidocPathAndFilename.put(pathToAdd, fileNameToAdd);
                    }
                }
            }
        }
    }

    public void createFile(String path) {
        String output = getCsvPagesInSections();
        Utilities.writeFileToDirectory(path, output);
        File outputFile = new File(path);
        if(outputFile.exists()) {
            System.out.println("Successfully created link file.");
        }
    }

    public String getCsvPagesInSections() {
        List<Section> sections = Section.fromMasterDirectory(this.masterDirectory);
        StringBuilder sb = new StringBuilder();
        for (Section section : sections) {
            for (AsciiDocPage page : section.getPages()) {
                sb.append(createCsvLine(section, page));
            }
        }
        return sb.toString();
    }

    private String createCsvLine(Section section, AsciiDocPage page) {
        String temp = page.getTitle() + ",";
        temp += section.getPrettyName() + ",";
        temp += page.getBaseName() + ".adoc,";
        temp += "/" + section.getBaseName() + "/" + page.getBaseName() + "\n";
        return temp;
    }

}
