import static org.asciidoctor.Asciidoctor.Factory.create;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.asciidoctor.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import java.util.*;
import java.io.*;

/**
 * Created by sean.osterberg on 2/6/15.
 */
public class AsciidocSerializer {
    static Logger logger = Logger.getLogger(AsciidocSerializer.class);
    private Asciidoctor asciidoctorProcessor;

    public AsciidocSerializer() {
        asciidoctorProcessor = create();
    }

    public List<String> getConvertedHtmlForAsciidocFiles(List<File> asciidocFiles) {
        String[] convertedFiles = asciidoctorProcessor.convertFiles(asciidocFiles, getOptionsForConversion());
        return Arrays.asList(convertedFiles);
    }

    public String getConvertedHtmlForSingleAsciidocPage(File asciidocFile) {
        String convertedFile = asciidoctorProcessor.convertFile(asciidocFile, getOptionsForConversion());
        logger.info("Successfully converted an Asciidoc file to html string.");
        return convertedFile;
    }

    public String getConvertedHtmlForAsciidocString(String asciidocContent) {
        String docHtml = asciidoctorProcessor.convert(asciidocContent, getOptionsForConversion());
        return docHtml;
    }

    private Options getOptionsForConversion() {
        Options options = new Options();
        options.setBackend("html");
        options.setToFile(false);
        options.setHeaderFooter(true);
        return options;
    }

    public List<DocPage> getConvertedDocPagesFromAsciidocFiles(File[] asciidocFiles, boolean excludeTocFile) {
        List<DocPage> docPages = new ArrayList<DocPage>();
        for(File file : asciidocFiles) {
            if(excludeTocFile) {
                if(!FilenameUtils.getBaseName(file.getName()).contentEquals("toc") && fileEndsWithDesiredExtension(file.getName())) {
                    docPages.add(getConvertedDocPageFromAsciidocFile(file));
                }
            } else {
                if(fileEndsWithDesiredExtension(file.getName())) {
                    docPages.add(getConvertedDocPageFromAsciidocFile(file));
                }
            }
        }
        return docPages;
    }

    public DocPage getConvertedDocPageFromAsciidocFile(File asciidocFile) {
        String asciidocContent = Utilities.getFileContentsFromFile(asciidocFile);
        String htmlContent = getConvertedHtmlFromAsciidoc(asciidocContent);

        DocPage page = new DocPage();
        page.setAsciidocSource(asciidocContent);
        page.setContentHtml(getOnlyContentDivFromHtml(htmlContent));
        page.setTitle(getTitleFromHtml(htmlContent));
        page.setSourceFilename(asciidocFile.getName());
        page.setSourceFilePath(asciidocFile.getPath());
        page.setOutputFilename(Utilities.replaceFileExtension(page.getSourceFilename(), ".html"));

        logger.info("Converted Asciidoc file to html and created DocPage for \"" + page.getTitle() + "\".");
        return page;
    }

    private String getConvertedHtmlFromAsciidoc(String asciidocContent) {
        String htmlContent = getConvertedHtmlForAsciidocString(asciidocContent);
        return Jsoup.parse(htmlContent, "UTF-8").html();
    }

    private String getOnlyContentDivFromHtml(String html) {
        Document doc = Jsoup.parse(html, "UTF-8");
        return doc.getElementById("content").html();
    }

    private String getTitleFromHtml(String html) {
        Document doc = Jsoup.parse(html, "UTF-8");
        return doc.title();
    }

    private static boolean fileEndsWithDesiredExtension(String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        if(extension.equalsIgnoreCase("ad") || extension.equalsIgnoreCase("asciidoc") || extension.equalsIgnoreCase("adoc"))
            return true;
        return false;
    }
}
