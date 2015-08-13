package com.mulesoft.documentation.builder.linkmanager;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 7/29/15.
 */
public class Link {

    public Link(String sectionName, String sectionBaseName, String pageName, String pageTitle, String linkUrl, String linkText) {
        this.sectionName = sectionName;
        this.sectionBaseName = sectionBaseName;
        this.pageBaseName = pageName;
        this.pageTitle = pageTitle;
        this.linkUrl = linkUrl;
        this.linkText = linkText;
    }

    private String sectionName;
    private String sectionBaseName;
    private String pageBaseName;
    private String pageTitle;
    private String linkUrl;
    private String linkText;
    private int lineNumber;

    public String getSectionName() {
        return sectionName;
    }

    public String getSectionBaseName() {
        return sectionBaseName;
    }

    public String getPageBaseName() {
        return pageBaseName;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public String getLinkText() {
        return linkText;
    }

    public int getLineNumber() { return lineNumber; }

}
