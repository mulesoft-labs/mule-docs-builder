import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import com.mulesoft.documentation.builder.previewer.SinglePage;
import com.mulesoft.documentation.builder.util.Utilities;

import java.net.URL;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/9/15.
 */
public class SinglePageTest {

    @Test
    public void fromAsciiDocFile_WithValidFile_Succeeds() {
        String adocFile = Utilities.getConcatPath(new String[] { getTestResourcesPath(), "index.adoc" });
        String adocOutput = Utilities.getConcatPath(new String[] { getTestResourcesPath(), "output"});
        SinglePage.fromAsciiDocFile(adocFile, adocOutput);

    }

    @Test
    public void tmpFileTest() {
        String adocFile = "/tmp/test.adoc";
        String adocOutput = "/tmp";

        SinglePage.fromAsciiDocFile(adocFile, adocOutput);
    }

    public static String getTestResourcesPath() {
        URL pathToTestResources = SinglePageTest.class.getClassLoader().getResource("");
        String testResourcesPath = "";
        if(pathToTestResources != null) {
            testResourcesPath = pathToTestResources.getFile();
            if (!StringUtils.isEmpty(testResourcesPath)) {
                return testResourcesPath;
            }
        }
        throw new RuntimeException("Test resources path was null.");
    }
}
