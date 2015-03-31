package org.mule.docs.internal.writer;

import org.jsoup.Jsoup;
import org.mule.docs.model.v2.*;
import org.mule.docs.processor.AsciiDocProcessor;
import org.mule.docs.writer.ISiteWriter;

import java.io.File;

/**
 * Created by Mulesoft.
 */
public class DefaultSiteWriter implements ISiteWriter, IPageElementVisitor {

    private File outputDirectory;

    @Override
    public boolean write(Site site, File outputDirectory) {
        this.outputDirectory = outputDirectory;
        site.accept(this);
        return true;
    }

    @Override public boolean visit(IPageElement pageElement) {
        return false;
    }

    @Override public boolean visit(Site site) {
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }
        return true;
    }

    @Override
    public boolean visit(Page page) {
        String html = AsciiDocProcessor.getProcessorInstance().convertFile(page.getOrigin());
        System.out.println(getPageTitle(html));
        return true;
    }

    String getPageTitle(String html) {
        return Jsoup.parse(html, "UTF-8").title();
    }

    @Override
    public boolean visit(TableOfContents toc) {
        return true;
    }

    @Override
    public boolean visit(Section section) {
        return true;
    }

    @Override
    public boolean visit(TocNode rootNode) {
        return true;
    }

    @Override public boolean visit(Asset asset) {
        return true;
    }
}
