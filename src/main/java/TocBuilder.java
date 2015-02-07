/**
 * Created by sean.osterberg on 1/18/15.
 */

import org.jsoup.select.*;
import org.jsoup.nodes.*;

import java.io.IOException;
import java.util.*;

public class TocBuilder {

    public String outputDirectoryForConvertedHtml = "/Users/sean.osterberg/Documents/MuleSoft Docs/New Doc Site/converted-output/";

    public void generateHtmlToc(Document doc) {


        /*TocNode firstNode = processToc(doc);

        // printNodesInToc(firstNode, 0);

        StringBuilder tocHtml = getTocHtml(firstNode, firstNode.children.get(2).url);
        StringBuilder breadcrumbHtml = getBreadcrumbsForTopic(firstNode, firstNode.children.get(2).url);

        System.out.println(tocHtml.toString());
        System.out.println(breadcrumbHtml.toString());
        */
    }

    public TocNode processToc(Document doc) throws IOException {
        Element firstElement = doc.select("ul").first();
        Element firstItem = firstElement.select("li").first();
        String parentLink = firstItem.select("a").first().attr("href");
        String parentTitle = firstItem.select("a").first().text();
        TocNode firstNode = new TocNode(parentLink, parentTitle, null);
        Elements firstChildren = firstItem.children().select("ul");
        if(firstChildren.size() == 0) {
            throw new IOException("TOC does not have a single parent node.");
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
        String parentLink = firstItem.select("a").first().attr("href");
        String parentTitle = firstItem.select("a").first().text();

        TocNode node = new TocNode(parentLink, parentTitle, parentNode);
        parentNode.children.add(node);

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
            String siblingLink = sibling.select("a").first().attr("href");
            String siblingTitle = sibling.select("a").first().text();

            // Add the next sibling as a child of the parent node
            TocNode siblingNode = new TocNode(siblingLink, siblingTitle, parentNode);
            parentNode.children.add(siblingNode);
        }
    }

    public void printTocNodes(TocNode parent) {
        printNodesInToc(parent, 0);
    }

    private void printNodesInToc(TocNode parent, int indent) {
        for(int i = 0; i < indent; i++) {
            System.out.print('\t');
        }
        System.out.println(parent.title);
        if(parent.children.size() == 0) {
            return;
        } else {
            for(TocNode child : parent.children) {
                if(child.children.size() > 0) {
                    printNodesInToc(child, indent + 1);
                } else {
                    int updated = indent + 1;
                    for(int i = 0; i < updated; i++) {
                        System.out.print('\t');
                    }
                    System.out.println(child.title);
                }
            }
        }
    }

    public StringBuilder getTocHtml(TocNode parent, String activeUrl) {
        StringBuilder builder = new StringBuilder();
        generateTocHtml(parent, builder, true, activeUrl);
        return builder;
    }

