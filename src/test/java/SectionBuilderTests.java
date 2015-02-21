import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

/**
 * Created by sean.osterberg on 2/16/15.
 */
public class SectionBuilderTests {


    @Test
    public void getDocSectionFromDirectory_WithValidDirectoryAndCurrentVersion_ReturnsValidSection() {
        SectionBuilder builder = getDefaultSectionBuilder();
        DocSection section = builder.getDocSectionFromDirectory(getAsciidocFilesValidPathAsFile(), "Current");
        assertTrue(section.getRootNodeForToc().getTitle().contentEquals("CloudHub"));
        assertTrue(section.getVersionName().contentEquals("Current"));
        assertTrue(section.getTocHtmlForInactiveSection().length() > 200);
    }

    @Test(expected = DocBuilderException.class)
    public void getDocSectionFromDirectory_WithInvalidDirectory_ThrowsException() {
        SectionBuilder builder = getDefaultSectionBuilder();
        DocSection section = builder.getDocSectionFromDirectory(new File("blah"), "Current");
    }

    @Test(expected = DocBuilderException.class)
    public void getDocSectionFromDirectory_WithEmptyVersion_ThrowsException() {
        SectionBuilder builder = getDefaultSectionBuilder();
        DocSection section = builder.getDocSectionFromDirectory(getAsciidocFilesValidPathAsFile(), "");
    }

    @Test(expected = DocBuilderException.class)
    public void getDocSectionFromDirectory_WithNullVersion_ThrowsException() {
        SectionBuilder builder = getDefaultSectionBuilder();
        DocSection section = builder.getDocSectionFromDirectory(getAsciidocFilesValidPathAsFile(), null);
    }

    @Test(expected = DocBuilderException.class)
    public void getDocSectionFromDirectory_WithEmptyDirectory_ThrowsException() {
        SectionBuilder builder = getDefaultSectionBuilder();
        File file = new File(TestHelpers.getPathForTestResourcesFile(new String[] { "empty" }));
        DocSection section = builder.getDocSectionFromDirectory(file, null);
    }

    /*
    @Test
    public void getCurrentDocSectionFromDirectory_WithValidDirectory_ReturnsValidSection() {
        SectionBuilder builder = getDefaultSectionBuilder();
        DocSection section = builder.getCurrentDocSectionFromDirectory(getAsciidocFilesValidPathAsFile());

        validateStandardDocSectionAttributes(section);
    }

    @Test(expected = DocBuilderException.class)
    public void getCurrentDocSectionFromDirectory_WithInvalidDirectory_ThrowsException() {
        SectionBuilder builder = getDefaultSectionBuilder();
        DocSection section = builder.getCurrentDocSectionFromDirectory(new File("foo"));
    }*/

    @Test
    public void getDocSectionsFromListOfDirectories_WithValidDirectory_ReturnsValidSections() {
        List<File> files = new ArrayList<File>();
        files.add(getAsciidocFilesValidPathAsFile());
        files.add(new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "cloudhub", "v", "4.0"})));
        SectionBuilder builder = getDefaultSectionBuilder();
        List<DocSection> docSections = builder.getDocSectionsFromListOfDirectories(files);

        validateStandardDocSectionAttributes(docSections.get(0));
        assertTrue(docSections.get(1).getRootNodeForToc().getTitle().contentEquals("CloudHub"));
        assertTrue(docSections.get(1).getVersionName().contentEquals("Current"));
        assertTrue(docSections.get(1).getTocHtmlForInactiveSection().length() > 300);
        assertTrue(docSections.get(1).getOtherVersions().size() == 0);
    }

    private File getAsciidocFilesValidPathAsFile() {
        return new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "cloudhub" }));
    }

    private void validateStandardDocSectionAttributes(DocSection section) {
        assertTrue(section.getRootNodeForToc().getTitle().contentEquals("CloudHub"));
        assertTrue(section.getVersionName().contentEquals("Current"));
        assertTrue(section.getTocHtmlForInactiveSection().length() > 300);
        assertTrue(section.getOtherVersions().size() == 1);
    }

    private SectionBuilder getDefaultSectionBuilder() {
        String sourceDir = TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid" });
        String outputDir = TestHelpers.getPathForTestResourcesFile(new String[] { "output" });
        SiteBuilder siteBuilder = new SiteBuilder(sourceDir, outputDir, "");
        List<PageTemplate> templates = siteBuilder.getPageTemplates();
        SectionBuilder sectionBuilder = new SectionBuilder(templates, "");
        return sectionBuilder;
    }
}
