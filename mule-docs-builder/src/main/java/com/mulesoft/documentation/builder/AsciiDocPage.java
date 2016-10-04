package com.mulesoft.documentation.builder;

import static org.asciidoctor.Asciidoctor.Factory.create;

import com.mulesoft.documentation.builder.util.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.ast.DocumentHeader;
import org.jsoup.Jsoup;

/**
 * Created by sean.osterberg on 2/20/15.
 */
public class AsciiDocPage {

    private static Logger logger = LoggerFactory.getLogger(AsciiDocPage.class);
    private static Asciidoctor processor;
    private String baseName;
    private String filePath;
    private String asciiDoc;
    private String html;
    private String title;
    // Todo: Get the template type name from the file and add as a property

    public AsciiDocPage(String filename, String baseName, String asciiDoc, String html, String title) {
        validateInputParams(new String[] { filename, asciiDoc, html });
        this.filePath = filename;
        this.baseName = baseName;
        this.asciiDoc = asciiDoc;
        this.html = html;
        this.title = title;
    }

    public static List<AsciiDocPage> fromFiles(List<File> asciiDocFiles) {
        List<AsciiDocPage> docPages = new ArrayList<>();
        asciiDocFiles.stream().forEach(file->{
            logger.debug("Creating AsciiDocPage from file: \"" + file.getPath() + "\".");
            docPages.add(getPageFromFile(file));
        });
        return docPages;
    }

    public static AsciiDocPage fromFile(File asciiDocFile) {
        processor = create();
        return getPageFromFile(asciiDocFile);
    }

    private static AsciiDocPage getPageFromFile(File asciiDocFile) {
        Utilities.validateAsciiDocFile(asciiDocFile);
        String html = AsciiDocProcessor.getProcessorInstance().convertFile(asciiDocFile);
        AsciiDocPage page = new AsciiDocPage(
                asciiDocFile.getPath(),
                FilenameUtils.getBaseName(asciiDocFile.getName()),
                Utilities.getFileContentsFromFile(asciiDocFile),
                html,
                getPageTitle(html)
        );
        return page;
    }

    public Map<String, Object> getAttributes() {
        Asciidoctor processor = this.getProcessor();
        DocumentHeader header = processor.readDocumentHeader(this.getAsciiDoc());
        return header.getAttributes();
    }

    public boolean containsAttribute(String attributeName) {
        Map<String, Object> attributes = getAttributes();
        return attributes.containsKey(attributeName);
    }

    public String getAttributeValue(String attributeName) {
        if (!containsAttribute(attributeName)) {
            return null;
        } else {
            Map<String, Object> attributes = getAttributes();
            return attributes.get(attributeName).toString();
        }
    }

    private static String getPageTitle(String html) {
        return Jsoup.parse(html).title().trim();
    }

    private void validateInputParams(String[] params) {
        Utilities.validateCtorStringInputParam(params, AsciiDocPage.class.getSimpleName());
    }

    public Asciidoctor getProcessor() {
        return processor;
    }

    public static void shutdownProcessor() {
        processor.shutdown();
    }

    public String getFilePath() {
        return filePath;
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

    /**
     * Get the URL segment that should be appended to the section URL.
     */
    public String getRelativeUrl() {
        return baseName.equals("index") ? "" : baseName;
    }

}

