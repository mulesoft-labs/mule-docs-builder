package org.mule.docs.model.v2;

import java.util.List;

/**
 * Created by Mulesoft.
 */
public class Section extends AbstractBaseDocElement {

    private String filePath;
    private String prettyName;
    private String baseName;
    private List<Section> versions;
    private List<Page> pages;
    private TableOfContents toc;

    public TableOfContents getToc() {
        return toc;
    }

    public void setToc(TableOfContents toc) {
        this.toc = toc;
    }

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

    @Override
    public void accept(IPageElementVisitor visitor) {
        if (visitor.visit(this)) {
            toc.accept(visitor);
            if (versions != null) {
                for (Section section : versions) {
                    section.accept(visitor);
                }
            }
            if (pages != null) {
                for (Page page : pages) {
                    page.accept(visitor);
                }
            }
        }
    }
}
