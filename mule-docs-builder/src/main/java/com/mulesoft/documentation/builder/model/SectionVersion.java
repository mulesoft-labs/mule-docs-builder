package com.mulesoft.documentation.builder.model;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/20/15.
 */
public class SectionVersion {
    private String sectionPrettyName;
    private String sectionBaseName;
    private String versionUrl;
    private String versionName;
    private boolean latestVersion;

    public SectionVersion(String sectionPrettyName,
                          String sectionBaseName,
                          String versionUrl,
                          String versionName,
                          boolean latestVersion) {
        this.sectionBaseName = sectionBaseName;
        this.versionUrl = versionUrl;
        this.versionName = versionName;
        this.latestVersion = latestVersion;
        this.sectionPrettyName = sectionPrettyName;
    }

    public String getSectionPrettyName() {
        return sectionPrettyName;
    }

    public String getSectionBaseName() {
        return sectionBaseName;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public String getVersionName() {
        return versionName;
    }

    public boolean isLatestVersion() {
        return latestVersion;
    }

    public boolean isRoot() {
        return sectionBaseName.isEmpty();
    }
}
