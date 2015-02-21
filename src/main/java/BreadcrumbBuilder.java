import org.apache.log4j.Logger;

/**
 * Created by sean.osterberg on 2/7/15.
 */

public class BreadcrumbBuilder {
    private static Logger logger = Logger.getLogger(BreadcrumbBuilder.class);
    private TocNode root;
    private String activeUrl;

    public BreadcrumbBuilder(TocNode root, String activeUrl) {
        validateRootNode(root);
        this.root = root;
        validateActiveUrl(activeUrl);
        this.activeUrl = activeUrl;
    }

    public String getBreadcrumbsForTopic() {
        StringBuilder builder = new StringBuilder();
        generateBreadcrumbsForTopic(root, activeUrl, builder);
        return builder.toString();
    }

    private void generateBreadcrumbsForTopic(TocNode parentNode, String activeUrl, StringBuilder html) {
        if(parentNode.getUrl().equalsIgnoreCase(activeUrl)) {
            html.append("<ol class=\"breadcrumb\"><li>MuleSoft Docs</li>");
            generateParentBreadcrumbsForTopic(parentNode, html);
            html.append("<li class=\"active\">" + parentNode.getTitle() + "</li>");
            html.append("</ol>");
        }
        else if(parentNode.getChildren().size() > 0) {
            for(TocNode node : parentNode.getChildren()) {
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

    //---- Validations ----//

    private void validateRootNode(TocNode root) {
        if(root == null) {
            String error = "Root node in BreadcrumbBuilder is null.";
            this.logger.fatal(error);
            throw new DocBuilderException(error);
        }
    }

    private void validateActiveUrl(String activeUrl) {
        if((activeUrl.isEmpty()) || (activeUrl == null)) {
            String error = "Active URL in BreadcrumbBuilder is an empty string.";
            this.logger.fatal(error);
            throw new DocBuilderException(error);
        }
    }
}
