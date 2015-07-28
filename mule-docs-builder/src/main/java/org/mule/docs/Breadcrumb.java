package org.mule.docs;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sean.osterberg on 2/22/15.
 */
public class Breadcrumb {
    private static Logger logger = Logger.getLogger(Breadcrumb.class);
    private TocNode root;

    public Breadcrumb(TocNode root) {
        this.root = root;
    }

    public static Breadcrumb fromRootNode(TocNode rootNode) {
        validateInputParams(new Object[] {rootNode});
        return new Breadcrumb(rootNode);
    }

    public String getHtmlForActiveUrl(String activeUrl, String baseUrl) {
        if (!Utilities.isActiveUrlInSection(this.root, activeUrl, false)) {
            return "";
        }
        return generateBreadcrumbsForActiveUrl(activeUrl, baseUrl);
    }

    private String generateBreadcrumbsForActiveUrl(String activeUrl, String baseUrl) {
        List<TocNode> nodes = getBreadcrumbs(activeUrl);
        StringBuilder html = new StringBuilder("<div class=\"breadcrumb-section\" data-swiftype-index='false'>");
        for (TocNode node : nodes) {
            if (node.getUrl().equals("")) { // This code is to make sure we can link to a section root properly
                node.setUrl("./");
            }
            String url = Utilities.getConcatPath(new String[]{baseUrl, node.getUrl()});
            if (!node.getUrl().equals(activeUrl)) {
                html.append("<a href=\"" + url + "\">" + node.getTitle() + "</a>/");
            } else {
                html.append("<a href=\"" + url + "\" class=\"breadcrumb-active-link\">" + node.getTitle() + "</a>");
            }
            if (node.getUrl().equals("./")) { // This code is to fix the above change before the TOC gets built
                node.setUrl("");
            }
        }
        html.append("</div>");
        logger.info("Created breadcrumb for \"" + baseUrl + "/" + activeUrl + "\".");
        return html.toString();
    }

    public List<TocNode> getBreadcrumbs(String activeUrl) {
        Utilities.validateIfActiveUrlIsInSection(this.root, activeUrl);
        List<TocNode> activeNode = new ArrayList<TocNode>();
        List<TocNode> nodes = new ArrayList<TocNode>();
        getActiveNode(this.root, activeUrl, activeNode);
        TocNode current = activeNode.get(0);
        nodes.add(current);
        while (current.getParent() != null) {
            nodes.add(current.getParent());
            current = current.getParent();
        }
        Collections.reverse(nodes);
        return nodes;
    }

    public static void getActiveNode(TocNode parentNode, String activeUrl, List<TocNode> activeNode) {
        if (parentNode.getUrl().equals(activeUrl)) {
            activeNode.add(parentNode);
            return;
        }
        if (parentNode.getChildren().size() > 0) {
            for (TocNode node : parentNode.getChildren()) {
                getActiveNode(node, activeUrl, activeNode);
            }
        }
    }

    private static void validateInputParams(Object[] params) {
        Utilities.validateCtorObjectsAreNotNull(params, Breadcrumb.class.getSimpleName());
    }

}
