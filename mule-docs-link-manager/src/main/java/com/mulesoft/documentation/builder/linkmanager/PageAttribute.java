package com.mulesoft.documentation.builder.linkmanager;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/21/15.
 */
public class PageAttribute {
    private String topicTitle;
    private String prettySectionName;
    private String versionName;
    private String sectionBaseName;
    private String topicBaseName;
    private String completeFilePath;

    public PageAttribute(String topicTitle,
                         String prettySectionName,
                         String versionName,
                         String sectionBaseName,
                         String topicBaseName,
                         String completeFilePath) {
        this.topicTitle = topicTitle;
        this.prettySectionName = prettySectionName;
        this.versionName = versionName;
        this.sectionBaseName = sectionBaseName;
        this.topicBaseName = topicBaseName;
        this.completeFilePath = completeFilePath;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public String getPrettySectionName() {
        return prettySectionName;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getSectionBaseName() {
        return sectionBaseName;
    }

    public String getTopicBaseName() {
        return topicBaseName;
    }

    public String getCompleteFilePath() {
        return completeFilePath;
    }
}
