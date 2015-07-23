package org.mule.docs; /**
 * Created by sean.osterberg on 2/22/15.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class needs to check the version of a section and determine
 * if a particular page existed in a previous version. Then, depending
 * on the page, output the version selector HTML contextual to the page.
 */
public class VersionSelector {
    Section section;

    public VersionSelector(Section section) {
        this.section = section;
    }

    public static VersionSelector fromSection(Section section) {
        return new VersionSelector(section);
    }

    public String htmlForPage(AsciiDocPage page) {
        String output = "";
        List<PageVersion> versions = getAllVersionMappingsForSection(this.section);
        for(PageVersion version : versions) {
            if(version.getBaseName().equals(page.getBaseName())) {
                output += "<label for=\"version-selector\">" + this.section.getPrettyName() + " Version</label>\n";
                output += "            <select id=\"version-selector\">\n";
                int count = 1;
                for(Map.Entry<String, String> entry : version.getVersionUrlAndPrettyName().entrySet()) {
                    output += "                    <option value=\"" + count + "\"><a href=\"" + entry.getKey() + "\">"
                            + entry.getValue() + "</a></option>\n";
                    count++;
                }
                output += "    </select>\n";
            }
        }

        return output;
    }

    public static List<PageVersion> getAllVersionMappingsForSection(Section section) {
        List<PageVersion> pageVersions = new ArrayList<PageVersion>();
        compareLatestPagesWithVersions(section, pageVersions);
        return pageVersions;
    }

    public static void compareLatestPagesWithVersions(Section section, List<PageVersion> pageVersions) {
        for(AsciiDocPage latestPage : section.getPages()) {
            for (Section version : section.getVersions()) {
                compareCurrentPageWithPagesInSection(version, latestPage, pageVersions);
            }
        }
    }

    private static void compareCurrentPageWithPagesInSection(Section section, AsciiDocPage current, List<PageVersion> pageVersions) {
        List<AsciiDocPage> pages = section.getPages();
        for(AsciiDocPage page : pages) {
            if(current.getBaseName().contentEquals(page.getBaseName())) {
                // There's an older version of this page; Add it
                matchPageAndCreateIfNotExist(current, pageVersions, section);
            }
            // Also add the other page even if it's not a match
            matchPageAndCreateIfNotExist(page, pageVersions, section);
        }
    }

    private static void matchPageAndCreateIfNotExist(AsciiDocPage page, List<PageVersion> pageVersions, Section section) {
        PageVersion match = getInstanceOfPageVersionIfMatch(page.getBaseName(), pageVersions);
        if(match == null) {
            PageVersion version = new PageVersion(page.getBaseName());
            version.addUrlAndName(removeRootFromSectionUrl(section.getUrl()), section.getVersionPrettyName());
            pageVersions.add(version);
        } else {
            if(!checkIfUrlAndNameAlreadyContains(match, section.getUrl(), section.getVersionPrettyName())) {
                match.addUrlAndName(removeRootFromSectionUrl(section.getUrl()), section.getVersionPrettyName());
            }
        }
    }

    private static String removeRootFromSectionUrl(String sectionUrl) {
        int index = sectionUrl.indexOf("/") + 1; // consume the slash as well as the section path
        return sectionUrl.substring(index, sectionUrl.length());
    }

    private static PageVersion getInstanceOfPageVersionIfMatch(String baseName, List<PageVersion> pageVersions) {
        for(PageVersion version : pageVersions) {
            if(version.getBaseName().contentEquals(baseName)){
                return version;
            }
        }
        return null;
    }

    private static boolean checkIfUrlAndNameAlreadyContains(PageVersion version, String url, String prettyName) {
        for(Map.Entry<String, String> item : version.getVersionUrlAndPrettyName().entrySet()) {
            if(item.getKey().contentEquals(url) && item.getValue().contentEquals(prettyName)) {
                return true;
            }
        }
        return false;
    }

}
