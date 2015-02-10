import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;

/**
 * Created by sean.osterberg on 2/6/15.
 */
public class AsciidocSerializerTests {
    private static AsciidocSerializer serializer;

    @BeforeClass
    public static void initializeTests() {
        serializer = new AsciidocSerializer();
    }

    @Test
    public void getConvertedHtmlForSingleAsciidocPage_WithValidAsciidocFile_CreatesString() {
        String result = serializer.getConvertedHtmlForSingleAsciidocPage(getTestFile());
        assertFalse(result.isEmpty());
        assertTrue(TestHelpers.isValidHtml(result));
    }

    @Test
    public void getConvertedHtmlForAsciidocFiles_WithValidAsciidocFiles_CreatesStrings() {
        File directory = new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid" }));
        List<String> result = serializer.getConvertedHtmlForAsciidocFiles(Arrays.asList(directory.listFiles()));
        assertTrue(!result.isEmpty());
        assertTrue(result.size() == 3);
        for(String s : result) {
            assertTrue(TestHelpers.isValidHtml(s));
        }
    }

    @Test
    public void getConvertedHtmlForAsciidocString_WithValidAsciidocText_CreatesString() {
        String pageContent = serializer.getConvertedHtmlForAsciidocString("Writing AsciiDoc is _easy_!");
        assertFalse(pageContent.isEmpty());
        assertTrue(TestHelpers.isValidHtml(pageContent));
    }

    @Test
    public void convertAndGetDocPagesFromAsciidocFiles_WithValidAsciiDocFiles_CreatesDocPages() {
        File[] asciidocFiles = getFilesFromTestDirectory();
        List<DocPage> docPages = serializer.getConvertedDocPagesFromAsciidocFiles(asciidocFiles, true);

        DocPage pageOne = docPages.get(0);
        DocPage pageTwo = docPages.get(1);

        assertFalse(pageOne.getContentHtml().isEmpty());
        assertFalse(pageOne.getAsciidocSource().isEmpty());
        assertTrue(pageOne.getTitle().contentEquals("CloudHub"));
        assertTrue(pageOne.getSourceFilePath().contentEquals(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", pageOne.getFilename()})));
        assertTrue(pageOne.getFilename().contentEquals("cloudhub.ad"));

        assertFalse(pageTwo.getContentHtml().isEmpty());
        assertFalse(pageTwo.getAsciidocSource().isEmpty());
        assertTrue(pageTwo.getTitle().contentEquals("Deploying a CloudHub Application"));
        assertTrue(pageTwo.getSourceFilePath().contentEquals(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", pageTwo.getFilename()})));
        assertTrue(pageTwo.getFilename().contentEquals("deploying-a-cloudhub-application.ad"));
    }

    @Test
    public void getConvertedDocPagesFromAsciidocFiles_WithTocFile_ReturnsListWithoutTocFile() {
        File[] asciidocFiles = getFilesFromTestDirectory();
        List<DocPage> docPages = serializer.getConvertedDocPagesFromAsciidocFiles(asciidocFiles, true);
        assertTrue(docPages.size() == 2);
        assertTrue(docPages.get(0).getTitle().contentEquals("CloudHub"));
        assertTrue(docPages.get(1).getTitle().contentEquals("Deploying a CloudHub Application"));
    }

    @Test
    public void getConvertedDocPagesFromAsciidocFiles_WithoutTocFileExcluded_ReturnsListWithTocFile() {
        File[] asciidocFiles = getFilesFromTestDirectory();
        List<DocPage> docPages = serializer.getConvertedDocPagesFromAsciidocFiles(asciidocFiles, false);
        assertTrue(docPages.size() == 3);
        assertTrue(docPages.get(0).getTitle().contentEquals("CloudHub"));
        assertTrue(docPages.get(1).getTitle().contentEquals("Deploying a CloudHub Application"));
    }

    @Test
    public void getConvertedDocPageFromAsciidocFile_WithValidAsciiDocFile_CreatesDocPage() {
        File asciidocFile = getTestFile();
        DocPage docPage = serializer.getConvertedDocPageFromAsciidocFile(asciidocFile);

        assertFalse(docPage.getContentHtml().isEmpty());
        assertFalse(docPage.getAsciidocSource().isEmpty());
        assertTrue(docPage.getTitle().contentEquals("CloudHub"));
        assertTrue(docPage.getSourceFilePath().contentEquals(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", docPage.getFilename()})));
        assertTrue(docPage.getFilename().contentEquals("cloudhub.ad"));
    }

    public File getTestFile() {
        File file = new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "cloudhub.ad" }));
        return file;
    }

    public File[] getFilesFromTestDirectory() {
        File directory = new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid" }));
        return directory.listFiles();
    }
}
