import com.sun.tools.javac.jvm.ClassFile;
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

    public Page(String content) {
        this.content = content;
    }

    public static List<Page> forSection(Section section, List<Section> allSections, Template template, SiteTableOfContents toc) {
        List<Page> pages = new ArrayList<Page>();
        for(AsciiDocPage page : section.getPages()) {
            Page current = new Page(getCompletePageContent(section, allSections, toc, page, template));
            pages.add(current);
        }
        return pages;
    }

    public static String getCompletePageContent(Section section, List<Section> sections, SiteTableOfContents toc, AsciiDocPage page, Template template) {
        StringBuilder html = new StringBuilder(template.getContents());
        html = Utilities.replaceText(html, "{{ page.title }}", getPageTitle(page));
        html = Utilities.replaceText(html, "{{ page.toc }}", getPageToc(toc, sections, section, page));
        html = Utilities.replaceText(html, "{{ page.breadcrumb }}", getBreadcrumbHtml(section, page));
        html = Utilities.replaceText(html, "{{ page.content }}", getContentHtml(page));
        html = Utilities.replaceText(html, "{{ page.version }}", getVersionHtml(section, page));
        logger.info("Built page from template for \"" + getPageTitle(page) + "\".");
        return html.toString();
    }

    private static String getPageTitle(AsciiDocPage page) {
        return page.getTitle();
    }

    private static String getPageToc(SiteTableOfContents toc, List<Section> sections, Section section, AsciiDocPage page) {
        SiteTocHtml tocHtml = SiteTocHtml.fromSiteTocAndSections(toc, sections);
        return tocHtml.getTocHtmlForSectionAndPage(section, page);
    }

    private static String getBreadcrumbHtml(Section section, AsciiDocPage page) {
        String pageUrl = Utilities.getConcatPath(new String[] {section.getUrl(), page.getBaseName()});
        return Breadcrumb.getBreadcrumbHtmlForActiveUrl(section.getRootNode(), pageUrl);
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
}
