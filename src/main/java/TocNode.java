/**
 * Created by sean.osterberg on 1/30/15.
 */

import java.util.ArrayList;
import java.util.List;

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