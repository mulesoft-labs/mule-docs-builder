package org.mule.docs;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;

/**
 * Created by sean.osterberg on 7/5/15.
 */
public class SwiftTypeMetadataTest {

    @Test
    public void getDescription_ReturnsExpectedResult() {
        String result = SwiftTypeMetadata.getDescription(getValidAsciiDocPage());
        assertEquals(result, "Integrate SaaS applications, Cloud Services and Enterprise applications in real-time. CloudHub is the world’s first integration Platform as a Servi...");
    }

    @Test
    public void getFirstParagraph_EndsWithEllipses() {
        String result = SwiftTypeMetadata.getDescription(getValidAsciiDocPage());
        assertTrue(result.endsWith("..."));
    }

    @Test
    public void getFirstParagraph_WithFewerThan147Chars_ReturnsExpectedResult() {
        String result = SwiftTypeMetadata.getDescription(getValidAsciiDocPage());
        assertTrue(result.endsWith("..."));
    }

    @Test
    public void getFirstParagraph_WithAdmonition_ReturnsTextFromAdmonition() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "deploying-a-cloudhub-application.ad"});
        AsciiDocPage page = AsciiDocPage.fromFile(new File(path));
        String result = SwiftTypeMetadata.getDescription(page);
        assertTrue(result.equals("You can deploy an application to CloudHub directly from Anypoint™ Studio, or you can deploy it using the CloudHub console. This page covers deploym..."));
    }

    @Test
    public void getFirstParagraph_WithLongParagraph_ReturnsExpectedResult() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "v", "4.0", "index.ad"});
        AsciiDocPage page = AsciiDocPage.fromFile(new File(path));
        String result = SwiftTypeMetadata.getDescription(page);
        assertEquals(result, "Integrate SaaS applications, Cloud Services and Enterprise applications in real-time. CloudHub is the world’s first integration Platform as a Servi...");
    }

    @Test
    public void getTitleMetadata_ReturnsExpectedResult() {
        String result = SwiftTypeMetadata.getTitleMetadata(getValidAsciiDocPage());
        assertTrue(result.equals("<meta class=\"swiftype\" name=\"title\" data-type=\"string\" content=\"CloudHub\" />\n"));
    }

    @Test
    public void getTitleMetadata_WithLongTitle_ReturnsExpectedResult() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "deploying-a-cloudhub-application.ad"});
        AsciiDocPage page = AsciiDocPage.fromFile(new File(path));
        String result = SwiftTypeMetadata.getTitleMetadata(page);
        assertTrue(result.equals("<meta class=\"swiftype\" name=\"title\" data-type=\"string\" content=\"Deploying a CloudHub Application\" />\n"));
    }

    private AsciiDocPage getValidAsciiDocPage() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
        return page;
    }

    private File getValidFile() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "index.ad"});
        return new File(path);
    }
}
