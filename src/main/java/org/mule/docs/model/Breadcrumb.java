package org.mule.docs.model;

import org.apache.log4j.Logger;
import org.mule.docs.utils.Utilities;

/**
 * Created by sean.osterberg on 2/22/15.
 */
public class Breadcrumb {
    private static Logger logger = Logger.getLogger(Breadcrumb.class);
    private String html;

    public Breadcrumb(String html) {
        this.html = html;
    }

    public static Breadcrumb fromRootNode(TocNode rootNode, String activeUrl) {
        validateInputParams(new Object[] {rootNode, activeUrl});
        Utilities.validateIfActiveUrlIsInSection(rootNode, activeUrl);
        String breadcrumbHtml = getBreadcrumbHtmlForActiveUrl(rootNode, activeUrl);
        logger.debug("Created breadcrumb for \"" + activeUrl + "\".");
        return new Breadcrumb(breadcrumbHtml);
    }

    public static String getBreadcrumbHtmlForActiveUrl(TocNode rootNode, String activeUrl) {
        StringBuilder builder = new StringBuilder();
        generateBreadcrumbsForActiveUrl(rootNode, activeUrl, builder);
        return builder.toString();
    }

    private static void generateBreadcrumbsForActiveUrl(TocNode parentNode, String activeUrl, StringBuilder html) {
        if(parentNode.getUrl().equalsIgnoreCase(activeUrl)) {
            html.append("<ol class=\"breadcrumb\"><li><a href=\"developers.mulesoft.com/docs/\">MuleSoft Docs</a></li>");
            generateParentBreadcrumbsForActiveUrl(parentNode, html);
            html.append("<li class=\"active\">" + parentNode.getTitle() + "</li>");
            html.append("</ol>");
        }
        else if(parentNode.getChildren().size() > 0) {
            for(TocNode node : parentNode.getChildren()) {
                generateBreadcrumbsForActiveUrl(node, activeUrl, html);
            }
        }
    }

    private static void generateParentBreadcrumbsForActiveUrl(TocNode node, StringBuilder html) {
        TocNode immediateParent = node.getParent();
        if(immediateParent != null) {
            generateParentBreadcrumbsForActiveUrl(immediateParent, html);
        } else {
            return;
        }
        html.append("<li><a href=\"" + immediateParent.getUrl() + "\">" + immediateParent.getTitle() + "</a></li>");
    }

    private static void validateInputParams(Object[] params) {
        Utilities.validateCtorObjectsAreNotNull(params, Breadcrumb.class.getSimpleName());
    }

    public String getHtml() {
        return html;
    }
}
