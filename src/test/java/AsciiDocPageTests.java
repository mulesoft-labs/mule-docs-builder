import org.asciidoctor.ast.ContentPart;
import org.asciidoctor.ast.Document;
import org.asciidoctor.ast.StructuredDocument;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.*;

/**
 * Created by sean.osterberg on 2/20/15.
 */
public class AsciiDocPageTests {

    @Test
    public void getPagesFromFiles_ReturnsListOfPages() {
        List<AsciiDocPage> pages = AsciiDocPage.fromFiles(getValidFiles());
        assertTrue(pages.size() > 0);
    }

    @Test(expected = DocBuildException.class)
    public void getInvalidPagesFromFiles_ThrowsException() {
        List<AsciiDocPage> pages = AsciiDocPage.fromFiles(getInvalidFiles());
        assertTrue(pages.size() > 0);
    }

    @Test
    public void getPagesFromFiles_ReturnsNonEmptyPages() {
        List<AsciiDocPage> pages = AsciiDocPage.fromFiles(getValidFiles());
        for(AsciiDocPage page : pages) {
            assertTrue(!Utilities.isStringNullOrEmptyOrWhitespace(page.getFilename()));
            assertTrue(!Utilities.isStringNullOrEmptyOrWhitespace(page.getAsciiDoc()));
        }
    }

    @Test
    public void getPageFromFile_ReturnsPage() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
        assertTrue(page != null);
    }

    @Test(expected = DocBuildException.class)
    public void getInvalidPageFromFile_ThrowsException() {
        AsciiDocPage page = AsciiDocPage.fromFile(getInvalidFile());
    }

    @Test
    public void getPageFromFile_ReturnsNonEmptyPage() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
        assertTrue(!Utilities.isStringNullOrEmptyOrWhitespace(page.getFilename()));
        assertTrue(!Utilities.isStringNullOrEmptyOrWhitespace(page.getAsciiDoc()));
    }

    private File getValidFile() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "cloudhub.ad"});
        return new File(path);
    }

    private List<File> getValidFiles() {
        List<File> files = new ArrayList<File>();
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub"});
        File cloudhubDir = new File(path);
        for(File file : cloudhubDir.listFiles()) {
            if(Utilities.fileEndsWithValidAsciidocExtension(file.getName())) {
                files.add(file);
            }
        }
        return files;
    }

    private File getInvalidFile() {
        String fooPath = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "foo.ad"});
        return new File(fooPath);
    }

    private List<File> getInvalidFiles() {
        List<File> files = new ArrayList<File>();
        String fooPath = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "foo.ad"});
        files.add(new File(fooPath));
        String barPath = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "bar.ad"});
        files.add(new File(barPath));
        return files;
    }
}
