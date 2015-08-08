package org.mule.docs.linkmanager;

import org.apache.commons.lang3.StringUtils;
import org.asciidoctor.ast.ContentPart;
import org.asciidoctor.ast.StructuredDocument;
import org.junit.Test;
import org.mule.docs.AsciiDocPage;
import org.mule.docs.Section;
import org.mule.docs.util.Utilities;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 7/31/15.
 */
public class BrokenLinkCheckerTest {

    @Test
    public void getLinksInPage() {
        String path = Utilities.getConcatPath(new String[] { getTestResourcesPath(), "3.2", "mule-agent" });
        String docPath = Utilities.getConcatPath(new String[] { getTestResourcesPath(), "many-links.adoc" });
        Section section = Section.fromDirectory(new File(path));
        AsciiDocPage page = AsciiDocPage.fromFile(new File(docPath));
        BrokenLinkChecker checker = new BrokenLinkChecker();
        List<Link> links = checker.getLinksFromPage(section, page);
        boolean foo = true;
    }

    public static String getTestResourcesPath() {
        URL pathToTestResources = BrokenLinkCheckerTest.class.getClassLoader().getResource("");
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
