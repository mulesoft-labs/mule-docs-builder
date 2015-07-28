package org.mule.docs;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Created by sean.osterberg on 2/21/15.
 */
public class SectionTocHtmlTest {

    @Test
    public void getUnselectedTocFromRootNode_WithValidRootNode_IsValidObject() {
        SectionTocHtml html = SectionTocHtml.getUnselectedTocFromRootNode(TestUtilities.getValidRootNode(), "");
        assertThat(html, instanceOf(SectionTocHtml.class));
    }

    @Test(expected = DocBuildException.class)
    public void getUnselectedTocFromRootNode_WithInvalidRootNode_IsValidObject() {
        SectionTocHtml.getUnselectedTocFromRootNode(TestUtilities.getInvalidRootNode(), "");
    }

    @Test
    public void getUnselectedTocFromRootNode_WithValidRootNode_ReturnsValidHtml() {
        SectionTocHtml html = SectionTocHtml.getUnselectedTocFromRootNode(TestUtilities.getValidRootNode(), "");
        assertTrue(html.getHtml().length() > 500);
    }

    @Test
    public void getUnselectedTocFromRootNode_WithValidRootNode_DoesNotContainActiveNode() {
        SectionTocHtml html = SectionTocHtml.getUnselectedTocFromRootNode(TestUtilities.getValidRootNode(), "test");
        assertFalse(html.getHtml().contains("active"));
    }

    @Test
    public void getSelectedTocFromRootNode_WithValidRootNode_ContainsActiveNode() {
        SectionTocHtml html = SectionTocHtml.getSelectedTocFromRootNode(TestUtilities.getValidRootNode(), "", "");
        assertTrue(html.getHtml().contains("<li class=\"active expanded\">"));
    }

    @Test(expected = DocBuildException.class)
    public void getSelectedTocFromRootNode_WithValidRootNodeButInvalidActiveUrl_ThrowsException() {
        SectionTocHtml html = SectionTocHtml.getSelectedTocFromRootNode(TestUtilities.getValidRootNode(), "foo", "");
    }

    @Test
    public void getSelectedTocFromRootNode_WithValidRootNode_ContainsActiveSectionNode() {
        SectionTocHtml html = SectionTocHtml.getSelectedTocFromRootNode(TestUtilities.getValidRootNode(), "cloudhub-at-a-glance", "");
        assertTrue(html.getHtml().contains("<li class=\"active\"><i></i><a href=\"cloudhub-at-a-glance\">CloudHub at a Glance</a></li>"));
    }

    @Test
    public void getSelectedTocFromRootNode_ForSectionVersion_ReturnsDesiredVersionInLinkUrl() {
        SectionTocHtml html = SectionTocHtml.getSelectedTocFromRootNode(TestUtilities.getValidRootNode(), "cloudhub-at-a-glance", "/docs/cloudhub/v/4.0");
        assertTrue(html.getHtml().contains("<a href=\"/docs/cloudhub/v/4.0/\">CloudHub</a>"));
    }

}
