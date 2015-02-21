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
        String result = serializer.getConvertedHtmlForSingleAsciidocFile(getTestFile());
        assertFalse(result.isEmpty());
        assertTrue(TestHelpers.isValidHtml(result));
    }

    @Test
    public void getConvertedHtmlForAsciidocFiles_WithValidAsciidocFiles_CreatesStrings() {
        File directory = new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "cloudhub", "v", "4.0" }));
        List<String> result = serializer.getConvertedHtmlForManyAsciidocFiles(Arrays.asList(directory.listFiles()));
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

    public File getTestFile() {
        File file = new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "cloudhub", "cloudhub.ad" }));
        return file;
    }

    public File[] getFilesFromTestDirectory() {
        File directory = new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "cloudhub" }));
        return directory.listFiles();
    }
}
