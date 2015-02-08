import java.util.ArrayList;
import java.util.List;

/**
 * Table of contents node for the Mule Doc site.
 *
 * @author Sean Osterberg
 * @version 1.0
 */

public class TocNode {
    public TocNode(String url, String title, TocNode parent) {
        this.url = url;
        this.title = title;
        this.parent = parent;
        children = new ArrayList<TocNode>();
    }
    public TocNode() {}

    private String url;
    private String title;
    private TocNode parent;
    private List<TocNode> children;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TocNode getParent() {
        return parent;
    }

    public void setParent(TocNode parent) {
        this.parent = parent;
    }

    public List<TocNode> getChildren() {
        return children;
    }

    public void addChild(TocNode node) {
        this.children.add(node);
    }
}