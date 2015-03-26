package org.mule.docs.model;

/**
 * Created by Mulesoft.
 */
public interface IPageElementVisitor {

    /**
     * Default visit method for all interfaces
     *
     * @param pageElement pageElement
     *
     * @return true if the child's need to be visited
     */
    boolean visit(IPageElement pageElement);

    /**
     * Visit site
     *
     * @param site site
     *
     * @return true if the child's need to be visited
     */
    boolean visit(Site site);

    /**
     * Visit asciiDocPage
     *
     * @param asciiDocPage asciiDocPage
     *
     * @return true if the child's need to be visited
     */
    boolean visit(AsciiDocPage asciiDocPage);

    /**
     * Visit page
     *
     * @param page page
     *
     * @return true if the child's need to be visited
     */
    boolean visit(Page page);

    /**
     * Visit the table of contents
     *
     * @param toc toc
     *
     * @return true if the child's need to be visited
     */
    boolean visit(SiteTableOfContents toc);

    /**
     * Visit a section
     *
     * @param section section
     *
     * @return true if the child's need to be visited
     */
    boolean visit(Section section);

    /**
     * Visit TocNode
     * @param rootNode node
     * @return true if the child's need to be visited
     */
    boolean visit(TocNode rootNode);
}
