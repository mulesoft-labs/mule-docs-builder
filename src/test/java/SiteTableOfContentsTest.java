import org.junit.Test;
import org.mule.docs.DocBuildException;
import org.mule.docs.SiteTableOfContents;
import org.mule.docs.Utilities;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.File;

/**
 * Created by sean.osterberg on 3/11/15.
 */
public class SiteTableOfContentsTest {

    @Test
    public void fromAsciiDocFile_canGetNewObjectFromValidFile() {
        SiteTableOfContents toc = SiteTableOfContents.fromAsciiDocFile(getValidTocFile());
        assertThat(toc, instanceOf(SiteTableOfContents.class));
    }

    @Test(expected = DocBuildException.class)
    public void fromAsciiDocFile_throwsExceptionWithInvalidFileType() {
        SiteTableOfContents toc = SiteTableOfContents.fromAsciiDocFile(new File("toc.blah"));
    }

    @Test
    public void fromAsciiDocFile_WithValidFile_ReturnsExpectedNodes() {
        SiteTableOfContents toc = SiteTableOfContents.fromAsciiDocFile(getValidTocFile());
        assertTrue(toc.getNodes().get(0).getTitle().contentEquals("Anypoint Platform"));
        assertTrue(toc.getNodes().get(1).getTitle().contentEquals("CloudHub"));
    }

    public File getValidTocFile() {
        String tocPath = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "_toc.adoc"});
        return new File(tocPath);
    }
}
