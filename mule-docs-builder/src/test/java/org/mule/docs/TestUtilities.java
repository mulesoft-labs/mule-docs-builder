package org.mule.docs;

import org.apache.commons.lang3.StringUtils;
import org.mule.docs.util.Utilities;

import java.io.File;
import java.net.URL;

/**
 * Created by sean.osterberg on 2/20/15.
 */
public class TestUtilities {

    public static String getTestResourcesPath() {
        URL pathToTestResources = TestUtilities.class.getClassLoader().getResource("");
        String testResourcesPath = "";
        if(pathToTestResources != null) {
            testResourcesPath = pathToTestResources.getFile();
            if (!StringUtils.isEmpty(testResourcesPath)) {
                return testResourcesPath;
            }
        }
        throw new RuntimeException("Test resources path was null.");
    }

    public static String getPathToMasterFolder() {
        return Utilities.getConcatPath(new String[] { getTestResourcesPath(), "master-folder" });
    }

    public static TocNode getValidRootNode() {
        String tocPath = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "_toc.adoc"});
        SectionTableOfContents toc = SectionTableOfContents.fromAsciiDocFile(new File(tocPath));
        return toc.getRootTocNode();
    }

    public static TocNode getInvalidRootNode() {
        String tocPath = Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "bad-files", "_toc.adoc"});
        SectionTableOfContents toc = SectionTableOfContents.fromAsciiDocFile(new File(tocPath));
        return toc.getRootTocNode();
    }
}
