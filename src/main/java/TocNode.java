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

    public String url;
    public String title;
    public TocNode parent;
    public List<TocNode> children;
}