package org.mule.docs.processor;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.asciidoctor.extension.JavaExtensionRegistry;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.asciidoctor.Asciidoctor.Factory.create;

/**
 * Created by Mulesoft.
 */
public class AsciiDocProcessor {

    private static AsciiDocProcessor processor;
    public Asciidoctor asciidoctor;
    private List<IHtmlProcessor> processorList;

    public static AsciiDocProcessor getProcessorInstance() {
        if (processor == null) {
            processor = new AsciiDocProcessor();
        }
        return processor;
    }

    private void registerProcessors() {
        //processorList.add(new TabProcessor());
    }

    private void registerExtensions() {

    }

    private AsciiDocProcessor() {
        asciidoctor = create();
    }

    public String convertFile(File asciiDocFile) {
        JavaExtensionRegistry extensionRegistry = asciidoctor.javaExtensionRegistry();
        extensionRegistry.block("tabs", YellBlock.class);
        return asciidoctor.convertFile(asciiDocFile, getOptionsForConversion());
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
        return attributes;
    }
}
