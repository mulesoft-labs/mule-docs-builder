import static org.asciidoctor.Asciidoctor.Factory.create;
import org.apache.log4j.Logger;
import org.asciidoctor.*;
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

    public String[] getConvertedHtmlForAsciidocPages(List<File> docPages) {
        String[] docHtml = asciidoctorProcessor.convertFiles(docPages, getOptionsForConversion());
        return docHtml;
    }

    public String getConvertedHtmlForSingleAsciidocPage(File docPage) {
        String docHtml = asciidoctorProcessor.convertFile(docPage, new HashMap<String, Object>());
        return docHtml;
    }

    public String getConvertedHtmlForAsciidocString(String asciidocContent) {
        String docHtml = asciidoctorProcessor.convert(asciidocContent, new HashMap<String, Object>());
        return docHtml;
    }

    private Options getOptionsForConversion() {
        Options options = new Options();
        options.setBackend("html");
        options.setInPlace(true);
        return options;
    }
}
