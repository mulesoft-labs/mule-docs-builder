import java.io.File;

/**
 * Created by sean.osterberg on 2/20/15.
 */
public class TestUtilities {

    public static String getTestResourcesPath() {
        return System.getProperty("user.dir") + "/src/test/resources/";
    }

    public static String getPathToMasterFolder() {
        return getTestResourcesPath() + "master-folder";
    }

    public static TocNode getValidRootNode() {
        String tocPath = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "toc.ad"});
        SectionTableOfContents toc = SectionTableOfContents.fromAsciiDocFile(new File(tocPath));
        return toc.getRootTocNode();
    }

    public static TocNode getInvalidRootNode() {
        String tocPath = Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "bad-files", "toc.ad"});
        SectionTableOfContents toc = SectionTableOfContents.fromAsciiDocFile(new File(tocPath));
        return toc.getRootTocNode();
    }
}
