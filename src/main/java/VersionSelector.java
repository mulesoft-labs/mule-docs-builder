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

    public static String forPage(Page page) {
        return null;
    }

    public static List<PageVersion> getAllVersionMappingsForSection(Section section) {
        List<PageVersion> pageVersions = new ArrayList<PageVersion>();
        compareLatestPagesWithVersions(section, pageVersions);
        comparePagesInVersionSections(section, pageVersions);
        return pageVersions;
    }

    public static void compareLatestPagesWithVersions(Section section, List<PageVersion> pageVersions) {
        for(AsciiDocPage latestPage : section.getPages()) {
            for (Section version : section.getVersions()) {
                compareCurrentPageWithPagesInSection(version, latestPage, pageVersions);
            }
        }
    }

    public static void comparePagesInVersionSections(Section section, List<PageVersion> pageVersions) {
        for(int i = 0; i < section.getVersions().size(); i++) {
            Section versionSection = section.getVersions().get(i);
            for(AsciiDocPage page : versionSection.getPages()) {
                for (int j = 0; j < section.getVersions().size(); j++) {
                    if (j != i) {
                        Section toCompare = section.getVersions().get(j);
                        compareCurrentPageWithPagesInSection(toCompare, page, pageVersions);
                    }
                }
            }
        }
    }

    private static void compareCurrentPageWithPagesInSection(Section section, AsciiDocPage current, List<PageVersion> pageVersions) {
        List<AsciiDocPage> pages = section.getPages();
        for(AsciiDocPage page : pages) {
            if(current.getBaseName().contentEquals(page.getBaseName())) {
                // There's an older version of this page -- add to a list for processing
                PageVersion match = getInstanceOfPageVersionIfMatch(current.getBaseName(), pageVersions);
                if(match == null) {
                    PageVersion version = new PageVersion(current.getBaseName());
                    version.addUrlAndName(section.getUrl(), section.getPrettyName());
                    pageVersions.add(version);
                } else {
                    if(!checkIfUrlAndNameAlreadyContains(match, section.getUrl(), section.getPrettyName())) {
                        match.addUrlAndName(section.getUrl(), section.getPrettyName());
                    }
                }
            }
        }
    }

    public static List<PageVersion> pageIsInOtherVersions(Section section, List<PageVersion> pageVersions) {
        for(AsciiDocPage latestPage : section.getPages()) {
            PageVersion pageVersion = new PageVersion(latestPage.getBaseName());
            for (Section version : section.getVersions()) {
                List<AsciiDocPage> pages = version.getPages();
                for(AsciiDocPage oldPage : pages) {
                    if(latestPage.getFilename().contentEquals(oldPage.getFilename())) {
                        // There's an older version of this page -- add to a list for processing
                        pageVersion.addUrlAndName(version.getUrl(), version.getPrettyName());
                    }
                }
            }
            pageVersions.add(pageVersion);
        }
        return pageVersions;
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

    public String htmlForPage(Page page) {
        return "foo";
    }
}
