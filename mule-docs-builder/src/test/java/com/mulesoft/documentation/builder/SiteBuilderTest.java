package com.mulesoft.documentation.builder;

import org.junit.Test;
import static org.junit.Assert.*;
import com.mulesoft.documentation.builder.util.Utilities;

import java.io.File;
import java.util.List;
import java.util.logging.LogManager;

/**
 * Created by sean.osterberg on 3/17/15.
 */
public class SiteBuilderTest {

    @Test
    public void buildSite_withValidFolders_BuildsSite() {
        SiteBuilder builder = new SiteBuilder(getValidSourceDirectory(), getValidOutputDirectory(),
                "http://github.com/mulesoft/mule-docs", "master", "developer.mulesoft.com/docs");
        builder.buildSite();
    }


    @Test
    public void buildSite_WithTestDir_BuildsSite() {
        File sourceDir = new File("/Users/sean.osterberg/docs-source/mulesoft-docs");
        File destDir = new File("/Users/sean.osterberg/docs-source/test-for-release-output");
        SiteBuilder builder = new SiteBuilder(sourceDir, destDir, "http://github.com/mulesoft/mule-docs", "master", "developer.mulesoft.com/docs");
        builder.buildSite();
    }

    @Test
    public void buildSite_WithRealDir_BuildsSite() {
        File sourceDir = new File("/Users/sean.osterberg/docs-source/sandbox");
        File destDir = new File("/Users/sean.osterberg/docs-source/test-for-release-output");
        SiteBuilder builder = new SiteBuilder(sourceDir, destDir, "http://github.com/mulesoft/mule-docs", "master", "developer.mulesoft.com/docs");

        builder.buildSite();
    }

    @Test
    public void writeHomePage_WithValidHomePage_CreatesHomePage() {
        SiteBuilder builder = new SiteBuilder(getValidSourceDirectory(), getValidOutputDirectory(),
                "http://github.com/mulesoft/mule-docs", "master", "developer.mulesoft.com/docs");
        String indexPagePath = Utilities.getConcatPath(new String[] { TestUtilities.getTestResourcesPath(), "master-folder", "index.adoc" });
        builder.writeHomePage(indexPagePath);
    }

    /*
    @Test
    public void getSections() {
        SiteBuilder builder = new SiteBuilder(getValidSourceDirectory(), getValidOutputDirectory(),
                "http://github.com/mulesoft/mule-docs", "master");
        builder.getSections(getValidSourceDirectory());
    }*/

    /*
    @Test
    public void buildSite_withRealFolders_BuildsSite() {
        //File source = new File("/Users/sean.osterberg/mulesoft-docs/_source/");
        File source = new File("/Users/sean.osterberg/docs-source/sandbox/");
        File output = new File("/Users/sean.osterberg/docs-source/test-for-release-output/");
        SiteBuilder builder = new SiteBuilder(source, output, "", "", "docs-stg.mulesoft.com");
        builder.buildSite();
        assertTrue(true);
    }*/

    /*
    @Test
    public void buildSite_withRealFolders_BuildsSite() {
        //File source = new File("/Users/sean.osterberg/mulesoft-docs/_source/");
        File source = new File("/Users/sean.osterberg/mulesoft-docs/docsite-demo-connect/site-source/");
        File output = new File("/Users/sean.osterberg/mulesoft-docs/_output/");
        SiteBuilder builder = new SiteBuilder(source, output, "http://github.com/mulesoft/mule-docs", "master");
        builder.buildSite();
    }*/

    private File getValidSourceDirectory() {
        return new File(Utilities.getConcatPath(new String[] { TestUtilities.getTestResourcesPath(), "master-folder" }));
    }

    private File getValidOutputDirectory() {
        return new File(Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "output"}));
    }
}
