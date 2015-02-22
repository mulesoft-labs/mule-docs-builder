import org.apache.log4j.Logger;

/**
 * Created by sean.osterberg on 2/21/15.
 */
public class SectionTocHtml {
    private static Logger logger = Logger.getLogger(SectionTocHtml.class);
    private String html;

    public SectionTocHtml(String html) {
        this.html = html;
    }

    public static SectionTocHtml getUnselectedTocFromRootNode(TocNode rootNode) {
        return getTocFromRootNode(rootNode, "");
    }

    public static SectionTocHtml getSelectedTocFromRootNode(TocNode rootNode, String url) {
        return getTocFromRootNode(rootNode, url);
    }

    private static SectionTocHtml getTocFromRootNode(TocNode rootNode, String url) {
        if(!Utilities.isStringNullOrEmptyOrWhitespace(url)) {
            validateIfInputUrlIsInSection(rootNode, url);
        }
        validateInputParams(new Object[] {rootNode}); // Don't validate url because it can be empty
        StringBuilder builder = new StringBuilder();
        generateTocHtml(rootNode, builder, true, url);
        return new SectionTocHtml(builder.toString());
    }

    public String getHtml() {
        return html;
    }

    private static void generateTocHtml(TocNode parent, StringBuilder html, boolean isFirstItem, String activeUrl) {
        String sectionId = Utilities.getRandomAlphaNumericString(10);

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
        } else if (isActiveTopicInSection(parent, activeUrl, false)) {
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

    private static boolean isActiveTopicInSection(TocNode parentNode, String activeUrl, boolean isInSection) {
        if(isInSection) {
            return true;
        }
        if(activeUrl.isEmpty()) {
            return false;
        }
        for(TocNode node : parentNode.getChildren()) {
            if(node.getUrl().equalsIgnoreCase(activeUrl)) {
                isInSection = true;
            }
            else if(node.getChildren().size() > 0) {
                isInSection = isActiveTopicInSection(node, activeUrl, isInSection);
            }
        }
        return isInSection;
    }

    private static void validateInputParams(Object[] params) {
        Utilities.validateCtorObjectsAreNotNull(params, SectionTocHtml.class.getSimpleName());
    }

    private static void validateIfInputUrlIsInSection(TocNode root, String activeUrl) {
        if(root.getUrl().contentEquals(activeUrl)) {
            return;
        } else if(!isActiveTopicInSection(root, activeUrl, false)) {
            String error = "Active URL does not exist in nodes for TOC: \"" + activeUrl + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }
}
