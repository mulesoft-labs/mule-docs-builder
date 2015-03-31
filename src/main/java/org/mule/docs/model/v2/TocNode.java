package org.mule.docs.model.v2;

import java.util.List;

/**
 * Created by Mulesoft.
 */
public class TocNode extends AbstractBaseDocElement {

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

    public void setChildren(List<TocNode> children) {
        this.children = children;
    }

    @Override
    public void accept(IPageElementVisitor visitor) {
        if (visitor.visit(this)) {
            for (TocNode node : children) {
                node.accept(visitor);
            }
        }
    }
}
