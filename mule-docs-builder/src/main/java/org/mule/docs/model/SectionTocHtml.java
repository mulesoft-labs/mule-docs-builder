package org.mule.docs.model;

import org.apache.log4j.Logger;
import org.mule.docs.utils.Utilities;
import org.mule.docs.writer.HtmlWriter;

/**
 * Created by sean.osterberg on 2/21/15.
 */
public class SectionTocHtml {

    private static Logger logger = Logger.getLogger(SectionTocHtml.class);
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
        validateInputParams(new Object[] { rootNode });
        StringBuilder builder = new StringBuilder();
        if (url != null) {
            Utilities.validateIfActiveUrlIsInSection(rootNode, url);
        }
        generateTocHtml(rootNode, builder, true, url, baseUrl);
        logger.debug("Created TOC HTML for section \"" + rootNode.getTitle() + "\".");
        return new SectionTocHtml(builder.toString());
    }

    public String getHtml() {
        return html;
    }

    private static void generateTocHtml(TocNode parent, StringBuilder html, boolean isFirstItem, String activeUrl, String baseUrl) {

        html.append(HtmlWriter.getInstance().getParentTocLink(activeUrl, baseUrl, parent, isFirstItem));

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
                html.append(HtmlWriter.getInstance().getChildTocLink(activeUrl, baseUrl, child));

            }
        }
    }

    private static void validateInputParams(Object[] params) {
        Utilities.validateCtorObjectsAreNotNull(params, SectionTocHtml.class.getSimpleName());
    }
}
