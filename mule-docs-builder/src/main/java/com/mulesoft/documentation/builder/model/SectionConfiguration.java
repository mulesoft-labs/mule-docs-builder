package com.mulesoft.documentation.builder.model;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/20/15.
 */
public class SectionConfiguration {

    private String sectionName;
    private String versionName;
    private boolean isLatest;

    public SectionConfiguration(String sectionName, String versionName, boolean isLatest) {
        this.sectionName = sectionName;
        this.versionName = versionName;
        this.isLatest = isLatest;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getVersionName() {
        return versionName;
    }

    public boolean isLatest() {
        return isLatest;
    }
}
