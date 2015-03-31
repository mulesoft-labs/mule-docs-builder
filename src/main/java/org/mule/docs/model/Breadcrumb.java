package org.mule.docs.model;

import org.apache.log4j.Logger;
import org.mule.docs.utils.Utilities;
import org.mule.docs.writer.HtmlWriter;

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
        assert(rootNode!=null);
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
        List<String> links = new ArrayList<String>();
        for (TocNode node : nodes) {
            links.add(HtmlWriter.getInstance().getBreadCrumbLink(node, activeUrl, baseUrl));
        }
        logger.info("Created breadcrumb for \"" + baseUrl + activeUrl + "\".");

        return HtmlWriter.getInstance().getBreadCrumb(links);
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
        if (parentNode.getUrl().contentEquals(activeUrl)) {
            activeNode.add(parentNode);
            return;
        }
        if (parentNode.getChildren().size() > 0) {
            for (TocNode node : parentNode.getChildren()) {
                getActiveNode(node, activeUrl, activeNode);
            }
        }
        return;
    }

}
