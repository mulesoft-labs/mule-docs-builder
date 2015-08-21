package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.util.Utilities;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by sean.osterberg on 5/28/15.
 */
public class SectionNavigatorTest {

    @Test
    public void getHtmlForPage_WithValidPageAndSections_ReturnsValidHtml() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
        String result = SectionNavigator.getHtmlForPage(page);
        assertTrue(result != null);
    }

    private File getValidFile() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "v", "4.0", "index.adoc"});
        return new File(path);
    }
}
