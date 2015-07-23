import org.junit.Test;
import static org.junit.Assert.*;

import org.mule.docs.AsciiDocPage;
import org.mule.docs.SwiftTypeMetadata;
import org.mule.docs.Utilities;

import java.io.File;

/**
 * Created by sean.osterberg on 7/5/15.
 */
public class SwiftTypeMetadataTest {

    @Test
    public void getDescription_ReturnsExpectedResult() {
        String result = SwiftTypeMetadata.getDescription(getValidAsciiDocPage());
        assertTrue(result.equals("To configure the Siebel connector global elements in your Mule application: Siebel connector is a operation based connector, which means that when ..."));
    }

    @Test
    public void getFirstParagraph_EndsWithEllipses() {
        String result = SwiftTypeMetadata.getDescription(getValidAsciiDocPage());
        assertTrue(result.endsWith("..."));
    }

    @Test
    public void getFirstParagraph_WithFewerThan147Chars_ReturnsExpectedResult() {
        String result = SwiftTypeMetadata.getDescription(getValidAsciiDocPage());
        assertTrue(result.endsWith("..."));
    }

    @Test
    public void getFirstParagraph_WithEmptyParagraph_ReturnsEmptyString() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "index.ad"});
        AsciiDocPage page = AsciiDocPage.fromFile(new File(path));
        String result = SwiftTypeMetadata.getDescription(page);
        assertTrue(result.equals(""));
    }

    @Test
    public void getFirstParagraph_WithLongParagraph_ReturnsExpectedResult() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "v", "4.0", "index.ad"});
        AsciiDocPage page = AsciiDocPage.fromFile(new File(path));
        String result = SwiftTypeMetadata.getDescription(page);
        assertTrue(result.equals("This section describes CloudHub. CloudHub enables you to integrate SaaS applications, Cloud Services and Enterprise applications in real-time. Clou..."));
    }

    @Test
    public void getTitleMetadata_ReturnsExpectedResult() {
        String result = SwiftTypeMetadata.getTitleMetadata(getValidAsciiDocPage());
        assertTrue(result.equals("<meta class=\"swiftype\" name=\"title\" data-type=\"string\" content=\"CloudHub\" />\n"));
    }

    @Test
    public void getTitleMetadata_WithLongTitle_ReturnsExpectedResult() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "deploying-a-cloudhub-application.ad"});
        AsciiDocPage page = AsciiDocPage.fromFile(new File(path));
        String result = SwiftTypeMetadata.getTitleMetadata(page);
        assertTrue(result.equals("<meta class=\"swiftype\" name=\"title\" data-type=\"string\" content=\"Deploying a CloudHub Application\" />\n"));
    }

    private AsciiDocPage getValidAsciiDocPage() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
        return page;
    }

    private File getValidFile() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "cloudhub.ad"});
        return new File(path);
    }
}
