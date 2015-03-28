package org.mule.docs.model.v2;

import java.util.List;

/**
 * Created by Mulesoft.
 */
public class Site {
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
}
