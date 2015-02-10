import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

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
    public void buildDocsInDirectory_WithValidDocsDirectory_ReturnsDocPages() throws FileNotFoundException, IOException {
        File sourceDirectory = new File(TestHelpers.getTestResourcesPath() + "/asciidoc-files-valid");
        File outputDirectory = new File(TestHelpers.getTestResourcesPath() + "/output");
        SiteBuilder builder = new SiteBuilder();
        builder.buildDocsInDirectory(sourceDirectory, outputDirectory);

        assertTrue(outputDirectory.listFiles().length == 2);
        assertTrue(outputDirectory.listFiles()[0].getName().contentEquals("cloudhub.html"));
        assertTrue(outputDirectory.listFiles()[1].getName().contentEquals("deploying-a-cloudhub-application.html"));
    }
}
