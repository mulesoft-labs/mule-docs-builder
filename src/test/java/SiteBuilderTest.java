import org.junit.Test;
import org.mule.docs.utils.Utilities;
import org.mule.docs.writer.SiteBuilder;

import java.io.File;

/**
 * Created by sean.osterberg on 3/17/15.
 */
public class SiteBuilderTest {

    @Test
    public void buildSite_withValidFolders_BuildsSite() {
        SiteBuilder.buildSite(getValidSourceDirectory(), getValidOutputDirectory());
    }

    @Test
    public void buildSite_withRealFolders_BuildsSite() {
        File source = new File(getClass().getClassLoader().getResource("mule-docs-test-site/_source").getFile());
        File output = new File(getClass().getClassLoader().getResource("mule-docs-test-site/_output").getFile());
        SiteBuilder.buildSite(source.getAbsoluteFile(), output.getAbsoluteFile());
    }

    private File getValidSourceDirectory() {
        return new File(Utilities.getConcatPath(new String[] { TestUtilities.getTestResourcesPath(), "master-folder" }));
    }

    private File getValidOutputDirectory() {
        return new File(Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "output"}));
    }
}
