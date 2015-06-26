package org.mule.docs;

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
        for(TocNode node : toc.getNodes()) {
            for(Section currentSection : this.sections) {
                if(currentSection.getBaseName().contentEquals(section.getBaseName())) {
                    if(node.getUrl().contentEquals(section.getBaseName())) { //
                        if(Utilities.isActiveUrlInSection(section.getRootNode(), page.getBaseName(), false)) {
                            builder.append(getSelectedSectionHtml(section, page));
                        } else {
                            builder.append(getUnselectedSectionHtml(currentSection));
                        }
                    }
                } else {
                    if(node.getUrl().contentEquals(currentSection.getUrl())) {
                        builder.append(getUnselectedSectionHtml(currentSection));
                    }
                }
            }
        }
        return builder.toString();
    }

    private String getSelectedSectionHtml(Section section, AsciiDocPage page) {
        String pageUrl = page.getBaseName();
        // Can insert "/docs/" as the third parameter for production site
        return SectionTocHtml.getSelectedTocFromRootNode(section.getRootNode(), pageUrl, "/" + section.getUrl()).getHtml(); // Todo: add global baseUrl
    }

    private String getUnselectedSectionHtml(Section section) {
        // Can insert "/docs/" as the third parameter for production site
        return SectionTocHtml.getUnselectedTocFromRootNode(section.getRootNode(), "/" + section.getUrl()).getHtml();
    }
}
