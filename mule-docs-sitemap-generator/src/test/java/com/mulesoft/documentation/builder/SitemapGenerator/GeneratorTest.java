package com.mulesoft.documentation.builder.SitemapGenerator;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import com.mulesoft.documentation.builder.SitemapGenerator.Util.Utilities;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 7/25/2015.
 */

public class GeneratorTest {

    @Test
    public void generate_OnValidDirectory_ReturnsCorrectNumberInSet() throws MalformedURLException {
        Set<String> result = getDefaultGenerator().getPathsForHtmlFiles();
        assertTrue(result.size() == 103);
    }

    @Test
    public void getUrlFromPath_WithValidDirectory_ReturnsCorrectNumberOfPaths() {
        Set<String> paths = getDefaultGenerator().getPathsForHtmlFiles();
        List<String> urls = new ArrayList<String>();
        for(String path : paths) {
            urls.add(getDefaultGenerator().getUrlFromPath(path));
        }
        assertTrue(urls.size() == 103);
    }

    @Test
    public void getUrlFromPath_WithSingleDirectory_ReturnsCorrectlyFormattedPaths() {
        Generator generator =
            new Generator("http://mulesoft.com", Utilities.getConcatPath(new String[]{getTestResourcesPath(), "single-section"}));
        Set<String> paths = generator.getPathsForHtmlFiles();
        List<String> urls = new ArrayList<String>();
        for(String path : paths) {
            urls.add(generator.getUrlFromPath(path));
        }
        for(String url : urls) {
            int index = url.lastIndexOf("/");
            String start = url.substring(0, index);
            String end = url.substring(index + 1, url.length());
            assertTrue(start.equals(("http://mulesoft.com/anypoint-data-gateway")));
            assertFalse(end.contains("/"));
        }
    }

    @Test
    public void getUrlFromPath_WithSingleDirectoryAndVersions_ReturnsCorrectlyFormattedPaths() {
        Generator generator =
                new Generator("http://mulesoft.com", Utilities.getConcatPath(new String[]{getTestResourcesPath(), "single-section-with-versions"}));
        Set<String> paths = generator.getPathsForHtmlFiles();
        List<String> urls = new ArrayList<String>();
        for(String path : paths) {
            urls.add(generator.getUrlFromPath(path));
        }
        for(String url : urls) {
            int index = url.lastIndexOf("/");
            String start = url.substring(0, index);
            String end = url.substring(index + 1, url.length());
            if(url.contains("/v/")) {
                assertTrue(start.equals(("http://mulesoft.com/anypoint-data-gateway/v/3.6")));
            } else {
                assertTrue(start.equals(("http://mulesoft.com/anypoint-data-gateway")));
            }
            assertFalse(end.contains("/"));
        }
    }

    @Test
    public void getUrlFromPath_WithValidDirectory_DoesNotContainIndexFileText() {
        Set<String> paths = getDefaultGenerator().getPathsForHtmlFiles();
        List<String> urls = new ArrayList<String>();
        for(String path : paths) {
            urls.add(getDefaultGenerator().getUrlFromPath(path));
        }
        for(String url : urls) {
            assertFalse(url.endsWith("index"));
        }
    }

    @Test
    public void generate_WithValidDirectory_WritesSitemapFile() {
        getDefaultGenerator().generate();
        File outputFilePath = new File(Utilities.getConcatPath(new String[] {getTestPath(), "sitemap.xml"}));
        assertTrue(outputFilePath.exists());
    }





    //todo: MASTER PATH TEST ================================================================================
    @Test
    public void generate_WithMasterDirectory_WritesSiteMapFile() {
        String masterPath = "/Users/sean.osterberg/mulesoft-docs/_output/";
        String rootUrl = "http://docs-stg.mulesoft.com";
        Generator generator = new Generator(rootUrl, masterPath);
        generator.generate();
    }
    //todo: MASTER PATH TEST ================================================================================



    private static String getTestPath() {
        return Utilities.getConcatPath(new String[]{getTestResourcesPath(), "site"});
    }

    private static Generator getDefaultGenerator() {
        return new Generator("http://mulesoft.com", getTestPath());
    }

    public static String getTestResourcesPath() {
        URL pathToTestResources = GeneratorTest.class.getClassLoader().getResource("");
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
