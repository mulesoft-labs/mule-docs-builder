import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

/**
 * Created by sean.osterberg on 2/6/15.
 */
public class PageBuilderTests {

    private PageBuilder setup() {
        String templateFilePath = TestHelpers.getPathForTestResourcesFile(new String[] { "template.html" });
        return new PageBuilder(templateFilePath, "");
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
        page.setContentHtml(content);
        page.setTocHtml(toc);
        page.setBreadcrumbHtml(breadcrumb);
        String result = builder.buildPageFromTemplate(page);
        assertTrue(result.contains(title));
        assertTrue(result.contains(content));
        assertTrue(result.contains(toc));
        assertTrue(result.contains(breadcrumb));
    }

    @Test(expected = IOException.class)
    public void buildPageFromTemplate_WithGivenTemplateAndInvalidPage_ThrowsException() throws IOException {
        PageBuilder builder = setup();
        DocPage page = new DocPage();
        String title = "aU2otg3oa7";
        String content = "YJts3zAknk";
        page.setTitle(title);
        page.setContentHtml(content);
        String result = builder.buildPageFromTemplate(page);
    }

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

    private boolean isFreshDocPageValid(DocPage page) {
        boolean isValid = true;
        if(page.getTitle().isEmpty()) { isValid = false; }
        if(page.getContentHtml().isEmpty()) { isValid = false; }
        if(page.getSourceFilePath().isEmpty()) { isValid = false; }
        return isValid;
    }

}
