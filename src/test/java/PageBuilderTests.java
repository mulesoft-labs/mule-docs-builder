import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.List;

/**
 * Created by sean.osterberg on 2/6/15.
 */
public class PageBuilderTests {

    private PageBuilder setup() {
        String sourceDir = TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid" });
        String outputDir = TestHelpers.getPathForTestResourcesFile(new String[] { "output" });
        SiteBuilder siteBuilder = new SiteBuilder(sourceDir, outputDir, "");
        List<PageTemplate> templates = siteBuilder.getPageTemplates();
        return new PageBuilder(templates, "");
    }

    @Test
    public void buildPageFromTemplate_WithGivenTemplate_InsertsText() throws IOException {
        PageBuilder builder = setup();
        DocPage page = new DocPage();
        String title = "aU2otg3oa7";
        String content = "YJts3zAknk";
        String toc = "pqmMAeUSUb";
        String breadcrumb = "ueVmXxt0uL";
        page.setTitle(title);
        page.setInitialContentHtml(content);
        page.setFinalTocHtml(toc);
        page.setBreadcrumbHtml(breadcrumb);
        String result = builder.getCompletePageContent(page);

        assertTrue(result.contains(title));
        assertTrue(result.contains(content));
        assertTrue(result.contains(toc));
        assertTrue(result.contains(breadcrumb));
    }

    @Test(expected = DocBuilderException.class)
    public void buildPageFromTemplate_WithGivenTemplateAndInvalidPage_ThrowsException() throws IOException {
        PageBuilder builder = setup();
        DocPage page = new DocPage();
        String title = "aU2otg3oa7";
        String content = "YJts3zAknk";
        page.setTitle(title);
        page.setInitialContentHtml(content);
        String result = builder.getCompletePageContent(page);
    }


    /*
    @Test
    public void getTocFile_WithValidDirectory_ReturnsDocPage() throws FileNotFoundException {
        PageBuilder builder = setup();
        DocPage page = builder.getTocFile(new File(TestHelpers.getPathForTestResourcesFile(new String[]{"asciidoc-files-valid"})));

        assertTrue(isFreshDocPageValid(page));
    }

    @Test(expected = FileNotFoundException.class)
    public void getTocFile_WithInvalidDirectory_ThrowsException() throws FileNotFoundException {
        PageBuilder builder = setup();
        DocPage page = builder.getTocFile(new File(TestHelpers.getPathForTestResourcesFile(new String[]{"blah"})));

        assertTrue(isFreshDocPageValid(page));
    }

    @Test
    public void getRootNodeInToc_WithValidTocDocPage_ReturnsRootNode() throws IOException {
        AsciidocSerializer serializer = new AsciidocSerializer();
        DocPage page = serializer.getConvertedDocPageFromAsciidocFile(new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "toc.ad" })));
        PageBuilder builder = setup();
        TocNode root = builder.getRootNodeInToc(page);

        assertTrue(root != null);
        assertTrue(root.getTitle().contentEquals("CloudHub"));
        assertTrue(root.getParent() == null);
        assertTrue(root.getChildren().size() > 1);
    }

    @Test
    public void setBreadcrumbHtmlForRootDocPage_WithValidRootNode_ReturnsEmptyContent() throws FileNotFoundException, IOException {
        AsciidocSerializer serializer = new AsciidocSerializer();
        DocPage page = serializer.getConvertedDocPageFromAsciidocFile(new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "cloudhub.ad" })));
        DocPage tocPage = serializer.getConvertedDocPageFromAsciidocFile(new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "toc.ad" })));
        PageBuilder builder = setup();
        builder.setDestinationFilePath(page);
        builder.setBreadcrumbHtml(tocPage, page);

        assertTrue(page.getBreadcrumbHtml().isEmpty());
    }

    @Test
    public void setBreadcrumbHtmlForChildDocPage_WithValidRootNode_ReturnsBreadcrumbContent() throws FileNotFoundException, IOException {
        AsciidocSerializer serializer = new AsciidocSerializer();
        DocPage page = serializer.getConvertedDocPageFromAsciidocFile(new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "deploying-a-cloudhub-application.ad" })));
        DocPage tocPage = serializer.getConvertedDocPageFromAsciidocFile(new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "toc.ad" })));
        PageBuilder builder = setup();
        builder.setDestinationFilePath(page);
        builder.setBreadcrumbHtml(tocPage, page);

        assertFalse(page.getBreadcrumbHtml().isEmpty());
        assertTrue(page.getBreadcrumbHtml().contains("Deploying a CloudHub Application"));
    }

    @Test
    public void setDestinationFilePath_WithValidDocPage_IsValid() {
        PageBuilder builder = setup();
        AsciidocSerializer serializer = new AsciidocSerializer();
        DocPage page = serializer.getConvertedDocPageFromAsciidocFile(new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "cloudhub.ad" })));
        builder.setDestinationFilePath(page);

        assertTrue(page.getSourceFilename().contentEquals("cloudhub.ad"));
        assertTrue(page.getFinalRelativeUrl().contentEquals("asciidoc-files-valid/cloudhub.html"));
    }

    @Test
    public void getRootNodeInToc_WithValidTocFile_ReturnsRootNode() throws IOException {
        PageBuilder builder = setup();
        AsciidocSerializer serializer = new AsciidocSerializer();
        DocPage page = serializer.getConvertedDocPageFromAsciidocFile(new File(TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "toc.ad" })));
        TocNode root = builder.getRootNodeInToc(page);

        assertTrue(root.getChildren().size() > 0);
        assertTrue(root.getParent() == null);
        assertTrue(root.getTitle().contentEquals("CloudHub"));
    }*/

    private boolean isFreshDocPageValid(DocPage page) {
        boolean isValid = true;
        if(page.getTitle().isEmpty()) { isValid = false; }
        if(page.getInitialContentHtml().isEmpty()) { isValid = false; }
        if(page.getSourceFilepath().isEmpty()) { isValid = false; }
        return isValid;
    }

}
