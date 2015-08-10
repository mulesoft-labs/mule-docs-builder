package org.mule.docs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.docs.util.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/9/15.
 */
public class ClientTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private PrintStream oldStdErr;
    private PrintStream oldStdOut;

    @Before
    public void setUpStreams() {
        oldStdErr = System.err;
        oldStdOut = System.out;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(oldStdOut);
        System.setErr(oldStdErr);
    }

    @Test
    public void buildSite_WithValidParams_BuildsSite() {
        cleanupFiles();
        String[] args = new String[] {
                "-s", TestUtilities.getPathToMasterFolder(),
                "-d", Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "output" }),
                "-github-repo", "http://github.com/mulesoft/mule-docs",
                "-github-branch", "master"
        };

        Client.main(args);
        File outputFolder = new File(Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "output" }));
        assertTrue(outputFolder.exists());
        assertEquals(2, outputFolder.listFiles().length);
    }

    @Test
    public void main_WithMissingSourceParam_ThrowsException() {
        String[] args = new String[] {
                "-d", Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "output" }),
        };
        Client.main(args);
        assertEquals("No source directory specified", outContent.toString().trim());
    }

    @Test
    public void main_WithMissingDestParam_ThrowsException() {
        String[] args = new String[] {
                "-s", Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "output" }),
        };
        Client.main(args);
        assertEquals("No destination directory specified", outContent.toString().trim());
    }

    @Test
    public void main_WithGitHubRepoButNoBranch_ThrowsException() {
        String[] args = new String[] {
                "-d", Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "output" }),
                "-s", Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "output" }),
                "-github-repo", "foo" };
        Client.main(args);
        assertEquals("No branch was specified for GitHub repo", outContent.toString().trim());
    }

    @Test
    public void main_WithGitHubBranchButNoRepo_ThrowsException() {
        String[] args = new String[] {
                "-d", Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "output" }),
                "-s", Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "output" }),
                "-github-branch", "foo" };
        Client.main(args);
        assertEquals("No repo was specified for GitHub branch", outContent.toString().trim());
    }

    public void cleanupFiles() {
        File outputFolder = new File(Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "output" }));
        outputFolder.delete();
    }

}
