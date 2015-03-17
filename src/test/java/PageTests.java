import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import java.io.*;
import java.util.*;

import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Created by sean.osterberg on 2/25/15.
 */
public class PageTests {

    @Test
    public void forSection_WithValidSectionAndTemplate_ReturnsNewInstance() {
        List<Page> pages = Page.forSection(validSection(), validSections(), validTemplate(), getSiteToc());
        assertThat(pages.get(0), instanceOf(Page.class));
    }

    @Test(expected = DocBuildException.class)
    public void forSection_WithInvalidSection_ThrowsException() {
        Section section = Section.fromDirectory(new File(Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "_templates"})));
        List<Page> pages = Page.forSection(section, validSections(), validTemplate(), getSiteToc());
    }

    @Test(expected = DocBuildException.class)
    public void forSection_WithInvalidTemplate_ThrowsException() {
        Template template = Template.fromFile(new File(Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "master-folder", "anypoint-platform", "cloudhub.ad"})));
        List<Page> pages = Page.forSection(validSection(), validSections(), template, getSiteToc());
    }

    @Test
    public void forSection_WithValidSectionAndTemplate_HasValidContent() {
        List<Page> pages = Page.forSection(validSection(), validSections(), validTemplate(), getSiteToc());
        assertTrue(pages.get(0).getContent().contains("CloudHub"));
    }

    private Section validSection() {
        return Section.fromDirectory(new File(Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "master-folder", "cloudhub"})));
    }

    private Template validTemplate() {
        return Template.fromFile(new File(Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "master-folder", "_templates", "default.template"})));
    }

    private List<Section> validSections() {
        List<Section> sections = new ArrayList<Section>();
        sections.add(Section.fromDirectory(new File(Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "master-folder", "cloudhub"}))));
        sections.add(Section.fromDirectory(new File(Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "master-folder", "anypoint-platform"}))));
        return sections;
    }

    private SiteTableOfContents getSiteToc() {
        return SiteTableOfContents.fromAsciiDocFile(new File(Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "master-folder", "toc.ad"})));
    }
}
