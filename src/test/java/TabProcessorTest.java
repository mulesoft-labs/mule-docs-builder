import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * Created by sean.osterberg on 5/28/15.
 */
public class TabProcessorTest {

    @Test
    public void testGetBlocks() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
    }

    private File getValidFile() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "cloudhub.ad"});
        return new File(path);
    }
}
