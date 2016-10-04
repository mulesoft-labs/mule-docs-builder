package com.mulesoft.documentation.builder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mulesoft.documentation.builder.model.SectionVersion;

/**
 * This class needs to check the version of a section and determine
 * if a particular page existed in a previous version. Then, depending
 * on the page, output the version selector HTML contextual to the page.
 *
 * Created by sean.osterberg on 2/22/15.
 */
public class VersionSelector {
    List<Section> sections;
    Section section;
    List<SectionVersion> sectionVersions;

    public VersionSelector(List<Section> sections, Section section, List<SectionVersion> sectionVersions) {
        this.sections = sections;
        this.section = section;
        this.sectionVersions = sectionVersions;
    }

    public String htmlForPage(AsciiDocPage page) {
        Map<String, String> versionLinkMapping = getVersionLinkMapping(page);
        String latestVersion = getLatestVersionString();
        boolean latestAlreadySet = false;
        int count = 1; // factors in 0 index for currently selected version, which also may or may not be the latest version
        String[] orderOfVersions = new String[versionLinkMapping.size()];

        // special for site index page
        if (section.isRoot()) {
            return "";
        // Check if the size is bigger than 0 and the version name isn't "latest", which is the default name
        }
        else if (versionLinkMapping.size() > 0 && !section.getVersionPrettyName().equals("latest")) {
            for (Map.Entry<String, String> entry : versionLinkMapping.entrySet()) {
                // First, get the current version
                if (entry.getValue().equals(section.getVersionPrettyName())) {
                    if (entry.getValue().equals(latestVersion)) { // first check if it's also the latest version
                        orderOfVersions[0] = "<option value=\"" + entry.getKey() + "\">" + entry.getValue() + " (Latest)</option>";
                        latestAlreadySet = true;
                    }
                    else {
                        orderOfVersions[0] = "<option value=\"" + entry.getKey() + "\">" + entry.getValue() + " (Active)</option>";
                    }
                // Then, get the latest version if it isn't the current version
                }
                else if (!latestAlreadySet && entry.getValue().equals(latestVersion)) {
                    orderOfVersions[count] = "<option value=\"" + entry.getKey() + "\">" + entry.getValue() + " (Latest)</option>";
                    count++;

                }
                else {
                    orderOfVersions[count] = "<option value=\"" + entry.getKey() + "\">" + entry.getValue() + "</option>";
                    count++;
                }
            }
        }
        else {
            return ""; // there was only one version
        }
        return addVersionOrderToOutputString(orderOfVersions);
    }

    private String addVersionOrderToOutputString(String[] outputOrder) {
        String output = "<label for=\"version-selector\">" + section.getPrettyName() + " Version</label>";
        output += "<select onChange=\"window.location.href=this.value\" id=\"version-selector\">";

        outputOrder = sortVersionOrder(outputOrder);

        for (String version : outputOrder) {
            output += version;
        }
        output += "</select>\n";
        return output;
    }

    private String[] sortVersionOrder(String[] original) {
        String[] replacement;
        if (original.length > 2) {
            replacement = new String[original.length];
            String[] tempArr = new String[original.length - 1];
            String temp = original[0];
            for (int i = 1; i < original.length; i++) {
                tempArr[i - 1] = original[i];
            }
            Arrays.sort(tempArr, Collections.reverseOrder());
            for (int i = 1; i <= tempArr.length; i++) {
                replacement[i] = tempArr[i - 1];
            }
            replacement[0] = temp;
            return replacement;
        }
        else {
            return original;
        }
    }

    private String getLatestVersionString() {
        for (SectionVersion version : sectionVersions) {
            if (version.isLatestVersion()) {
                return version.getVersionName();
            }
        }
        return ""; // return an empty string, because site builder shouldn't fail if there isn't a match
    }

    public Map<String, String> getVersionLinkMapping(AsciiDocPage page) {
        // root section (aka space) has no versions, so just return
        if (sectionVersions.size() == 1 && sectionVersions.get(0).isRoot()) {
            return Collections.<String, String>emptyMap();
        }

        Map<String, String> mapping = new HashMap<>();
        String relativePageUrl = page.getRelativeUrl();

        for (SectionVersion version : sectionVersions) {
            String target = version.getVersionUrl();
            if (!relativePageUrl.isEmpty()) {
                Optional<Section> sectionForVersion = sections.stream().filter(s -> s.getSectionVersion().equals(version)).findFirst();
                if (sectionForVersion.isPresent() && sectionForVersion.get().containsPageMatching(page)) {
                    target = target.concat("/").concat(relativePageUrl);
                }
            }
            mapping.put(target, version.getVersionName());
        }

        return mapping;
    }
}
