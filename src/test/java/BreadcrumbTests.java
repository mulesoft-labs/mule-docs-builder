import org.junit.Test;
import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Created by sean.osterberg on 2/22/15.
 */
public class BreadcrumbTests {

    @Test
    public void fromRootNode_WithValidRootNodeAndUrl_ReturnsObject() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode(), "cloudhub-at-a-glance");
        assertThat(breadcrumb, instanceOf(Breadcrumb.class));
    }

    @Test(expected = DocBuildException.class)
    public void fromRootNode_WithInvalidRootNodeAndUrl_ThrowsException() {
        Breadcrumb.fromRootNode(TestUtilities.getInvalidRootNode(), "cloudhub-at-a-glance");
    }

    @Test
    public void fromRootNode_WithValidRootNodeAndUrl_ContainsParentTopics() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode(), "cloudhub-at-a-glance");
        assertTrue(breadcrumb.getHtml().contains("MuleSoft Docs"));
        assertTrue(breadcrumb.getHtml().contains("CloudHub"));
        assertTrue(breadcrumb.getHtml().contains("CloudHub at a Glance"));
    }

    @Test
    public void fromRootNode_WithValidRootNodeAndUrl_ReturnsUrlAsActive() {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(TestUtilities.getValidRootNode(), "cloudhub-at-a-glance");
        assertTrue(breadcrumb.getHtml().contains("active"));
    }

    @Test(expected = DocBuildException.class)
    public void fromRootNode_WithValidRootNodeAndInvalidUrl_ThrowsException() {
        Breadcrumb.fromRootNode(TestUtilities.getValidRootNode(), "foo");
    }
}
