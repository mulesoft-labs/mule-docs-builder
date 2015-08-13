package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.util.Utilities;

import java.io.File;

/**
 * Created by sean.osterberg on 5/28/15.
 */
public class TabProcessorTest {

    /*@Test
    public void testGetBlocks() {
        List<AsciiDocPage> pages = new ArrayList<AsciiDocPage>();
        File[] tabFiles = getManyTabFiles();
        for (File f : tabFiles) {
            if (Utilities.fileEndsWithValidAsciidocExtension(f.getName())) {
                AsciiDocPage page = AsciiDocPage.fromFile(f);
                pages.add(page);
            }
        }
    }*/

    private File getValidFile() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "cloudhub.ad"});
        return new File(path);
    }

    private File[] getManyTabFiles() {
        String path = "/Users/sean.osterberg/mulesoft-docs/temp-tab-files/";
        File tempTabDirectory = new File(path);
        if (tempTabDirectory.isDirectory()) {
            return tempTabDirectory.listFiles();
        }
        return null;
    }
}
