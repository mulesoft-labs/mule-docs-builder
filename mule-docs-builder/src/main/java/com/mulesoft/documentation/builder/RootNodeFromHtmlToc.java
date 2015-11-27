package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.model.TocNode;
import com.mulesoft.documentation.builder.util.Utilities;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by sean.osterberg on 2/20/15.
 */
public class RootNodeFromHtmlToc {
    private static Logger logger = Logger.getLogger(RootNodeFromHtmlToc.class);
    private TocNode rootNode;

    public RootNodeFromHtmlToc(TocNode rootNode) {
        validateInputParams(new Object[] {rootNode});
        this.rootNode = rootNode;
    }

    public static RootNodeFromHtmlToc fromTocAsciiDocPage(AsciiDocPage asciiDocPage) {
        validateThatAsciiDocPageIsToc(asciiDocPage);
        logger.info("Creating root node for TOC page \"" + asciiDocPage.getFilePath() + "\".");
        String contentHtml = Utilities.getOnlyContentDivFromHtml(asciiDocPage.getHtml());
        Document htmlToc = Jsoup.parse(contentHtml, "UTF-8");
        TocNode rootNode = getRootNodeFromRawTocHtml(htmlToc);
        return new RootNodeFromHtmlToc(rootNode);
    }

    public static TocNode getRootNodeFromRawTocHtml(Document doc) {
        Element firstElement = doc.select("ul").first();
        Element firstItem = firstElement.select("li").first();
        String parentLink = cleanupLink(firstItem.select("a").first().attr("href"));
        String parentTitle = firstItem.select("a").first().text();
        TocNode firstNode = new TocNode(parentLink, parentTitle, null);
        Elements firstChildren = firstItem.children().select("ul");
        validateOnlyOneRootNode(firstChildren);
        processToc(firstChildren.first(), firstNode);
        firstItem.remove();
        processToc(firstElement, firstNode);

        return firstNode;
    }

    private static void processToc(Element listElement, TocNode parentNode) {
        Element firstItem = listElement.select("li").first();
        if (firstItem == null) {
            return;
        }
        String parentLink = cleanupLink(firstItem.select("a").first().attr("href"));
        String parentTitle = firstItem.select("a").first().text();
        TocNode node = new TocNode(parentLink, parentTitle, parentNode);
        parentNode.addChild(node);
        Elements firstChildren = firstItem.children().select("ul");
        Elements listItems = firstChildren.select("li");

        if (firstChildren.size() > 1) {
            processToc(firstChildren.first(), node);
            firstItem.remove();
            processToc(listElement, parentNode);
        } else if (firstChildren.size() == 0) {
            firstItem.remove();
            processToc(listElement, parentNode);
        } else {
            processSection(listItems, node);
            firstItem.remove();
            processToc(listElement, parentNode);
        }
    }

    private static void processSection(Elements siblings, TocNode parentNode) {
        for (Element sibling : siblings) {
            String siblingLink = cleanupLink(sibling.select("a").first().attr("href"));
            String siblingTitle = sibling.select("a").first().text();
            TocNode siblingNode = new TocNode(siblingLink, siblingTitle, parentNode);
            parentNode.addChild(siblingNode);
        }
    }

    private static void validateOnlyOneRootNode(Elements elements) {
        if (elements.size() == 0) {
            String error = "TOC file has more than one root node and appears to be malformed.";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static String cleanupLink(String link) {
        if(link.contains("/")) {
            int index = link.lastIndexOf("/");

            link = link.substring(index + 1, link.length());
        }
        link = removeHtmlExtension(link);
        link = removeIndexFromLink(link);
        return link;
    }

    private static String removeHtmlExtension(String link) {
        return link.replace(".html", "");
    }

    private static String removeIndexFromLink(String link) {
        if (link.equals("index")) {
            link = "";
        }
        return link;
    }

    private void validateInputParams(Object[] params) {
        Utilities.validateCtorObjectsAreNotNull(params, RootNodeFromHtmlToc.class.getSimpleName());
    }

    private static void validateThatAsciiDocPageIsToc(AsciiDocPage page) {
        if (!page.getFilePath().endsWith("_toc.adoc")) {
            String error = "AsciiDoc page appears to be invalid for processing table of contents: \"" + page.getFilePath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public TocNode getRootNode() {
        return rootNode;
    }

}
