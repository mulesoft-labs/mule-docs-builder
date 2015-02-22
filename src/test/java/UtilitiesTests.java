import org.asciidoctor.ast.Section;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sun.jvm.hotspot.debugger.Page;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sean.osterberg on 2/20/15.
 */
public class UtilitiesTests {

    @Test
    public void validateAsciiDocFile_IsValidForAsciiDocExtension() {
        Utilities.validateAsciiDocFile(new File(Utilities.getConcatPath(
                new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "cloudhub.ad"})));
        assertTrue(true);
    }

    @Test(expected = DocBuildException.class)
    public void validateAsciiDocFile_ThrowsExceptionForNonExistentFile() {
        Utilities.validateAsciiDocFile(new File(Utilities.getConcatPath(
                new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "blah"})));
        assertTrue(true);
    }

    @Test(expected = DocBuildException.class)
    public void validateAsciiDocFile_ThrowsExceptionForBadExtension() {
        Utilities.validateAsciiDocFile(new File(Utilities.getConcatPath(
                new String[]{TestUtilities.getPathToMasterFolder(), "_templates", "default.html"})));
        assertTrue(true);
    }

    @Test
    public void fileEndsWithValidAsciidocExtension_ReturnsFalseWithInvalidExtension() {
        boolean result = Utilities.fileEndsWithValidAsciidocExtension(Utilities.getConcatPath(
                new String[]{TestUtilities.getPathToMasterFolder(), "_templates", "default.html"}));
        assertFalse(result);
    }

    @Test
    public void fileEndsWithValidAsciidocExtension_ReturnsTrueWithValidExtension() {
        boolean result = Utilities.fileEndsWithValidAsciidocExtension(Utilities.getConcatPath(
                new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "cloudhub.ad"}));
        assertTrue(result);
    }

    @Test
    public void getConcatenatedFilepath_WithTwoDirectories_IsValid() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub"});
        assertTrue(path.contentEquals("/Users/sean.osterberg/mulesoft-docs/dev/src/test/resources/master-folder/cloudhub"));
    }

    @Test
    public void getConcatenatedFilepath_WithTwoDirectoriesAndFilename_IsValid() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "cloudhub.ad"});
        assertTrue(path.contentEquals("/Users/sean.osterberg/mulesoft-docs/dev/src/test/resources/master-folder/cloudhub/cloudhub.ad"));
    }

    @Test
    public void getConcatenatedFilepath_WithDirectoryAndFilenameAndAnotherDirectory_IsValid() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "cloudhub.ad", "cloudhub"});
        assertTrue(path.contentEquals("/Users/sean.osterberg/mulesoft-docs/dev/src/test/resources/master-folder/cloudhub/cloudhub.ad/cloudhub"));
    }

    @Test
    public void isStringNullOrEmptyOrWhitespace_WithNullString_ReturnsTrue() {
        String foo = null;
        boolean result = Utilities.isStringNullOrEmptyOrWhitespace(foo);
        assertTrue(result);
    }

    @Test
    public void isStringNullOrEmptyOrWhitespace_WithEmptyString_ReturnsTrue() {
        String foo = "";
        boolean result = Utilities.isStringNullOrEmptyOrWhitespace(foo);
        assertTrue(result);
    }

    @Test
    public void isStringNullOrEmptyOrWhitespace_WithWhitespaceString_ReturnsTrue() {
        String foo = "        ";
        boolean result = Utilities.isStringNullOrEmptyOrWhitespace(foo);
        assertTrue(result);
    }

    @Test
    public void validateStringInputParam_WithValidParam_IsValid() {
        String foo = "valid";
        Utilities.validateCtorStringInputParam(new String[] {foo}, "MyClass");
        assertTrue(true);
    }

    @Test
    public void validateStringInputParam_WithInvalidParam_ExceptionMessageHasCorrectName() {
        expectedException.expect(DocBuildException.class);
        expectedException.expectMessage("Constructor input parameter for MyClass cannot be null, empty, or whitespace.");
        String foo = "";
        Utilities.validateCtorStringInputParam(new String[] {foo}, "MyClass");
    }

    @Test(expected = DocBuildException.class)
    public void validateStringInputParam_WithInvalidParam_ThrowsException() {
        String foo = "";
        Utilities.validateCtorStringInputParam(new String[] {foo}, "MyClass");
    }

    @Test
    public void validateCtorObjectsAreNotNull_WithNonNullParams_IsValid() {
        File file1 = new File(Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "cloudhub.ad"}));
        File file2 = new File(Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "cloudhub-at-a-glance.ad"}));
        Utilities.validateCtorObjectsAreNotNull(new Object[] { file1, file2 }, "MyClass");
    }

    @Test
    public void validateCtorObjectsAreNotNull_WithNullParams_ThrowsException() {
        expectedException.expect(DocBuildException.class);
        expectedException.expectMessage("Constructor input parameter for MyClass cannot be null.");
        Utilities.validateCtorObjectsAreNotNull(new Object[] { null, null }, "MyClass");
    }

    @Test
    public void validateCtorObjectsAreNotNull_WithNullObjectAndStringParams_ThrowsException() {
        expectedException.expect(DocBuildException.class);
        expectedException.expectMessage("Constructor input parameter for MyClass cannot be null, empty, or whitespace.");
        String foo = "";
        Utilities.validateCtorObjectsAreNotNull(new Object[] { foo, null }, "MyClass");
    }

    @Test
    public void getOnlyContentDivFromHtml_WithValidHtml_ReturnsDesiredContent() {
        File file = new File(Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "cloudhub.ad"}));
        AsciiDocPage page = AsciiDocPage.fromFile(file);
        assertTrue(!Utilities.getOnlyContentDivFromHtml(page.getHtml()).contains("<div id=\"content\">"));
    }

    @Test
    public void getFileContentsFromFiles_SpecifiedExistingFilesContents_AreReturned() {
        List<String> sampleText = new ArrayList<String>();
        sampleText.add(0, "The quick brown fox jumps over the lazy dog.");
        sampleText.add(1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        String sampleFilePath = Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "text-files" });
        File[] sampleFiles = new File(sampleFilePath).listFiles();
        List<File> filesInSampleDirectory = filesInSampleDirectory = Arrays.asList(sampleFiles);
        List<String> fileText = Utilities.getFileContentsFromFiles(filesInSampleDirectory);
        for(int i = 0; i < fileText.size(); i++) {
            assertTrue(fileText.get(i).equalsIgnoreCase(sampleText.get(i)));
        }
    }

    @Test(expected = DocBuildException.class)
    public void getFileContentsFromFiles_FilesDoNotExist_ThrowsException() {
        String sampleFilePath1 = Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "text-files", "foo.txt" });
        String sampleFilePath2 = Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "text-files", "bar.txt" });
        List<File> files = new ArrayList<File>(){};
        files.add(new File(sampleFilePath1));
        files.add(new File(sampleFilePath2));
        Utilities.getFileContentsFromFiles(files);
    }

    @Test
    public void getFileContentsFromFile_SpecifiedExistingFileContents_AreReturned() {
        String sample1 = "The quick brown fox jumps over the lazy dog.";
        String sampleFilePath = Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "text-files", "sample-1.txt"});
        File file = new File(sampleFilePath);
        String returnedText = Utilities.getFileContentsFromFile(file);
        assertTrue(sample1.equalsIgnoreCase(returnedText));
    }

    @Test(expected = DocBuildException.class)
    public void getFileContentsFromFile_FileDoesNotExist_ThrowsException() {
        String sampleFilePath = Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "text-files", "foo.txt"});
        Utilities.getFileContentsFromFile(new File(sampleFilePath));
    }

    @Test
    public void getRandomAlphaNumericString_IsValid() {
        List<String> randomStrings = new ArrayList<String>();
        for(int i = 0; i < 10; i++) {
            String random = Utilities.getRandomAlphaNumericString(10);
            randomStrings.add(random);
        }
        for(int i = 0; i< 10; i++) {
            for(int j = 0; j < 10; j++) {
                if(i != j) {
                    assertFalse(randomStrings.get(i).contentEquals(randomStrings.get(j)));
                }
            }
            assertTrue(randomStrings.get(i).length() == 10);
        }
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
}
