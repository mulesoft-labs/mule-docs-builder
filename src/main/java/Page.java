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
    private Template template;

    public void createPage() {
        // Need built html page from template

    }

    public static List<Page> fromSectionAndTemplate(Section section, List<Section> allSections, Template template) {

        return new ArrayList<Page>();
    }

    public String getCompletePageContent(Section section, List<Section> sections, AsciiDocPage page) {
        StringBuilder html = new StringBuilder();
        html = Utilities.replaceText(html, "{{ page.title }}", getPageTitle(page));
        html = Utilities.replaceText(html, "{{ page.toc }}", "");
        html = Utilities.replaceText(html, "{{ page.breadcrumb }}", getBreadcrumbHtml(section, page));
        html = Utilities.replaceText(html, "{{ page.content }}", getContentHtml(page));
        this.logger.info("Built page from template for \"" + page.getTitle() + "\".");
        return html.toString();
    }

    private String getPageTitle(AsciiDocPage page) {
        return page.getTitle();
    }

    private String getBreadcrumbHtml(Section section, AsciiDocPage page) {
        String pageUrl = Utilities.getConcatPath(new String[] {section.getUrl(), page.getBaseName()});
        return Breadcrumb.getBreadcrumbHtmlForActiveUrl(section.getRootNode(), pageUrl);
    }

    private String getContentHtml(AsciiDocPage page) {
        Document doc = Jsoup.parse(page.getHtml(), "UTF-8");
        return Utilities.getOnlyContentDivFromHtml(doc.html());
    }

    private void getCompletePageToc() {

    }

    public String getContent() {
        return content;
    }
}
