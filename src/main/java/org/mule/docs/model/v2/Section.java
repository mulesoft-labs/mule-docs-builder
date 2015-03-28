package org.mule.docs.model.v2;

import java.util.List;

/**
 * Created by Mulesoft.
 */
public class Section {
    private String filePath;
    private String prettyName;
    private String baseName;
    private List<Section> versions;
    private List<Page> pages;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPrettyName() {
        return prettyName;
    }

    public void setPrettyName(String prettyName) {
        this.prettyName = prettyName;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public List<Section> getVersions() {
        return versions;
    }

    public void setVersions(List<Section> versions) {
        this.versions = versions;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
