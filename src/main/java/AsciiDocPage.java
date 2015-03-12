import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import static org.asciidoctor.Asciidoctor.Factory.create;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.ast.Document;

import java.io.File;
import java.util.*;

import org.jsoup.Jsoup;

/**
 * Created by sean.osterberg on 2/20/15.
 */
public class AsciiDocPage {
    private static Logger logger = Logger.getLogger(AsciiDocPage.class);
    private static Asciidoctor processor;
    private String baseName;
    private String filepath;
    private String asciiDoc;
    private String html;
    private String title;
    // Todo: Get the template type name from the file and add as a property

    public AsciiDocPage(String filename, String baseName, String asciiDoc, String html, String title) {
        validateInputParams(new String[]{filename, asciiDoc, html});
        this.filepath = filename;
        this.baseName = baseName;
        this.asciiDoc = asciiDoc;
        this.html = html;
        this.title = title;
    }

    public static List<AsciiDocPage> fromFiles(List<File> asciiDocFiles) {
        processor = create();
        List<AsciiDocPage> docPages = new ArrayList<AsciiDocPage>();
        for(File file : asciiDocFiles) {
            docPages.add(getPageFromFile(file));
            logger.info("Created AsciiDocPage from file: \"" + file.getPath() + "\".");
        }
        return docPages;
    }

    public static AsciiDocPage fromFile(File asciiDocFile) {
        processor = create();
        return getPageFromFile(asciiDocFile);
    }

    private static AsciiDocPage getPageFromFile(File asciiDocFile) {
        Utilities.validateAsciiDocFile(asciiDocFile);
        String html = processor.convertFile(asciiDocFile, getOptionsForConversion());
        AsciiDocPage page = new AsciiDocPage(
                asciiDocFile.getPath(),
                FilenameUtils.getBaseName(asciiDocFile.getName()),
                Utilities.getFileContentsFromFile(asciiDocFile),
                html,
                getPageTitle(html)
        );
        return page;
    }

    private static String getPageTitle(String html) {
        return Jsoup.parse(html, "UTF-8").title();
    }

    private static Options getOptionsForConversion() {
        Options options = new Options();
        options.setBackend("html");
        options.setToFile(false);
        options.setHeaderFooter(true);
        return options;
    }

    private void validateInputParams(String[] params) {
        Utilities.validateCtorStringInputParam(params, AsciiDocPage.class.getSimpleName());
    }

    public String getFilename() {
        return filepath;
    }

    public String getAsciiDoc() {
        return asciiDoc;
    }

    public String getHtml() {
        return html;
    }

    public String getBaseName() {
        return baseName;
    }

    public String getTitle() {
        return title;
    }
}
