package org.mule.docs;

import org.apache.commons.lang3.StringUtils;

import org.junit.Test;
import org.mule.docs.util.Utilities;

import static org.junit.Assert.*;

import java.io.File;
import java.util.*;

/**
 * Created by sean.osterberg on 2/20/15.
 */
public class AsciiDocPageTest {

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
        for (AsciiDocPage page : pages) {
            assertFalse(StringUtils.isBlank(page.getFilePath()));
            assertFalse(StringUtils.isBlank(page.getAsciiDoc()));
        }
    }

    @Test
    public void getPageFromFile_ReturnsPage() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
        assertNotNull(page);
    }

    @Test(expected = DocBuildException.class)
    public void getInvalidPageFromFile_ThrowsException() {
        AsciiDocPage page = AsciiDocPage.fromFile(getInvalidFile());
    }

    @Test
    public void getPageFromFile_ReturnsNonEmptyPage() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
        assertFalse(StringUtils.isBlank(page.getFilePath()));
        assertFalse(StringUtils.isBlank(page.getAsciiDoc()));
    }

    @Test
    public void getAttributes_ReturnsExpectedAttributes() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
        Map<String, Object> attributes = page.getAttributes();
        assertTrue(attributes.containsKey("test-attribute"));
        assertTrue(attributes.containsValue("foo"));
    }

    @Test
    public void containsAttribute_ForExistingAttribute_ReturnsTrue() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
        assertTrue(page.containsAttribute("test-attribute"));
    }

    @Test
    public void containsAttribute_ForInvalidAttribute_ReturnsFalse() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
        assertFalse(page.containsAttribute("foo-attribute"));
    }

    @Test
    public void getAttributeValue_ForValidAttribute_ReturnsValue() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
        assertTrue(page.getAttributeValue("test-attribute").equals("foo"));
    }

    @Test
    public void getAttributeValue_ForInvalidAttribute_ReturnsNull() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
        assertNull(page.getAttributeValue("foo-attribute"));
    }

    private File getValidFile() {
        String path = Utilities.getConcatPath(new String[] { TestUtilities.getPathToMasterFolder(), "cloudhub", "index.adoc" });
        return new File(path);
    }

    private List<File> getValidFiles() {
        List<File> files = new ArrayList<File>();
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub"});
        File cloudhubDir = new File(path);
        for (File file : cloudhubDir.listFiles()) {
            if (Utilities.fileEndsWithValidAsciidocExtension(file.getName())) {
                files.add(file);
            }
        }
        return files;
    }

    private File getInvalidFile() {
        String fooPath = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "foo.adoc"});
        return new File(fooPath);
    }

    private List<File> getInvalidFiles() {
        List<File> files = new ArrayList<File>();
        String fooPath = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "foo.adoc"});
        files.add(new File(fooPath));
        String barPath = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "bar.adoc"});
        files.add(new File(barPath));
        return files;
    }
}
