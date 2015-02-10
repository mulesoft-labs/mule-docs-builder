import org.apache.log4j.Logger;
import org.jsoup.select.*;
import org.jsoup.nodes.*;
import java.io.IOException;
import java.util.*;

/**
 * Table of contents builder for the Mule Doc site.
 *
 * @author Sean Osterberg
 * @version 1.0
 */
public class TocBuilder {
    private String baseUrl;
    private static Logger logger = Logger.getLogger(TocBuilder.class);

    public TocBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public TocNode getRootNodeFromProcessedToc(Document doc) throws IOException {
        Element firstElement = doc.select("ul").first();
        Element firstItem = firstElement.select("li").first();
        String parentLink = baseUrl + firstItem.select("a").first().attr("href");
        String parentTitle = firstItem.select("a").first().text();
        TocNode firstNode = new TocNode(parentLink, parentTitle, null);
        Elements firstChildren = firstItem.children().select("ul");
        if(firstChildren.size() == 0) {
            String error = "TOC does not have a single parent node.";
            logger.fatal(error);
            throw new IOException(error);
        }
        tocProcessor(firstChildren.first(), firstNode);
        firstItem.remove();
        tocProcessor(firstElement, firstNode);

        return firstNode;
    }

    private void tocProcessor(Element listElement, TocNode parentNode) {
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
            tocProcessor(firstChildren.first(), node);
            firstItem.remove();
            tocProcessor(listElement, parentNode);
        } else if(firstChildren.size() == 0) {
            firstItem.remove();
            tocProcessor(listElement, parentNode);
        } else {
            sectionProcessor(listItems, node);
            firstItem.remove();
            tocProcessor(listElement, parentNode);
        }
    }

    private void sectionProcessor(Elements siblings, TocNode parentNode) {
        for (Element sibling : siblings) {
            // Get the next sibling's url and title
            String siblingLink = baseUrl + sibling.select("a").first().attr("href");
            String siblingTitle = sibling.select("a").first().text();

            // Add the next sibling as a child of the parent node
            TocNode siblingNode = new TocNode(siblingLink, siblingTitle, parentNode);
            parentNode.addChild(siblingNode);
        }
    }

    public void printTocNodes(TocNode parent) {
        printNodesInToc(parent, 0);
    }

    private void printNodesInToc(TocNode parent, int indent) {
        for(int i = 0; i < indent; i++) {
            System.out.print('\t');
        }
        System.out.println(parent.getTitle());
        if(parent.getChildren().size() == 0) {
            return;
        } else {
            for(TocNode child : parent.getChildren()) {
                if(child.getChildren().size() > 0) {
                    printNodesInToc(child, indent + 1);
                } else {
                    int updated = indent + 1;
                    for(int i = 0; i < updated; i++) {
                        System.out.print('\t');
                    }
                    System.out.println(child.getTitle());
                }
            }
        }
    }

    public StringBuilder getTocHtml(TocNode parent, String activeUrl) {
        StringBuilder builder = new StringBuilder();
        generateTocHtml(parent, builder, true, baseUrl + activeUrl);
        return builder;
    }

    private String getRandomString(int length) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(length);
        for( int i = 0; i < length; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    private void generateTocHtml(TocNode parent, StringBuilder html, boolean isFirstItem, String activeUrl) {
        Random random = new Random();
        String sectionId = getRandomString(8);
        boolean isInSection = isActiveTopicInSection(parent, activeUrl);

        if(parent.getUrl().equalsIgnoreCase(activeUrl)) {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header active\"><a href=\"");
            html.append(parent.getUrl() + "\" style=\"color: white\">" + parent.getTitle() + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse in\" id=\"" + sectionId + "\">");
        } else if(isFirstItem && !activeUrl.isEmpty()) {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header\"><a href=\"");
            html.append(parent.getUrl() + "\">" + parent.getTitle() + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse in\" id=\"" + sectionId + "\">");
        } else if (isFirstItem) {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header\"><a href=\"");
            html.append(parent.getUrl() + "\">" + parent.getTitle() + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse\" id=\"" + sectionId + "\" style=\"height: 0px;\">");
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

    public StringBuilder getBreadcrumbsForTopic(TocNode parentNode, String activeUrl) {
        StringBuilder builder = new StringBuilder();
        generateBreadcrumbsForTopic(parentNode, activeUrl, builder);
        return builder;
    }

    private void generateBreadcrumbsForTopic(TocNode parentNode, String activeUrl, StringBuilder html) {
        for(TocNode node : parentNode.getChildren()) {
            if(node.getUrl().equalsIgnoreCase(activeUrl)) {
                html.append("<ol class=\"breadcrumb\">");

                generateParentBreadcrumbsForTopic(node, html);
                html.append("<li class=\"active\">" + node.getTitle() + "</li>");
                html.append("</ol>");
            }
            else if(node.getChildren().size() > 0) {
                generateBreadcrumbsForTopic(node, activeUrl, html);
            }
        }
    }

    private void generateParentBreadcrumbsForTopic(TocNode node, StringBuilder html) {
        TocNode immediateParent = node.getParent();
        if(immediateParent != null) {
            generateParentBreadcrumbsForTopic(immediateParent, html);
        } else {
            return;
        }
        html.append("<li><a href=\"" + immediateParent.getUrl() + "\">" + immediateParent.getTitle() + "</a></li>");
    }
}
