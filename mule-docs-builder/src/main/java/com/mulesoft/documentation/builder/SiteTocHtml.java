package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.model.SectionVersion;
import com.mulesoft.documentation.builder.model.TocNode;
import com.mulesoft.documentation.builder.util.Utilities;

import java.util.ArrayList;
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

    // For a given page in a given section, build out a complete TOC. The behavior should be as follows:
    //     * Get the section TOC for the current version
    //     * Get the latest TOCs for the other versions (which are inactive)
    public void getTocHtmlForSectionAndPage2(Section section, AsciiDocPage page) {
        StringBuilder builder = new StringBuilder();
        for(TocNode node : this.toc.getNodes()) {
            if(node.getUrl().equals(section.getBaseName())) { // this spot in the toc can be replaced by a toc with the current section and selected topic
                String fixedBaseName = Utilities.fixIndexBaseName(page.getBaseName());
                if (Utilities.isActiveUrlInSection(section.getRootNode(), fixedBaseName, false)) {
                    builder.append(getSelectedSectionHtml(section, page));
                } else {
                    builder.append(getUnselectedSectionHtml(section));
                }
            } else { // it's not a match with this section,

            }
        }



        String fixedBaseName = Utilities.fixIndexBaseName(page.getBaseName());
        if (Utilities.isActiveUrlInSection(section.getRootNode(), fixedBaseName, false)) {
            builder.append(getSelectedSectionHtml(section, page));
        } else {
            builder.append(getUnselectedSectionHtml(section));
        }
    }


    /**
     * The logic is like this: We need to reproduce the exact order of the TOC. Therefore:
     *      * Loop through the master root nodes of the TOC
     *      * For each root node, loop through the sections to find the right one
     *      * If the section is the same one as the input section, process that. Otherwise, get the unselected TOC.
     *      * Build out the entire TOC tree depending on the selected page
     *
     */

    public String getTocHtmlForSectionAndPage(Section section, AsciiDocPage page) {
        List<String> alreadyAddedSectionBaseNames = new ArrayList<String>(); // necessary because we need to make sure we don't add a different version of the same section
        StringBuilder builder = new StringBuilder();
        for (TocNode node : this.toc.getNodes()) { // loop through each top-level root node
            for (Section currentSection : this.sections) { // loop through each section

                if(currentSection.getBaseName().equals(section.getBaseName())) {
                    // if the current section is the right one (version and section name match), then use it
                    if (currentSection.getUrl().equals(section.getUrl())) { // need to compare URLs because the section name isn't enough -- there are different versions with the same base section name
                        if (node.getUrl().equals(section.getBaseName())) { //
                            String fixedBaseName = Utilities.fixIndexBaseName(page.getBaseName());
                            if (Utilities.isActiveUrlInSection(section.getRootNode(), fixedBaseName, false) && !alreadyAddedSectionBaseNames.contains(currentSection.getBaseName())) {
                                builder.append(getSelectedSectionHtml(section, page));
                                alreadyAddedSectionBaseNames.add(currentSection.getBaseName());
                            } else if (!alreadyAddedSectionBaseNames.contains(currentSection.getBaseName())) {
                                builder.append(getUnselectedSectionHtml(currentSection));
                                alreadyAddedSectionBaseNames.add(currentSection.getBaseName());
                            }
                        }
                    }
                // if the current section has a different name
                } else {
                    if(!currentSection.getBaseName().equals(section.getBaseName())) { // need to make sure we keep looking for other versions of the current section
                        for (Section s : this.sections) {
                            if (s.getBaseName().equals(node.getUrl()) &&
                                    s.getSectionVersion().isLatestVersion() &&
                                    !alreadyAddedSectionBaseNames.contains(s.getBaseName()) &&
                                    !s.getBaseName().equals(section.getBaseName())) { // get the TOC for the latest version of other root node
                                builder.append(getUnselectedSectionHtml(s));
                                alreadyAddedSectionBaseNames.add(s.getBaseName());
                            }
                        }
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
