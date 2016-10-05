package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.model.SectionVersion;
import com.mulesoft.documentation.builder.util.Utilities;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean.osterberg on 7/5/15.
 */
public class SwiftypeMetadataTest {

    @Test
    public void getDescription_ReturnsExpectedResult() {
        String result = SwiftypeMetadata.getDescription(getValidAsciiDocPage());
        assertEquals(result, "Integrate SaaS applications, Cloud Services and Enterprise applications in real-time. CloudHub is the world’s first integration Platform as a Servi...");
    }

    @Test
    public void getFirstParagraph_EndsWithEllipses() {
        String result = SwiftypeMetadata.getDescription(getValidAsciiDocPage());
        assertTrue(result.endsWith("..."));
    }

    @Test
    public void getFirstParagraph_WithFewerThan147Chars_ReturnsExpectedResult() {
        String result = SwiftypeMetadata.getDescription(getValidAsciiDocPage());
        assertTrue(result.endsWith("..."));
    }

    @Test
    public void getFirstParagraph_WithAdmonition_ReturnsTextFromAdmonition() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "v", "4.0", "deploying-a-cloudhub-application.adoc"});
        AsciiDocPage page = AsciiDocPage.fromFile(new File(path));
        String result = SwiftypeMetadata.getDescription(page);
        assertTrue(result.equals("To deploy a CloudHub application, you simply upload it to CloudHub and the platform automatically deploys your application.  You can deploy an..."));
    }

    @Test
    public void getFirstParagraph_WithLongParagraph_ReturnsExpectedResult() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "v", "4.0", "index.adoc"});
        AsciiDocPage page = AsciiDocPage.fromFile(new File(path));
        String result = SwiftypeMetadata.getDescription(page);
        assertEquals(result, "Integrate SaaS applications, Cloud Services and Enterprise applications in real-time. CloudHub is the world’s first integration Platform as a Servi...");
    }

    @Test
    public void getTitleMetadata_ReturnsExpectedResult() {
        String result = SwiftypeMetadata.getTitleMetadata(getValidAsciiDocPage());
        assertTrue(result.equals("<meta class=\"swiftype\" name=\"title\" data-type=\"string\" content=\"CloudHub\" />"));
    }

    @Test
    public void getTitleMetadata_WithLongTitle_ReturnsExpectedResult() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "v", "4.0", "deploying-a-cloudhub-application.adoc"});
        AsciiDocPage page = AsciiDocPage.fromFile(new File(path));
        String result = SwiftypeMetadata.getTitleMetadata(page);
        assertTrue(result.equals("<meta class=\"swiftype\" name=\"title\" data-type=\"string\" content=\"Deploying a CloudHub Application\" />"));
    }
    
    @Test
    public void getVersionMetadata_ReturnsExpectedResult() {
        String result = SwiftypeMetadata.getVersionMetadata(getValidSection());
        assertTrue(result.equals("<meta class=\"swiftype\" name=\"version\" data-type=\"string\" content=\"4.0\" />"));
    }
    
    private Section getValidSection() {
        SectionVersion version = new SectionVersion("Anypoint Platform for APIs", "cloudhub", "/cloudhub/v/4.0", "4.0", true);
        List<AsciiDocPage> pages = new ArrayList<>();
        pages.add(getValidAsciiDocPage());
        return new Section(pages, null, getValidFile().getParent(), version.getVersionUrl(), version.getSectionPrettyName(), version.getVersionName(), version.getSectionBaseName(), version);
    }

    private AsciiDocPage getValidAsciiDocPage() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
        return page;
    }

    private File getValidFile() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "v", "4.0", "index.adoc"});
        return new File(path);
    }
}
