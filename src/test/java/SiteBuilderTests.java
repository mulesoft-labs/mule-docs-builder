import org.junit.Test;
import org.mule.docs.SiteBuilder;
import org.mule.docs.Utilities;

import java.io.File;

/**
 * Created by sean.osterberg on 3/17/15.
 */
public class SiteBuilderTests {

    @Test
    public void buildSite_withValidFolders_BuildsSite() {
        SiteBuilder.buildSite(getValidSourceDirectory(), getValidOutputDirectory());
    }

    /*@Test
    public void buildSite_withRealFolders_BuildsSite() {
        //File source = new File("/Users/sean.osterberg/mulesoft-docs/_source/");
        File source = new File("/Users/sean.osterberg/mulesoft-docs/docsite-demo-connect/anypoint-connector-devkit/output/");
        File output = new File("/Users/sean.osterberg/mulesoft-docs/_output/docs");
        SiteBuilder.buildSite(source, output);
    }*/

    @Test
    public void buildSite_withRealFolders_BuildsSite() {
        //File source = new File("/Users/sean.osterberg/mulesoft-docs/_source/");
        File source = new File("/Users/sean.osterberg/mulesoft-docs/docsite-demo-connect/site-source/");
        File output = new File("/Users/sean.osterberg/mulesoft-docs/docsite-demo-connect/_output/");
        SiteBuilder.buildSite(source, output);
    }

    private File getValidSourceDirectory() {
        return new File(Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "master-folder"}));
    }

    private File getValidOutputDirectory() {
        return new File(Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "output"}));
    }
}
