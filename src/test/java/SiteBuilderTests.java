import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sean.osterberg on 2/9/15.
 */
public class SiteBuilderTests {

    @AfterClass
    public static void tearDown() {
        File outputDirectory = new File(TestHelpers.getTestResourcesPath() + "/output");
        for(File file : outputDirectory.listFiles()) {
            file.delete();
        }
    }

    @Test
    public void getPageTemplates_WithValidTemplatesDirectoryAndOneTemplate_ReturnsTemplate() {
        SiteBuilder builder = getDefaultSiteBuilder();

        assertTrue(builder.getPageTemplates().size() == 1);
        assertTrue(builder.getPageTemplates().get(0).getTemplateType().equals(PageTemplateType.DEFAULT));
    }

    @Test
    public void getPageTemplates_WithValidTemplatesDirectoryAndTwoTemplates_ReturnsTemplates() {
        String templatesDir = TestHelpers.getPathForTestResourcesFile(new String[]{"asciidoc-files-valid", "_templates"});
        Utilities.writeFileToDirectory(Utilities.getConcatenatedFilepath(new String[] { templatesDir, "landing_page.html"}), "blah blah");
        SiteBuilder builder = getDefaultSiteBuilder();

        assertTrue(builder.getPageTemplates().size() == 2);
        assertTrue(builder.getPageTemplates().get(0).getTemplateType().equals(PageTemplateType.DEFAULT));
        assertTrue(builder.getPageTemplates().get(1).getTemplateType().equals(PageTemplateType.LANDING_PAGE));
        File tempFile = new File(Utilities.getConcatenatedFilepath(new String[]{templatesDir, "landing_page.html"}));
        tempFile.delete();
    }

    @Test(expected = DocBuilderException.class)
    public void getPageTemplates_WithInvalidTemplatesDirectory_ThrowsException() {
        SiteBuilder builder = new SiteBuilder("blah", "blah", "");
    }

    @Test
    public void setFinalHtmlContentForAllSectionsAndVersions_WithValidSourceDirectory_ReturnsValidSections() {
        SiteBuilder builder = getDefaultSiteBuilder();
        SectionBuilder sectionBuilder = new SectionBuilder(builder.getPageTemplates(), "");
        List<File> directories = new ArrayList(Arrays.asList(new File(TestHelpers.getPathForTestResourcesFile(new String[]{"asciidoc-files-valid"})).listFiles()));
        for(int i = 0; i < directories.size(); i++) {
            if(directories.get(i).getName().startsWith("_")) {
                directories.remove(i);
            }
        }
        List<DocSection> sections = sectionBuilder.getDocSectionsFromListOfDirectories(directories);
        builder.setFinalHtmlContentForAllSectionsAndVersions(sections);
        assertTrue(sections.get(0).getPagesInSection().get(0).getFinalTocHtml().contains("aU2otg3oa7"));
    }

    @Test
    public void updateAllVersionsOfPagesWithCompleteToc_WithValidSections_ReturnsValidCompleteToc() {
        SiteBuilder builder = getDefaultSiteBuilder();
        List<DocSection> sections = getDocSections(builder);
        builder.updateAllVersionsOfPagesWithCompleteToc(sections);
        builder.setFinalHtmlContentForAllSectionsAndVersions(sections);
        assertTrue(sections.get(0).getPagesInSection().get(0).getFinalTocHtml().contains("aU2otg3oa7"));
    }

    private SiteBuilder getDefaultSiteBuilder() {
        String sourceDir = TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid" });
        String outputDir = TestHelpers.getPathForTestResourcesFile(new String[] { "output" });
        SiteBuilder siteBuilder = new SiteBuilder(sourceDir, outputDir, "");
        return siteBuilder;
    }

    private List<DocSection> getDocSections(SiteBuilder builder) {
        SectionBuilder sectionBuilder = new SectionBuilder(builder.getPageTemplates(), "");
        List<File> directories = new ArrayList(Arrays.asList(new File(TestHelpers.getPathForTestResourcesFile(new String[]{"asciidoc-files-valid"})).listFiles()));
        for(int i = 0; i < directories.size(); i++) {
            if(directories.get(i).getName().startsWith("_")) {
                directories.remove(i);
            }
        }
        return sectionBuilder.getDocSectionsFromListOfDirectories(directories);
    }
}
