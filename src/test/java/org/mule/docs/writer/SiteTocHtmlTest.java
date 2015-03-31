package org.mule.docs.writer;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.mule.docs.model.AsciiDocPage;
import org.mule.docs.model.Section;
import org.mule.docs.model.SiteTableOfContents;
import org.mule.docs.model.SiteTocHtml;
import org.mule.docs.utils.TestUtilities;
import org.mule.docs.utils.Utilities;

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
        assertTrue(html.contains("<li class=\"child active\">Deploying a CloudHub Application</li>"));
    }

    @Test
    public void getTocHtmlForSectionAndPage_WithOrphanedPage_ReturnsUnselectedHtml() {
        SiteTocHtml siteToc = SiteTocHtml.fromSiteTocAndSections(getValidTocFile(), getValidSections());
        String html = siteToc.getTocHtmlForSectionAndPage(getValidSection(), getOrphanedAsciiDocPage());
        assertFalse(StringUtils.isBlank(html));
        assertTrue(html.length() > 500);
        assertFalse(html.contains("Testing123"));
    }

    @Test
    public void getTocHtmlForSectionAndPage_ForOldVersion_ReturnsValidHtml() {
        SiteTocHtml siteToc = SiteTocHtml.fromSiteTocAndSections(getValidTocFile(), getValidSections());
        String html = siteToc.getTocHtmlForSectionAndPage(getOldVersionSection(), getValidOldAsciiDocPage());
        assertFalse(StringUtils.isBlank(html));
        assertTrue(html.length() > 500);
        assertTrue(html.contains("<a href=\"/docs/cloudhub/v/4.0/cloudhub\">CloudHub</a>"));
        assertTrue(html.contains("<li class=\"child active\">Deploying a CloudHub Application</li>"));
    }

    public SiteTableOfContents getValidTocFile() {
        String tocPath = Utilities.getConcatPath( TestUtilities.getPathToMasterFolder(), "toc.ad" );
        return SiteTableOfContents.fromAsciiDocFile(new File(tocPath));
    }

    private List<Section> getValidSections() {
        List<Section> sections = new ArrayList<Section>();
        File section1 = new File(Utilities.getConcatPath( TestUtilities.getPathToMasterFolder(), "cloudhub" ));
        File section2 = new File(Utilities.getConcatPath( TestUtilities.getPathToMasterFolder(), "anypoint-platform" ));
        sections.add(Section.fromDirectory(section1));
        sections.add(Section.fromDirectory(section2));
        return sections;
    }

    private Section getValidSection() {
        return Section.fromDirectory(new File(Utilities.getConcatPath( TestUtilities.getPathToMasterFolder(), "cloudhub" )));
    }

    private Section getOldVersionSection() {
        return Section.fromDirectory(new File(Utilities.getConcatPath( TestUtilities.getPathToMasterFolder(), "cloudhub", "v", "4.0" )));
    }

    private AsciiDocPage getValidAsciiDocPage() {
        return AsciiDocPage.fromFile(new File(Utilities.getConcatPath( TestUtilities.getPathToMasterFolder(), "cloudhub", "deploying-a-cloudhub-application.ad" )));
    }

    private AsciiDocPage getValidOldAsciiDocPage() {
        return AsciiDocPage
                .fromFile(new File(Utilities.getConcatPath( TestUtilities.getPathToMasterFolder(), "cloudhub", "v", "4.0", "deploying-a-cloudhub-application.ad" )));
    }

    private AsciiDocPage getOrphanedAsciiDocPage() {
        return AsciiDocPage.fromFile(new File(Utilities.getConcatPath( TestUtilities.getPathToMasterFolder(), "cloudhub", "index.ad" )));
    }

}
