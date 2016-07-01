package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.util.Utilities;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.asciidoctor.Asciidoctor.Factory.create;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/8/15.
 */
public class PdfAsciiDocProcessor {

    private static Logger logger = LoggerFactory.getLogger(PdfAsciiDocProcessor.class);
    private static PdfAsciiDocProcessor processor;
    public Asciidoctor asciidoctor;

    public static PdfAsciiDocProcessor getProcessorInstance() {
        if (processor == null) {
            processor = new PdfAsciiDocProcessor();
        }
        return processor;
    }

    private PdfAsciiDocProcessor() {
        asciidoctor = create();
    }

    public boolean convertAsciiDocStringToPdfFile(String asciiDoc, String originalFilePath, String outputFilePath) {
        boolean success = false;
        logger.info("Converting to PDF: " + originalFilePath);
        try {
            asciidoctor.convert(asciiDoc, getOptionsForConversion(outputFilePath));
            success = true;
        } catch (Exception e) {
            logger.error("FAILED: PDF conversion failed for file: " + originalFilePath, e);
        }
        if(new File(outputFilePath).exists()) {
            logger.info("Successfully converted file to PDF: " + outputFilePath);
        } else {
            logger.error("FAILED: PDF conversion failed for file: " + originalFilePath);
        }
        return success;
    }

    private Options getOptionsForConversion(String outputFilePath) {
        Options options = new Options();
        options.setBackend("pdf");
        options.setToFile(outputFilePath);
        options.setSafe(SafeMode.SAFE);
        options.setAttributes(getAttributesForConversion());

        return options;
    }

    private Map<String, Object> getAttributesForConversion() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("pdf-stylesdir", getPdfStylesPath());
        attributes.put("pdf-style", getPdfTheme());
        attributes.put("pdf-fontsdir", getPdfFontsPath());
        attributes.put("sectanchors", "true");
        attributes.put("idprefix", "");
        attributes.put("idseparator", "-");
        attributes.put("icons", "font");
        attributes.put("source-highlighter", "coderay");
        attributes.put("coderay-linenums-mode", "table");
        attributes.put("imagesdir", "../images");
        return attributes;
    }

    private String getPdfStylesPath() {
        URL pathToResources = this.getClass().getClassLoader().getResource("");
        String stylesPath = "";
        if(pathToResources != null) {
            stylesPath = Utilities.getConcatPath(new String[]{pathToResources.toString(), "pdf-styles"});
        }
        return stylesPath;
    }

    private String getPdfFontsPath() {
        return Utilities.getConcatPath(new String[] { getPdfStylesPath(), "fonts" });
    }

    private String getPdfTheme() {
        return "basic-theme.yml";
    }
}
