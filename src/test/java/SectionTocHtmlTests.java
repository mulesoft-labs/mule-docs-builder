import org.junit.Test;
import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.File;

/**
 * Created by sean.osterberg on 2/21/15.
 */
public class SectionTocHtmlTests {

    @Test
    public void getUnselectedTocFromRootNode_WithValidRootNode_IsValidObject() {
        SectionTocHtml html = SectionTocHtml.getUnselectedTocFromRootNode(getValidRootNode());
        assertThat(html, instanceOf(SectionTocHtml.class));
    }

    @Test(expected = DocBuildException.class)
    public void getUnselectedTocFromRootNode_WithInvalidRootNode_IsValidObject() {
        SectionTocHtml html = SectionTocHtml.getUnselectedTocFromRootNode(getInvalidRootNode());
    }

    @Test
    public void getUnselectedTocFromRootNode_WithValidRootNode_ReturnsValidHtml() {
        SectionTocHtml html = SectionTocHtml.getUnselectedTocFromRootNode(getValidRootNode());
        assertTrue(html.getHtml().length() > 500);
    }

    @Test
    public void getUnselectedTocFromRootNode_WithValidRootNode_DoesNotContainActiveNode() {
        SectionTocHtml html = SectionTocHtml.getUnselectedTocFromRootNode(getValidRootNode());
        assertFalse(html.getHtml().contains("active"));
    }

    @Test
    public void getSelectedTocFromRootNode_WithValidRootNode_ContainsActiveNode() {
        SectionTocHtml html = SectionTocHtml.getSelectedTocFromRootNode(getValidRootNode(), "cloudhub.html");
        assertTrue(html.getHtml().contains("<div class=\"toc-section-header active\">"));
        assertFalse(html.getHtml().contains("<li class=\"child active\">"));
    }

    @Test(expected = DocBuildException.class)
    public void getSelectedTocFromRootNode_WithValidRootNodeButInvalidActiveUrl_ThrowsException() {
        SectionTocHtml html = SectionTocHtml.getSelectedTocFromRootNode(getValidRootNode(), "foo.html");
    }

    @Test
    public void getSelectedTocFromRootNode_WithValidRootNode_ContainsActiveSectionNode() {
        SectionTocHtml html = SectionTocHtml.getSelectedTocFromRootNode(getValidRootNode(), "cloudhub-at-a-glance.html");
        assertFalse(html.getHtml().contains("<div class=\"toc-section-header active\">"));
        assertTrue(html.getHtml().contains("<a href=\"cloudhub-at-a-glance.html\"><li class=\"child active\">CloudHub at a Glance</li></a>"));
    }


    public TocNode getValidRootNode() {
        String tocPath = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "toc.ad"});
        SectionTableOfContents toc = SectionTableOfContents.fromAsciiDocFile(new File(tocPath));
        return toc.getRootTocNode();
    }

    public TocNode getInvalidRootNode() {
        String tocPath = Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "bad-files", "toc.ad"});
        SectionTableOfContents toc = SectionTableOfContents.fromAsciiDocFile(new File(tocPath));
        return toc.getRootTocNode();
    }
}
