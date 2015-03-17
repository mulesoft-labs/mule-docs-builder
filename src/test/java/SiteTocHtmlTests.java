import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Created by sean.osterberg on 2/22/15.
 */
public class SiteTocHtmlTests {

    @Test
    public void fromSiteTocAndSections_WithValidParams_CreatesInstance() {
        SiteTocHtml siteToc = SiteTocHtml.fromSiteTocAndSections(getValidTocFile(), getValidSections());
        assertThat(siteToc, instanceOf(SiteTocHtml.class));
    }

    @Test
    public void getTocHtmlForSectionAndPage_WithValidSectionAndPage_ReturnsValidHtml() {
        SiteTocHtml siteToc = SiteTocHtml.fromSiteTocAndSections(getValidTocFile(), getValidSections());
        String html = siteToc.getTocHtmlForSectionAndPage(getValidSection(), getValidAsciiDocPage());
        assertFalse(Utilities.isStringNullOrEmptyOrWhitespace(html));
        assertTrue(html.length() > 500);
        assertTrue(html.contains("<li class=\"child active\">Deploying a CloudHub Application</li>"));
    }

    @Test
    public void getTocHtmlForSectionAndPage_WithOrphanedPage_ReturnsUnselectedHtml() {
        SiteTocHtml siteToc = SiteTocHtml.fromSiteTocAndSections(getValidTocFile(), getValidSections());
        String html = siteToc.getTocHtmlForSectionAndPage(getValidSection(), getOrphanedAsciiDocPage());
        assertFalse(Utilities.isStringNullOrEmptyOrWhitespace(html));
        assertTrue(html.length() > 500);
        assertFalse(html.contains("Testing123"));
    }

    public SiteTableOfContents getValidTocFile() {
        String tocPath = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "toc.ad"});
        return SiteTableOfContents.fromAsciiDocFile(new File(tocPath));
    }

    private List<Section> getValidSections() {
        List<Section> sections = new ArrayList<Section>();
        File section1 = new File(Utilities.getConcatPath(new String[] {TestUtilities.getPathToMasterFolder(), "cloudhub"}));
        File section2 = new File(Utilities.getConcatPath(new String[] {TestUtilities.getPathToMasterFolder(), "anypoint-platform"}));
        sections.add(Section.fromDirectory(section1));
        sections.add(Section.fromDirectory(section2));
        return sections;
    }

    private Section getValidSection() {
        return Section.fromDirectory(new File(Utilities.getConcatPath(new String[] {TestUtilities.getPathToMasterFolder(), "cloudhub"})));
    }

    private AsciiDocPage getValidAsciiDocPage() {
        return AsciiDocPage.fromFile(new File(Utilities.getConcatPath(new String[] {TestUtilities.getPathToMasterFolder(), "cloudhub", "deploying-a-cloudhub-application.ad"})));
    }

    private AsciiDocPage getOrphanedAsciiDocPage() {
        return AsciiDocPage.fromFile(new File(Utilities.getConcatPath(new String[] {TestUtilities.getPathToMasterFolder(), "cloudhub", "index.ad"})));
    }

}
