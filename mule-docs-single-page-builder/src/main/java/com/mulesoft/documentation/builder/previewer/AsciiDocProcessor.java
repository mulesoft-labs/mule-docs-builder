package com.mulesoft.documentation.builder.previewer;

import com.mulesoft.documentation.builder.CodeLineNumberPreProcessor;
import com.mulesoft.documentation.builder.TabProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.asciidoctor.extension.JavaExtensionRegistry;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.asciidoctor.Asciidoctor.Factory.create;

/**
 * Created by MuleSoft.
 */
public class AsciiDocProcessor {

    private static Logger logger = LoggerFactory.getLogger(AsciiDocProcessor.class);
    private static AsciiDocProcessor processor;
    public Asciidoctor asciidoctor;

    public static AsciiDocProcessor getProcessorInstance() {
        if (processor == null) {
            processor = new AsciiDocProcessor();
        }
        return processor;
    }

    private AsciiDocProcessor() {
        asciidoctor = create();
    }

    public String convertFile(File asciiDocFile) {
        registerExtensions();
        //System.out.println("Processing AsciiDoc file: \"" + asciiDocFile.getAbsolutePath());
        String result = asciidoctor.convertFile(asciiDocFile, getOptionsForConversion());
        //System.out.println("Successfully processed AsciiDoc file: \"" + asciiDocFile.getAbsolutePath());
        return result;
    }

    public String convertAsciiDocString(String asciiDoc) {
        registerExtensions();
        return asciidoctor.convert(asciiDoc, getOptionsForConversion());
    }

    public void registerExtensions() {
        JavaExtensionRegistry extensionRegistry = asciidoctor.javaExtensionRegistry();
        extensionRegistry.preprocessor(CodeLineNumberPreProcessor.class);
        extensionRegistry.block("tabs", com.mulesoft.documentation.builder.previewer.TabProcessor.class);
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
