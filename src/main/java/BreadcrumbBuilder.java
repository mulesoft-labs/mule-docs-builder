import org.apache.log4j.Logger;

/**
 * Created by sean.osterberg on 2/7/15.
 */
public class BreadcrumbBuilder {
    private static Logger logger = Logger.getLogger(BreadcrumbBuilder.class);
    private TocNode root;
    private String activeUrl;

    public BreadcrumbBuilder(TocNode root, String activeUrl) {
        if(root != null) {
            this.root = root;
        } else {
            String error = "Root node in BreadcrumbBuilder is null.";
            logger.fatal(error);
            throw new NullPointerException(error);
        } if(!activeUrl.isEmpty()) {
            this.activeUrl = activeUrl;
        } else {
            String error = "Active URL in BreadcrumbBuilder is an empty string.";
            logger.fatal(error);
            throw new NullPointerException(error);
        }
    }

    public String getBreadcrumbsForTopic() {
        StringBuilder builder = new StringBuilder();
        generateBreadcrumbsForTopic(root, activeUrl, builder);
        return builder.toString();
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
        html.append("<li><a href=\"" + immediateParent.getUrl() + "\">" + immediateParent.getUrl() + "</a></li>");
    }
}
