import java.io.*;
import java.util.*;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by sean.osterberg on 2/4/15.
 */

public class PageBuilder {
    private static Logger logger = Logger.getLogger(PageBuilder.class);
    List<PageTemplate> pageTemplates;
    private String baseUrl;
    private Map<String, String> inactiveTocHtml;

    public PageBuilder(List<PageTemplate> templates, String baseUrl) {
        this.pageTemplates = templates;
        this.baseUrl = baseUrl;
    }

    public List<DocPage> getInitialDocPagesForSection(DocSection section) {
        List<DocPage> docPages = new ArrayList<DocPage>();
        File[] pagesInSection = new File(section.getSourceFilepath()).listFiles();
        for(File docFile : pagesInSection) {
            if((Utilities.fileEndsWithValidAsciidocExtension(docFile.getName())) &&
                    (!docFile.getName().contentEquals("toc.ad"))) {
                docPages.add(getConvertedDocPageFromAsciidocFile(docFile, section));
            }
        }
        return docPages;
    }

    public String getBreadcrumbHtml(TocNode root, DocPage docPage) {
        BreadcrumbBuilder builder = new BreadcrumbBuilder(root, docPage.getFinalRelativeUrl());
        return builder.getBreadcrumbsForTopic();
    }

    public void setDestinationFilePath(DocPage page) {
        String path = Utilities.getConcatenatedUrl(new String[] { this.baseUrl, Utilities.replaceFileExtension(page.getSourceFilename(), ".html") });
        page.setFinalRelativeUrl(path);
    }

    public void updatePageWithCompleteTocHtml(DocPage page, String tocHtml) {
        page.setInitialTocHtml(tocHtml);
        page.setInitialContentHtml(getCompletePageContent(page));
    }

    public TocNode getRootNodeInToc(DocPage tocFile) {
        Document doc = Jsoup.parse(tocFile.getInitialContentHtml(), "UTF-8");
        TocBuilder builder = new TocBuilder(this.baseUrl, "");
        TocNode firstNode;
        return builder.getRootNode();
    }

    public String getCompletePageContent(DocPage docPage) {
        StringBuilder html = new StringBuilder(pageTemplates.get(0).getTemplateContents()); // Todo: Replace with logic that checks Asciidoc for specified template
        validatePageComponents(docPage);
        html = Utilities.replaceText(html, "{{ page.title }}", docPage.getTitle());
        html = Utilities.replaceText(html, "{{ page.toc }}", docPage.getFinalTocHtml());
        html = Utilities.replaceText(html, "{{ page.breadcrumb }}", docPage.getBreadcrumbHtml());
        html = Utilities.replaceText(html, "{{ page.content }}", docPage.getInitialContentHtml());
        this.logger.info("Built page from template for \"" + docPage.getTitle() + "\".");
        return html.toString();
    }

    private void validatePageComponents(DocPage page) {
        if((Utilities.isStringNullOrEmptyOrWhitespace(page.getTitle()) ||
            (Utilities.isStringNullOrEmptyOrWhitespace(page.getFinalTocHtml())) ||
            (Utilities.isStringNullOrEmptyOrWhitespace(page.getBreadcrumbHtml()) ||
            (Utilities.isStringNullOrEmptyOrWhitespace(page.getInitialContentHtml()))))) {
                String error = "Could not build DocPage because one or more required properties were null or empty.";
                this.logger.fatal(error);
                throw new DocBuilderException(error);
        }
    }

    public DocPage getConvertedDocPageFromAsciidocFile(File asciidocFile, DocSection section) {
        DocPage page = new DocPage();
        String asciidocContent = Utilities.getFileContentsFromFile(asciidocFile);
        String htmlContent = Utilities.getRawConvertedHtmlFromAsciidocString(asciidocContent);
        String finalRelativeUrl = Utilities.getConcatenatedFilepath(new String[]{this.baseUrl, Utilities.replaceFileExtension(asciidocFile.getName(), ".html")});
        TocBuilder builder = new TocBuilder(this.baseUrl, Utilities.getConcatenatedFilepath(new String[]{section.getSourceFilepath(), "toc.ad"}));

        page.setAsciidocSource(asciidocContent);
        page.setInitialContentHtml(Utilities.getOnlyContentDivFromHtml(htmlContent));
        page.setTitle(Utilities.getTitleFromHtml(htmlContent));
        page.setSourceFilename(asciidocFile.getName());
        page.setSourceFilepath(asciidocFile.getPath());
        page.setOutputFilename(Utilities.replaceFileExtension(page.getSourceFilename(), ".html"));
        page.setFinalRelativeUrl(finalRelativeUrl);
        page.setInitialTocHtml(builder.getTocHtmlForActiveUrl(finalRelativeUrl));
        page.setBreadcrumbHtml(getBreadcrumbHtml(builder.getRootNode(), page));

        this.logger.info("Converted Asciidoc file to html and created DocPage object for \"" + page.getTitle() + "\".");
        return page;
    }

}