    private void generateTocHtml(TocNode parent, StringBuilder html, boolean isFirstItem, String activeUrl) {
        Random random = new Random();
        short sectionId = (short) random.nextInt(Short.MAX_VALUE + 1);
        boolean isInSection = isActiveTopicInSection(parent, activeUrl);

        if(parent.url.equalsIgnoreCase(activeUrl)) {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header active\"><a href=\"");
            html.append(parent.url + "\" style=\"color: white\">" + parent.title + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse in\" id=\"" + sectionId + "\">");
        } else if(isFirstItem && !activeUrl.isEmpty()) {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header\"><a href=\"");
            html.append(parent.url + "\">" + parent.title + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse in\" id=\"" + sectionId + "\">");
        } else if (isFirstItem) {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header\"><a href=\"");
            html.append(parent.url + "\">" + parent.title + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse\" id=\"" + sectionId + "\" style=\"height: 0px;\">");
        } else if (isActiveTopicInSection(parent, activeUrl)) {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header child\"><a href=\"");
            html.append(parent.url + "\">" + parent.title + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse in\" id=\"" + sectionId + "\">");
        } else {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header child\"><a href=\"");
            html.append(parent.url + "\">" + parent.title + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse\" id=\"" + sectionId + "\" style=\"height: 0px;\">");
        }

        if(parent.children.size() == 0) {
            return;
        } else {
            for(TocNode child : parent.children) {
                if(child.children.size() > 0) {
                    generateTocHtml(child, html, false, activeUrl);
                } else {
                    html.append("<a href=\"" + child.url + "\"><li class=\"child");
                    if(activeUrl.equalsIgnoreCase(child.url)) {
                        html.append(" active");
                    }
                    html.append("\">" + child.title + "</li></a>");
                }
            }
        }
        html.append("</ul></li>");
    }

    private boolean isActiveTopicInSection(TocNode parentNode, String activeUrl) {
        for(TocNode node : parentNode.children) {
            if(node.url.equalsIgnoreCase(activeUrl)) {
                return true;
            }
            else if(node.children.size() > 0) {
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
        for(TocNode node : parentNode.children) {
            if(node.url.equalsIgnoreCase(activeUrl)) {
                html.append("<ol class=\"breadcrumb\">");

                generateParentBreadcrumbsForTopic(node, html);
                html.append("<li class=\"active\">" + node.title + "</li>");
                html.append("</ol>");
            }
            else if(node.children.size() > 0) {
                generateBreadcrumbsForTopic(node, activeUrl, html);
            }
        }
    }

    private void generateParentBreadcrumbsForTopic(TocNode node, StringBuilder html) {
        TocNode immediateParent = node.parent;
        if(immediateParent != null) {
            generateParentBreadcrumbsForTopic(immediateParent, html);
        } else {
            return;
        }
        html.append("<li><a href=\"" + immediateParent.url + "\">" + immediateParent.title + "</a></li>");
    }

    /*
    public void recursiveTocGenerator(Element listElement, TocNode parentNode) {
        // Get the first item in the section
        Element firstItem = listElement.select("li").first();
        String parentLink = firstItem.select("a").first().attr("href");
        String parentTitle = firstItem.select("a").first().text();
        firstItem.remove();

        TocNode node = new TocNode(parentLink, parentTitle, parentNode);
        parentNode.children.add(node);

        Element secondItem = listElement.select("li").first();
        String secondLink = secondItem.select("a").first().attr("href");
        String secondTitle = secondItem.select("a").first().text();

        TocNode secondNode = new TocNode(secondLink, secondTitle, node);
        node.children.add(secondNode);

        // If the <li> contains a <ul>, get the <li>s beneath

        Elements secondChildren = secondItem.children().select("ul");
        if(secondChildren.size() > 1) {

        } else if (secondChildren.size() == 0){

        } else {
            // It's a section without any subsections
            Elements listItems = secondChildren.select("li");
            sectionProcessor(listItems, secondNode);
            secondItem.remove();
        }

        // ------ END SECOND -----------
        // ------ THIRD -----------

        Element thirdItem = listElement.select("li").first();
        String thirdLink = thirdItem.select("a").first().attr("href");
        String thirdTitle = thirdItem.select("a").first().text();

        TocNode thirdNode = new TocNode(thirdLink, thirdTitle, secondNode);
        node.children.add(thirdNode);

        Elements thirdChildren = thirdItem.children().select("ul");
        if(thirdChildren.size() > 1) {
        } else if(thirdChildren.size() == 0) {
            thirdItem.remove();
        } else {
            // It's a section without any subsections
            Elements listItems = thirdChildren.select("li");
            sectionProcessor(listItems, thirdNode);
            thirdItem.remove();
        }

        // ------- END THIRD ------------
        // ------- START FOURTH ---------

        Element fourthItem = listElement.select("li").first();
        String fourthLink = fourthItem.select("a").first().attr("href");
        String fourthTitle = fourthItem.select("a").first().text();

        TocNode fourthNode = new TocNode(fourthLink, fourthTitle, thirdNode);
        node.children.add(fourthNode);

        Elements fourthChildren = fourthItem.children().select("ul");
        if(fourthChildren.size() > 1) {
        } else if (fourthChildren.size() == 0) {
            fourthItem.remove();
        } else {
            // It's a section without any subsections
            Elements listItems = fourthChildren.select("li");
            sectionProcessor(listItems, fourthNode);
            fourthItem.remove();
        }

        // ------- END FOURTH ------------
        // ------- START FIFTH ---------

        Element fifthItem = listElement.select("li").first();
        String fifthLink = fifthItem.select("a").first().attr("href");
        String fifthTitle = fifthItem.select("a").first().text();

        TocNode fifthNode = new TocNode(fifthLink, fifthTitle, fourthNode);
        node.children.add(fifthNode);

        Elements fifthChildren = fifthItem.children().select("ul");
        if(fifthChildren.size() > 1) {
        } else if (fifthChildren.size() == 0) {
            fifthItem.remove();
        } else {
            // It's a section without any subsections
            Elements listItems = fifthChildren.select("li");
            sectionProcessor(listItems, fifthNode);
            fifthItem.remove();
        }

        // ------- END FIFTH ---------
        // ------- START SIXTH ---------


        Element sixthItem = listElement.select("li").first();
        String sixthLink = sixthItem.select("a").first().attr("href");
        String sixthTitle = sixthItem.select("a").first().text();

        TocNode sixthNode = new TocNode(sixthLink, sixthTitle, fifthNode);
        node.children.add(sixthNode);

        Elements sixthChildren = sixthItem.children().select("ul");
        if(sixthChildren.size() > 1) {
        } else if (sixthChildren.size() == 0) {
            sixthItem.remove();
        } else {
            // It's a section without any subsections
            Elements listItems = sixthChildren.select("li");
            sectionProcessor(listItems, sixthNode);
            sixthItem.remove();
        }

        // ------- END SIXTH ---------

        // ------- START SEVENTH ---------

        Element seventhItem = listElement.select("li").first();
        String seventhLink = seventhItem.select("a").first().attr("href");
        String seventhTitle = seventhItem.select("a").first().text();

        TocNode seventhNode = new TocNode(seventhLink, seventhTitle, sixthNode);
        node.children.add(seventhNode);

        Elements seventhChildren = seventhItem.children().select("ul");
        if(seventhChildren.size() > 1) {
            Element eighthItem = seventhChildren.select("li").first();
            String eighthLink = eighthItem.select("a").first().attr("href");
            String eighthTitle = eighthItem.select("a").first().text();

            TocNode eighthNode = new TocNode(eighthLink, eighthTitle, seventhNode);
            seventhNode.children.add(eighthNode);

            Elements eighthChildren = eighthItem.children().select("ul");
            if(eighthChildren.size() > 1) {
            } else if (eighthChildren.size() == 0) {
                eighthItem.remove();
            } else {
                // It's a section without any subsections
                Elements listItems = eighthChildren.select("li");
                sectionProcessor(listItems, eighthNode);
                eighthItem.remove();
            }

        } else if (seventhChildren.size() == 0) {
            seventhItem.remove();
        } else {
            // It's a section without any subsections
            Elements listItems = seventhChildren.select("li");
            sectionProcessor(listItems, sixthNode);
            sixthItem.remove();
        }

        System.out.println();

    }*/
}
