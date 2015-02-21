import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

/**
 * Created by sean.osterberg on 2/6/15.
 */
public class UtilitiesTests {

    @Test
    public void fileEndsWithDesiredExtension_FileWithAdExtensionAndAdInput_ReturnsTrue() {
        boolean result = Utilities.fileEndsWithDesiredExtension("Cloudhub.ad", new String[] { "ad" });
        assertTrue(result);
    }

    @Test
    public void fileEndsWithDesiredExtension_FileWithAdExtensionAndMdInput_ReturnsFalse() {
        boolean result = Utilities.fileEndsWithDesiredExtension("Cloudhub.ad", new String[] { "md" });
        assertFalse(result);
    }

    @Test
    public void fileEndsWithDesiredExtension_FileWithAdExtensionAndMultipleValidInput_ReturnsTrue() {
        boolean result = Utilities.fileEndsWithDesiredExtension("Cloudhub.ad", new String[] { "asciidoc", "ad" });
        assertTrue(result);
    }

    @Test
    public void fileEndsWithDesiredExtension_FileWithAdExtensionAndMultipleInvalidInput_ReturnsTrue() {
        boolean result = Utilities.fileEndsWithDesiredExtension("Cloudhub.ad", new String[] { "asciidoc", "adc" });
        assertFalse(result);
    }

    @Test
    public void makeTargetDirectory_SpecifiedNonExistentDirectoryPath_IsCreated() {
        String newFolderPath = TestHelpers.getPathForTestResourcesFile(new String[] { "test-folder" });
        boolean result = Utilities.makeTargetDirectory(newFolderPath);
        File file = new File(newFolderPath);
        assertTrue(file.exists());
        assertTrue(result);
        Utilities.deleteTargetDirectory(newFolderPath);
    }

    @Test
    public void makeTargetDirectory_SpecifiedExistingDirectoryPath_IsNotCreated() {
        String newFolderPath = TestHelpers.getPathForTestResourcesFile(new String[] { "test-folder" });
        boolean result = Utilities.makeTargetDirectory(newFolderPath);
        if(result) {
            result = Utilities.makeTargetDirectory(newFolderPath);
        }
        File file = new File(newFolderPath);
        assertTrue(file.exists());
        assertFalse(result);
        Utilities.deleteTargetDirectory(newFolderPath);
    }

    @Test
    public void deleteTargetDirectory_SpecifiedExistingDirectoryPath_IsDeleted() {
        String newFolderPath = TestHelpers.getPathForTestResourcesFile(new String[] { "test-folder" });
        Utilities.makeTargetDirectory(newFolderPath);
        File file = new File(newFolderPath);
        assertTrue(file.exists());
        boolean result = Utilities.deleteTargetDirectory(newFolderPath);
        assertFalse(file.exists());
        assertTrue(result);
    }

    @Test
    public void deleteTargetDirectory_SpecifiedNonExistentDirectoryPath_IsDeleted() {
        String newFolderPath = TestHelpers.getTestResourcesPath() + "test-folder";
        File file = new File(newFolderPath);
        assertTrue(!file.exists());
        boolean result = Utilities.deleteTargetDirectory(newFolderPath);
        assertFalse(file.exists());
        assertFalse(result);
    }

    @Test
    public void getFileContentsFromFiles_SpecifiedExistingFilesContents_AreReturned() {
        List<String> sampleText = new ArrayList<String>();
        sampleText.add(0, "The quick brown fox jumps over the lazy dog.");
        sampleText.add(1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        String sampleFilePath = TestHelpers.getPathForTestResourcesFile(new String[] { "text-files" });
        List<File> filesInSampleDirectory = Arrays.asList(new File(sampleFilePath).listFiles());
        List<String> fileText = Utilities.getFileContentsFromFiles(filesInSampleDirectory);

        for(int i = 0; i < fileText.size(); i++) {
            assertTrue(fileText.get(i).equalsIgnoreCase(sampleText.get(i)));
        }
    }

    @Test
    public void getFileContentsFromFile_SpecifiedExistingFileContents_AreReturned() {
        String sample1 = "The quick brown fox jumps over the lazy dog.";
        String sampleFilePath = TestHelpers.getPathForTestResourcesFile(new String[] { "text-files", "sample-1.txt" });
        File file = new File(sampleFilePath);
        String returnedText = Utilities.getFileContentsFromFile(file);
        assertTrue(sample1.equalsIgnoreCase(returnedText));
    }

    @Test
    public void getConcatenatedFilepath_WithValidDirectory_ReturnsValidPath() {
        String newFolderPath = TestHelpers.getPathForTestResourcesFile(new String[] { "text-files" });
        String result = Utilities.getConcatenatedFilepath(new String[]{newFolderPath});
        assertTrue(FilenameUtils.getExtension(result).isEmpty());
        assertTrue(result.endsWith("/"));
    }

    @Test
    public void getConcatenatedFilepath_WithValidFile_ReturnsValidPath() {
        String sampleFilepath = TestHelpers.getPathForTestResourcesFile(new String[] { "template.html" });
        String result = Utilities.getConcatenatedFilepath(new String[]{sampleFilepath});
        assertTrue(FilenameUtils.getExtension(result).contentEquals("html"));
        assertTrue(result.endsWith("html"));
    }

    @Test
    public void getConcatenatedFilepath_ForTwoDirectories_ReturnsValidPath() {
        String sampleFilepath = TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid" });
        String result = Utilities.getConcatenatedFilepath(new String[]{sampleFilepath, "cloudhub"});
        String desiredResult = TestHelpers.getTestResourcesPath().concat("asciidoc-files-valid/cloudhub");
        assertTrue(result.contentEquals(desiredResult));
        assertTrue(new File(result).isDirectory());
    }

    @Test
     public void getConcatenatedFilepath_ForFolderWithPeriod_ReturnsValidPath() {
        String sampleFilepath = TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "cloudhub"});
        String result = Utilities.getConcatenatedFilepath(new String[]{sampleFilepath, "v", "4.0" });
        assertTrue(new File(result).isDirectory());
    }

    @Test
    public void getConcatenatedUrl_ForValidUrl_ReturnsValidPath() {
        String sampleUrl = "http://localhost:4000";
        String result = Utilities.getConcatenatedUrl(new String[] { sampleUrl, "cloudhub.html"});
        assertTrue(result.contentEquals("http://localhost:4000/cloudhub.html"));
    }
}
