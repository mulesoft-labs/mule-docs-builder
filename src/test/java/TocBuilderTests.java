import org.junit.*;
import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.select.*;
import org.jsoup.nodes.*;

/**
 * Created by sean.osterberg on 2/9/15.
 */
public class TocBuilderTests {
    TocBuilder tocBuilder;

    @Test(expected = DocBuilderException.class)
    public void TocBuilder_WithInvalidBaseUrl_ThrowsException() {
        TocBuilder builder = new TocBuilder(null, getTocFilepathTestResources());
    }

    @Test(expected = DocBuilderException.class)
    public void TocBuilder_WithInvalidTocFileExtension_ThrowsException() {
        TocBuilder builder = new TocBuilder("", getTocFilepathTestResources() + "d");
    }

    @Test(expected = DocBuilderException.class)
    public void TocBuilder_WithInvalidTocFilename_ThrowsException() {
        TocBuilder builder = new TocBuilder("", TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "cloudhub", "cloudhub.ad" }));
    }

    @Test
    public void TocBuilder_WithValidBaseUrlAndFilename_CreatesInstance() {
        TocBuilder builder = new TocBuilder("", getTocFilepathTestResources());
        assertTrue(builder != null);
    }

    @Test
    public void getRootNode_WithValidBaseUrlAndFilename_ReturnsRootNode() {
        TocBuilder builder = getTocBuilderForTocFileInTestResources();
        TocNode root = builder.getRootNode();
        assertTrue(root != null);
        assertTrue(root.getChildren().size() > 0);
        assertTrue(root.getTitle().contentEquals("CloudHub"));
    }

    @Test
    public void getRootNode_WithLongerBaseUrl_ReturnsRootNodeWithBaseUrl() {
        TocBuilder builder = new TocBuilder("cloudhub/", TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "cloudhub", "toc.ad" }));
        TocNode root = builder.getRootNode();
        assertTrue(root != null);
        assertTrue(root.getUrl().contentEquals("cloudhub/cloudhub.html"));
    }


    @Test
    public void getRootNode_WithEvenLongerBaseUrl_ReturnsRootNodeWithBaseUrl() {
        TocBuilder builder = new TocBuilder("cloudhub/v/r41/", TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "cloudhub", "toc.ad" }));
        TocNode root = builder.getRootNode();
        assertTrue(root != null);
        assertTrue(root.getUrl().contentEquals("cloudhub/v/r41/cloudhub.html"));
    }

    @Test
         public void getTocHtmlForActiveUrl_WithActiveUrlAsSectionHeader_ReturnsCorrectActiveUrl() {
        TocBuilder builder = getTocBuilderForTocFileInTestResources();
        String html = builder.getTocHtmlForActiveUrl("cloudhub.html");
        assertTrue(html.contains("<div class=\"toc-section-header active\">"));
        assertFalse(html.contains("<li class=\"child active\">"));
    }

    @Test
    public void getTocHtmlForActiveUrl_WithActiveUrlAsChildItem_ReturnsCorrectActiveUrl() {
        TocBuilder builder = getTocBuilderForTocFileInTestResources();
        String html = builder.getTocHtmlForActiveUrl("cloudhub-at-a-glance.html");
        assertFalse(html.contains("<div class=\"toc-section-header active\">"));
        assertTrue(html.contains("<a href=\"/cloudhub-at-a-glance.html\"><li class=\"child active\">CloudHub at a Glance</li></a>"));
    }

    @Test
    public void getTocHtmlForInactiveSection_ReturnsNoSelectedSectionsOrUrls() {
        TocBuilder builder = getTocBuilderForTocFileInTestResources();
        String html = builder.getTocHtmlForInactiveSection();
        assertFalse(html.contains("<div class=\"toc-section-header active\">"));
        assertFalse(html.contains("<li class=\"child active\">"));
    }

    public String getTocFilepathTestResources() {
        return TestHelpers.getPathForTestResourcesFile(new String[] { "asciidoc-files-valid", "cloudhub", "toc.ad" });
    }

    // Getting the root node is an expensive IO operation because it reads file and processes it.
    public TocBuilder getTocBuilderForTocFileInTestResources() {
        if(tocBuilder == null) {
            tocBuilder = new TocBuilder("", TestHelpers.getPathForTestResourcesFile(new String[]{"asciidoc-files-valid", "cloudhub", "toc.ad"}));
            tocBuilder.getRootNode();
        }
        return tocBuilder;
    }
}
