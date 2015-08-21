package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.model.SectionVersion;
import com.mulesoft.documentation.builder.util.Utilities;
import org.apache.commons.lang3.StringUtils;

import org.junit.Test;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Created by sean.osterberg on 2/22/15.
 */
public class SiteTocHtmlTest {

    @Test
    public void fromSiteTocAndSections_WithValidParams_CreatesInstance() {
        SiteTocHtml siteToc = SiteTocHtml.fromSiteTocAndSections(getValidTocFile(), getValidSections());
        assertThat(siteToc, instanceOf(SiteTocHtml.class));
    }

    @Test
    public void getTocHtmlForSectionAndPage_WithValidSectionAndPage_ReturnsValidHtml() {
        SiteTocHtml siteToc = SiteTocHtml.fromSiteTocAndSections(getValidTocFile(), getValidSections());
        String html = siteToc.getTocHtmlForSectionAndPage(getValidSection(), getValidAsciiDocPage());
        assertFalse(StringUtils.isBlank(html));
        assertTrue(html.length() > 500);
        assertTrue(html.contains("<li class=\"active\"><i></i><a href=\"/cloudhub/deploying-a-cloudhub-application\">Deploying a CloudHub Application</a>"));
    }

    @Test
    public void getTocHtmlForSectionAndPage_WithOrphanedPage_ReturnsUnselectedHtml() {
        SiteTocHtml siteToc = SiteTocHtml.fromSiteTocAndSections(getValidTocFile(), getValidSections());
        String html = siteToc.getTocHtmlForSectionAndPage(getValidSection(), getOrphanedAsciiDocPage());
        assertFalse(StringUtils.isBlank(html));
        assertTrue(html.length() > 500);
        assertFalse(html.contains("Testing123"));
    }

    /*
    @Test
    public void getTocHtmlForSectionAndPage_ForOldVersion_ReturnsValidHtml() {
        SiteTocHtml siteToc = SiteTocHtml.fromSiteTocAndSections(getValidTocFile(), getValidSections());
        String html = siteToc.getTocHtmlForSectionAndPage(getOldVersionSection(), getValidOldAsciiDocPage());
        assertFalse(StringUtils.isBlank(html));
        assertTrue(html.length() > 500);
        assertTrue(html.contains("<a href=\"/cloudhub/v/4.0/\">CloudHub</a>"));
        assertTrue(html.contains("<li class=\"active\"><i></i><a href=\"/cloudhub/v/4.0/deploying-a-cloudhub-application\">Deploying a CloudHub Application</a>"));
    }*/

    public SiteTableOfContents getValidTocFile() {
        String tocPath = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "_toc.adoc"});
        return SiteTableOfContents.fromAsciiDocFile(new File(tocPath));
    }

    private List<Section> getValidSections() {
        List<Section> sections = new ArrayList<Section>();
        File section1 = new File(Utilities.getConcatPath(new String[] {TestUtilities.getPathToMasterFolder(), "cloudhub", "v", "4.0"}));
        sections.add(Section.fromDirectoryAndSectionVersion(section1, getValidSectionVersion()));
        return sections;
    }

    private Section getValidSection() {
        return Section.fromDirectoryAndSectionVersion(
                new File(Utilities.getConcatPath(new String[] { TestUtilities.getPathToMasterFolder(), "cloudhub", "v", "4.0"})),
                getValidSectionVersion());
    }

    private SectionVersion getValidSectionVersion() {
        return new SectionVersion("CloudHub", "cloudhub", "cloudhub/v/", "latest", true);
    }


    private AsciiDocPage getValidAsciiDocPage() {
        return AsciiDocPage.fromFile(new File(Utilities.getConcatPath(new String[] {TestUtilities.getPathToMasterFolder(), "cloudhub", "v", "4.0", "deploying-a-cloudhub-application.adoc"})));
    }

    private AsciiDocPage getValidOldAsciiDocPage() {
        return AsciiDocPage.fromFile(new File(Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "v", "4.0", "deploying-a-cloudhub-application.adoc"})));
    }

    private AsciiDocPage getOrphanedAsciiDocPage() {
        return AsciiDocPage.fromFile(new File(Utilities.getConcatPath(new String[] {TestUtilities.getPathToMasterFolder(), "cloudhub", "v", "4.0", "index.adoc"})));
    }

}
