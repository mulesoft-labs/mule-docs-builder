package org.mule.docs;

import org.mule.docs.util.Utilities;

import java.util.List;

/**
 * Created by sean.osterberg on 2/20/15.
 */

public class SiteTocHtml {
    private SiteTableOfContents toc;
    private List<Section> sections;

    public SiteTocHtml(SiteTableOfContents toc, List<Section> sections) {
        this.toc = toc;
        this.sections = sections;
    }

    public static SiteTocHtml fromSiteTocAndSections(SiteTableOfContents toc, List<Section> sections) {
        return new SiteTocHtml(toc, sections);
    }

    public String getTocHtmlForSectionAndPage(Section section, AsciiDocPage page) {
        StringBuilder builder = new StringBuilder();
        for (TocNode node : this.toc.getNodes()) {
            for (Section currentSection : this.sections) {
                if (currentSection.getBaseName().equals(section.getBaseName())) {
                    if (node.getUrl().equals(section.getBaseName())) { //
                        String fixedBaseName = Utilities.fixIndexBaseName(page.getBaseName());
                        if (Utilities.isActiveUrlInSection(section.getRootNode(), fixedBaseName, false)) {
                            builder.append(getSelectedSectionHtml(section, page));
                        } else {
                            builder.append(getUnselectedSectionHtml(currentSection));
                        }
                    }
                } else {
                    if (node.getUrl().equals(currentSection.getUrl())) {
                        builder.append(getUnselectedSectionHtml(currentSection));
                    }
                }
            }
        }
        return builder.toString();
    }

    private String getSelectedSectionHtml(Section section, AsciiDocPage page) {
        String pageUrl = Utilities.fixIndexBaseName(page.getBaseName()); // Replaces "index" with "" so it matches the node's expected URL
        // Can insert "/docs/" as the third parameter for production site
        return SectionTocHtml.getSelectedTocFromRootNode(section.getRootNode(), pageUrl, "/" + section.getUrl()).getHtml(); // Todo: add global baseUrl
    }

    private String getUnselectedSectionHtml(Section section) {
        // Can insert "/docs/" as the third parameter for production site
        return SectionTocHtml.getUnselectedTocFromRootNode(section.getRootNode(), "/" + section.getUrl()).getHtml();
    }
}
