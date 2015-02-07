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
        File directory = new File(TestHelpers.getTestResourcesPath() + "/asciidoc-files-valid");
        List<String> result = serializer.getConvertedHtmlForAsciidocFiles(Arrays.asList(directory.listFiles()));
        assertTrue(!result.isEmpty());
        assertTrue(result.size() == 2);
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
        List<File> asciidocFiles = getFilesFromTestDirectory();
        List<DocPage> docPages = serializer.convertAndGetDocPagesFromAsciidocFiles(asciidocFiles);

        DocPage pageOne = docPages.get(0);
        DocPage pageTwo = docPages.get(1);

        assertFalse(pageOne.getContentHtml().isEmpty());
        assertFalse(pageOne.getAsciidocSource().isEmpty());
        assertTrue(pageOne.getTitle().contentEquals("CloudHub"));
        assertTrue(pageOne.getSourceFilePath().contentEquals(TestHelpers.getTestResourcesPath() + "asciidoc-files-valid/" + pageOne.getFilename() + ".ad"));
        assertTrue(pageOne.getFilename().contentEquals("cloudhub"));

        assertFalse(pageTwo.getContentHtml().isEmpty());
        assertFalse(pageTwo.getAsciidocSource().isEmpty());
        assertTrue(pageTwo.getTitle().contentEquals("Deploying a CloudHub Application"));
        assertTrue(pageTwo.getSourceFilePath().contentEquals(TestHelpers.getTestResourcesPath() + "asciidoc-files-valid/" + pageTwo.getFilename() + ".ad"));
        assertTrue(pageTwo.getFilename().contentEquals("deploying-a-cloudhub-application"));
    }

    public File getTestFile() {
        File file = new File(TestHelpers.getTestResourcesPath() + "/asciidoc-files-valid/cloudhub.ad");
        return file;
    }

    public List<File> getFilesFromTestDirectory() {
        File directory = new File(TestHelpers.getTestResourcesPath() + "/asciidoc-files-valid");
        return Arrays.asList(directory.listFiles());
    }
}
