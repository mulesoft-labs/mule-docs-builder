/**
 * Created by sean.osterberg on 2/6/15.
 */
public class DocPage {
    private String title;
    private String initialTocHtml;
    private String initialContentHtml;
    private String finalTocHtml;
    private String finalContentHtml;
    private String breadcrumbHtml;
    private String versionHtml;
    private String pdfLink;
    private String pdfLinkSection;
    private String ratingHtml;
    private String gitHubLink;
    private String asciidocSource;
    private String sourceFilename;
    private String outputFilename;
    private String sourceFilepath;
    private String finalRelativeUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInitialTocHtml() {
        return initialTocHtml;
    }

    public void setInitialTocHtml(String initialTocHtml) {
        this.initialTocHtml = initialTocHtml;
    }

    public String getFinalTocHtml() {
        return finalTocHtml;
    }

    public void setFinalTocHtml(String finalTocHtml) {
        this.finalTocHtml = finalTocHtml;
    }

    public String getFinalContentHtml() {
        return finalContentHtml;
    }

    public void setFinalContentHtml(String finalContentHtml) {
        this.finalContentHtml = finalContentHtml;
    }

    public String getInitialContentHtml() {
        return initialContentHtml;
    }

    public void setInitialContentHtml(String initialContentHtml) {
        this.initialContentHtml = initialContentHtml;
    }

    public String getBreadcrumbHtml() {
        return breadcrumbHtml;
    }

    public void setBreadcrumbHtml(String breadcrumbHtml) {
        this.breadcrumbHtml = breadcrumbHtml;
    }

    public String getVersionHtml() {
        return versionHtml;
    }

    public void setVersionHtml(String versionHtml) {
        this.versionHtml = versionHtml;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public String getPdfLinkSection() {
        return pdfLinkSection;
    }

    public void setPdfLinkSection(String pdfLinkSection) {
        this.pdfLinkSection = pdfLinkSection;
    }

    public String getRatingHtml() {
        return ratingHtml;
    }

    public void setRatingHtml(String ratingHtml) {
        this.ratingHtml = ratingHtml;
    }

    public String getGitHubLink() {
        return gitHubLink;
    }

    public void setGitHubLink(String gitHubLink) {
        this.gitHubLink = gitHubLink;
    }

    public String getAsciidocSource() {
        return asciidocSource;
    }

    public void setAsciidocSource(String asciidocSource) {
        this.asciidocSource = asciidocSource;
    }

    public String getSourceFilename() {
        return sourceFilename;
    }

    public void setSourceFilename(String sourceFilename) {
        this.sourceFilename = sourceFilename;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    public String getSourceFilepath() {
        return sourceFilepath;
    }

    public void setSourceFilepath(String sourceFilepath) {
        this.sourceFilepath = sourceFilepath;
    }

    public String getFinalRelativeUrl() {
        return finalRelativeUrl;
    }

    public void setFinalRelativeUrl(String finalRelativeUrl) {
        this.finalRelativeUrl = finalRelativeUrl;
    }
}
