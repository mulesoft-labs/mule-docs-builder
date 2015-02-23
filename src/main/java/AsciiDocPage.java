import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import static org.asciidoctor.Asciidoctor.Factory.create;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;

import java.io.File;
import java.util.*;

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

    public AsciiDocPage(String filename, String baseName, String asciiDoc, String html) {
        validateInputParams(new String[]{filename, asciiDoc, html});
        this.filepath = filename;
        this.baseName = baseName;
        this.asciiDoc = asciiDoc;
        this.html = html;
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
        AsciiDocPage page = new AsciiDocPage(
                asciiDocFile.getPath(),
                FilenameUtils.getBaseName(asciiDocFile.getName()),
                Utilities.getFileContentsFromFile(asciiDocFile),
                processor.convertFile(asciiDocFile, getOptionsForConversion())
        );
        return page;
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

    public void setFilename(String filename) {
        this.filepath = filename;
    }

    public String getAsciiDoc() {
        return asciiDoc;
    }

    public void setAsciiDoc(String asciiDoc) {
        this.asciiDoc = asciiDoc;
    }

    public String getHtml() {
        return html;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
