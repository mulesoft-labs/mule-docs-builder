package com.mulesoft.documentation.builder;

import static org.asciidoctor.Asciidoctor.Factory.create;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.asciidoctor.extension.JavaExtensionRegistry;

/**
 * Created by MuleSoft.
 */
public class AsciiDocProcessor {

    private static Logger logger = Logger.getLogger(AsciiDocProcessor.class);
    private static AsciiDocProcessor processor;
    private Asciidoctor asciidoctor;

    public static AsciiDocProcessor getProcessorInstance() {
        if (processor == null) {
            processor = new AsciiDocProcessor();
        }
        return processor;
    }

    private AsciiDocProcessor() {
        asciidoctor = create();
        registerExtensions();
    }

    public String convertFile(File asciiDocFile) {

        logger.debug("Processing AsciiDoc file: \"" + asciiDocFile.getAbsolutePath());
        String result = asciidoctor.convertFile(asciiDocFile, getOptionsForConversion());
        return result;
    }

    public String convertAsciiDocString(String asciiDoc) {
        return asciidoctor.convert(asciiDoc, getOptionsForConversion());
    }

    private void registerExtensions() {
        JavaExtensionRegistry extensionRegistry = asciidoctor.javaExtensionRegistry();
        //extensionRegistry.preprocessor(CodeLineNumberPreProcessor.class);
        extensionRegistry.block("tabs", TabProcessor.class);
        //extensionRegistry.preprocessor(LinenumsProcessor.class);
    }

    private Options getOptionsForConversion() {
        Options options = new Options();
        options.setBackend("html");
        options.setToFile(false);
        options.setHeaderFooter(true);
        options.setSafe(SafeMode.SAFE);
        options.setAttributes(getAttributesForConversion());
        return options;
    }

    private Map<String, Object> getAttributesForConversion() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("sectanchors", "true");
        attributes.put("idprefix", "");
        attributes.put("idseparator", "-");
        attributes.put("icons", "font");
        attributes.put("source-highlighter", "coderay");
        attributes.put("coderay-linenums-mode", "table");
        attributes.put("imagesdir", "./_images");
        return attributes;
    }
}