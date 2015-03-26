package org.mule.docs.model;

import org.apache.log4j.Logger;

/**
 * Created by Mulesoft.
 */
public class DefaultPageElementVisitor implements IPageElementVisitor {
    private static Logger logger = Logger.getLogger(DefaultPageElementVisitor.class);
    @Override
    public boolean visit(IPageElement pageElement) {
        logger.debug("Visiting pageElement:" + pageElement);
        return true;
    }

    @Override
    public boolean visit(Site site) {
        logger.debug("Visiting site:" + site);
        return true;
    }

    @Override
    public boolean visit(AsciiDocPage asciiDocPage) {
        logger.debug("Visiting asciiDocPage:" + asciiDocPage);
        return true;
    }

    @Override
    public boolean visit(Page page) {
        logger.debug("Visiting page:" + page);
        return true;
    }

    @Override
    public boolean visit(SiteTableOfContents toc) {
        logger.debug("Visiting toc:" + toc);
        return true;
    }

    @Override
    public boolean visit(Section section) {
        logger.debug("Visiting section:" + section);
        return true;
    }

    @Override
    public boolean visit(TocNode rootNode) {
        logger.debug("Visiting TocNode:" + rootNode);
        return true;
    }
}
