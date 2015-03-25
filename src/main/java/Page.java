import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean.osterberg on 2/22/15.
 */
public class Page {
    private static Logger logger = Logger.getLogger(Page.class);
    private String content;
    private String baseName;

    public Page(String content, String baseName) {
        this.content = content;
        this.baseName = baseName;
    }

    public static List<Page> forSection(Section section, List<Section> allSections, List<Template> templates, SiteTableOfContents toc) {
        List<Page> pages = new ArrayList<Page>();
        for(AsciiDocPage page : section.getPages()) {
            Page current = new Page(getCompletePageContent(section, allSections, toc, page, templates), page.getBaseName());
            pages.add(current);
        }
        return pages;
    }

    public static String getCompletePageContent(Section section, List<Section> sections, SiteTableOfContents toc, AsciiDocPage page, List<Template> templates) {
        StringBuilder html = new StringBuilder(getTemplateContents(page, templates));
        html = Utilities.replaceText(html, "{{ page.title }}", getPageTitle(page));
        html = Utilities.replaceText(html, "{{ page.toc }}", getPageToc(toc, sections, section, page));
        html = Utilities.replaceText(html, "{{ page.breadcrumb }}", getBreadcrumbHtml(section, page));
        html = Utilities.replaceText(html, "{{ page.content }}", getContentHtml(page));
        html = Utilities.replaceText(html, "{{ page.version }}", getVersionHtml(section, page));
        logger.info("Built page from template for \"" + getPageTitle(page) + "\".");
        return html.toString();
    }

    private static String getTemplateContents(AsciiDocPage page, List<Template> templates) {
        if(!page.containsAttribute("mule-template")) {
            for(Template template : templates) {
                if(template.getType().equals(TemplateType.DEFAULT)) {
                    return template.getContents();
                }
            }
        }
        // Todo: implement with logic for other templates and probably move to the Template class instead.
        return null;
    }

    private static String getPageTitle(AsciiDocPage page) {
        return page.getTitle();
    }

    private static String getPageToc(SiteTableOfContents toc, List<Section> sections, Section section, AsciiDocPage page) {
        SiteTocHtml tocHtml = SiteTocHtml.fromSiteTocAndSections(toc, sections);
        return tocHtml.getTocHtmlForSectionAndPage(section, page);
    }

    private static String getBreadcrumbHtml(Section section, AsciiDocPage page) {
        return Breadcrumb.getBreadcrumbHtmlForActiveUrl(section.getRootNode(), page.getBaseName());
    }

    private static String getContentHtml(AsciiDocPage page) {
        Document doc = Jsoup.parse(page.getHtml(), "UTF-8");
        return Utilities.getOnlyContentDivFromHtml(doc.html());
    }

    private static String getVersionHtml(Section section, AsciiDocPage page) {
        VersionSelector version = VersionSelector.fromSection(section);
        return version.htmlForPage(page);
    }

    public String getContent() {
        return content;
    }

    public String getBaseName() {
        return baseName;
    }
}
