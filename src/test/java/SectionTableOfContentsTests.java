import org.junit.Test;
import org.mule.docs.DocBuildException;
import org.mule.docs.SectionTableOfContents;
import org.mule.docs.TocNode;
import org.mule.docs.Utilities;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.File;

/**
 * Created by sean.osterberg on 2/20/15.
 */
public class SectionTableOfContentsTests {

    @Test
    public void fromAsciiDocFile_canGetNewObjectFromValidFile() {
        SectionTableOfContents toc = SectionTableOfContents.fromAsciiDocFile(getValidTocFile());
        assertThat(toc, instanceOf(SectionTableOfContents.class));
    }

    @Test(expected = DocBuildException.class)
    public void fromAsciiDocFile_throwsExceptionWithInvalidFileType() {
        SectionTableOfContents toc = SectionTableOfContents.fromAsciiDocFile(new File("toc.blah"));
    }

    @Test
    public void getRootNode_returnsValidRootNode() {
        SectionTableOfContents toc = SectionTableOfContents.fromAsciiDocFile(getValidTocFile());
        TocNode root = toc.getRootTocNode();
        assertTrue(root != null);
        assertTrue(root.getChildren() != null && root.getChildren().size() > 0);
    }

    public File getValidTocFile() {
        String tocPath = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "toc.ad"});
        return new File(tocPath);
    }
}
