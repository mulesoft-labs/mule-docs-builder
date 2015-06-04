package org.mule.docs;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.ast.Block;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.Reader;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

import java.util.*;

public class TabProcessor extends BlockProcessor {

    private AsciiDocParser parser;
    private AsciiDocProcessor processor;

    private static Map<String, Object> CONFIG = new HashMap<String, Object>() {{
        put("contexts", Arrays.asList(":listing"));
        put("content_model", ":compound");
    }};

    public TabProcessor(String name, Map<String, Object> config) {
        super(name, CONFIG);
        IRubyObject parserRubyClass = rubyRuntime.evalScriptlet("Asciidoctor::Parser");
        processor = AsciiDocProcessor.getProcessorInstance();
        this.parser = (AsciiDocParser) JavaEmbedUtils.rubyToJava(rubyRuntime, parserRubyClass, AsciiDocParser.class);
    }

    @Override
    public Object process(AbstractBlock parent, Reader reader, Map<String, Object> attributes) {
        Map<String, Object> inherited = new HashMap<String, Object>();

        AbstractBlock container = createBlock(parent, "open", (String) null, inherited, new HashMap<Object, Object>());
        Map<String, String> titleAndId = new LinkedHashMap<String, String>();

        int count = 0;

        while (reader.hasMoreLines()) {
            Block child = parser.nextBlock(reader, parent, new HashMap<String, Object>());
            if (child != null) {
                String tabTitle = child.title();
                String uniqueId = Utilities.cleanPageFileNames(tabTitle) + "-" +
                        Utilities.getRandomAlphaNumericString(5);
                titleAndId.put(tabTitle, uniqueId);

                String singleTabHtmlOpen;
                if(count == 0) {
                    singleTabHtmlOpen = "<div class=\"tab-pane in active fade no-padding\" id=\"" + uniqueId + "\">";
                } else {
                    singleTabHtmlOpen = "<div class=\"tab-pane fade no-padding\" id=\"" + uniqueId + "\">";
                }

                String converted = processor.convertAsciiDocString(child.content().toString());
                converted = Utilities.getOnlyContentDivFromHtml(converted);
                converted = singleTabHtmlOpen + converted + "</div>";
                Block block = createBlock(parent, "pass", converted, inherited, new HashMap<Object, Object>());

                count++;

                // NOTE workaround missing append method on the AbstractBlock interface
                JavaEmbedUtils.invokeMethod(rubyRuntime, container, "append", new Object[]{block}, Object.class);
            }
        }

        String tabsOpen = "<div class=\"panel panel-default no-padding\">\n" +
                "    <div class=\"panel-heading no-padding\">\n" +
                "        <!-- Nav tabs -->\n" +
                "        <ul class=\"nav nav-tabs\" role=\"tablist\">\n";

        List<Map.Entry<String,String>> randAccess = new ArrayList<Map.Entry<String,String>>(titleAndId.entrySet());

        for(int i = 0; i < titleAndId.size(); i++) {
            if(i == 0) {
                tabsOpen += "<li class=\"active\">";
                tabsOpen += "<a href=\"#" + randAccess.get(i).getValue() +
                        "\" role=\"tab\" data-toggle=\"tab\">" + randAccess.get(i).getKey() + "</a></li>\n";
            } else {
                tabsOpen += "<li><a href=\"#" + randAccess.get(i).getValue() +
                        "\" role=\"tab\" data-toggle=\"tab\">" + randAccess.get(i).getKey() + "</a></li>\n";
            }
        }

        tabsOpen += "</ul>\n</div>\n<div class=\"panel-body tab-content no-padding\">";
        String tabsClose = "</div></div>";

        String containerContents = tabsOpen + container.content().toString() + tabsClose;
        AbstractBlock containerNew = createBlock(parent, "pass", containerContents, inherited, new HashMap<Object, Object>());
        container = containerNew;

        return container;
    }
}

