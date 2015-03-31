package org.mule.docs.model.v2;

import org.apache.log4j.Logger;

/**
 * Created by Mulesoft.
 */
public class DefaultPageElementVisitor implements IPageElementVisitor {

    private static Logger logger = Logger.getLogger(DefaultPageElementVisitor.class);

    @Override
    public boolean visit(IPageElement pageElement) {
        logger.info("Visiting pageElement:" + pageElement);
        return true;
    }

    @Override public boolean visit(Site site) {
        logger.info("Visiting asciiDocPage:" + site);
        return true;
    }

    @Override
    public boolean visit(Page page) {
        logger.info("Visiting page:" + page);
        return true;
    }

    @Override public boolean visit(TableOfContents toc) {
        logger.info("Visiting toc:" + toc);
        return true;
    }

    @Override
    public boolean visit(Section section) {
        logger.info("Visiting section:" + section);
        return true;
    }

    @Override
    public boolean visit(TocNode rootNode) {
        logger.info("Visiting TocNode:" + rootNode);
        return true;
    }

    @Override public boolean visit(Asset asset) {
        logger.info("Visiting asset:" + asset);
        return true;
    }
}
