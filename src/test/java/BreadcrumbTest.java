import org.junit.Test;
import org.mule.docs.Breadcrumb;
import org.mule.docs.DocBuildException;
import org.mule.docs.TocNode;
import org.mule.docs.Utilities;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Created by sean.osterberg on 2/22/15.
 */
public class BreadcrumbTest {

    @Test
    public void fromRootNode_WithValidRootNodeAndUrl_ReturnsObject() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode());
        assertThat(breadcrumb, instanceOf(Breadcrumb.class));
    }

    @Test(expected = DocBuildException.class)
    public void fromRootNode_WithInvalidRootNodeAndUrl_ThrowsException() {
        Breadcrumb.fromRootNode(TestUtilities.getInvalidRootNode());
    }

    @Test
    public void fromRootNode_WithValidRootNodeAndUrl_ContainsParentTopics() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode());
        assertTrue(breadcrumb.getHtmlForActiveUrl("cloudhub-at-a-glance", "").contains("CloudHub"));
        assertTrue(breadcrumb.getHtmlForActiveUrl("cloudhub-at-a-glance", "").contains("CloudHub at a Glance"));
    }

    @Test
    public void getActiveNode_WithValidRootNodeAndUrl_ReturnsDesiredNode() {
        Utilities.validateIfActiveUrlIsInSection(TestUtilities.getValidRootNode(), "cloudhub-at-a-glance");
        ArrayList<TocNode> activeNode = new ArrayList<TocNode>();
        Breadcrumb.getActiveNode(TestUtilities.getValidRootNode(), "cloudhub-at-a-glance", activeNode);
        assertTrue(activeNode.size() == 1);
        assertTrue(activeNode.get(0).getUrl().contentEquals("cloudhub-at-a-glance"));
    }

    @Test
    public void getBreadcrumbs_WithValidRootNodeAndUrl_ReturnsNodesInCorrectOrder() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode());
        List<TocNode> nodes = breadcrumb.getBreadcrumbs("cloudhub-at-a-glance");
        assertTrue(nodes.get(2).getUrl().contentEquals("cloudhub-at-a-glance"));
        assertTrue(nodes.get(1).getUrl().contentEquals("getting-started-with-cloudhub"));
        assertTrue(nodes.get(0).getUrl().contentEquals(""));
    }

    @Test
    public void getBreadcrumbs_WithValidRootNodeAndUrl_ReturnsCorrectHtml() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode());
        String html = breadcrumb.getHtmlForActiveUrl("cloudhub-managing-queues", "");
        String desiredResult = "<div class=\"breadcrumb-section\"><a href=\"./\">CloudHub</a>/"
                + "<a href=\"managing-cloudhub-applications\">Managing CloudHub Applications</a>/"
                + "<a href=\"cloudhub-managing-queues\" class=\"breadcrumb-active-link\">Managing Queues</a></div>";
        assertTrue(html.contentEquals(desiredResult));
    }

    @Test
    public void getBreadcrumbs_WithBaseUrl_ReturnsCorrectHtml() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode());
        String html = breadcrumb.getHtmlForActiveUrl("cloudhub-managing-queues", "/docs/cloudhub");
        String desiredResult = "<div class=\"breadcrumb-section\"><a href=\"/docs/cloudhub/./\">CloudHub</a>/"
                + "<a href=\"/docs/cloudhub/managing-cloudhub-applications\">Managing CloudHub Applications</a>/"
                + "<a href=\"/docs/cloudhub/cloudhub-managing-queues\" class=\"breadcrumb-active-link\">Managing Queues</a></div>";
        assertTrue(html.contentEquals(desiredResult));
    }

    @Test
    public void getBreadcrumbs_WithVersionSection_ReturnsCorrectHtml() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode());
        String html = breadcrumb.getHtmlForActiveUrl("cloudhub-managing-queues", "/docs/cloudhub/v/3.6");
        String desiredResult = "<div class=\"breadcrumb-section\"><a href=\"/docs/cloudhub/v/3.6/./\">CloudHub</a>/"
                + "<a href=\"/docs/cloudhub/v/3.6/managing-cloudhub-applications\">Managing CloudHub Applications</a>/"
                + "<a href=\"/docs/cloudhub/v/3.6/cloudhub-managing-queues\" class=\"breadcrumb-active-link\">Managing Queues</a></div>";
        assertTrue(html.contentEquals(desiredResult));
    }


    @Test(expected = DocBuildException.class)
    public void getBreadcrumbs_WithValidRootNodeAndInvalidUrl_ThrowsException() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode());
        List<TocNode> nodes = breadcrumb.getBreadcrumbs("foobar");
    }
}
