package com.mulesoft.documentation.builder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sean.osterberg on 2/22/15.
 */
public class PageVersion {
    private String baseName;
    private Map<String, String> versionUrlAndPrettyName;

    public PageVersion(String baseName) {
        this.baseName = baseName;
        this.versionUrlAndPrettyName = new HashMap<String, String>();
    }

    public void addUrlAndName(String versionUrl, String prettyName) {
        this.versionUrlAndPrettyName.put(versionUrl, prettyName);
    }

    public String getBaseName() {
        return baseName;
    }

    public Map<String, String> getVersionUrlAndPrettyName() {
        return versionUrlAndPrettyName;
    }

}
