/**
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
    public static VersionSelector fromSection(Section section) {
        if(section.getVersions().size() > 0) {

        }
        return new VersionSelector();
    }

    public String htmlForPage(AsciiDocPage page) {
        return "foo";
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
            version.addUrlAndName(section.getUrl(), section.getPrettyName());
            pageVersions.add(version);
        } else {
            if(!checkIfUrlAndNameAlreadyContains(match, section.getUrl(), section.getPrettyName())) {
                match.addUrlAndName(section.getUrl(), section.getPrettyName());
            }
        }
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
