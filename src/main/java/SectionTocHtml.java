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

    public static SectionTocHtml getUnselectedTocFromRootNode(TocNode rootNode, String baseUrl) {
        return getTocFromRootNode(rootNode, null, baseUrl);
    }

    public static SectionTocHtml getSelectedTocFromRootNode(TocNode rootNode, String url, String baseUrl) {
        return getTocFromRootNode(rootNode, url, baseUrl);
    }

    private static SectionTocHtml getTocFromRootNode(TocNode rootNode, String url, String baseUrl) {
        validateInputParams(new Object[] {rootNode});
        StringBuilder builder = new StringBuilder();
        if(url != null) {
            Utilities.validateIfActiveUrlIsInSection(rootNode, url);
        }
        generateTocHtml(rootNode, builder, true, url, baseUrl);
        logger.info("Created TOC HTML for section \"" + rootNode.getTitle() + "\".");
        return new SectionTocHtml(builder.toString());
    }

    public String getHtml() {
        return html;
    }

    private static void generateTocHtml(TocNode parent, StringBuilder html, boolean isFirstItem, String activeUrl, String baseUrl) {
        String sectionId = Utilities.getRandomAlphaNumericString(10);

        if(activeUrl == null) {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header child\"><a href=\"");
            html.append(getCompleteUrl(baseUrl, parent.getUrl()) + "\">" + parent.getTitle() + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse in\" id=\"" + sectionId + "\">");

        } else if(parent.getUrl().equalsIgnoreCase(activeUrl)) {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header active\"><a href=\"");
            html.append(getCompleteUrl(baseUrl, parent.getUrl()) + "\" style=\"color: white\">" + parent.getTitle() + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse in\" id=\"" + sectionId + "\">");
        } else if(isFirstItem && !activeUrl.isEmpty()) {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header child\"><a href=\"");
            html.append(getCompleteUrl(baseUrl, parent.getUrl()) + "\">" + parent.getTitle() + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse in\" id=\"" + sectionId + "\" style=\"height: 0px;\">");
        } else if (Utilities.isActiveUrlInSection(parent, activeUrl, false)) {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header child\"><a href=\"");
            html.append(getCompleteUrl(baseUrl, parent.getUrl()) + "\">" + parent.getTitle() + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse in\" id=\"" + sectionId + "\">");
        } else {
            html.append("<li class=\"toc-section\"><div class=\"toc-section-header child\"><a href=\"");
            html.append(getCompleteUrl(baseUrl, parent.getUrl()) + "\">" + parent.getTitle() + "</a>");
            html.append("<a href=\"#" + sectionId +  "\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div>");
            html.append("<ul class=\"collapsed child-section collapse\" id=\"" + sectionId + "\" style=\"height: 0px;\">");
        }

        if(parent.getChildren().size() == 0) {
            return;
        } else {
            getHtmlForChildren(parent, html, activeUrl, baseUrl);
        }
        html.append("</ul></li>");
    }

    private static void getHtmlForChildren(TocNode parent, StringBuilder html, String activeUrl, String baseUrl) {
        for(TocNode child : parent.getChildren()) {
            if(child.getChildren().size() > 0) {
                generateTocHtml(child, html, false, activeUrl, baseUrl);
            } else {
                html.append("<a href=\"" + getCompleteUrl(baseUrl, child.getUrl()) + "\"><li class=\"child");
                if(activeUrl != null && activeUrl.equalsIgnoreCase(child.getUrl())) {
                    html.append(" active");
                }
                html.append("\">" + child.getTitle() + "</li></a>");
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
