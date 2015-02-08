/**
 * Created by sean.osterberg on 2/6/15.
 */
public class DocPage {
    private TocNode node;
    private String title;
    private String tocHtml;
    private String contentHtml;
    private String breadcrumbHtml;
    private String versionHtml;
    private String pdfLink;
    private String pdfLinkSection;
    private String ratingHtml;
    private String gitHubLink;
    private String asciidocSource;
    private String filename;
    private String sourceFilePath;
    private String destinationFilePath;

    public TocNode getNode() {
        return node;
    }

    public void setNode(TocNode node) {
        this.node = node;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTocHtml() {
        return tocHtml;
    }

    public void setTocHtml(String tocHtml) {
        this.tocHtml = tocHtml;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public void setSourceFilePath(String sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
    }

    public String getDestinationFilePath() {
        return destinationFilePath;
    }

    public void setDestinationFilePath(String destinationFilePath) {
        this.destinationFilePath = destinationFilePath;
    }
}
