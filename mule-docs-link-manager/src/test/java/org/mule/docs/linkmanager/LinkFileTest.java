package org.mule.docs.linkmanager;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.mule.docs.util.Utilities;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 7/28/15.
 */
public class LinkFileTest {

    @Test
    public void fromDirectory_WithValidDirectory_CreatesFile() {
        LinkFile links = new LinkFile(Utilities.getConcatPath( new String[] { getTestResourcesPath(), "3.2" }));
        String outputFilePath = Utilities.getConcatPath(new String[] { getTestResourcesPath(), "links.csv" });
        links.createFile(outputFilePath);
        assertTrue(new File(outputFilePath).exists());
    }

    @Test
    public void outputCSVFileHasExpectedNumberOfLines() {
        String outputFilePath = Utilities.getConcatPath(new String[] { getTestResourcesPath(), "links.csv" });
        File outputFile = new File(outputFilePath);
        String contents = "";
        if(outputFile.exists()) {
            contents = Utilities.getFileContentsFromFile(outputFile);
        }
        String[] lines = contents.split("\n");
        assertEquals(875, lines.length);
    }

    @Test
    public void outputCSVFileHasExpectedContentsAndFormat() {
        String outputFilePath = Utilities.getConcatPath(new String[] { getTestResourcesPath(), "links.csv" });
        File outputFile = new File(outputFilePath);
        String contents = "";
        if(outputFile.exists()) {
            contents = Utilities.getFileContentsFromFile(outputFile);
        }
        contents.contains("OAuth V1,Anypoint Connector DevKit,oauth-v1.adoc,/anypoint-connector-devkit/oauth-v1");
    }

    public static String getTestResourcesPath() {
        URL pathToTestResources = LinkFileTest.class.getClassLoader().getResource("");
        String testResourcesPath = "";
        if(pathToTestResources != null) {
            testResourcesPath = pathToTestResources.getFile();
            if (!StringUtils.isEmpty(testResourcesPath)) {
                return testResourcesPath;
            }
        }
        throw new RuntimeException("Test resources path was null.");
    }
}
