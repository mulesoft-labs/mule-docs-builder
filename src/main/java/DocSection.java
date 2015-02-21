import java.util.*;

/**
 * Created by sean.osterberg on 2/15/15.
 */
public class DocSection {
    private String sectionName;
    private String sectionPathFromBaseUrl;
    private String sourceFilepath;
    private String tocHtmlForInactiveSection;
    private String versionName;
    private List<DocPage> pagesInSection;
    private List<DocSection> otherVersions;
    private TocNode rootNodeForToc;

    public DocSection(String sourceFilePath, String versionName) {
        this.sourceFilepath = sourceFilePath;
        this.versionName = versionName;
    }

    public String getSourceFilepath() {
        return sourceFilepath;
    }

    public void setSourceFilepath(String sourceFilepath) {
        this.sourceFilepath = sourceFilepath;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<DocPage> getPagesInSection() {
        return pagesInSection;
    }

    public void setPagesInSection(List<DocPage> pagesInSection) {
        this.pagesInSection = pagesInSection;
    }

    public List<DocSection> getOtherVersions() {
        return otherVersions;
    }

    public void setOtherVersions(List<DocSection> otherVersions) {
        this.otherVersions = otherVersions;
    }

    public String getSectionPathFromBaseUrl() {
        return sectionPathFromBaseUrl;
    }

    public void setSectionPathFromBaseUrl(String sectionPathFromBaseUrl) {
        this.sectionPathFromBaseUrl = sectionPathFromBaseUrl;
    }

    public TocNode getRootNodeForToc() {
        return rootNodeForToc;
    }

    public void setRootNodeForToc(TocNode rootNodeForToc) {
        this.rootNodeForToc = rootNodeForToc;
    }

    public String getTocHtmlForInactiveSection() {
        return tocHtmlForInactiveSection;
    }

    public void setTocHtmlForInactiveSection(String tocHtmlForInactiveSection) {
        this.tocHtmlForInactiveSection = tocHtmlForInactiveSection;
    }
}
