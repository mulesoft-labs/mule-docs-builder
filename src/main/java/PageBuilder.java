import java.io.IOException;
import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by sean.osterberg on 2/4/15.
 */

public class PageBuilder {
    private static Logger logger = Logger.getLogger(PageBuilder.class);
    private AsciidocSerializer serializer;
    private String templateFileContents;
    private String baseUrl;

    public PageBuilder(String templateFilePath, String baseUrl) {
        templateFileContents = Utilities.getFileContentsFromFile(new File(templateFilePath));
        serializer = new AsciidocSerializer();
        this.baseUrl = baseUrl;
    }

    public List<DocPage> getCompletedDocPagesFromDirectory(File directory) throws FileNotFoundException, IOException {
        List<DocPage> docPages = serializer.getConvertedDocPagesFromAsciidocFiles(directory.listFiles(), true);

        for(DocPage docPage : docPages) {
            setTocHtmlForDocPage(directory, docPage);
            setBreadcrumbHtmlForDocPage(directory, docPage);
            docPage.setContentHtml(buildPageFromTemplate(docPage));
        }
        return docPages;
    }

    public void setBreadcrumbHtmlForDocPage(File directory, DocPage docPage) throws FileNotFoundException, IOException{
        DocPage tocFile = getTocFile(directory);
        TocNode root = getRootNodeInToc(tocFile);
        BreadcrumbBuilder builder = new BreadcrumbBuilder(root, docPage.getFilename());
        docPage.setBreadcrumbHtml(builder.getBreadcrumbsForTopic());
        logger.info("Set breadcrumbs for doc \"" + docPage.getTitle() + "\"");
    }

    public void setTocHtmlForDocPage(File directory, DocPage docPage) throws FileNotFoundException, IOException {
        DocPage tocFile = getTocFile(directory);
        TocNode root = getRootNodeInToc(tocFile);
        TocBuilder builder = new TocBuilder();
        docPage.setTocHtml(builder.getTocHtml(root, docPage.getFilename()).toString());
        logger.info("Set TOC for doc \"" + docPage.getTitle() + "\"");
    }

    public DocPage getTocFile(File directory) throws FileNotFoundException {
        File tocFile = new File(directory.getPath() + "/toc.ad");
        if(!tocFile.exists()) {
            String noTocError = "Cannot find table of contents file \"toc.ad\" in " + directory.getPath();
            logger.fatal(noTocError);
            throw new FileNotFoundException(noTocError);
        }
        return serializer.getConvertedDocPageFromAsciidocFile(tocFile);
    }

    public TocNode getRootNodeInToc(DocPage tocFile) throws IOException {
        Document doc = Jsoup.parse(tocFile.getContentHtml(), "UTF-8");
        TocBuilder builder = new TocBuilder();
        TocNode firstNode;
        try {
            firstNode = builder.processToc(doc);
        } catch (IOException ioe) {
            logger.fatal("Couldn't process TOC file in directory \"" + tocFile.getSourceFilePath() + "\"");
            throw new IOException(ioe.getMessage());
        }

        return firstNode;
    }

    public String buildPageFromTemplate(DocPage docPage) throws IOException {
        StringBuilder html = new StringBuilder(templateFileContents);

        if(docPage.getTitle() != null && docPage.getTocHtml() != null && docPage.getBreadcrumbHtml() != null && docPage.getContentHtml() != null) {
            html = Utilities.replaceText(html, "{{ page.title }}", docPage.getTitle());
            html = Utilities.replaceText(html, "{{ page.toc }}", docPage.getTocHtml());
            html = Utilities.replaceText(html, "{{ page.breadcrumb }}", docPage.getBreadcrumbHtml());
            html = Utilities.replaceText(html, "{{ page.content }}", docPage.getContentHtml());
        } else {
            String error = "Could not build DocPage because one or more required properties were null.";
            logger.fatal(error);
            throw new IOException(error);
        }
        logger.info("Created page from template for \"" + docPage.getTitle() + "\".");
        return html.toString();
    }

    /*
    Temporarily held for backwards compatability
     */
    public StringBuilder generatePage(String templateFilePath,
                                      String contentHtml,
                                      String title,
                                      String tocHtml,
                                      String breadcrumbHtml,
                                      String fileName) throws FileNotFoundException, IOException {

        StringBuilder templateFile = new StringBuilder(Utilities.getFileContentsFromFile(new File(templateFilePath)));
        templateFile = Utilities.replaceText(templateFile, "{{ page.title }}", title);
        templateFile = Utilities.replaceText(templateFile, "{{ page.toc }}", tocHtml);
        templateFile = Utilities.replaceText(templateFile, "{{ page.breadcrumb }}", breadcrumbHtml);
        templateFile = Utilities.replaceText(templateFile, "{{ page.content }}", contentHtml);

        return templateFile;
    }



}
