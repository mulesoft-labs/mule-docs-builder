package org.mule.docs.model;

import org.apache.log4j.Logger;
import org.mule.docs.writer.HtmlWriter;
import org.mule.docs.writer.Template;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean.osterberg on 2/22/15.
 */
public class Page implements IPageElement {

    private static Logger logger = Logger.getLogger(Page.class);
    private String content;
    private String baseName;

    public Page(String content, String baseName) {
        this.content = content;
        this.baseName = baseName;
    }

    public static List<Page> forSection(Section section, List<Section> allSections, List<Template> templates, SiteTableOfContents toc) {
        List<Page> pages = new ArrayList<Page>();
        for (AsciiDocPage page : section.getPages()) {
            Page current = new Page(getCompletePageContent(section, allSections, toc, page, templates), page.getBaseName());
            pages.add(current);
        }
        return pages;
    }

    public static String getCompletePageContent(Section section, List<Section> sections, SiteTableOfContents toc, AsciiDocPage page, List<Template> templates) {

        logger.debug("Built page from template for \"" + page.getTitle() + "\".");
        return HtmlWriter.getIntance().getPageContent(section, sections, toc, page);
    }

    public String getContent() {
        return content;
    }

    public String getBaseName() {
        return baseName;
    }

    @Override
    public void accept(IPageElementVisitor visitor) {
        visitor.visit(this);
    }
}
