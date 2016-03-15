package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.util.Utilities;
import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.ast.Block;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.ContentModel;
import org.asciidoctor.extension.Contexts;
import org.asciidoctor.extension.Reader;

import java.util.*;

@Contexts(Contexts.CONTEXT_LISTING)
@ContentModel(ContentModel.COMPOUND)
public class TabProcessor extends BlockProcessor {

    private AsciiDocProcessor processor;

    public TabProcessor() {
        processor = AsciiDocProcessor.getProcessorInstance();
    }

    @Override
    public Object process(AbstractBlock parent, Reader reader, Map<String, Object> attributes) {
        Map<String, Object> inherited = new HashMap<String, Object>();
        int tabHash = parent.toString().hashCode();

        AbstractBlock container = createBlock(parent, "open", (String)null, inherited, new HashMap<Object, Object>());
        Map<String, String> titleAndId = new LinkedHashMap<String, String>();

        int count = 0;

        parseContent(container, reader.readLines());
        for (int i = 0; i < container.getBlocks().size(); i++) {
            AbstractBlock child = container.getBlocks().get(i);
            String tabTitle = child.getTitle();
            String uniqueId = Utilities.cleanPageFileNames(tabTitle) + "-" + tabHash;
            titleAndId.put(tabTitle, uniqueId);

            String singleTabHtmlOpen;
            if (count == 0) {
                singleTabHtmlOpen = "<div class=\"tab-pane in active fade no-padding\" id=\"" + uniqueId + "\">";
            } else {
                singleTabHtmlOpen = "<div class=\"tab-pane fade no-padding\" id=\"" + uniqueId + "\">";
            }

            String converted = processor.convertAsciiDocString(child.content().toString());
            converted = Utilities.getOnlyContentDivFromHtml(converted);
            converted = singleTabHtmlOpen + converted + "</div>";
            Block block = createBlock(parent, "pass", converted, inherited, new HashMap<Object, Object>());

            count++;

            // NOTE workaround missing append method on the StructuralNode interface
            //JavaEmbedUtils.invokeMethod(rubyRuntime, container, "append", new Object[]{block}, Object.class);
            container.getBlocks().set(i, block);
        }

        String tabsOpen = "<div class=\"panel panel-default no-padding\">\n" +
                "    <div class=\"panel-heading no-padding\">\n" +
                "        <!-- Nav tabs -->\n" +
                "        <ul class=\"nav nav-tabs\" role=\"tablist\">\n";

        List<Map.Entry<String,String>> randAccess = new ArrayList<Map.Entry<String,String>>(titleAndId.entrySet());

        for (int i = 0; i < titleAndId.size(); i++) {
            if (i == 0) {
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

        titleAndId = null;
        randAccess = null;
        String containerContents = tabsOpen + container.getContent().toString() + tabsClose;
        return createBlock(parent, "pass", containerContents, inherited, new HashMap<Object, Object>());
    }
}

