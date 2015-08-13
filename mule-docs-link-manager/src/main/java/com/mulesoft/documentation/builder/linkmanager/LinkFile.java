package com.mulesoft.documentation.builder.linkmanager;

import com.mulesoft.documentation.builder.*;
import com.mulesoft.documentation.builder.util.Utilities;

import org.mule.docs.*;

import java.io.File;
import java.util.List;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 7/28/15.
 */
public class LinkFile {
    private File masterDirectory;

    public LinkFile(String masterDirectory) {
        Utilities.validateIsDirectory(new File(masterDirectory));
        this.masterDirectory = new File(masterDirectory);
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
