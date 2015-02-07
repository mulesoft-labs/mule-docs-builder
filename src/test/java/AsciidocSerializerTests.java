import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.*;

/**
 * Created by sean.osterberg on 2/6/15.
 */
public class AsciidocSerializerTests {

    /*
    @Test
    public void validAsciidocFileSuccessfullyGeneratesDocPage() {
        AsciidocSerializer serializer = new AsciidocSerializer();
        DocPage page = serializer.getConvertedHtmlForSingleAsciidocPage(getTestFile());

        assertTrue(!page.getContentHtml().isEmpty());
    }*/

    @Test
    public void validAsciidocTextSuccessfullyGeneratesDocPage() {
        AsciidocSerializer serializer = new AsciidocSerializer();
        String pageContent = serializer.getConvertedHtmlForAsciidocString("Writing AsciiDoc is _easy_!");

        assertTrue(!pageContent.isEmpty());
        assertTrue(TestHelpers.isValidHtml(pageContent));
    }

    @Test
    public void validAsciidocFilesSuccessfullyGenerateDocPages() {
        AsciidocSerializer serializer = new AsciidocSerializer();
        String pageContent = serializer.getConvertedHtmlForAsciidocString("Writing AsciiDoc is _easy_!");

        assertTrue(!pageContent.isEmpty());
        assertTrue(TestHelpers.isValidHtml(pageContent));
    }

    public File getTestFile() {
        String testAsciiDocFilePath = System.getProperty("user.dir") + "/src/test/test-resources/cloudhub.ad";
        System.out.println(testAsciiDocFilePath);
        File file = new File(testAsciiDocFilePath);
        return file;
    }

    /*
    public List<File> getTestFiles() {
        TestHelpers.getTestResourcesPath();
        Utilities.getFileContentsFromFiles()

        File file = new File(testAsciiDocFilePath);
        return file;
    }*/


}
