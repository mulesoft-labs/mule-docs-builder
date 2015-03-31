package org.mule.docs.model.v2;

/**
 * Created by Mulesoft.
 */
public interface IPageElementVisitor {

    /**
     * Default visit method for all interfaces
     *
     * @param pageElement pageElement
     * @return true if the child's need to be visited
     */
    boolean visit(IPageElement pageElement);

    /**
     * Visit site
     *
     * @param site site
     * @return true if the child's need to be visited
     */
    boolean visit(Site site);

    /**
     * Visit page
     *
     * @param page page
     * @return true if the child's need to be visited
     */
    boolean visit(Page page);

    /**
     * Visit the table of contents
     *
     * @param toc toc
     * @return true if the child's need to be visited
     */
    boolean visit(TableOfContents toc);

    /**
     * Visit a section
     *
     * @param section section
     * @return true if the child's need to be visited
     */
    boolean visit(Section section);

    /**
     * Visit TocNode
     *
     * @param rootNode node
     * @return true if the child's need to be visited
     */
    boolean visit(TocNode rootNode);

    /**
     * Visit asset
     * @param asset asset
     * @return true if it should visit the child's
     */
    boolean visit(Asset asset);
}
