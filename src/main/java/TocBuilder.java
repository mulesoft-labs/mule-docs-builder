import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.select.*;
import org.jsoup.nodes.*;

import java.io.*;

/**
 * Table of contents builder for the Mule Doc site.
 *
 * @author Sean Osterberg
 * @version 1.0
 */
public class TocBuilder {
    private static Logger logger = Logger.getLogger(TocBuilder.class);
    private String baseUrl;
    private String tocFilepath;
    private TocNode rootNode;

    public TocBuilder(String baseUrl, String tocFilepath) {
        this.baseUrl = validateBaseUrlValid(baseUrl);
        this.tocFilepath = validateTocFilepath(tocFilepath);
    }

    public TocNode getRootNode() {
        if(rootNode == null) {
            Document doc = Utilities.getProcessedJsoupDocFromConvertedAsciiDocHtml(tocFilepath);
            rootNode = getRootNodeFromRawTocHtml(doc);
        }
        return rootNode;
    }

    public String getTocHtmlForActiveUrl(String activeUrl) {
        StringBuilder builder = new StringBuilder();
        generateTocHtml(getRootNode(), builder, true, baseUrl + activeUrl);
        return builder.toString();
    }

    public String getTocHtmlForInactiveSection() {
        return getTocHtmlForActiveUrl("");
    }


    private TocNode getRootNodeFromRawTocHtml(Document doc) {
        Element firstElement = doc.select("ul").first();
        Element firstItem = firstElement.select("li").first();
        String parentLink = baseUrl + firstItem.select("a").first().attr("href");
        String parentTitle = firstItem.select("a").first().text();
        TocNode firstNode = new TocNode(parentLink, parentTitle, null);
        Elements firstChildren = firstItem.children().select("ul");
        if(firstChildren.size() == 0) {
            String error = "TOC does not have a single parent node.";
            logger.fatal(error);
            throw new DocBuilderException(error);
        }
        processToc(firstChildren.first(), firstNode);
        firstItem.remove();
        processToc(firstElement, firstNode);

        return firstNode;
    }

    private void processToc(Element listElement, TocNode parentNode) {
        Element firstItem = listElement.select("li").first();
        if(firstItem == null) {
            return;
        }
        String parentLink = baseUrl + firstItem.select("a").first().attr("href");
        String parentTitle = firstItem.select("a").first().text();
        TocNode node = new TocNode(parentLink, parentTitle, parentNode);
        parentNode.addChild(node);
        Elements firstChildren = firstItem.children().select("ul");
        Elements listItems = firstChildren.select("li");

        if(firstChildren.size() > 1) {
            processToc(firstChildren.first(), node);
            firstItem.remove();
            processToc(listElement, parentNode);
        } else if(firstChildren.size() == 0) {
            firstItem.remove();
            processToc(listElement, parentNode);
        } else {
            processSection(listItems, node);
            firstItem.remove();
            processToc(listElement, parentNode);
        }
    }

    private void processSection(Elements siblings, TocNode parentNode) {
        for (Element sibling : siblings) {
            String siblingLink = baseUrl + sibling.select("a").first().attr("href");
            String siblingTitle = sibling.select("a").first().text();
            TocNode siblingNode = new TocNode(siblingLink, siblingTitle, parentNode);
            parentNode.addChild(siblingNode);
        }
    }

    private void generateTocHtml(TocNode parent, StringBuilder html, boolean isFirstItem, String activeUrl) {
        String sectionId = Utilities.getRandomAlphaNumericString(8);

        if(parent.getUrl().equalsIgnoreCase(activeUrl)) {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header active\"><a href=\"");
            html.append(parent.getUrl() + "\" style=\"color: white\">" + parent.getTitle() + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse in\" id=\"" + sectionId + "\">");
        } else if(isFirstItem && !activeUrl.isEmpty()) {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header\"><a href=\"");
            html.append(parent.getUrl() + "\">" + parent.getTitle() + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse in\" id=\"" + sectionId + "\" style=\"height: 0px;\">");
        } else if (isActiveTopicInSection(parent, activeUrl)) {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header child\"><a href=\"");
            html.append(parent.getUrl() + "\">" + parent.getTitle() + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse in\" id=\"" + sectionId + "\">");
        } else {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header child\"><a href=\"");
            html.append(parent.getUrl() + "\">" + parent.getTitle() + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse\" id=\"" + sectionId + "\" style=\"height: 0px;\">");
        }

        if(parent.getChildren().size() == 0) {
            return;
        } else {
            for(TocNode child : parent.getChildren()) {
                if(child.getChildren().size() > 0) {
                    generateTocHtml(child, html, false, activeUrl);
                } else {
                    html.append("<a href=\"" + child.getUrl() + "\"><li class=\"child");
                    if(activeUrl.equalsIgnoreCase(child.getUrl())) {
                        html.append(" active");
                    }
                    html.append("\">" + child.getTitle() + "</li></a>");
                }
            }
        }
        html.append("</ul></li>");
    }


    private boolean isActiveTopicInSection(TocNode parentNode, String activeUrl) {
        for(TocNode node : parentNode.getChildren()) {
            if(node.getUrl().equalsIgnoreCase(activeUrl)) {
                return true;
            }
            else if(node.getChildren().size() > 0) {
                isActiveTopicInSection(node, activeUrl);
            }
        }
        return false;
    }

    private String validateTocFilepath(String tocFilepath) {
        File tocFile = new File(tocFilepath);
        if(!tocFile.exists()) {
            String error = "TOC file does not exist at specified path: \"" + tocFilepath + "\".";
            logger.fatal(error);
            throw new DocBuilderException(error);
        } else if(!Utilities.fileEndsWithValidAsciidocExtension(tocFilepath)) {
            String error = "TOC file does not end with valid AsciiDoc extension: \"" + tocFilepath + "\".";
            logger.fatal(error);
            throw new DocBuilderException(error);
        } else if (!FilenameUtils.getBaseName(tocFilepath).contentEquals("toc")) {
            String error = "TOC file is not named \"toc\" and may not contain required structure. Specified file: \"" + tocFilepath + "\".";
            logger.fatal(error);
            throw new DocBuilderException(error);
        } else {
            return tocFilepath;
        }
    }

    private String validateBaseUrlValid(String baseUrl) {
        if(baseUrl != null) {
            if(baseUrl.endsWith("/")) {
                return baseUrl;
            } else {
                return baseUrl + "/";
            }
        } else {
            String error = "BaseUrl for TocBuilder cannot be null.";
            logger.fatal(error);
            throw new DocBuilderException(error);
        }
    }

    public void printNodesInToc(TocNode parent, int indent) {
        for(int i = 0; i < indent; i++) {
            logger.info('\t');
        }
        logger.info(parent.getTitle());
        if(parent.getChildren().size() == 0) {
            return;
        } else {
            for(TocNode child : parent.getChildren()) {
                if(child.getChildren().size() > 0) {
                    printNodesInToc(child, indent + 1);
                } else {
                    int updated = indent + 1;
                    for(int i = 0; i < updated; i++) {
                        logger.info('\t');
                    }
                    logger.info(child.getTitle());
                }
            }
        }
    }
}
