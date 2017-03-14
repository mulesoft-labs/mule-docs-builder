package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.model.TocNode;
import com.mulesoft.documentation.builder.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sean.osterberg on 2/21/15.
 */
public class SectionTocHtml {
    private static Logger logger = LoggerFactory.getLogger(SectionTocHtml.class);
    private String html;

    public SectionTocHtml(String html) {
        this.html = html;
    }

    public static SectionTocHtml getUnselectedTocFromRootNode(TocNode rootNode, String baseUrl) {
        return getTocFromRootNode(rootNode, null, baseUrl);
    }

    public static SectionTocHtml getSelectedTocFromRootNode(TocNode rootNode, String url, String baseUrl) {
        return getTocFromRootNode(rootNode, url, baseUrl);
    }

    private static SectionTocHtml getTocFromRootNode(TocNode rootNode, String url, String baseUrl) {
        validateInputParams(new Object[] {rootNode});
        StringBuilder builder = new StringBuilder();
        if (url != null) {
            Utilities.validateIfActiveUrlIsInSection(rootNode, url);
        }
        generateTocHtml(rootNode, builder, true, url, baseUrl);
        return new SectionTocHtml(builder.toString());
    }

    public String getHtml() {
        return html;
    }

    private static void generateTocHtml(TocNode parent, StringBuilder html, boolean isFirstItem, String activeUrl, String baseUrl) {
        String completeUrl = getCompleteUrl(baseUrl, parent.getUrl());
        // FIXME there are random cases when either parent.getUrl() is ./ or baseUrl has a trailing ./
        if (completeUrl.endsWith("/./")) {
            logger.info("removed trailing ./ segment from path: " + completeUrl);
            completeUrl = completeUrl.substring(0, completeUrl.length() - 2);
        }
        String closed = "<li>\n<i></i>\n<span><a href=\"" + completeUrl +
                "\">" + parent.getTitle() + "</a><span><ul>\n";
        String opened = "<li class=\"expanded\">\n<i></i>\n<a href=\"" + completeUrl +
                "\">" + parent.getTitle() + "</a><ul style=\"display: block;\">\n";
        String selected = "<li class=\"active expanded\">\n<i></i>\n<a href=\"" + completeUrl +
                "\">" + parent.getTitle() + "</a><ul style=\"display: block;\">\n";

        if (activeUrl != null) {
            // Case: The parent topic for the expanded section should open its children
            if (parent.getUrl().equalsIgnoreCase(activeUrl)) {
                html.append(selected);

                // Case: The item is the first in the section (the absolute parent), therefore the section should be open
            } else if (isFirstItem && !activeUrl.isEmpty()) {
                html.append(opened);

                // Case: The selected topic is deeper in the section, therefore the section must be expanded
            } else if (Utilities.isActiveUrlInSection(parent, activeUrl, false)) {
                html.append(opened);

                // Case: The default, which isn't expanded
            } else {
                html.append(closed);
            }
        } else {
            html.append(closed);
        }

        if (parent.getChildren().size() == 0) {
            return;
        } else {
            getHtmlForChildren(parent, html, activeUrl, baseUrl);
        }
        html.append("</ul></li>\n");
    }

    private static void getHtmlForChildren(TocNode parent, StringBuilder html, String activeUrl, String baseUrl) {
        for (TocNode child : parent.getChildren()) {
            if (child.getChildren().size() > 0) {
                generateTocHtml(child, html, false, activeUrl, baseUrl);
            } else {
                html.append("<li");
                if (activeUrl != null && activeUrl.equalsIgnoreCase(child.getUrl())) {
                    html.append(" class=\"active\"");
                }
                html.append("><i></i><span><a href=\"" + getCompleteUrl(baseUrl, child.getUrl()) + "\">");
                html.append(child.getTitle() + "</a><span></li>");
            }
        }
    }

    private static String getCompleteUrl(String baseUrl, String url) {
        return Utilities.getConcatPath(new String[] {baseUrl, url});
    }

    private static void validateInputParams(Object[] params) {
        Utilities.validateCtorObjectsAreNotNull(params, SectionTocHtml.class.getSimpleName());
    }
}
