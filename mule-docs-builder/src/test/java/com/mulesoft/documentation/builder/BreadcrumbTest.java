package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.util.Utilities;
import org.junit.Test;

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
        assertEquals(1, activeNode.size());
        assertTrue(activeNode.get(0).getUrl().equals("cloudhub-at-a-glance"));
    }

    @Test
    public void getBreadcrumbs_WithValidRootNodeAndUrl_ReturnsNodesInCorrectOrder() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode());
        List<TocNode> nodes = breadcrumb.getBreadcrumbs("cloudhub-at-a-glance");
        assertTrue(nodes.get(2).getUrl().equals("cloudhub-at-a-glance"));
        assertTrue(nodes.get(1).getUrl().equals("getting-started-with-cloudhub"));
        assertTrue(nodes.get(0).getUrl().equals(""));
    }

    @Test
    public void getBreadcrumbs_WithValidRootNodeAndUrl_ReturnsCorrectHtml() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode());
        String html = breadcrumb.getHtmlForActiveUrl("cloudhub-managing-queues", "");
        String desiredResult = "<div class=\"breadcrumb-section\" data-swiftype-index='false'><a href=\"./\">CloudHub</a>/"
                + "<a href=\"managing-cloudhub-applications\">Managing CloudHub Applications</a>/"
                + "<a href=\"cloudhub-managing-queues\" class=\"breadcrumb-active-link\">Managing Queues</a></div>";
        assertTrue(html.equals(desiredResult));
    }

    @Test
    public void getBreadcrumbs_WithBaseUrl_ReturnsCorrectHtml() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode());
        String html = breadcrumb.getHtmlForActiveUrl("cloudhub-managing-queues", "/docs/cloudhub");
        String desiredResult = "<div class=\"breadcrumb-section\" data-swiftype-index='false'><a href=\"/docs/cloudhub/./\">CloudHub</a>/"
                + "<a href=\"/docs/cloudhub/managing-cloudhub-applications\">Managing CloudHub Applications</a>/"
                + "<a href=\"/docs/cloudhub/cloudhub-managing-queues\" class=\"breadcrumb-active-link\">Managing Queues</a></div>";
        assertTrue(html.equals(desiredResult));
    }

    @Test
    public void getBreadcrumbs_WithVersionSection_ReturnsCorrectHtml() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode());
        String html = breadcrumb.getHtmlForActiveUrl("cloudhub-managing-queues", "/docs/cloudhub/v/3.6");
        String desiredResult = "<div class=\"breadcrumb-section\" data-swiftype-index='false'><a href=\"/docs/cloudhub/v/3.6/./\">CloudHub</a>/"
                + "<a href=\"/docs/cloudhub/v/3.6/managing-cloudhub-applications\">Managing CloudHub Applications</a>/"
                + "<a href=\"/docs/cloudhub/v/3.6/cloudhub-managing-queues\" class=\"breadcrumb-active-link\">Managing Queues</a></div>";
        assertTrue(html.equals(desiredResult));
    }


    @Test(expected = DocBuildException.class)
    public void getBreadcrumbs_WithValidRootNodeAndInvalidUrl_ThrowsException() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode());
        List<TocNode> nodes = breadcrumb.getBreadcrumbs("foobar");
    }
}
