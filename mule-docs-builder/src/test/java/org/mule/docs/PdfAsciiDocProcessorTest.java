package org.mule.docs;

import org.junit.Test;
import org.mule.docs.util.Utilities;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/8/15.
 */
public class PdfAsciiDocProcessorTest {

    @Test
    public void convertAsciiDocStringToPdfFile_WithValidFile_SavesInExpectedLocation() {
        PdfAsciiDocProcessor processor = PdfAsciiDocProcessor.getProcessorInstance();
        String outputFilePath = Utilities.getConcatPath(new String[] { TestUtilities.getTestResourcesPath(), "pdf-output", "index.pdf" });
        processor.convertAsciiDocStringToPdfFile(getContentsFromValidFile(), getValidFilePath(), outputFilePath);
        assertTrue(new File(outputFilePath).exists());
        deleteValidFile();
    }

    @Test
    public void convertAsciiDocStringToPdfFile_WithInvalidFile_ThrowsExceptionButKeepsProcessing() {
        PdfAsciiDocProcessor processor = PdfAsciiDocProcessor.getProcessorInstance();
        String outputFilePath1 = Utilities.getConcatPath(new String[] { TestUtilities.getTestResourcesPath(), "pdf-output", "index.pdf" });
        processor.convertAsciiDocStringToPdfFile(getContentsFromInvalidFile(), getInvalidFilePath(), outputFilePath1);
        String outputFilePath2 = Utilities.getConcatPath(new String[] { TestUtilities.getTestResourcesPath(), "pdf-output", "index.pdf" });
        processor.convertAsciiDocStringToPdfFile(getContentsFromValidFile(), getValidFilePath(), outputFilePath2);
        assertTrue(new File(outputFilePath2).exists());
        deleteValidFile();
    }

    private String getContentsFromValidFile() {
        String path = Utilities.getConcatPath(new String[] { TestUtilities.getPathToMasterFolder(), "cloudhub", "index.adoc" });
        return Utilities.getFileContentsFromFile(new File(path));
    }

    private String getContentsFromInvalidFile() {
        String path = Utilities.getConcatPath(new String[] { TestUtilities.getTestResourcesPath(), "bad-files", "siebel-connector.adoc" });
        return Utilities.getFileContentsFromFile(new File(path));
    }

    private String getValidFilePath() {
        return Utilities.getConcatPath(new String[] { TestUtilities.getPathToMasterFolder(), "cloudhub", "index.adoc" });
    }

    private String getInvalidFilePath() {
        return Utilities.getConcatPath(new String[] { TestUtilities.getTestResourcesPath(), "bad-files", "siebel-connector.adoc" });
    }

    private void deleteValidFile() {
        File validFile = new File(Utilities.getConcatPath(new String[] { TestUtilities.getTestResourcesPath(), "pdf-output", "index.pdf" }));
        if(validFile.exists()) {
            validFile.delete();
        }
    }
}
