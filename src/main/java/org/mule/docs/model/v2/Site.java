package org.mule.docs.model.v2;


import java.util.List;

/**
 * Created by Mulesoft.
 */
public class Site extends AbstractBaseDocElement {

    List<Section> sections;
    TableOfContents toc;

    public TableOfContents getToc() {
        return toc;
    }

    public void setToc(TableOfContents toc) {
        this.toc = toc;
    }

    public List<Section> getSections() {

        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    @Override
    public void accept(IPageElementVisitor visitor) {
        if (visitor.visit(this)) {
            toc.accept(visitor);
            for (Section section : sections) {
                section.accept(visitor);
            }
        }
    }
}
