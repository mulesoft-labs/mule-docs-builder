package org.mule.docs.model;

import java.util.List;

/**
 * Created by Mulesoft.
 */
public class Site implements IPageElement {

    private List<Section> sections;
    private SiteTableOfContents toc;

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public SiteTableOfContents getToc() {
        return toc;
    }

    public void setToc(SiteTableOfContents toc) {
        this.toc = toc;
    }

    @Override
    public void accept(IPageElementVisitor visitor) {
        if (visitor.visit(this)) {
            visitor.visit(toc);
            for (Section section : sections) {
                section.accept(visitor);
            }
        }
    }
}
