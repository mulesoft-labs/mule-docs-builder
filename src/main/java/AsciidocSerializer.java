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

    public List<String> getConvertedHtmlForManyAsciidocFiles(List<File> asciidocFiles) {
        String[] convertedFiles = asciidoctorProcessor.convertFiles(asciidocFiles, getOptionsForConversion());
        return Arrays.asList(convertedFiles);
    }

    public String getConvertedHtmlForSingleAsciidocFile(File asciidocFile) {
        String convertedFile = asciidoctorProcessor.convertFile(asciidocFile, getOptionsForConversion());
        logger.info("Successfully converted AsciiDoc file to HTML string: \"" + asciidocFile.getName() + "\".");
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
}
