package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.model.TocNode;
import com.mulesoft.documentation.builder.util.Utilities;

import java.io.File;

/**
 * Created by sean.osterberg on 2/20/15.
 */
public class SectionTableOfContents {
    private TocNode rootTocNode;

    public SectionTableOfContents(TocNode rootNode, AsciiDocPage tocSourcePage) {
        validateInputParams(new Object[]{rootNode});
        this.rootTocNode = rootNode;
    }

    public static SectionTableOfContents fromAsciiDocFile(File asciiDocFile) {
        Utilities.validateAsciiDocFile(asciiDocFile);
        AsciiDocPage asciiDocPage = AsciiDocPage.fromFile(asciiDocFile);
        RootNodeFromHtmlToc rootNode = RootNodeFromHtmlToc.fromTocAsciiDocPage(asciiDocPage);
        return new SectionTableOfContents(rootNode.getRootNode(), asciiDocPage);
    }

    private void validateInputParams(Object[] params) {
        Utilities.validateCtorObjectsAreNotNull(params, SectionTableOfContents.class.getSimpleName());
    }

    public TocNode getRootTocNode() {
        return rootTocNode;
    }

    public void setRootTocNode(TocNode rootTocNode) {
        this.rootTocNode = rootTocNode;
    }
}
